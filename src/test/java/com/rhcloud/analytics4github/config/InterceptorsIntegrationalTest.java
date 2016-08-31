package com.rhcloud.analytics4github.config;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.rhcloud.analytics4github.service.StargazersService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InterceptorsIntegrationalTest {
    private static Logger LOG = LoggerFactory.getLogger(InterceptorsIntegrationalTest.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StargazersService stargazersService;

    @Test
    public void testIncreaseRateLimitByOAuthTokenInterceptor() throws URISyntaxException {
        //given a REST template(@Autowired), an Interceptor (@Autowired) and an URL
        String URL = "https://api.github.com/user";
        //restTemplate.setInterceptors(Collections.singletonList(oAuthTokenInterceptor));
        LOG.debug("Send GET request with OAuth token in the header to: " + URL);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(URL, String.class);
        String rateLimit = responseEntity.getHeaders().get("X-RateLimit-Limit").get(0);
        LOG.debug("rate limit is:" + rateLimit);
        //then rate limit must be 5000
        assertEquals(Integer.parseInt(rateLimit), 5000);
    }

    @Test
    public void serviceIntegrationalTest() throws ClassNotFoundException, IOException, URISyntaxException, ExecutionException, InterruptedException {
        ArrayNode thisWeekStargazersFrequencyPerProject = stargazersService.getThisWeekStargazersFrequencyPerProject("mewo2/terrain");
        LOG.debug(thisWeekStargazersFrequencyPerProject.toString());
    }

}