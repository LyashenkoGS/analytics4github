package com.rhcloud.analytics4github.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.rhcloud.analytics4github.config.GitHubApiEndpoints;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author lyashenkogs.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GithubApiIteratorTest {
    private static Logger LOG = LoggerFactory.getLogger(GithubApiIteratorTest.class);
    private static String PROJECT_NAME = "DevLight-Mobile-Agency/InfiniteCycleViewPager";
    @Autowired
    private RestTemplate template;

    @Test
    public void testCommitsBatchNext() throws URISyntaxException, ExecutionException, InterruptedException {
        GithubApiIterator githubApiIterator = new GithubApiIterator(PROJECT_NAME, template, GitHubApiEndpoints.COMMITS);
        List<JsonNode> pages = new ArrayList<>();
        while (githubApiIterator.hasNext()) {
            pages.addAll(githubApiIterator.next(5));
        }
        githubApiIterator.close();
        LOG.debug("commits pages: " + pages.toString());
        Assert.assertFalse(pages.isEmpty());
    }

    @Test
    public void testStargazersBatchNext() throws URISyntaxException, ExecutionException, InterruptedException {
        GithubApiIterator githubApiIterator = new GithubApiIterator(PROJECT_NAME, template, GitHubApiEndpoints.STARGAZERS);
        List<JsonNode> pages = new ArrayList<>();
        while (githubApiIterator.hasNext()) {
            pages.addAll(githubApiIterator.next(5));
        }
        githubApiIterator.close();
        LOG.debug("stargazer pages: " + pages.toString());
        Assert.assertFalse(pages.isEmpty());
    }
}