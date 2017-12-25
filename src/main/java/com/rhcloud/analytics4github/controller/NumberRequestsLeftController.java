package com.rhcloud.analytics4github.controller;

import com.rhcloud.analytics4github.util.GitHubApiIterator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class NumberRequestsLeftController {

    @GetMapping(value = "/getRequestsNumberLeft")
    Integer getRequestsLeft() {
        return GitHubApiIterator.requestsLeft;
    }
}
