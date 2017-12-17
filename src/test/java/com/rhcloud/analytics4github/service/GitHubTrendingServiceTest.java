package com.rhcloud.analytics4github.service;

import com.rhcloud.analytics4github.TestApplicationContext;
import com.rhcloud.analytics4github.exception.TrendingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;


/**
 * @author lyashenkogs.
 * @since 9/3/16
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = TestApplicationContext.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GitHubTrendingServiceTest {

    @Autowired
    private GitHubTrendingService trendingService;

    @Test
    public void getThisMonthTrendingRepos() throws Exception {
        trendingService.parseTrendingReposWebPage();
        assertTrue("We expect that there is more than zero trending repositories", trendingService.getTrendingRepos().size() > 0);
    }

    @Test(expected = TrendingException.class)
    public void noAccessToGitHubTrending() throws Exception {
        //break the service
        String originalURL = GitHubTrendingService.GITHUB_TRENDING_URL;
        GitHubTrendingService.GITHUB_TRENDING_URL = "";
        trendingService.parseTrendingReposWebPage();
        trendingService.getTrendingRepos();
        //fix the service
        GitHubTrendingService.GITHUB_TRENDING_URL = originalURL;
    }

}