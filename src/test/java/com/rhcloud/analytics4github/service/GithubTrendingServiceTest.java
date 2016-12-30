package com.rhcloud.analytics4github.service;

import com.rhcloud.analytics4github.exception.TrendingException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;


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
       assertTrue("We expect that there is more than zero trending repositories", trendingService.getTrendingRepos().size() > 0);
    }

    @Test(expected = TrendingException.class)
    public void noAccesToGithubtrendingTest(){
        //break the service
        GithubTrendingService.HTTPS_GITHUB_COM_TRENDING_SINCE_MONTHLY="";
        System.out.println(trendingService.getThisMonthTrendingRepos());
    }

}