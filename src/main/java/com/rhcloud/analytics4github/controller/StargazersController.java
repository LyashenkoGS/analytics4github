package com.rhcloud.analytics4github.controller;

import com.rhcloud.analytics4github.dto.RequestFromFrontendDto;
import com.rhcloud.analytics4github.dto.ResponceForFrontendDto;
import com.rhcloud.analytics4github.exception.GitHubRESTApiException;
import com.rhcloud.analytics4github.service.StargazersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class StargazersController {
    private static Logger LOG = LoggerFactory.getLogger(StargazersController.class);

    @Autowired
    private StargazersService stargazersService;

    @RequestMapping(value = "/stargazers", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public List<ResponceForFrontendDto> getStargazersByProject(@ModelAttribute RequestFromFrontendDto requestFromFrontendDto) throws IOException, URISyntaxException, ClassNotFoundException, ExecutionException, InterruptedException, GitHubRESTApiException {
        LOG.info("projectName parameter :" + requestFromFrontendDto.getProjectName());
        return Collections.singletonList(stargazersService.getThisWeekStargazersFrequencyPerProject(requestFromFrontendDto.getProjectName()));
    }

    @RequestMapping(value = "/stargazersPerMonth", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public List<ResponceForFrontendDto> getMonthStargazersByProject(@ModelAttribute RequestFromFrontendDto requestFromFrontendDto) throws InterruptedException, ExecutionException, URISyntaxException, IOException, ClassNotFoundException, GitHubRESTApiException {
        LOG.info("projectName parameter :" + requestFromFrontendDto.getProjectName());
        return Collections.singletonList(stargazersService.getThisMonthStargazersFrequencyPerProject(requestFromFrontendDto));
    }

}
