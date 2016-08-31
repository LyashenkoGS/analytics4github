package com.rhcloud.analytics4github.config;

import com.rhcloud.analytics4github.domain.RequestToAPI;
import com.rhcloud.analytics4github.repository.RequestToApiRepository;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.calls;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author lyashenkogs.
 */
@RunWith(SpringRunner.class)
//@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
public class StatisticsAggregationFilterTest {

    @Autowired
    private StatisticsAggregationFilter statisticsAggregationFilter;

    @Autowired
    private RequestToApiRepository repository;

    private String projectName = "StatisticsAggregationFilterTest";
     HttpServletRequest httpServletRequest;
    private HttpServletResponse httpServletResponse;
    private FilterChain filterChain;

    @Before
    public void setup() {
        // create the objects to be mocked
        httpServletRequest=null;
        httpServletRequest = mock(HttpServletRequest.class);
        httpServletResponse = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
        // mock the getRequestURI() response

        when(httpServletRequest.getQueryString()).thenReturn("projectName=" + projectName);
        when(httpServletRequest.getParameter("projectName")).thenReturn(projectName);
    }

    @Test
    public void testCommitsPersisting() throws IOException, ServletException {
        when(httpServletRequest.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/commits"));
        Filter filter = statisticsAggregationFilter.requestsStatisticAggregatorFilter();
        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);
     //   verify(httpServletRequest, atLeastOnce());
        RequestToAPI requestToAPI = this.repository.findByRepository(projectName).get(0);
        Assert.assertEquals(requestToAPI.getRepository(), projectName);
        //handcraft rollback
        this.repository.delete(requestToAPI);
    }

    @Test
    public void testStargazersPersisting() throws IOException, ServletException {
        when(httpServletRequest.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/stargazersPerMonth"));
        Filter filter = statisticsAggregationFilter.requestsStatisticAggregatorFilter();
        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);
    //    verify(httpServletRequest, atLeastOnce());
        RequestToAPI requestToAPI = this.repository.findByRepository(projectName).get(0);
        Assert.assertEquals(requestToAPI.getRepository(), projectName);
        //handcraft rollback
        this.repository.delete(requestToAPI);
    }

    @Test
    public void testUniqueContributorsPersisting() throws IOException, ServletException {
        when(httpServletRequest.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/uniqueContributors"));
        Filter filter = statisticsAggregationFilter.requestsStatisticAggregatorFilter();
        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);
      //  verify(httpServletRequest, atLeastOnce());
        RequestToAPI requestToAPI = this.repository.findByRepository(projectName).get(0);
        Assert.assertEquals(requestToAPI.getRepository(), projectName);
        //handcraft rollback
        this.repository.delete(requestToAPI);
    }



}