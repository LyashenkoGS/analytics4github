package com.rhcloud.analytics4github.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author lyashenkogs.
 * @since 9/3/16
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GithubTrendingServiceTest {

    @Autowired
    GithubTrendingService trendingService;

    @Test
    public void getThisMonthTrendingRepos() throws Exception {
        Assert.assertTrue(trendingService.getTrendingRepos().size() > 0);
    }

}