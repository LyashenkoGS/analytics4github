package com.rhcloud.analytics4github.controller;

import com.rhcloud.analytics4github.domain.RequestToAPI;
import com.rhcloud.analytics4github.service.StatisticsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    @ApiOperation(value = "get the application visitors requests list", notes = "get requests statistic")
    @GetMapping(value = "/requests")
    List<RequestToAPI> getRequests() {
        return statisticsService.getRequestsStatistic();
    }

}
