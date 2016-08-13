package com.rhcloud.analytics4github.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rhcloud.analytics4github.service.StargazersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

@RestController
public class ProjectController {

    @Autowired
    StargazersService stargazersService;

    @RequestMapping(value = "/stargazers", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public JsonNode getStargazersByProject(HttpServletResponse response) throws IOException {
       //Todo extract to a filter to allow acces for clients from other domains
        response.setHeader("Access-Control-Allow-Origin", "*");
       // return stargazersService.getThisWeekStargazersFrequencyPerProject("FallibleInc/security-guide-for-developers");
        //Todo remove the mock
        return new ObjectMapper()
                .readTree(new ClassPathResource("mockWeekStargazers.json")
                        .getInputStream());
    }
}
