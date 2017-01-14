package com.rhcloud.analytics4github.exception;

/**
 * Created by nazar on 14.01.17.
 */
public class GitHubApiRestException extends Exception{
    public GitHubApiRestException(String message) {
        super(message);
    }

    public GitHubApiRestException(String message, Throwable cause) {
        super(message, cause);
    }
}
