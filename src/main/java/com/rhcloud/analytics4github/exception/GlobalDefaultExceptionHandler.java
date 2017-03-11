package com.rhcloud.analytics4github.exception;


import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Nazar on 28.12.2016.
 * exception handling  apply across the whole application, not just to an individual controller.
 */
@ControllerAdvice
public class GlobalDefaultExceptionHandler extends ResponseEntityExceptionHandler {
    private static Logger LOG = getLogger(GlobalDefaultExceptionHandler.class);


    @ExceptionHandler(value = {GitHubRESTApiException.class})
    public ResponseEntity gitHubRESTAPIException(GitHubRESTApiException exception) {
        LOG.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

}
