package com.rhcloud.analytics4github.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.rhcloud.analytics4github.domain.Author;
import com.rhcloud.analytics4github.dto.RequestFromFrontendDto;
import com.rhcloud.analytics4github.util.Utils;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
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
    private static String UNIQUE_SINCE = "2016-08-01T00:00:00Z";
    private static String UNIQUE_UNTIL = "2016-08-31T00:00:00Z";

   /*@Autowired
  private RestTemplate restTemplate;*/

    @Autowired
    private UniqueContributorsService uniqueContributorsService;

    @Ignore
    @Test
    public void isUniqueContributor() throws Exception {
        assertTrue(uniqueContributorsService.isUniqueContributor(PROJECT, AUTHOR_1, Instant.parse(UNIQUE_SINCE),
                Instant.parse(UNIQUE_UNTIL)));
        assertFalse(uniqueContributorsService.isUniqueContributor(PROJECT, AUTHOR_2, Instant.parse(UNIQUE_SINCE),
                Instant.parse(UNIQUE_UNTIL)));
    }

    @Test
    public void getUniqueContributors() throws InterruptedException, ExecutionException, URISyntaxException {
        uniqueContributorsService.getUniqueContributors(PROJECT, Instant.parse(UNIQUE_SINCE),
                Instant.parse(UNIQUE_UNTIL));
    }

    @Test
    public void getCommits() throws Exception {
        uniqueContributorsService.getCommits(PROJECT, Instant.parse(UNIQUE_SINCE), Instant.parse(UNIQUE_UNTIL));
    }

    @Test
    public void getAuthorNameAndEmail() throws InterruptedException, ExecutionException, URISyntaxException {
        List<JsonNode> commitsPerMonth = uniqueContributorsService.getCommits(PROJECT, Utils.getThisWeekBeginInstant(), null);
        uniqueContributorsService.getAuthorNameAndEmail(commitsPerMonth);
    }

    @Test
    public void getFirstContributionDate() throws Exception {
        uniqueContributorsService.getFirstContributionDate(AUTHOR_1, PROJECT);
        uniqueContributorsService.getFirstContributionDate(AUTHOR_2, PROJECT);
    }

    @Test
    public void getFirstAuthorCommitFrequencyList() throws InterruptedException, ExecutionException, URISyntaxException {
        uniqueContributorsService.getFirstAuthorCommitFrequencyList(PROJECT, Instant.parse(UNIQUE_SINCE), Instant.parse(UNIQUE_UNTIL));
    }

    @Test
    public void uniqueContributorsFrequencyByMonth() throws InterruptedException, ExecutionException, URISyntaxException, IOException, ClassNotFoundException {
        RequestFromFrontendDto requestFromFrontendDto = new RequestFromFrontendDto();
        requestFromFrontendDto.setProjectName(PROJECT);
        requestFromFrontendDto.setStartPeriod(LocalDate.parse("2016-08-01"));
        requestFromFrontendDto.setEndPeriod(LocalDate.parse("2016-08-31"));
        uniqueContributorsService.getUniqueContributorsFrequencyByMonth(requestFromFrontendDto);
    }

    @Test
    public void uniqueContributorsFrequencyByWeek() throws InterruptedException, ExecutionException, URISyntaxException, IOException, ClassNotFoundException {
        uniqueContributorsService.getUniqueContributorsFrequencyByWeek(PROJECT);
    }
}