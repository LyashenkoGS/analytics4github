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
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

/**
 * @author lyashenkogs.
 */
@Service
public class CommitsService {
    private static Logger LOG = LoggerFactory.getLogger(CommitsService.class);
    @Autowired
    private RestTemplate template;

    private void parseCommitPagesBatch(List<LocalDate> thisWeekCommitsDateList, List<JsonNode> commitPagesBatch) {
        for (JsonNode commitPage : commitPagesBatch) {
            for (JsonNode commit : commitPage) {
                LOG.debug("commit: " + commit);
                LOG.debug("commitDate: " + commit.get("commit").get("author").get("date").textValue());
                LocalDate localDate = Utils.parseTimestamp(commit.get("commit").get("author").get("date").textValue());
                LOG.debug("parsed commit date: "+ localDate.toString());
                thisWeekCommitsDateList.add(localDate);
            }
        }
    }

    public List<LocalDate> getWeekCommitsList(String projectName) throws URISyntaxException, IOException, ExecutionException, InterruptedException, GitHubRESTApiException {
        List<LocalDate> thisWeekCommitsDateList = new LinkedList<>();
        GitHubApiIterator stargazersIterator = new GitHubApiIterator(projectName, template, GitHubApiEndpoints.COMMITS, Instant.now()
                .minus(7, ChronoUnit.DAYS)
                .truncatedTo(ChronoUnit.SECONDS),null);
        while (stargazersIterator.hasNext()) {
            List<JsonNode> commitPagesBatch = stargazersIterator.next(5);
            //Get localDatesList
            parseCommitPagesBatch(thisWeekCommitsDateList, commitPagesBatch);
        }
        stargazersIterator.close();
        LOG.debug("finish parsing commits" + thisWeekCommitsDateList.toString());
        return thisWeekCommitsDateList;
    }

    public List<LocalDate> getMonthCommitsList(RequestFromFrontendDto requestFromFrontendDto) throws URISyntaxException, IOException, ExecutionException, InterruptedException, GitHubRESTApiException {
        List<LocalDate> thisMonthCommitsDateList = new LinkedList<>();
        GitHubApiIterator stargazersIterator = new GitHubApiIterator(requestFromFrontendDto.getProjectName(), template,
                GitHubApiEndpoints.COMMITS, Utils.getPeriodInstant(requestFromFrontendDto.getStartPeriod()),
                Utils.getPeriodInstant(requestFromFrontendDto.getEndPeriod()));
        while (stargazersIterator.hasNext()) {
            List<JsonNode> commitPagesBatch = stargazersIterator.next(5);
            //Get localDatesList
            parseCommitPagesBatch(thisMonthCommitsDateList, commitPagesBatch);
        }
        stargazersIterator.close();
        LOG.debug("finish parsing commits" + thisMonthCommitsDateList.toString());
        return thisMonthCommitsDateList;
    }

    public ResponceForFrontendDto getThisWeekCommitsFrequencyPerProject(String projectName) throws IOException, InterruptedException, ExecutionException, URISyntaxException, ClassNotFoundException, GitHubRESTApiException {
        TreeMap<LocalDate, Integer> weekStargazersFrequencyMap = Utils.buildStargazersFrequencyMap(getWeekCommitsList(projectName));
        List<Integer> frequencyList = Utils.parseWeekStargazersMapFrequencyToWeekFrequencyList(weekStargazersFrequencyMap);
        ResponceForFrontendDto responceForFrontendDto = Utils.buildJsonForFrontend(frequencyList);
        LOG.debug("builded json for highchart.js :" + responceForFrontendDto);
        return responceForFrontendDto;

    }

    public ResponceForFrontendDto getThisMonthCommitsFrequencyPerProject(RequestFromFrontendDto requestFromFrontendDto) throws IOException, InterruptedException, ExecutionException, URISyntaxException, ClassNotFoundException, GitHubRESTApiException {
        TreeMap<LocalDate, Integer> commitsFrequencyMap = Utils.buildStargazersFrequencyMap(getMonthCommitsList(requestFromFrontendDto));
        List<Integer> frequencyList = Utils.parseMonthFrequencyMapCommitToFrequencyLIst(commitsFrequencyMap, requestFromFrontendDto);
        ResponceForFrontendDto responceForFrontendDto = Utils.buildJsonForFrontend(frequencyList);
        LOG.debug("builded json for highchart.js :" + responceForFrontendDto);
        return responceForFrontendDto;
    }
}
