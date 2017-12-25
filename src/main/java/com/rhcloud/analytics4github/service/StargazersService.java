package com.rhcloud.analytics4github.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.rhcloud.analytics4github.config.GitHubApiEndpoints;
import com.rhcloud.analytics4github.dto.RequestFromFrontendDto;
import com.rhcloud.analytics4github.dto.ResponceForFrontendDto;
import com.rhcloud.analytics4github.exception.GitHubRESTApiException;
import com.rhcloud.analytics4github.util.GitHubApiIterator;
import com.rhcloud.analytics4github.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author lyashenkogs
 */
@Service
public class StargazersService {
    private static Logger LOG = LoggerFactory.getLogger(StargazersService.class);
    @Autowired
    private RestTemplate template;


    public ResponceForFrontendDto stargazersFrequency(RequestFromFrontendDto requestFromFrontendDto) throws InterruptedException, ExecutionException, URISyntaxException, IOException, ClassNotFoundException, GitHubRESTApiException {
        //if week
        long period = ChronoUnit.DAYS.between(requestFromFrontendDto.getStartPeriod(), requestFromFrontendDto.getEndPeriod());
        if (period <= 7) {
            TreeMap<LocalDate, Integer> stargazersFrequencyMap = Utils.buildStargazersFrequencyMap(getWeekStargazersList(requestFromFrontendDto));
            List<Integer> frequencyList = Utils.parseWeekStargazersMapFrequencyToWeekFrequencyList(stargazersFrequencyMap);
            ResponceForFrontendDto buildedDtoForFrontend = Utils.buildJsonForFrontend(frequencyList);
            LOG.debug("builded json for highchart.js :" + buildedDtoForFrontend);
            return buildedDtoForFrontend;
        }
        //if month
        else {
            TreeMap<LocalDate, Integer> stargazersFrequencyMap = Utils.buildStargazersFrequencyMap(getMonthStargazersList(requestFromFrontendDto));
            List<Integer> frequencyList = Utils.parseMonthFrequencyMapToFrequencyLIst(stargazersFrequencyMap);
            ResponceForFrontendDto buildedDtoForFrontend = Utils.buildJsonForFrontend(frequencyList);
            LOG.debug("builded json for highchart.js :" + buildedDtoForFrontend);
            return buildedDtoForFrontend;
        }
    }

    public List<LocalDate> getWeekStargazersList(RequestFromFrontendDto requestFromFrontendDto) throws URISyntaxException, ExecutionException, InterruptedException, GitHubRESTApiException {
        List<LocalDate> thisWeekAllStargazersDateList = new LinkedList<>();
        GitHubApiIterator stargazersIterator = new GitHubApiIterator(requestFromFrontendDto.getAuthor() + "/" + requestFromFrontendDto.getRepository(), template, GitHubApiEndpoints.STARGAZERS);
        while (stargazersIterator.hasNext()) {
            List<JsonNode> stargazerPagesBatch = stargazersIterator.next(5);

            List<LocalDate> stargazersFrequency = stargazerPagesBatch.stream()
                    .map(jsonNode -> jsonNode.get(0).get("starred_at"))//get element "starred_at from each JSON inside the node
                    .map(starredAt -> Utils.parseTimestamp(starredAt.textValue()))
                    .filter(Utils::isWithinThisWeekRange)
                    .collect(Collectors.toList());
            LOG.debug(stargazersFrequency.toString());

            thisWeekAllStargazersDateList.addAll(stargazersFrequency);

            boolean batchContainStargazersOutOfRange = stargazerPagesBatch.stream()
                    .map(jsonNode -> jsonNode.get(0).get("starred_at"))//get element "starred_at from each JSON inside the node
                    .map(starredAt -> Utils.parseTimestamp(starredAt.textValue()))
                    .anyMatch((starredAtData) -> !Utils.isWithinThisWeekRange(starredAtData));
            if (batchContainStargazersOutOfRange) break;
        }
        stargazersIterator.close();

        LOG.debug("finish parsing stargazers" + thisWeekAllStargazersDateList.toString());
        return thisWeekAllStargazersDateList;
    }

    public List<LocalDate> getMonthStargazersList(RequestFromFrontendDto requestFromFrontendDto) throws URISyntaxException, ExecutionException, InterruptedException, GitHubRESTApiException {
        List<LocalDate> thisMonthAllStargazersDateList = new LinkedList<>();
        GitHubApiIterator stargazersIterator = new GitHubApiIterator(requestFromFrontendDto.getAuthor() + "/" + requestFromFrontendDto.getRepository(), template, GitHubApiEndpoints.STARGAZERS);
        while (stargazersIterator.hasNext()) {
            List<JsonNode> stargazerPagesBatch = stargazersIterator.next(5);
            //parse pages where localDate withing month period
            List<LocalDate> stargazersFrequency = stargazerPagesBatch.stream()
                    .map(jsonNode -> jsonNode.get(0).get("starred_at"))//get element "starred_at from each JSON inside the node
                    .map(jsonNode -> Utils.parseTimestamp(jsonNode.textValue()))
                    .filter(localDate -> Utils.isWithinThisMonthRange(localDate, requestFromFrontendDto))
                    .collect(Collectors.toList());
            LOG.debug(stargazersFrequency.toString());

            thisMonthAllStargazersDateList.addAll(stargazersFrequency);

            boolean batchContainStargazersOutOfRange = stargazerPagesBatch.stream()
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
