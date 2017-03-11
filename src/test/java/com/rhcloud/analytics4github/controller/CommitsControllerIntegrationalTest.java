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
public class CommitsControllerIntegrationalTest {
    private static String PROJECT_NAME = "mewo2/terrain";
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void testCommitsPerWeekEndpoint() {
        assertEquals(this.testRestTemplate.getForEntity("/commits?projectName=" + PROJECT_NAME, String.class).getStatusCodeValue(), 200);
    }

    @Test
    public void testCommitsPerMonthEndpoint() {
        assertEquals(this.testRestTemplate.getForEntity("/stargazersPerMonth?projectName=mewo2/terrain&startPeriod=2017-01-01&endPeriod=2017-01-31", String.class).getStatusCodeValue(), 200);
    }
}
