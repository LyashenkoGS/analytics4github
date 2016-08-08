package com.rhcloud.analytics4github.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProjectControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void testStargazersEndpoint(){
        assertEquals(this.testRestTemplate.getForEntity("/stargazers", String.class).getStatusCodeValue(),200);
    }
}
