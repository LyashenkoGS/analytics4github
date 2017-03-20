package com.rhcloud.analytics4github.config;


import com.rhcloud.analytics4github.util.GithubApiIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Created by ivan on 17.03.17.
 */
@Configuration
public class SaveCoockiesFilter {
    private static Logger LOG = LoggerFactory.getLogger(SaveCoockiesFilter.class);

    @Bean
    public FilterRegistrationBean registrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(this.requestsSaveCoockiesFilter());
        registrationBean.addUrlPatterns("/commits");
        registrationBean.addUrlPatterns("/stargazers");
        registrationBean.addUrlPatterns("/uniqueContributors");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean(name = "requestsSaveCoockiesFilter")
    public Filter requestsSaveCoockiesFilter(){
        return new Filter() {
            @Override
            public void init(FilterConfig filterConfig) throws ServletException {

            }

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                if (request instanceof HttpServletRequest){
                    Cookie[] cookies = ((HttpServletRequest)request).getCookies();
                    if (cookies != null) {
                        boolean isCookiesExist = false;
                        for (Cookie cookie : cookies) {
                            if ("freeTokens".equals(cookie.getName())) {
                                int c = Integer.parseInt(cookie.getValue());
                                cookie.setValue(String.valueOf(c-20));
                                System.out.println(c);
                                cookie.setMaxAge(24*60*60);
                                cookie.setPath("/");
                                ((HttpServletResponse)response).addCookie(cookie);
                                isCookiesExist = true;
                                System.out.println("This cookies exists");
                                break;
                            }
                        }
                        if (!isCookiesExist){
                            Cookie newCookie = new Cookie("freeTokens", "400");
                            newCookie.setMaxAge(24 * 60 * 60);
                            newCookie.setPath("/");
                            ((HttpServletResponse)response).addCookie(newCookie);
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
