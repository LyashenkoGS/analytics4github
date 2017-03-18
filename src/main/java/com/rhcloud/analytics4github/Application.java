package com.rhcloud.analytics4github;

import com.rhcloud.analytics4github.exception.TrendingException;
import com.rhcloud.analytics4github.service.GitHubTrendingService;
import com.rhcloud.analytics4github.util.GithubApiIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;


@SpringBootApplication
public class Application {

    private static Logger LOG = LoggerFactory.getLogger(Application.class);

    @Autowired
    private GitHubTrendingService gitHubTrendingService;

    @Autowired
    private RestTemplate restTemplate;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void initIt() {
        try {
            GithubApiIterator.initializeRequestsLeft(restTemplate);
            gitHubTrendingService.parseTrendingReposWebPage();
        } catch (TrendingException exception) {
            LOG.error(String.valueOf(exception));
        }
    }
}
