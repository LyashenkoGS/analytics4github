package com.rhcloud.analytics4github.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.rhcloud.analytics4github.domain.Author;
import com.rhcloud.analytics4github.util.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author lyashenkogs.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UniqueContributorsServiceTest {
    private static String PROJECT = "e-government-ua/i";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UniqueContributorsService uniqueContributorsService;

    @Test
    public void isUniqueContributor() throws Exception {
        Author author1 = new Author("ElenaShebaldenkova", "");
        Author author2 = new Author("kurbpa", "");
        assertTrue(uniqueContributorsService.isUniqueContributor(PROJECT, author1, Instant.parse("2016-08-01T00:00:00Z")));
        assertFalse(uniqueContributorsService.isUniqueContributor(PROJECT, author2, Instant.parse("2016-08-01T00:00:00Z")));
    }

    @Test
    public void getUniqueContributors() throws InterruptedException, ExecutionException, URISyntaxException {
        uniqueContributorsService.getUniqueContributors(PROJECT, Utils.getThisMonthBeginInstant());
    }

    @Test
    public void getCommits() throws Exception {
        uniqueContributorsService.getCommits(PROJECT, Utils.getThisMonthBeginInstant());
    }

    @Test
    public void getAuthorNameAndEmail() throws InterruptedException, ExecutionException, URISyntaxException {
        List<JsonNode> commitsPerMonth = uniqueContributorsService.getCommits(PROJECT, Utils.getThisWeekBeginInstant());
        uniqueContributorsService.getAuthorNameAndEmail(commitsPerMonth);
    }
}