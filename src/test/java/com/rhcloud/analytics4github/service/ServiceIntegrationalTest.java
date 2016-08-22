package com.rhcloud.analytics4github.service;

import com.fasterxml.jackson.databind.node.ArrayNode;

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
import java.util.concurrent.ExecutionException;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceIntegrationalTest {
    private static Logger LOG = LoggerFactory.getLogger(com.rhcloud.analytics4github.config.InterceptorsIntegrationalTest.class);

    @Autowired
    private StargazersService stargazersService;

    /**
     * Assert that no errors occurred during parsing weekStargazers for repositories
     * in /resources/RepositoriesForTest.txt
     */
    @Test
    public void serviceIntegrationalTest() throws ClassNotFoundException, IOException, URISyntaxException, ExecutionException, InterruptedException {
        InputStream repositoriesList = new ClassPathResource("RepositoriesForTest.txt")
                .getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(repositoriesList));
        String repositoryName;
        while ((repositoryName = br.readLine()) != null) {
            LOG.debug(repositoryName);
            ArrayNode thisWeekStargazersFrequencyPerProject = stargazersService.getThisWeekStargazersFrequencyPerProject(repositoryName);
            LOG.debug(thisWeekStargazersFrequencyPerProject.toString());
        }
    }

}