package com.rhcloud.analytics4github.controller;

import com.rhcloud.analytics4github.domain.RequestToAPI;
import com.rhcloud.analytics4github.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lyashenkogs.
 * @since 8/31/16
 */
@RestController
@RequestMapping(value = "/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @RequestMapping(value = "/requests")
    private List<RequestToAPI> getRequests() {
        return statisticsService.getRequestsStatistic();
    }

}
