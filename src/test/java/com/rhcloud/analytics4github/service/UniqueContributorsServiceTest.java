package com.rhcloud.analytics4github.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.rhcloud.analytics4github.domain.Author;
import com.rhcloud.analytics4github.exception.GitHubRESTApiException;
import com.rhcloud.analytics4github.util.Utils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
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
    //private static Logger LOG = LoggerFactory.getLogger(UniqueContributorsServiceTest.class);
    private static String PROJECT = "e-government-ua/i";
    private static Author AUTHOR_1 = new Author("ElenaShebaldenkova", "");
    private static Author AUTHOR_2 = new Author("kurbpa", "");

   /*@Autowired
  private RestTemplate restTemplate;*/

    @Autowired
    private UniqueContributorsService uniqueContributorsService;

    @Test
    public void isUniqueContributor() throws Exception {
        assertTrue(uniqueContributorsService.isUniqueContributor(PROJECT, AUTHOR_1, Instant.parse("2016-08-01T00:00:00Z")));
        assertFalse(uniqueContributorsService.isUniqueContributor(PROJECT, AUTHOR_2, Instant.parse("2016-08-01T00:00:00Z")));
    }

    @Test
    public void getUniqueContributors() throws InterruptedException, ExecutionException, URISyntaxException, GitHubRESTApiException {
        uniqueContributorsService.getUniqueContributors(PROJECT, Utils.getThisMonthBeginInstant());
    }

    @Test
    public void getCommits() throws Exception {
        uniqueContributorsService.getCommits(PROJECT, Utils.getThisMonthBeginInstant());
    }

    @Test
    public void getAuthorNameAndEmail() throws InterruptedException, ExecutionException, URISyntaxException, GitHubRESTApiException {
        List<JsonNode> commitsPerMonth = uniqueContributorsService.getCommits(PROJECT, Utils.getThisWeekBeginInstant());
        uniqueContributorsService.getAuthorNameAndEmail(commitsPerMonth);
    }

    @Test
    public void getFirstContributionDate() throws Exception {
        uniqueContributorsService.getFirstContributionDate(AUTHOR_1, PROJECT);
        uniqueContributorsService.getFirstContributionDate(AUTHOR_2, PROJECT);
    }

    @Test
    public void getFirstAuthorCommitFrequencyList() throws InterruptedException, ExecutionException, URISyntaxException, GitHubRESTApiException {
        uniqueContributorsService.getFirstAuthorCommitFrequencyList(PROJECT, Utils.getThisMonthBeginInstant());
    }

    @Test
    public void uniqueContributorsFrequencyByMonth() throws InterruptedException, ExecutionException, URISyntaxException, IOException, ClassNotFoundException, GitHubRESTApiException {
        uniqueContributorsService.getUniqueContributorsFrequencyByMonth(PROJECT);
    }

    @Test
    public void uniqueContributorsFrequencyByWeek() throws InterruptedException, ExecutionException, URISyntaxException, IOException, ClassNotFoundException, GitHubRESTApiException {
        uniqueContributorsService.getUniqueContributorsFrequencyByWeek(PROJECT);
    }
}