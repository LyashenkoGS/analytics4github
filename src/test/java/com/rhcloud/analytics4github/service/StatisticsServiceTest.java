package com.rhcloud.analytics4github.service;

import com.rhcloud.analytics4github.domain.RequestToAPI;
import com.rhcloud.analytics4github.repository.RequestToApiRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author lyashenkogs.
 * @since 8/31/16
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StatisticsServiceTest {

    @Mock
    RequestToApiRepository repository;

    @InjectMocks
    @Autowired
    StatisticsService statisticsService;

    @Test
    public void getRequestsStatistic() throws Exception {
        ArrayList<RequestToAPI> requestToAPIs = new ArrayList<>();
        requestToAPIs.add(new RequestToAPI("testRepository", "/commits"));
        when(repository.findAll()).thenReturn(requestToAPIs);
        List<RequestToAPI> requestsStatistic = statisticsService.getRequestsStatistic();
        assertEquals(requestsStatistic, requestToAPIs);
        verify(repository, atLeastOnce());
    }

}