package com.rhcloud.analytics4github.controller;

import com.rhcloud.analytics4github.dto.RequestFromFrontendDto;
import com.rhcloud.analytics4github.dto.ResponceForFrontendDto;
import com.rhcloud.analytics4github.service.StargazersService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ExecutionException;

@RestController
public class StargazersController {
    private static Logger LOG = LoggerFactory.getLogger(StargazersController.class);

    @Autowired
    private StargazersService stargazersService;

    @RequestMapping(value = "/stargazers", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public Collection<ResponceForFrontendDto> getStargazersByProject(RequestFromFrontendDto requestFromFrontendDto) throws IOException, URISyntaxException, ClassNotFoundException, ExecutionException, InterruptedException {
        LOG.info("projectName parameter :" + requestFromFrontendDto.getProjectName());
        return Collections.singletonList(stargazersService.getThisWeekStargazersFrequencyPerProject(requestFromFrontendDto.getProjectName()));
    }

    @RequestMapping(value = "/stargazersPerMonth", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public Collection<ResponceForFrontendDto> getMonthStargazersByProject(RequestFromFrontendDto requestFromFrontendDto) throws InterruptedException, ExecutionException, URISyntaxException, IOException, ClassNotFoundException {
        LOG.info("projectName parameter :" + requestFromFrontendDto.getProjectName());
        return Collections.singletonList(stargazersService.getThisMonthStargazersFrequencyPerProject(requestFromFrontendDto.getProjectName()));
    }

}
