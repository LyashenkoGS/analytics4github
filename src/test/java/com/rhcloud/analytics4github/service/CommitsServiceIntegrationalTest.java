package com.rhcloud.analytics4github.service;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.rhcloud.analytics4github.exception.GitHubRESTApiException;
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
    public void testGetThisWeekCommitsFrequencyPerProject() throws ClassNotFoundException, IOException, URISyntaxException, ExecutionException, InterruptedException, GitHubRESTApiException {
        InputStream repositoriesList = new ClassPathResource("RepositoriesForTest.txt")
                .getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(repositoriesList));
        String repositoryName;
        while ((repositoryName = br.readLine()) != null) {
            LOG.debug(repositoryName);
            ArrayNode thisWeekCommitsFrequencyPerProject = commitsService.getThisWeekCommitsFrequencyPerProject(repositoryName);
            LOG.debug(thisWeekCommitsFrequencyPerProject.toString());
        }
    }


    @Test
    public void getMonthStargazersListTest() throws InterruptedException, ExecutionException, URISyntaxException, IOException, GitHubRESTApiException {
        List<LocalDate> monthStargazersList = commitsService.getMonthCommitsList(PROJECT_NAME);
        LOG.debug(monthStargazersList.toString());
    }


    @Test
    public void getWeekCommitsListTest() throws InterruptedException, ExecutionException, URISyntaxException, IOException, GitHubRESTApiException {
        List<LocalDate> weekCommitsList = commitsService.getWeekCommitsList(PROJECT_NAME);
        LOG.debug(weekCommitsList.toString());
    }

}