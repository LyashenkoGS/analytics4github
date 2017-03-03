package com.rhcloud.analytics4github.controller;

import com.rhcloud.analytics4github.exception.TrendingException;
import com.rhcloud.analytics4github.service.GitHubTrendingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author lyashenkogs.
 * @since 9/3/16
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GitHubTrendingControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    GitHubTrendingService gitHubTrendingService;

    @Test
    public void getRandomTrendingRepo() throws TrendingException {
        gitHubTrendingService.parseTrendingReposWebPage();
        assertEquals(this.testRestTemplate.getForEntity("/randomRequestTrendingRepoName", String.class).getStatusCodeValue(), 200);
    }

}