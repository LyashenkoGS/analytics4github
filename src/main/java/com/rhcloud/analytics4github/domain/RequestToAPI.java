package com.rhcloud.analytics4github.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * @author lyashenkogs.
 */
@Document
public class RequestToAPI {
    @Id
    private String id;
    private final String repository;
    private final String requestTime;
    private final String endpoint;

    public RequestToAPI(String repository, String endpoint) {
        this.repository = repository;
        this.endpoint = endpoint;
        this.requestTime = Instant.now().truncatedTo(ChronoUnit.SECONDS).toString();
    }

    public String getRequestTime() {
        return requestTime;
    }

    public String getEndpoint() {return endpoint;}

    public String getRepository() {
        return repository;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RequestToAPI{");
        sb.append("id='").append(id).append('\'');
        sb.append(", repository='").append(repository).append('\'');
        sb.append(", requestTime='").append(requestTime).append('\'');
        sb.append(", endpoint='").append(endpoint).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
