package com.rhcloud.analytics4github.exception;

/**
 * Created by nazar on 14.01.17.
 */
public class GitHubRESTApiException extends Exception {
    public GitHubRESTApiException(String message) {
        super(message);
    }

    public GitHubRESTApiException(String message, Throwable cause) {
        super(message, cause);
    }

}
