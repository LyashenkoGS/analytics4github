package com.rhcloud.analytics4github.controller;

import com.rhcloud.analytics4github.dto.RequestFromFrontendDto;
import com.rhcloud.analytics4github.dto.ResponceForFrontendDto;
import com.rhcloud.analytics4github.exception.GitHubRESTApiException;
import com.rhcloud.analytics4github.service.StargazersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class StargazersController {

    @Autowired
    private StargazersService stargazersService;

    @GetMapping("/{author}/{repository}/stargazers")
    List<ResponceForFrontendDto> stargazers(@ModelAttribute RequestFromFrontendDto requestFromFrontendDto) throws IOException, URISyntaxException, ClassNotFoundException, ExecutionException, InterruptedException, GitHubRESTApiException {
        return Collections.singletonList(stargazersService.stargazersFrequency(requestFromFrontendDto));
    }

}
