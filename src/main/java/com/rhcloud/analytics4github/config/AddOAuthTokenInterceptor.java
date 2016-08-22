package com.rhcloud.analytics4github.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Add OAuth token to all RestTemplate initialized by this class
 *
 * @author lyashenkogs
 */
@Component
public class AddOAuthTokenInterceptor implements ClientHttpRequestInterceptor {
    private static Logger LOG = LoggerFactory.getLogger(AddOAuthTokenInterceptor.class);

    @Value("${token.file}")
    private String tokenFile;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {

        //Todo replace with NIO features; Getting token procedure should be execute once during the context load
        String tokenValue = "";
        try (FileInputStream fstream = new FileInputStream(tokenFile);
             BufferedReader br = new BufferedReader(new InputStreamReader(fstream))) {
            tokenValue = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LOG.debug("token value: " + tokenValue);
        if (tokenValue.isEmpty()) {
            LOG.warn("token value from " + tokenFile + " is empty");
            LOG.warn("cant get token from a file, trying to get from the environment variable GITHUB_TOKEN");
            tokenValue = System.getenv("GITHUB_TOKEN");
            LOG.debug("token value: " + tokenValue);
        }


        HttpHeaders headers = request.getHeaders();
        headers.add("Authorization", "token " + tokenValue);
        return execution.execute(request, body);
    }
}
