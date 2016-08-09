package com.rhcloud.analytics4github.service;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author lyashenkogs
 */
@Service
public class StargazersService {

    private final RestTemplate restTemplate;

    //Todo: can I get rid of this and don't broke tests
    public StargazersService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public JsonNode getStargazersPerProject(String projectName) {
        return restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", JsonNode.class);
    }
}
