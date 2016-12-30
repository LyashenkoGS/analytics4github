package com.rhcloud.analytics4github.exception;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Nazar on 28.12.2016.
 */
@ControllerAdvice
public class GlobalDefaultExceptionHandler  {
    private static final Logger logger = Logger.getLogger(GlobalDefaultExceptionHandler.class);


    @ExceptionHandler(value = {TrendingException.class,IllegalArgumentException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "trending doesn't work")  // 400
    public String trendingException(HttpServletRequest req, TrendingException e) throws TrendingException,IllegalArgumentException {
        logger.error("Trending Exception", e);

        // Nothing to do
        return e.getMessage();
    }
}
