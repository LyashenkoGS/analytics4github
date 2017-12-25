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
import java.util.List;
import java.util.concurrent.ExecutionException;


@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = TestApplicationContext.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StargazersServiceTest {
    private static Logger LOG = LoggerFactory.getLogger(com.rhcloud.analytics4github.config.InterceptorsIntegrationalTest.class);
    private static String PROJECT_NAME = "mewo2/terrain";

    @Autowired
    private StargazersService stargazersService;

    /**
     * Assert that no errors occurred during parsing weekStargazers for repositories
     * in /resources/RepositoriesForTest.txt
     */
    @Test
    public void thisWeekStargazersFrequencyPerProjectTest() throws ClassNotFoundException, IOException, URISyntaxException, ExecutionException, InterruptedException, GitHubRESTApiException {
        InputStream repositoriesList = new ClassPathResource("RepositoriesForTest.txt")
                .getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(repositoriesList));
        String repositoryName;
        while ((repositoryName = br.readLine()) != null) {
            LOG.debug(repositoryName);
            RequestFromFrontendDto requestFromFrontendDto = new RequestFromFrontendDto();
            requestFromFrontendDto.setAuthor(repositoryName.split("/")[0]);
            requestFromFrontendDto.setRepository(repositoryName.split("/")[1]);
            requestFromFrontendDto.setStartPeriod(LocalDate.parse("2017-01-01"));
            requestFromFrontendDto.setEndPeriod(LocalDate.parse("2017-01-31"));
            ResponceForFrontendDto thisWeekStargazersFrequencyPerProject = stargazersService.stargazersFrequency(requestFromFrontendDto);
            LOG.debug(thisWeekStargazersFrequencyPerProject.toString());
        }
    }

    @Test
    public void getMonthStargazersListTest() throws InterruptedException, ExecutionException, URISyntaxException, GitHubRESTApiException {
        RequestFromFrontendDto requestFromFrontendDto = new RequestFromFrontendDto();
        requestFromFrontendDto.setAuthor(PROJECT_NAME.split("/")[0]);
        requestFromFrontendDto.setRepository(PROJECT_NAME.split("/")[1]);
        requestFromFrontendDto.setStartPeriod(LocalDate.parse("2017-01-01"));
        requestFromFrontendDto.setEndPeriod(LocalDate.parse("2017-01-31"));
        List<LocalDate> monthStargazersList = stargazersService.getMonthStargazersList(requestFromFrontendDto);
        LOG.debug(monthStargazersList.toString());
    }

    @Test
    public void getWeekStargazersListTest() throws InterruptedException, ExecutionException, URISyntaxException, IOException, GitHubRESTApiException {
        RequestFromFrontendDto requestFromFrontendDto = new RequestFromFrontendDto();
        requestFromFrontendDto.setAuthor(PROJECT_NAME.split("/")[0]);
        requestFromFrontendDto.setRepository(PROJECT_NAME.split("/")[1]);
        List<LocalDate> monthStargazersList = stargazersService.getWeekStargazersList(requestFromFrontendDto);
        LOG.debug(monthStargazersList.toString());
    }
}