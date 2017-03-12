package com.rhcloud.analytics4github.service;

import com.rhcloud.analytics4github.dto.RequestFromFrontendDto;
import com.rhcloud.analytics4github.dto.ResponceForFrontendDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
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
@SpringBootTest
public class CommitsServiceIntegrationalTest {
    private static Logger LOG = LoggerFactory.getLogger(CommitsServiceIntegrationalTest.class);
    private static String PROJECT_NAME = "DevLight-Mobile-Agency/InfiniteCycleViewPager";

    @Autowired
    private CommitsService commitsService;

    /**
     * Assert that no errors occurred during parsing weekStargazers for repositories
     * in /resources/RepositoriesForTest.txt
     */
    @Test
    public void testGetThisWeekCommitsFrequencyPerProject() throws ClassNotFoundException, IOException, URISyntaxException, ExecutionException, InterruptedException {
        InputStream repositoriesList = new ClassPathResource("RepositoriesForTest.txt")
                .getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(repositoriesList));
        String repositoryName;
        while ((repositoryName = br.readLine()) != null) {
            LOG.debug(repositoryName);
            ResponceForFrontendDto thisWeekCommitsFrequencyPerProject = commitsService.getThisWeekCommitsFrequencyPerProject(repositoryName);
            LOG.debug(thisWeekCommitsFrequencyPerProject.toString());
        }
    }

    @Test
    public void getMonthStargazersListTest() throws InterruptedException, ExecutionException, URISyntaxException, IOException {
        RequestFromFrontendDto requestFromFrontendDto = new RequestFromFrontendDto();
        requestFromFrontendDto.setProjectName(PROJECT_NAME);
        requestFromFrontendDto.setStartPeriod(LocalDate.parse("2017-01-01"));
        requestFromFrontendDto.setEndPeriod(LocalDate.parse("2017-01-31"));
        List<LocalDate> monthStargazersList = commitsService.getMonthCommitsList(requestFromFrontendDto);
        LOG.debug(monthStargazersList.toString());
    }

    @Test
    public void getWeekCommitsListTest() throws InterruptedException, ExecutionException, URISyntaxException, IOException {
        List<LocalDate> weekCommitsList = commitsService.getWeekCommitsList(PROJECT_NAME);
        LOG.debug(weekCommitsList.toString());
    }

}