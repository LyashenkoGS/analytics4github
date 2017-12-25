package com.rhcloud.analytics4github.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.rhcloud.analytics4github.TestApplicationContext;
import com.rhcloud.analytics4github.config.GitHubApiEndpoints;
import com.rhcloud.analytics4github.dto.RequestFromFrontendDto;
import com.rhcloud.analytics4github.exception.GitHubRESTApiException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author lyashenkogs.
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = TestApplicationContext.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GitHubApiIteratorTest {
    private static Logger LOG = LoggerFactory.getLogger(GitHubApiIteratorTest.class);
    private static String PROJECT_NAME = "DevLight-Mobile-Agency/InfiniteCycleViewPager";
    @Autowired
    private RestTemplate template;


    @Test
    public void testCommitsBatchNext() throws URISyntaxException, ExecutionException, InterruptedException, GitHubRESTApiException {
        GitHubApiIterator gitHubApiIterator = new GitHubApiIterator(PROJECT_NAME, template, GitHubApiEndpoints.COMMITS);
        List<JsonNode> pages = new ArrayList<>();
        while (gitHubApiIterator.hasNext()) {
            pages.addAll(gitHubApiIterator.next(5));
        }
        gitHubApiIterator.close();
        LOG.debug("commits pages: " + pages.toString());
        assertFalse(pages.isEmpty());
    }

    @Test
    public void testStargazersBatchNext() throws URISyntaxException, ExecutionException, InterruptedException, GitHubRESTApiException {
        GitHubApiIterator gitHubApiIterator = new GitHubApiIterator(PROJECT_NAME, template, GitHubApiEndpoints.STARGAZERS);
        List<JsonNode> pages = new ArrayList<>();
        while (gitHubApiIterator.hasNext()) {
            pages.addAll(gitHubApiIterator.next(5));
        }
        gitHubApiIterator.close();
        LOG.debug("stargazer pages: " + pages.toString());
        assertFalse(pages.isEmpty());
    }

    @Test
    public void testHasNext() throws URISyntaxException, GitHubRESTApiException {
        RequestFromFrontendDto requestFromFrontendDto = new RequestFromFrontendDto();
        requestFromFrontendDto.setAuthor("terryum");
        requestFromFrontendDto.setRepository("awesome-deep-learning-papers");
        requestFromFrontendDto.setStartPeriod(LocalDate.parse("2017-01-01"));
        requestFromFrontendDto.setEndPeriod(LocalDate.parse("2017-01-31"));
        GitHubApiIterator stargazersIterator = new GitHubApiIterator(requestFromFrontendDto.getAuthor() + "/"
                + requestFromFrontendDto.getRepository(), template,
                GitHubApiEndpoints.COMMITS, Utils.getPeriodInstant(requestFromFrontendDto.getStartPeriod()),
                Utils.getPeriodInstant(requestFromFrontendDto.getEndPeriod()));
        assertTrue("We expect hasNext equals false", stargazersIterator.hasNext());
    }

}