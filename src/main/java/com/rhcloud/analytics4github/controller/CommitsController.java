package com.rhcloud.analytics4github.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.rhcloud.analytics4github.dto.RequestFromFrontendDto;
import com.rhcloud.analytics4github.dto.ResponceForFrontendDto;
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
import java.util.Collection;
import java.util.Collections;
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
    public Collection<ResponceForFrontendDto> getStargazersByProject(RequestFromFrontendDto requestFromFrontendDto) throws IOException, URISyntaxException, ClassNotFoundException, ExecutionException, InterruptedException {
        LOG.info("projectName parameter :" + requestFromFrontendDto.getProjectName());
        return Collections.singletonList(commitsService.getThisWeekCommitsFrequencyPerProject(requestFromFrontendDto.getProjectName()));
    }

    @RequestMapping(value = "/commitsPerMonth", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public Collection<ResponceForFrontendDto> getMonthStargazersByProject(RequestFromFrontendDto requestFromFrontendDto) throws InterruptedException, ExecutionException, URISyntaxException, IOException, ClassNotFoundException {
        LOG.info("projectName parameter :" + requestFromFrontendDto.getProjectName());
        return Collections.singletonList(commitsService.getThisMonthCommitsFrequencyPerProject(requestFromFrontendDto.getProjectName()));
    }
}
