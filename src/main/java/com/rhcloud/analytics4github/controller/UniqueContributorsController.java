package com.rhcloud.analytics4github.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.rhcloud.analytics4github.exception.GitHubRESTApiException;
import com.rhcloud.analytics4github.service.UniqueContributorsService;
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

@RestController
public class UniqueContributorsController {
    private static Logger LOG = LoggerFactory.getLogger(StargazersController.class);

    @Autowired
    private UniqueContributorsService uniqueContributorsService;

    @RequestMapping(value = "/uniqueContributors", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public JsonNode getStargazersByProject(@RequestParam String projectName) throws IOException, URISyntaxException, ClassNotFoundException, ExecutionException, InterruptedException, GitHubRESTApiException {
        LOG.info("projectName parameter :" + projectName);
        return uniqueContributorsService.getUniqueContributorsFrequencyByWeek(projectName);
    }

    @RequestMapping(value = "/uniqueContributorsPerMonth", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public JsonNode getMonthStargazersByProject(@RequestParam String projectName) throws InterruptedException, ExecutionException, URISyntaxException, IOException, ClassNotFoundException, GitHubRESTApiException {
        LOG.info("projectName parameter :" + projectName);
        return uniqueContributorsService.getUniqueContributorsFrequencyByMonth(projectName);
    }
}
