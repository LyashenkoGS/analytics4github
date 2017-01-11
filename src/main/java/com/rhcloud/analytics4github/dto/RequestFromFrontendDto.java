package com.rhcloud.analytics4github.dto;

/**
 * Created by Iron on 27.12.2016.
 */
public class RequestFromFrontendDto {
    private String repositoryName;
    private String startPeriod;
    private String endPeriod;

    public RequestFromFrontendDto() {
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(String startPeriod) {
        this.startPeriod = startPeriod;
    }

    public String getEndPeriod() {
        return endPeriod;
    }

    public void setEndPeriod(String endPeriod) {
        this.endPeriod = endPeriod;
    }
}
