package com.rhcloud.analytics4github.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import static org.junit.Assert.assertEquals;

/**
 * @author lyashenkogs
 */
@RunWith(SpringRunner.class)
@RestClientTest(StargazersService.class)
public class StargazersServiceTest {
    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private StargazersService service;

    @Test
    public void testServiceDoesntChangeJSONFromServer() {
        //TODO externalize datasets for test
        String body = "{\"azaza\":\"azaza\"}";
        this.server.expect(MockRestRequestMatchers.requestTo("http://gturnquist-quoters.cfapps.io/api/random"))
                .andRespond(MockRestResponseCreators.withSuccess(body, MediaType.APPLICATION_JSON));

        assertEquals(service.getStargazersPerProject("ss").toString(), body);
    }
}
