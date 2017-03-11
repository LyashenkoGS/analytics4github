package com.rhcloud.analytics4github.controller;


import com.rhcloud.analytics4github.exception.TrendingException;
import com.rhcloud.analytics4github.service.GitHubTrendingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

/**
 * @author lyashenkogs.
 * @since 9/3/16
 */
@RestController
public class GitHubTrendingController {
    private static Logger LOG = LoggerFactory.getLogger(GitHubTrendingController.class);
    private GitHubTrendingService trendingService;

    @Autowired
    public void setTrendingService(GitHubTrendingService trendingService) {
        this.trendingService = trendingService;
    }

    @RequestMapping(value = "/randomRequestTrendingRepoName", method = RequestMethod.GET)
    public ResponseEntity getRandomTrendingRepo() {
        try {
            List<String> trendingRepos = trendingService.getTrendingRepos();
            int index = new Random().nextInt(trendingService.getTrendingRepos().size());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(trendingRepos.get(index));
        } catch (TrendingException e) {
            LOG.error(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}

