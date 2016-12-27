package com.rhcloud.analytics4github.dto;

import java.util.List;

/**
 * Created by Iron on 27.12.2016.
 */
public class ResponceForFrontendDto {
    private String name;
    private List<Integer> data;

    public String getName() {
        return name;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }
}
