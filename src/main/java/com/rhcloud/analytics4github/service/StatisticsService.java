package com.rhcloud.analytics4github.service;

import com.rhcloud.analytics4github.domain.RequestToAPI;
import com.rhcloud.analytics4github.repository.RequestToApiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lyashenkogs.
 * @since 8/31/16
 */
@Service
public class StatisticsService {
    @Autowired
    private RequestToApiRepository repository;


    public List<RequestToAPI> getRequestsStatistic() {
        return repository.findAll();
    }
}
