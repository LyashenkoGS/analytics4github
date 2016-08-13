package com.rhcloud.analytics4github.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Interceptor to change request header for Github API
 * to include timestamp in response for GET /repos/:owner/:repo/stargazers endpoint
 *
 * @author lyashenkogs
 * @see <a href="https://developer.github.com/v3/activity/starring/">https://developer.github.com/v3/activity/starring/</a>
 */
@Component
public class ChangeAcceptTypeForStargazersInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        headers.add("Accept", "application/vnd.github.v3.star+json");
        return execution.execute(request, body);
    }
}
