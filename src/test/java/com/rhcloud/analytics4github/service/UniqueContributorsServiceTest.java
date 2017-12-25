package com.rhcloud.analytics4github.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.rhcloud.analytics4github.TestApplicationContext;
import com.rhcloud.analytics4github.domain.Author;
import com.rhcloud.analytics4github.dto.RequestFromFrontendDto;
import com.rhcloud.analytics4github.dto.ResponceForFrontendDto;
import com.rhcloud.analytics4github.exception.GitHubRESTApiException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

/**
 * @author lyashenkogs.
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = TestApplicationContext.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UniqueContributorsServiceTest {
    private static final String PROJECT = "LyashenkoGS/analytics4github";
    private static final Author AUTHOR_1 = new Author("iivanyshyn", "");
    private static final Author AUTHOR_2 = new Author("lyashenkogs", "");
    private static final String UNIQUE_SINCE = "2016-12-01T00:00:00Z";
    private static final String UNIQUE_UNTIL = "2016-12-31T00:00:00Z";

    @Autowired
    private UniqueContributorsService uniqueContributorsService;

    @Test
    public void isUniqueContributor() throws Exception {
        assertTrue(uniqueContributorsService.isUniqueContributor(PROJECT, AUTHOR_1, Instant.parse(UNIQUE_SINCE)));
        assertFalse(uniqueContributorsService.isUniqueContributor(PROJECT, AUTHOR_2, Instant.parse(UNIQUE_SINCE)));
    }

    @Test
    public void getUniqueContributors() throws InterruptedException, ExecutionException, URISyntaxException, GitHubRESTApiException {
        Set<Author> uniqueContributors = uniqueContributorsService.getUniqueContributors(PROJECT, Instant.parse(UNIQUE_SINCE),
                Instant.parse(UNIQUE_UNTIL));
        assertEquals("[Author{name='Grog', email='grigoryi.lyashenko@heg.com'}, Author{name='vshpelyk'," +
                " email='vshpelyk@gmail.com'}, Author{name='iivanyshyn', email='iivanyshyn@gmail.com'}, " +
                "Author{name='naz1719', email='khimin1719@gmail.com'}]", uniqueContributors.toString());
    }


    @Test
    public void getAuthorNameAndEmail() throws InterruptedException, ExecutionException, URISyntaxException, GitHubRESTApiException {
        List<JsonNode> commitsPerMonth = uniqueContributorsService.getCommits(PROJECT, Instant.parse(UNIQUE_SINCE), Instant.parse(UNIQUE_UNTIL));
        Set<Author> authorNameAndEmail = uniqueContributorsService.getAuthorNameAndEmail(commitsPerMonth);
        assertEquals("[Author{name='Grog', email='grigoryi.lyashenko@heg.com'}, Author{name='vshpelyk'," +
                        " email='vshpelyk@gmail.com'}, Author{name='iivanyshyn', email='iivanyshyn@gmail.com'}, Author{name='naz1719', " +
                        "email='khimin1719@gmail.com'}, Author{name='lyashenkogs', email='lyashenkogs@gmail.com'}]",
                authorNameAndEmail.toString());
    }

    @Test
    public void getFirstContributionDate() throws Exception {
        LocalDate firstContributionDate = uniqueContributorsService.getFirstContributionDate(AUTHOR_1, PROJECT);
        assertEquals("2016-12-22", firstContributionDate.toString());
        LocalDate firstContributionDate1 = uniqueContributorsService.getFirstContributionDate(AUTHOR_2, PROJECT);
        assertEquals("2017-03-20", firstContributionDate1.toString());
    }

    @Test
    public void getFirstAuthorCommitFrequencyList() throws InterruptedException, ExecutionException, URISyntaxException, GitHubRESTApiException {
        List<LocalDate> firstAuthorCommitFrequencyList = uniqueContributorsService
                .getFirstAuthorCommitFrequencyList(PROJECT, Instant.parse(UNIQUE_SINCE), Instant.parse(UNIQUE_UNTIL));
        assertEquals("[2016-12-08, 2016-12-22, 2016-12-22, 2016-12-30]", firstAuthorCommitFrequencyList.toString());
    }

    @Test
    public void uniqueContributorsFrequencyByMonth() throws InterruptedException, ExecutionException, URISyntaxException, IOException, ClassNotFoundException, GitHubRESTApiException {
        RequestFromFrontendDto requestFromFrontendDto = new RequestFromFrontendDto();
        requestFromFrontendDto.setAuthor(PROJECT.split("/")[0]);
        requestFromFrontendDto.setRepository(PROJECT.split("/")[1]);
        requestFromFrontendDto.setStartPeriod(LocalDate.parse("2016-08-01"));
        requestFromFrontendDto.setEndPeriod(LocalDate.parse("2016-08-31"));
        ResponceForFrontendDto uniqueContributorsFrequency = uniqueContributorsService.getUniqueContributorsFrequency(requestFromFrontendDto);
        assertEquals("ResponceForFrontendDto{name='Stars', " +
                        "requestsLeft=0, data=[0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0]}",
                uniqueContributorsFrequency.toString());
    }

    @Test
    public void uniqueContributorsFrequencyByWeek() throws InterruptedException, ExecutionException, URISyntaxException, IOException, ClassNotFoundException, GitHubRESTApiException {
        RequestFromFrontendDto requestFromFrontendDto = new RequestFromFrontendDto();
        requestFromFrontendDto.setAuthor(PROJECT.split("/")[0]);
        requestFromFrontendDto.setRepository(PROJECT.split("/")[1]);
        requestFromFrontendDto.setStartPeriod(LocalDate.parse("2016-08-01"));
        requestFromFrontendDto.setEndPeriod(LocalDate.parse("2016-08-07"));
        ResponceForFrontendDto uniqueContributorsFrequency = uniqueContributorsService.getUniqueContributorsFrequency(requestFromFrontendDto);
        assertEquals("ResponceForFrontendDto{name='Stars', requestsLeft=0, data=[0, 0, 0, 0, 0, 0, 0]}",
                uniqueContributorsFrequency.toString());
    }
}