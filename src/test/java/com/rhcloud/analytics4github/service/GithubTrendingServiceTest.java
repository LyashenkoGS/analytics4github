package com.rhcloud.analytics4github.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

/**
 * @author lyashenkogs.
 * @since 9/3/16
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GithubTrendingServiceTest {

    @Autowired
    private GithubTrendingService trendingService;

    @Test
    public void getThisMonthTrendingRepos() throws Exception {
        System.out.println(trendingService.getThisMonthTrendingRepos());
  //      assertTrue(trendingService.getTrendingRepos().size() > 0);
    }

}