package com.rhcloud.analytics4github.repository;

import com.rhcloud.analytics4github.domain.RequestToAPI;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Integration test with an embedded mondodb
 *
 * @author lyashenkogs.
 */
@RunWith(SpringRunner.class)
/*@TestPropertySource(locations = "classpath:application-test.properties")*/
@SpringBootTest
public class RequestToApiRepositoryTest {
    private static Logger LOG = LoggerFactory.getLogger(RequestToApiRepositoryTest.class);

    @Autowired
    private RequestToApiRepository repository;

    @Test
    public void saveReadTest() {
        final String testRepositoryName = "testRepository";
        repository.save(new RequestToAPI(testRepositoryName, "/commits"));
        List<RequestToAPI> testRepository = repository.findByRepository(testRepositoryName);
        List<RequestToAPI> retrievedTestRepository = repository.findByRepository(testRepositoryName);
        LOG.info("get test repository from embedded mongoDb: " + testRepository.toString());
        assertEquals(retrievedTestRepository.size(), 1);
        //handcraft rollback
        repository.delete(retrievedTestRepository.get(0));
    }
}