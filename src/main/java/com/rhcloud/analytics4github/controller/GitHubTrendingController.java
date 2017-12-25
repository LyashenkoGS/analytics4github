package com.rhcloud.analytics4github.controller;


import com.rhcloud.analytics4github.exception.TrendingException;
import com.rhcloud.analytics4github.service.GitHubTrendingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lyashenkogs.
 * @since 9/3/16
 */
@RestController
public class GitHubTrendingController {
    private static Logger LOG = LoggerFactory.getLogger(GitHubTrendingController.class);

    @Autowired
    private GitHubTrendingService trendingService;

    @GetMapping(value = "/randomRequestTrendingRepoName", consumes = MediaType.ALL_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    ResponseEntity getRandomTrendingRepo() {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(trendingService.getRandomTrendingRepo());
        } catch (TrendingException e) {
            LOG.error(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}

