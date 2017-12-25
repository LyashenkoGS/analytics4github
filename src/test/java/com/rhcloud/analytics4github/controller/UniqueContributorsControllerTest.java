package com.rhcloud.analytics4github.controller;

import com.rhcloud.analytics4github.TestApplicationContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * Integration test. Thought test fully functional Application is running
 * on free random port and the emulating real http requests to endpoints.
 *
 * @author lyashenkogs
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = TestApplicationContext.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UniqueContributorsControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void uniqueContributorsPerWeek() {
        ResponseEntity<String> response = testRestTemplate.getForEntity("/mewo2/terrain/uniqueContributors?startPeriod=2017-01-01&endPeriod=2017-01-07", String.class);
        //language=JSON
        assertEquals("[{\"name\":\"Stars\",\"requestsLeft\":0,\"data\":[0,0,0,0,0,0,0]}]", response.getBody());
    }

    @Test
    public void uniqueContributorsPerMonth() {
        ResponseEntity<String> response = testRestTemplate.getForEntity("/mewo2/terrain/uniqueContributors?startPeriod=2017-01-01&endPeriod=2017-01-31", String.class);
        //language=JSON
        assertEquals("[{\"name\":\"Stars\",\"requestsLeft\":0,\"data\":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]}]", response.getBody());
    }
}