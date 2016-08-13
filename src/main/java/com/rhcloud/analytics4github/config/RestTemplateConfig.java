package com.rhcloud.analytics4github.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;

/**
 * Configure RestTemplate for Github API
 * @see <a href="https://developer.github.com/v3/">https://developer.github.com/v3/</a>
 */
@Configuration
public class RestTemplateConfig {

    @Autowired
    AddOAuthTokenInterceptor oAuthTokenInterceptor;
    @Autowired
    ChangeAcceptTypeForStargazersInterceptor changeAcceptTypeForStargazersInterceptor;

    @Bean
    RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        LinkedList<ClientHttpRequestInterceptor> clientHttpRequestInterceptors = new LinkedList<>();
        clientHttpRequestInterceptors.add(oAuthTokenInterceptor);
        clientHttpRequestInterceptors.add(changeAcceptTypeForStargazersInterceptor);

        restTemplate.setInterceptors(clientHttpRequestInterceptors);
        return restTemplate;
    }
}
