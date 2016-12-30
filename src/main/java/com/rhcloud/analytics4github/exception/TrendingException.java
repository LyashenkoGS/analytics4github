package com.rhcloud.analytics4github.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Represents an exception during interaction with github trending service
 * Created by Nazar on 28.12.2016.
 */
public class TrendingException extends RuntimeException {

    public TrendingException(String message) {
        super(message);
    }
    public TrendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
