package com.rhcloud.analytics4github.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.rhcloud.analytics4github.service.StargazersService;

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

import javax.servlet.http.HttpServletResponse;

@RestController
public class ProjectController {
    private static Logger LOG = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private StargazersService stargazersService;

    @RequestMapping(value = "/stargazers", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public JsonNode getStargazersByProject(HttpServletResponse response, @RequestParam String projectName) throws IOException, URISyntaxException, ClassNotFoundException, ExecutionException, InterruptedException {
        //Todo extract to a filter to allow access to the endpoint for clients from other domains
        response.setHeader("Access-Control-Allow-Origin", "*");
        LOG.info("projectName parameter :" + projectName);
        return stargazersService.getThisWeekStargazersFrequencyPerProject(projectName);
    }

    @RequestMapping(value = "/stargazersPerMonth", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public JsonNode getMonthStargazersByProject(HttpServletResponse response, @RequestParam String projectName) throws InterruptedException, ExecutionException, URISyntaxException, IOException, ClassNotFoundException {
        //Todo extract to a filter to allow access to the endpoint for clients from other domains
        response.setHeader("Access-Control-Allow-Origin", "*");
        LOG.info("projectName parameter :" + projectName);
        return stargazersService.getThisMonthStargazersFrequencyPerProject(projectName);
    }
}
