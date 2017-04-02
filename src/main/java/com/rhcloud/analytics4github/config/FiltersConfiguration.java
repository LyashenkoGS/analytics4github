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
import java.util.Arrays;
import java.util.Optional;

/**
 * @author lyashenkogs.
 */
@Configuration
public class FiltersConfiguration {
    private static Logger LOG = LoggerFactory.getLogger(FiltersConfiguration.class);

    static final String FREE_REQUESTS_NUMBER_PER_NEW_USER = "20";
    static final String FREE_REQUESTS_COOKIE_NAME = "freeRequests";
    private static final int COOKIE_MAX_AGE = 24 * 60 * 60;

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
        FilterRegistrationBean filterRequestsLimitationBean = new FilterRegistrationBean();
        filterRequestsLimitationBean.setFilter(userRequestsLimitationFilter());
        filterRequestsLimitationBean.setName("userRequestsLimitationRegistrationBean");//need to set name to avoid conflicts with other FilterRegistrationBeans'
        filterRequestsLimitationBean.addUrlPatterns("/commits");
        filterRequestsLimitationBean.addUrlPatterns("/stargazers");
        filterRequestsLimitationBean.addUrlPatterns("/uniqueContributors");
        return filterRequestsLimitationBean;
    }

    public Filter userRequestsLimitationFilter() {
        return new Filter() {
            @Override
            public void init(FilterConfig filterConfig) throws ServletException {

            }

            /**
             * Manages cookies to account number of requests a user can perform without registration.
             *<p>
             * 1. For returned user, decreases the cookie value by 1
             * 2. For new user, sets a cookie with value FREE_REQUESTS_NUMBER_PER_NEW_USER<br>
             * @param request http request
             * @param response http response
             * @param chain filters chain
             * @throws IOException can't delegate to the filters chain
             * @throws ServletException can't delegate to the filters chain
             */
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                if (request instanceof HttpServletRequest) {
                    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
                    LOG.info("intercepted " + httpServletRequest + "\n"
                            + "in the userRequestsLimitationFilter");
                    //avoiding null pointer exception
                    Cookie[] cookies = Optional.ofNullable(httpServletRequest.getCookies())
                            .orElse(new Cookie[]{});
                    Cookie updatedCookie = Arrays.stream(cookies)
                            .filter(cookie -> cookie.getName().equals(FREE_REQUESTS_COOKIE_NAME))
                            .map(cookie -> {
                                LOG.debug("Decreases freeRequestsCookie value by 1");
                                int freeRequestsLeftNumber = Integer.parseInt(cookie.getValue());
                                cookie.setValue(String.valueOf(freeRequestsLeftNumber - 1));
                                LOG.info("free requests number: " + cookie.getValue());
                                return cookie;
                            })
                            .findFirst()
                            .orElseGet(() -> {
                                LOG.debug("Create a new cookies");
                                Cookie newCookie = new Cookie(FREE_REQUESTS_COOKIE_NAME, FREE_REQUESTS_NUMBER_PER_NEW_USER);
                                newCookie.setMaxAge(COOKIE_MAX_AGE);
                                newCookie.setPath("/");
                                LOG.info("free requests number: " + newCookie.getValue());
                                return newCookie;
                            });
                    ((HttpServletResponse) response).addCookie(updatedCookie);
                }

                chain.doFilter(request, response);
            }

            @Override
            public void destroy() {

            }
        };
    }

}
