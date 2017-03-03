package com.rhcloud.analytics4github.exception;

/**
 * Created by nazar on 14.01.17.
 * Custom exception for GitHub REST API
 */

public class GitHubRESTApiException extends Exception {

    public GitHubRESTApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
