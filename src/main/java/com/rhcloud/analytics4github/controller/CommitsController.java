package com.rhcloud.analytics4github.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.rhcloud.analytics4github.exception.GitHubRESTApiException;
import com.rhcloud.analytics4github.service.CommitsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

/**
 * @author lyashenkogs.
 */
@RestController
public class CommitsController {
    private static Logger LOG = LoggerFactory.getLogger(CommitsController.class);

    @Autowired
    private CommitsService commitsService;

    @RequestMapping(value = "/commits", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public JsonNode getStargazersByProject(@RequestParam String projectName) throws IOException, URISyntaxException, ClassNotFoundException, ExecutionException, InterruptedException, GitHubRESTApiException {
        LOG.info("projectName parameter :" + projectName);
        return commitsService.getThisWeekCommitsFrequencyPerProject(projectName);
    }

    @RequestMapping(value = "/commitsPerMonth", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public JsonNode getMonthStargazersByProject(@RequestParam String projectName) throws InterruptedException, ExecutionException, URISyntaxException, IOException, ClassNotFoundException, GitHubRESTApiException {
        LOG.info("projectName parameter :" + projectName);
        return commitsService.getThisMonthCommitsFrequencyPerProject(projectName);
    }
}
