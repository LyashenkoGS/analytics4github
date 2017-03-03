package com.rhcloud.analytics4github.exception;

/**
 * Created by Nazar on 28.12.2016.
 * Represents an exception during interaction with github trending service
 */
public class TrendingException extends Exception {

    public TrendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
