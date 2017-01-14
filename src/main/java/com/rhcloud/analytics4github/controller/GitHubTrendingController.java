package com.rhcloud.analytics4github.controller;


import com.rhcloud.analytics4github.exception.TrendingException;
import com.rhcloud.analytics4github.service.GitHubTrendingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
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
    public ResponseEntity<String> getRandomTrendingRepo() throws TrendingException,IllegalArgumentException {

        try {
            Random random = new Random();
            int index = random.nextInt(trendingService.getTrendingRepos().size());
            return new ResponseEntity<String>(trendingService.getTrendingRepos().get(index), HttpStatus.OK);
        } catch (Exception e) {
            LOG.error("tCan't parse top trending repositories !");
            return new ResponseEntity<String>("Can't parse top trending repositories,    "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostConstruct
    public void initIt() {
        try {
            trendingService.parseTrendingReposWebPage();
        } catch (TrendingException exception) {
            LOG.error(String.valueOf(exception));
        }
    }


}

