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


@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = TestApplicationContext.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommitsControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void commitsPerWeek() {
        ResponseEntity<String> response = testRestTemplate.getForEntity("/mewo2/terrain/commits?startPeriod=2016-08-10&endPeriod=2016-08-17", String.class);
        //language=JSON
        assertEquals("[{\"name\":\"Stars\",\"requestsLeft\":0,\"data\":[3,0,1,0,0,0,0]}]", response.getBody());
    }

    @Test
    public void commitsPerMonth() {
        ResponseEntity<String> response = testRestTemplate.getForEntity("/mewo2/terrain/commits?startPeriod=2016-08-01&endPeriod=2016-08-31", String.class);
        //language=JSON
        assertEquals("[{\"name\":\"Stars\",\"requestsLeft\":0,\"data\":[0,0,0,0,0,0,0,0,0,1,0,0,0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]}]", response.getBody());
    }
}
