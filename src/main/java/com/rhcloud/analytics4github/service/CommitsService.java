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

/**
 * @author lyashenkogs.
 */
@Service
public class CommitsService {
    private static Logger LOG = LoggerFactory.getLogger(CommitsService.class);
    @Autowired
    private RestTemplate template;

    public List<LocalDate> getCommitsList(RequestFromFrontendDto requestFromFrontendDto) throws URISyntaxException, ExecutionException, InterruptedException, GitHubRESTApiException {
        List<LocalDate> thisMonthCommitsDateList = new LinkedList<>();
        GitHubApiIterator iterator = new GitHubApiIterator(requestFromFrontendDto.getAuthor() + "/" + requestFromFrontendDto.getRepository(), template,
                GitHubApiEndpoints.COMMITS, Utils.getPeriodInstant(requestFromFrontendDto.getStartPeriod()),
                Utils.getPeriodInstant(requestFromFrontendDto.getEndPeriod()));
        while (iterator.hasNext()) {
            List<JsonNode> commitPagesBatch = iterator.next(5);
            //Get localDatesList
            for (JsonNode commitPage : commitPagesBatch) {
                for (JsonNode commit : commitPage) {
                    LOG.debug("commit: " + commit);
                    LOG.debug("commitDate: " + commit.get("commit").get("author").get("date").textValue());
                    LocalDate localDate = Utils.parseTimestamp(commit.get("commit").get("author").get("date").textValue());
                    LOG.debug("parsed commit sinceMonth: " + localDate.toString());
                    thisMonthCommitsDateList.add(localDate);
                }
            }
        }
        iterator.close();
        LOG.debug("finish parsing commits" + thisMonthCommitsDateList.toString());
        return thisMonthCommitsDateList;
    }


    public ResponceForFrontendDto getCommitsFrequency(RequestFromFrontendDto requestFromFrontendDto) throws IOException, InterruptedException, ExecutionException, URISyntaxException, ClassNotFoundException, GitHubRESTApiException {
        TreeMap<LocalDate, Integer> frequencyMap = Utils.buildStargazersFrequencyMap(getCommitsList(requestFromFrontendDto));
        long period = ChronoUnit.DAYS.between(requestFromFrontendDto.getStartPeriod(), requestFromFrontendDto.getEndPeriod());
        //if week
        if (period <= 7) {
            List<Integer> frequencyList = Utils.parseWeekStargazersMapFrequencyToWeekFrequencyList(frequencyMap);
            ResponceForFrontendDto responceForFrontendDto = Utils.buildJsonForFrontend(frequencyList);
            LOG.debug("builded json for highchart.js :" + responceForFrontendDto);
            return responceForFrontendDto;
            //if month
        } else {
            List<Integer> frequencyList = Utils.parseMonthFrequencyMapToFrequencyLIst(frequencyMap);
            ResponceForFrontendDto responceForFrontendDto = Utils.buildJsonForFrontend(frequencyList);
            LOG.debug("builded json for highchart.js :" + responceForFrontendDto);
            return responceForFrontendDto;
        }
    }

}

