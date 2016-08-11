package com.rhcloud.analytics4github.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * @author lyashenkogs
 */
@RunWith(SpringRunner.class)
@RestClientTest(StargazersService.class)
@TestPropertySource(locations="classpath:application-test.properties")
public class StargazersServiceTest {
    private Logger logger = LoggerFactory.getLogger(StargazersServiceTest.class);

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private StargazersService service;

    @Test
    public void testServiceDoesntChangeJSONFromServer() throws IOException {
        //Todo remove mocks
        InputStream stargazersFileInpStream=(new ClassPathResource("mockWeekStargazers.json")
                .getInputStream());
        JsonNode stargazerJSON = new ObjectMapper().readTree(stargazersFileInpStream);

        this.server.expect(MockRestRequestMatchers.requestTo("http://gturnquist-quoters.cfapps.io/api/random"))
                .andRespond(MockRestResponseCreators.withSuccess(stargazerJSON.toString(), MediaType.APPLICATION_JSON));

        assertEquals(service.getThisWeekStargazersFrequencyPerProject("mockProjectName").toString(), stargazerJSON.toString());
    }
}
