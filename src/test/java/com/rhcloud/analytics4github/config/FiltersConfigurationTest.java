package com.rhcloud.analytics4github.config;

import com.rhcloud.analytics4github.domain.RequestToAPI;
import com.rhcloud.analytics4github.repository.RequestToApiRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author lyashenkogs.
 */
@RunWith(SpringRunner.class)
//@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
public class FiltersConfigurationTest {

    HttpServletRequest httpServletRequest;
    @Autowired
    private FiltersConfiguration filtersConfiguration;
    @Autowired
    private RequestToApiRepository repository;
    private String projectName = "FiltersConfigurationTest";
    private HttpServletResponse httpServletResponse;
    private FilterChain filterChain;

    @Before
    public void setup() {
        // create the objects to be mocked
        httpServletRequest = null;
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
        Filter filter = filtersConfiguration.requestsStatisticAggregatorFilter();
        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);
        //   verify(httpServletRequest, atLeastOnce());
        RequestToAPI requestToAPI = this.repository.findByRepository(projectName).get(0);
        assertEquals(requestToAPI.getRepository(), projectName);
        //handcraft rollback
        this.repository.delete(requestToAPI);
    }

    @Test
    public void testStargazersPersisting() throws IOException, ServletException {
        when(httpServletRequest.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/stargazersPerMonth"));
        Filter filter = filtersConfiguration.requestsStatisticAggregatorFilter();
        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);
        //    verify(httpServletRequest, atLeastOnce());
        RequestToAPI requestToAPI = this.repository.findByRepository(projectName).get(0);
        assertEquals(requestToAPI.getRepository(), projectName);
        //handcraft rollback
        this.repository.delete(requestToAPI);
    }

    @Test
    public void testUniqueContributorsPersisting() throws IOException, ServletException {
        when(httpServletRequest.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/uniqueContributors"));
        Filter filter = filtersConfiguration.requestsStatisticAggregatorFilter();
        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);
        //  verify(httpServletRequest, atLeastOnce());
        RequestToAPI requestToAPI = this.repository.findByRepository(projectName).get(0);
        assertEquals(requestToAPI.getRepository(), projectName);
        //handcraft rollback
        this.repository.delete(requestToAPI);
    }

    @Test
    public void testCreateNewCoockies() throws IOException, ServletException {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        mockHttpServletRequest.setRequestURI("http://localhost:8080/uniqueContributors");
        assertNull("We expect no cookies", mockHttpServletResponse.getCookie("freeTokens"));
        Filter filter = filtersConfiguration.userRequestsLimitationFilter();
        filter.doFilter(mockHttpServletRequest, mockHttpServletResponse, filterChain);
        System.out.println(mockHttpServletRequest);
        assertEquals("We expect a cookie with 20 requests", mockHttpServletResponse.getCookie("freeTokens").getValue(),
                FiltersConfiguration.FREE_REQUESTS_NUMBER_PER_NEW_USER);
    }

    @Test
    public void testDicreseCoockiesValue() throws IOException, ServletException {

    }

}