package com.rhcloud.analytics4github.config;

import com.rhcloud.analytics4github.domain.RequestToAPI;
import com.rhcloud.analytics4github.repository.RequestToApiRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author lyashenkogs.
 */
@Configuration
public class FiltersConfiguration {
    private static Logger LOG = LoggerFactory.getLogger(FiltersConfiguration.class);

    @Autowired
    private RequestToApiRepository requestToApiRepository;

    @Bean
    public FilterRegistrationBean requestsStatisticsRegistrationBean() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setName("requestsStatisticsRegistrationBean");//need to set name to avoid conflicts with other FilterRegistrationBeans'
        bean.setFilter(requestsStatisticAggregatorFilter());
        bean.addUrlPatterns("/commitsPerMonth");
        bean.addUrlPatterns("/stargazersPerMonth");
        bean.addUrlPatterns("/uniqueContributorsPerMonth");
        return bean;
    }

    public Filter requestsStatisticAggregatorFilter() {
        return new Filter() {
            @Override
            public void init(FilterConfig filterConfig) throws ServletException {
            }

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                if (request instanceof HttpServletRequest) {
                    StringBuffer requestURL = ((HttpServletRequest) request).getRequestURL();
                    String url = requestURL.toString();
                    String queryString = ((HttpServletRequest) request).getQueryString();
                    LOG.info(url + "?" + queryString);
                    RequestToAPI interceptedRequest = new RequestToAPI(request.getParameter("projectName"), url);
                    LOG.info(interceptedRequest.toString());
                    try {
                        requestToApiRepository.save(interceptedRequest);
                    } catch (Exception ex) {
                        //TODO: implement notification and failover policy
                        LOG.error("CANT SAVE STATISTIC DO DB !!");
                        ex.printStackTrace();
                    }
                }
                chain.doFilter(request, response);
            }

            @Override
            public void destroy() {
            }
        };
    }

    @Bean
    public FilterRegistrationBean userRequestsLimitationRegistrationBean() {
        FilterRegistrationBean azaza = new FilterRegistrationBean();
        azaza.setFilter(userRequestsLimitationFilter());
        azaza.setName("userRequestsLimitationRegistrationBean");//need to set name to avoid conflicts with other FilterRegistrationBeans'
        azaza.addUrlPatterns("/commits");
        azaza.addUrlPatterns("/stargazers");
        azaza.addUrlPatterns("/uniqueContributors");
        return azaza;
    }

    public Filter userRequestsLimitationFilter() {
        return new Filter() {
            @Override
            public void init(FilterConfig filterConfig) throws ServletException {

            }

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                if (request instanceof HttpServletRequest) {
                    Cookie[] cookies = ((HttpServletRequest) request).getCookies();
                    if (cookies != null) {
                        boolean isCookiesExist = false;
                        for (Cookie cookie : cookies) {
                            if ("freeRequests".equals(cookie.getName())) {
                                int c = Integer.parseInt(cookie.getValue());
                                LOG.info("free requests: " + String.valueOf(c-1));
                                cookie.setValue(String.valueOf(c - 1));
                                cookie.setMaxAge(24 * 60 * 60);
                                cookie.setPath("/");
                                cookie.setHttpOnly(true);
                                ((HttpServletResponse) response).addCookie(cookie);
                                isCookiesExist = true;
                                break;
                            }
                        }
                        if (!isCookiesExist) {
                            Cookie newCookie = new Cookie("freeRequests", "20");
                            newCookie.setMaxAge(24 * 60 * 60);
                            newCookie.setPath("/");
                            newCookie.setHttpOnly(true);
                            ((HttpServletResponse) response).addCookie(newCookie);
                            LOG.info("free requests: " + newCookie.getValue());
                        }
                    }

                }
                chain.doFilter(request, response);

            }

            @Override
            public void destroy() {

            }
        };
    }

}
