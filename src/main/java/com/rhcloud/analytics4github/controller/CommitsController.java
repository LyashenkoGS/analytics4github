package com.rhcloud.analytics4github.controller;

import com.rhcloud.analytics4github.dto.RequestFromFrontendDto;
import com.rhcloud.analytics4github.dto.ResponceForFrontendDto;
import com.rhcloud.analytics4github.exception.GitHubRESTApiException;
import com.rhcloud.analytics4github.service.CommitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author lyashenkogs.
 */
@RestController
public class CommitsController {

    @Autowired
    private CommitsService commitsService;

    @GetMapping("/{author}/{repository}/commits")
    List<ResponceForFrontendDto> getCommits(@ModelAttribute RequestFromFrontendDto requestFromFrontendDto) throws IOException, URISyntaxException, ClassNotFoundException, ExecutionException, InterruptedException, GitHubRESTApiException {
        return Collections.singletonList(commitsService.getCommitsFrequency(requestFromFrontendDto));
    }

}
