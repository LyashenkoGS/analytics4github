package com.rhcloud.analytics4github.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * Integration test. Thought test fully functional Application is running
 * on free random port and the emulating real http requests to endpoints.
 *
 * @author lyashenkogs
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StargazersControllerIntegrationalTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void testStargazersEndpoint() {
        assertEquals(200, testRestTemplate.getForEntity("/stargazers?projectName=mewo2/terrain",
                String.class).getStatusCodeValue());
    }

    @Test
    public void testStargazersPerMonthEndpoint() {
        assertEquals(200, testRestTemplate.getForEntity(
                "/stargazersPerMonth?projectName=mewo2/terrain&startPeriod=2017-01-01&endPeriod=2017-01-31",
                String.class).getStatusCodeValue());
    }
}
