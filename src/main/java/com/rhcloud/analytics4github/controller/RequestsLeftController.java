package com.rhcloud.analytics4github.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
public class RequestsLeftController {

    private final String URL = "https://api.github.com/users/whatever";

    @RequestMapping(value = "/getRequestsLeft")
    public Integer getRequestsLeft(){
        RestTemplate template = new RestTemplate();
        HttpEntity<String> entity = template.getForEntity(URL, String.class);
        HttpHeaders headers = entity.getHeaders();
        int result = -1;
        try {
            result = Integer.parseInt(headers.get("X-RateLimit-Remaining").get(0));
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
