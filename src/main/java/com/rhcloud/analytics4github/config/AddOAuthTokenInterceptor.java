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
    private static Logger logger = LoggerFactory.getLogger(AddOAuthTokenInterceptor.class);

    @Value("${token.file}")
    private String tokenFile;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        //Todo replace with NIO features and try-catch with resources, externalize file path
        FileInputStream fstream = new FileInputStream(tokenFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String tokenValue = br.readLine();
        logger.info("token value: " + tokenValue);
        br.close();

        HttpHeaders headers = request.getHeaders();
        headers.add("Authorization", "token " + tokenValue);
        return execution.execute(request, body);
    }
}
