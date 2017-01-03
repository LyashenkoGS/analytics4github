package com.rhcloud.analytics4github.exception;

/**
 * Represents an exception during interaction with github trending service
 * Created by Nazar on 28.12.2016.
 */
public class TrendingException extends Exception {

    public TrendingException(String message) {
        super(message);
    }

    public TrendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
