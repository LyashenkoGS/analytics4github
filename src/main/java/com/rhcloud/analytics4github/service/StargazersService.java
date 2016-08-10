package com.rhcloud.analytics4github.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

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

    public JsonNode getStargazersPerProject(String projectName) throws IOException {
        //  return restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", JsonNode.class);
        //Todo remove mock
        return new ObjectMapper()
                .readTree(new ClassPathResource("mockWeekStargazers.json")
                        .getInputStream());
    }
}
