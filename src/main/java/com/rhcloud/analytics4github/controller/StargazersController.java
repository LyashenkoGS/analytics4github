package com.rhcloud.analytics4github.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import com.rhcloud.analytics4github.dto.ResponceForFrontendDto;
import com.rhcloud.analytics4github.service.StargazersService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class StargazersController {
    private static Logger LOG = LoggerFactory.getLogger(StargazersController.class);

    @Autowired
    private StargazersService stargazersService;

    @RequestMapping(value = "/stargazers", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public Collection<ResponceForFrontendDto> getStargazersByProject(@RequestParam String projectName) throws IOException, URISyntaxException, ClassNotFoundException, ExecutionException, InterruptedException {
        LOG.info("projectName parameter :" + projectName);
        Collection<ResponceForFrontendDto> responseToFrontend = new ArrayList<>();
        responseToFrontend.add(stargazersService.getThisWeekStargazersFrequencyPerProject(projectName));
        return responseToFrontend;
    }


    @RequestMapping(value = "/stargazersPerMonth", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public Collection<ResponceForFrontendDto> getMonthStargazersByProject(@RequestParam String projectName) throws InterruptedException, ExecutionException, URISyntaxException, IOException, ClassNotFoundException {
        LOG.info("projectName parameter :" + projectName);
        Collection<ResponceForFrontendDto> responseToFrontend = new ArrayList<>();
        responseToFrontend.add(stargazersService.getThisMonthStargazersFrequencyPerProject(projectName));
        return responseToFrontend;
    }

}
