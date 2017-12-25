package com.rhcloud.analytics4github.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Created by Iron on 27.12.2016.
 */
@ApiModel
public class RequestFromFrontendDto {
    @ApiParam(required = true, example = "mewo2", value = "a repository author. Example: mewo2")
    private String author;
    @ApiParam(required = true, example = "terrain", value = "a repository author. Example: terrain")
    private String repository;
    @ApiParam(required = true, example = "2016-08-10", value = "a data to start analyze from. Example: 2016-08-10")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startPeriod;
    @ApiParam(required = true, example = "2016-08-17", value = "a data until analyze. Example: 2016-08-17")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endPeriod;

    public RequestFromFrontendDto() {
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
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
