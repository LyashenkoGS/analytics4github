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
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author lyashenkogs.
 */
@Configuration
public class StatisticsAggregationFilter {
    private static Logger LOG = LoggerFactory.getLogger(StatisticsAggregationFilter.class);

    @Autowired
    private RequestToApiRepository requestToApiRepository;

    @Bean
    public FilterRegistrationBean registrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(this.requestsStatisticAggregatorFilter());
        registrationBean.addUrlPatterns("/commits");
        registrationBean.addUrlPatterns("/stargazers");
        registrationBean.addUrlPatterns("/uniqueContributors");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean(name = "requestsStatisticAggregatorFilter")
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
}
