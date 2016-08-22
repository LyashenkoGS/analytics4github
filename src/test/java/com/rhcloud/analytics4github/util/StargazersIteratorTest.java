package com.rhcloud.analytics4github.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

/**
 * @author lyashenkogs.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StargazersIteratorTest {

    @Autowired
    private RestTemplate template;


    @Test
    public void testStargazersBatchNext() throws URISyntaxException, ExecutionException, InterruptedException {
        StargazersIterator stargazersIterator = new StargazersIterator("mewo2/terrain", template);
        while (stargazersIterator.hasNext()) {
            stargazersIterator.next(10);
        }
    }

}