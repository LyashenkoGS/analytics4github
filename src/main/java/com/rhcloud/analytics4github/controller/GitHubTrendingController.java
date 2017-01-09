package com.rhcloud.analytics4github.controller;


import com.rhcloud.analytics4github.exception.TrendingException;
import com.rhcloud.analytics4github.service.GitHubTrendingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

/**
 * @author lyashenkogs.
 * @since 9/3/16
 */
@RestController
public class GitHubTrendingController {
    private static Logger LOG = LoggerFactory.getLogger(GitHubTrendingController.class);

    @Autowired
    private GitHubTrendingService trendingService;

    @RequestMapping(value = "/randomRequestTrendingRepoName", method = RequestMethod.GET)
    @ResponseBody
    public String getRandomTrendingRepo() {
        Random random = new Random();
        trendingService.getCachedTrendingRepos().size();
        int index = random.nextInt(trendingService.getCachedTrendingRepos().size());
        return trendingService.getCachedTrendingRepos().get(index);
    }
    @ExceptionHandler(value = {TrendingException.class,IllegalArgumentException.class})
    public String trendingException(HttpServletRequest req, TrendingException e) throws TrendingException,IllegalArgumentException {
        LOG.error("Trending Exception", e);
        // Nothing to do
        return e.getMessage();
    }

}

