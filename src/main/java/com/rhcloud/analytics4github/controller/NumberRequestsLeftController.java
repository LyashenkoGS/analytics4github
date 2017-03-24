package com.rhcloud.analytics4github.controller;

import com.rhcloud.analytics4github.util.GitHubApiIterator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class NumberRequestsLeftController {

    @RequestMapping(value = "/getRequestsNumberLeft")
    public Integer getRequestsLeft() {
        return GitHubApiIterator.requestsLeft;
    }
}
