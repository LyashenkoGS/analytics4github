package com.rhcloud.analytics4github.service;

import com.rhcloud.analytics4github.TestApplicationContext;
import com.rhcloud.analytics4github.dto.RequestFromFrontendDto;
import com.rhcloud.analytics4github.dto.ResponceForFrontendDto;
import com.rhcloud.analytics4github.exception.GitHubRESTApiException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = TestApplicationContext.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommitsServiceTest {
    private static Logger LOG = LoggerFactory.getLogger(CommitsServiceTest.class);
    private static String PROJECT_NAME = "DevLight-Mobile-Agency/InfiniteCycleViewPager";

    @Autowired
    private CommitsService commitsService;

    /**
     * Assert that no errors occurred during parsing weekStargazers for repositories
     * in /resources/RepositoriesForTest.txt
     */
    @Test
    public void getThisWeekCommitsFrequencyPerProject_SmokeTest() throws ClassNotFoundException, IOException, URISyntaxException, ExecutionException, InterruptedException, GitHubRESTApiException {
        InputStream repositoriesList = new ClassPathResource("RepositoriesForTest.txt")
                .getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(repositoriesList));
        String repositoryName;
        while ((repositoryName = br.readLine()) != null) {
            RequestFromFrontendDto requestFromFrontendDto = new RequestFromFrontendDto();
            requestFromFrontendDto.setAuthor(repositoryName.split("/")[0]);
            requestFromFrontendDto.setRepository(repositoryName.split("/")[1]);
            requestFromFrontendDto.setStartPeriod(LocalDate.parse("2016-08-01"));
            requestFromFrontendDto.setEndPeriod(LocalDate.parse("2016-08-07"));
            LOG.info(repositoryName);
            ResponceForFrontendDto commitsDataList = commitsService.getCommitsFrequency(requestFromFrontendDto);
            LOG.debug(commitsDataList.toString());
        }
    }

    @Test
    public void getMonthCommitsList() throws InterruptedException, ExecutionException, URISyntaxException, GitHubRESTApiException {
        RequestFromFrontendDto requestFromFrontendDto = new RequestFromFrontendDto();
        requestFromFrontendDto.setAuthor(PROJECT_NAME.split("/")[0]);
        requestFromFrontendDto.setRepository(PROJECT_NAME.split("/")[1]);
        requestFromFrontendDto.setStartPeriod(LocalDate.parse("2016-08-01"));
        requestFromFrontendDto.setEndPeriod(LocalDate.parse("2016-08-31"));
        List<LocalDate> monthStargazersList = commitsService.getCommitsList(requestFromFrontendDto);
        assertEquals("[2016-08-30, 2016-08-25, 2016-08-25, 2016-08-24, 2016-08-22, 2016-08-22, 2016-08-22, " +
                        "2016-08-22, 2016-08-22, 2016-08-22, 2016-08-22, 2016-08-22, 2016-08-22, 2016-08-22, 2016-08-22]",
                monthStargazersList.toString());
    }

    @Test
    public void getMonthCommits_EmptyList() throws InterruptedException, GitHubRESTApiException, ExecutionException, URISyntaxException {
        RequestFromFrontendDto requestFromFrontendDto = new RequestFromFrontendDto();
        requestFromFrontendDto.setAuthor("terryum");
        requestFromFrontendDto.setRepository("awesome-deep-learning-papers");
        requestFromFrontendDto.setStartPeriod(LocalDate.parse("2017-01-01"));
        requestFromFrontendDto.setEndPeriod(LocalDate.parse("2017-01-31"));
        List<LocalDate> monthStargazersList = commitsService.getCommitsList(requestFromFrontendDto);
        assertEquals("We expect an empty list", new ArrayList<LocalDate>(), monthStargazersList);
    }

    @Test
    public void getWeekCommitsList() throws InterruptedException, ExecutionException, URISyntaxException, GitHubRESTApiException {
        RequestFromFrontendDto requestFromFrontendDto = new RequestFromFrontendDto();
        requestFromFrontendDto.setAuthor(PROJECT_NAME.split("/")[0]);
        requestFromFrontendDto.setRepository(PROJECT_NAME.split("/")[1]);
        requestFromFrontendDto.setStartPeriod(LocalDate.parse("2016-08-22"));
        requestFromFrontendDto.setEndPeriod(LocalDate.parse("2016-08-28"));
        List<LocalDate> weekCommitsList = commitsService.getCommitsList(requestFromFrontendDto);
        assertEquals("[2016-08-25, 2016-08-25, 2016-08-24, 2016-08-22, 2016-08-22, 2016-08-22, 2016-08-22, " +
                        "2016-08-22, 2016-08-22, 2016-08-22, 2016-08-22, 2016-08-22, 2016-08-22, 2016-08-22]"
                , weekCommitsList.toString());
    }

}