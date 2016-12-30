package com.rhcloud.analytics4github.controller;

import com.rhcloud.analytics4github.service.GithubTrendingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * @author lyashenkogs.
 * @since 9/3/16
 */
@RestController
public class GithubTrendingController {
    @Autowired
    private GithubTrendingService trendingService;

    @RequestMapping(value = "/randomRequestTrendingRepoName", method = RequestMethod.GET)
    @ResponseBody
    public String getRandomTrendingRepo() {
        Random random = new Random();
        trendingService.getTrendingRepos().size();
        int index = random.nextInt(trendingService.getTrendingRepos().size());
        return trendingService.getTrendingRepos().get(index);
    }
}