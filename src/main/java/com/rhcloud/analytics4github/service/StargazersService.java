package com.rhcloud.analytics4github.service;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.rhcloud.analytics4github.config.GitHubApiEndpoints;
import com.rhcloud.analytics4github.dto.RequestFromFrontendDto;
import com.rhcloud.analytics4github.dto.ResponceForFrontendDto;
import com.rhcloud.analytics4github.util.GithubApiIterator;
import com.rhcloud.analytics4github.util.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author lyashenkogs
 */
@Service
public class StargazersService {
    private static Logger LOG = LoggerFactory.getLogger(StargazersService.class);
    @Autowired
    private RestTemplate template;

    public ResponceForFrontendDto getThisWeekStargazersFrequencyPerProject(String projectName) throws IOException, URISyntaxException, ClassNotFoundException, ExecutionException, InterruptedException{
        TreeMap<LocalDate, Integer> weekStargazersFrequencyMap = Utils.buildStargazersFrequencyMap(getWeekStargazersList(projectName));
        List<Integer> frequencyList = Utils.parseWeekStargazersMapFrequencyToWeekFrequencyList(weekStargazersFrequencyMap);
        ResponceForFrontendDto buildedDtoForFrontend = Utils.buildJsonForFrontend(frequencyList);
        LOG.debug("builded json for highchart.js :" + buildedDtoForFrontend);
        return buildedDtoForFrontend;
    }

    public ResponceForFrontendDto getThisMonthStargazersFrequencyPerProject(RequestFromFrontendDto requestFromFrontendDto) throws InterruptedException, ExecutionException, URISyntaxException, IOException, ClassNotFoundException {
        TreeMap<LocalDate, Integer> stargazersFrequencyMap = Utils.buildStargazersFrequencyMap(getMonthStargazersList(requestFromFrontendDto));
        List<Integer> frequencyList = Utils.parseMonthFrequencyMapToFrequencyLIst(stargazersFrequencyMap);
        ResponceForFrontendDto buildedDtoForFrontend = Utils.buildJsonForFrontend(frequencyList);
        LOG.debug("builded json for highchart.js :" + buildedDtoForFrontend);
        return buildedDtoForFrontend;
    }

    public List<LocalDate> getWeekStargazersList(String projectName) throws URISyntaxException, IOException, ExecutionException, InterruptedException {
        List<LocalDate> thisWeekAllStargazersDateList = new LinkedList<>();
        GithubApiIterator stargazersIterator = new GithubApiIterator(projectName, template, GitHubApiEndpoints.STARGAZERS);
        while (stargazersIterator.hasNext()) {
            List<JsonNode> stargazerPagesBatch = stargazersIterator.next(5);

            List<LocalDate> stargazersFrequency = StreamSupport.stream(stargazerPagesBatch.spliterator(), false)
                    .map(e -> e.get(0).get("starred_at"))//get element "starred_at from each JSON inside the node
                    .map(e -> Utils.parseTimestamp(e.textValue()))
                    .filter(Utils::isWithinThisWeekRange)
                    .collect(Collectors.toList());
            LOG.debug(stargazersFrequency.toString());

            thisWeekAllStargazersDateList.addAll(stargazersFrequency);

            boolean batchContainStargazersOutOfRange = StreamSupport.stream(stargazerPagesBatch.spliterator(), false)
                    .map(e -> e.get(0).get("starred_at"))//get element "starred_at from each JSON inside the node
                    .map(e -> Utils.parseTimestamp(e.textValue()))
                    .anyMatch((e) -> !Utils.isWithinThisWeekRange(e));
            if (batchContainStargazersOutOfRange) break;
        }
        stargazersIterator.close();

        LOG.debug("finish parsing stargazers" + thisWeekAllStargazersDateList.toString());
        return thisWeekAllStargazersDateList;
    }

    public List<LocalDate> getMonthStargazersList(RequestFromFrontendDto requestFromFrontendDto) throws URISyntaxException, ExecutionException, InterruptedException {
        List<LocalDate> thisMonthAllStargazersDateList = new LinkedList<>();
        GithubApiIterator stargazersIterator = new GithubApiIterator(requestFromFrontendDto.getProjectName(), template, GitHubApiEndpoints.STARGAZERS);
        while (stargazersIterator.hasNext()) {
            List<JsonNode> stargazerPagesBatch = stargazersIterator.next(5);
            //parse pages where localDate withing month period
            List<LocalDate> stargazersFrequency = StreamSupport.stream(stargazerPagesBatch.spliterator(), false)
                    .map(jsonNode -> jsonNode.get(0).get("starred_at"))//get element "starred_at from each JSON inside the node
                    .map(jsonNode -> Utils.parseTimestamp(jsonNode.textValue()))
                    .filter(localDate -> Utils.isWithinThisMonthRange(localDate, requestFromFrontendDto))
                    .collect(Collectors.toList());
            LOG.debug(stargazersFrequency.toString());

            thisMonthAllStargazersDateList.addAll(stargazersFrequency);

            boolean batchContainStargazersOutOfRange = StreamSupport.stream(stargazerPagesBatch.spliterator(), false)
                    .map(jsonNode -> jsonNode.get(0).get("starred_at"))//get element "starred_at from each JSON inside the node
                    .map(jsonNode -> Utils.parseTimestamp(jsonNode.textValue()))
                    .anyMatch((localDate) -> !Utils.isWithinThisMonthRange(localDate, requestFromFrontendDto));
            if (batchContainStargazersOutOfRange) break;
        }
        stargazersIterator.close();

        LOG.debug("finish parsing stargazers" + thisMonthAllStargazersDateList.toString());
        return thisMonthAllStargazersDateList;
    }
}
