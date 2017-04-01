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
    static final String FREE_REQUESTS_NUMBER_PER_NEW_USER = "20";
    private static Logger LOG = LoggerFactory.getLogger(FiltersConfiguration.class);
    static final String COOKIE_NAME = "freeRequests";

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
             * for new user add in cookies freeRequests value 20
             * for exists user in cookies freeRequests decrease value by 1
             * @param request
             * @param response
             * @param chain
             * @throws IOException
             * @throws ServletException
             */
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                if (request instanceof HttpServletRequest) {
                    LOG.info("intercepted " + ((HttpServletRequest) request) + "\n"
                            + "in the userRequestsLimitationFilter");
                    Cookie[] cookies = ((HttpServletRequest) request).getCookies();
                    if (cookies != null) {
                        boolean isCookieExists = false;
                        for (Cookie cookie : cookies) {
                            if (COOKIE_NAME.equals(cookie.getName())) {
                                isCookieExists = true;
                                int c = Integer.parseInt(cookie.getValue());
                                cookie.setValue(String.valueOf(c - 1));
                                LOG.info("free requests: " + cookie.getValue());
                                cookie.setMaxAge(24 * 60 * 60);
                                cookie.setPath("/");
                                cookie.setHttpOnly(true);
                                ((HttpServletResponse) response).addCookie(cookie);
                                System.out.println("This cookies exists");
                                break;
                            }
                        }
                        if (!isCookieExists){
                            Cookie newCookie = new Cookie(COOKIE_NAME, FREE_REQUESTS_NUMBER_PER_NEW_USER);
                            newCookie.setMaxAge(24 * 60 * 60);
                            newCookie.setPath("/");
                            ((HttpServletResponse) response).addCookie(newCookie);
                        }
                    } else {
                        Cookie newCookie = new Cookie(COOKIE_NAME, FREE_REQUESTS_NUMBER_PER_NEW_USER);
                        newCookie.setMaxAge(24 * 60 * 60);
                        newCookie.setPath("/");
                        ((HttpServletResponse) response).addCookie(newCookie);
                    }
                }

                chain.doFilter(request, response);
            }


            @Override
            public void destroy() {

            }
        }

                ;
    }

}
