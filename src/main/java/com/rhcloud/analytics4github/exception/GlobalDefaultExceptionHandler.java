package com.rhcloud.analytics4github.exception;


import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Nazar on 28.12.2016.
 */
@ControllerAdvice
public class GlobalDefaultExceptionHandler  {
    private static Logger LOG = getLogger(GlobalDefaultExceptionHandler.class);

    @ExceptionHandler(value = {GitHubRESTApiException.class})
    public ResponseEntity<String> gitHubRESTAPIException(GitHubRESTApiException exception) throws GitHubRESTApiException {
        LOG.error(exception.getMessage(), exception);
        return new ResponseEntity<String>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

}
