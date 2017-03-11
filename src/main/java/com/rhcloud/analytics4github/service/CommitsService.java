package com.rhcloud.analytics4github.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.rhcloud.analytics4github.config.GitHubApiEndpoints;
import com.rhcloud.analytics4github.exception.GitHubRESTApiException;
import com.rhcloud.analytics4github.util.GithubApiIterator;
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
        for (JsonNode commitPage :
                commitPagesBatch) {
            for (JsonNode commit :
                    commitPage) {
                LOG.debug("commit: " + commit);
                JsonNode jsonNode = commit.get("commit")
                        .get("author")
                        .get("date");
                String s = jsonNode.textValue();
                LOG.debug("commitDate: " + s);
                LocalDate localDate = Utils.parseTimestamp(s);
                LOG.debug(localDate.toString());
                thisWeekCommitsDateList.add(localDate);
            }
        }
    }

    public List<LocalDate> getWeekCommitsList(String projectName) throws URISyntaxException, IOException, ExecutionException, InterruptedException, GitHubRESTApiException {
        List<LocalDate> thisWeekCommitsDateList = new LinkedList<>();
        GithubApiIterator stargazersIterator = new GithubApiIterator(projectName, template, GitHubApiEndpoints.COMMITS, Instant.now()
                .minus(7, ChronoUnit.DAYS)
                .truncatedTo(ChronoUnit.SECONDS));
        while (stargazersIterator.hasNext()) {
            List<JsonNode> commitPagesBatch = stargazersIterator.next(5);
            //Get localDatesList
            parseCommitPagesBatch(thisWeekCommitsDateList, commitPagesBatch);
        }
        stargazersIterator.close();
        LOG.debug("finish parsing commits" + thisWeekCommitsDateList.toString());
        return thisWeekCommitsDateList;
    }

    public List<LocalDate> getMonthCommitsList(String projectName) throws URISyntaxException, IOException, ExecutionException, InterruptedException, GitHubRESTApiException {
        List<LocalDate> thisMonthCommitsDateList = new LinkedList<>();

        GithubApiIterator stargazersIterator = new GithubApiIterator(projectName, template, GitHubApiEndpoints.COMMITS, Utils.getThisMonthBeginInstant());
        while (stargazersIterator.hasNext()) {
            List<JsonNode> commitPagesBatch = stargazersIterator.next(5);
            //Get localDatesList
            parseCommitPagesBatch(thisMonthCommitsDateList, commitPagesBatch);
        }
        stargazersIterator.close();
        LOG.debug("finish parsing commits" + thisMonthCommitsDateList.toString());
        return thisMonthCommitsDateList;
    }

    public ArrayNode getThisWeekCommitsFrequencyPerProject(String projectName) throws IOException, InterruptedException, ExecutionException, URISyntaxException, ClassNotFoundException, GitHubRESTApiException {
        TreeMap<LocalDate, Integer> weekStargazersFrequencyMap = Utils.buildStargazersFrequencyMap(getWeekCommitsList(projectName));
        List<Integer> frequencyList = Utils.parseWeekStargazersMapFrequencyToWeekFrequencyList(weekStargazersFrequencyMap);
        ArrayNode buildedJsonForHighChart = Utils.buildJsonForHIghChart(frequencyList);
        LOG.debug("builded json for highchart.js :" + buildedJsonForHighChart);
        return buildedJsonForHighChart;

    }

    public JsonNode getThisMonthCommitsFrequencyPerProject(String projectName) throws IOException, InterruptedException, ExecutionException, URISyntaxException, ClassNotFoundException, GitHubRESTApiException {
        TreeMap<LocalDate, Integer> commitsFrequencyMap = Utils.buildStargazersFrequencyMap(getMonthCommitsList(projectName));
        List<Integer> frequencyList = Utils.parseMonthFrequencyMapToFrequencyLIst(commitsFrequencyMap);
        ArrayNode buildedJsonForHighChart = Utils.buildJsonForHIghChart(frequencyList);
        LOG.debug("builded json for highchart.js :" + buildedJsonForHighChart);
        return buildedJsonForHighChart;
    }
}
