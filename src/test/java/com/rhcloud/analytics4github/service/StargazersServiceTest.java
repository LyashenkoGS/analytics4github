package com.rhcloud.analytics4github.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.springframework.test.web.client.ExpectedCount.min;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

/**
 * @author lyashenkogs
 */
@RunWith(SpringRunner.class)
@RestClientTest(StargazersService.class)
@AutoConfigureWebClient(registerRestTemplate=true)//because RestTempalate is @Autowired into the service
@TestPropertySource(locations = "classpath:application-test.properties")
public class StargazersServiceTest {
    private Logger logger = LoggerFactory.getLogger(StargazersServiceTest.class);

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private StargazersService service;

    @Test
    public void testServiceAccessGithubAtLeasOnce() throws IOException, URISyntaxException {
        server.expect(min(1), requestTo("https://api.github.com/repos/FallibleInc/security-guide-for-developers/stargazers"))
                .andRespond(MockRestResponseCreators.withSuccess());
     service.getThisWeekStargazersFrequencyPerProject("FallibleInc/security-guide-for-developers");
       server.verify();
    }

   }
