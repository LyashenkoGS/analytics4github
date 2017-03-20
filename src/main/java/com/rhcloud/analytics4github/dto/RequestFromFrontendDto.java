package com.rhcloud.analytics4github.dto;

import io.swagger.annotations.ApiModel;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Created by Iron on 27.12.2016.
 */
@ApiModel
public class RequestFromFrontendDto {

    private String projectName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startPeriod;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endPeriod;

    public RequestFromFrontendDto() {
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public LocalDate getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(LocalDate startPeriod) {
        this.startPeriod = startPeriod;
    }

    public LocalDate getEndPeriod() {
        return endPeriod;
    }

    public void setEndPeriod(LocalDate endPeriod) {
        this.endPeriod = endPeriod;
    }
}
