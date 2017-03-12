package com.rhcloud.analytics4github.controller;

import com.rhcloud.analytics4github.util.GithubApiIterator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RequestsLeftController {

    @RequestMapping(value = "/getRequestsLeft")
    public Integer getRequestsLeft(){
        return GithubApiIterator.requestsLeft;
    }
}
