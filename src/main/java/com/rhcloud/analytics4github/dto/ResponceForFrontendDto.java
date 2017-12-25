package com.rhcloud.analytics4github.dto;

import io.swagger.annotations.ApiModel;

import java.util.List;

@ApiModel
public class ResponceForFrontendDto {

    private String name;
    private int requestsLeft;
    private List<Integer> data;

    public ResponceForFrontendDto() {
    }

    public ResponceForFrontendDto(String name, List<Integer> data) {
        this.name = name;
        this.data = data;
    }

    public ResponceForFrontendDto(String name, List<Integer> data, int requestsLeft) {
        this.name = name;
        this.data = data;
        this.requestsLeft = requestsLeft;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }

    public Integer getRequestsLeft() {
        return requestsLeft;
    }

    public void setRequestsLeft(Integer requestsLeft) {
        this.requestsLeft = requestsLeft;
    }
}
