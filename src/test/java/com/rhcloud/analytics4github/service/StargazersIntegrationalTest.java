package com.rhcloud.analytics4github.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author lyashenkogs.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StargazersIntegrationalTest {

    @Autowired
    StargazersService service;

    @Test
    public void testGetStargazersFrequencyMap() throws IOException, URISyntaxException {
        service.getThisWeekStargazersFrequencyPerProject("FallibleInc/security-guide-for-developers");
    }
}
