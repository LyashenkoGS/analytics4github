package com.rhcloud.analytics4github.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rhcloud.analytics4github.service.StargazersService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author lyashenkogs
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ProjectController.class)
public class ProjectControllerWebMvcTest {

    @MockBean
    private StargazersService stargazersService;

    @Autowired
    private MockMvc mvc;

    @Test
    public void testThatControllerDoentChangingJsonFromService() throws Exception {
        //TODO externalize datasets for test
        String givenJson = "{\n" +
                "\t\"mock\": \"mockValue\"\n" +
                "}";
        JsonNode jsonNode = new ObjectMapper().readTree(givenJson);
        BDDMockito.given(this.stargazersService.getStargazersPerProject("ss"))
                .willReturn(jsonNode);
        this.mvc.perform(get("/stargazers"))
                .andExpect(status().isOk()).andExpect(content().json(givenJson));
    }
}
