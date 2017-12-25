package com.rhcloud.analytics4github.controller;

import com.rhcloud.analytics4github.TestApplicationContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author lyashenkogs.
 * @since 8/31/16
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = TestApplicationContext.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StatisticsControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void testStatisticsEndpoint() {
        assertEquals(200, testRestTemplate.getForEntity("/statistics/requests", String.class).getStatusCodeValue());
    }
}