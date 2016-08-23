package com.rhcloud.analytics4github.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.rhcloud.analytics4github.util.StargazersIterator;
import com.rhcloud.analytics4github.util.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author lyashenkogs
 */
@Service
public class StargazersService {
    private static Logger LOG = LoggerFactory.getLogger(StargazersService.class);
    @Autowired
    private RestTemplate template;

    public ArrayNode getThisWeekStargazersFrequencyPerProject(String projectName) throws IOException, URISyntaxException, ClassNotFoundException, ExecutionException, InterruptedException {
        TreeMap<LocalDate, Integer> weekStargazersFrequencyMap = buildStargazersFrequencyMap(getWeekStargazersList(projectName));
        List<Integer> frequencyList = parseWeekStargazersMapFrequencyToWeekFrequencyList(weekStargazersFrequencyMap);
        ArrayNode buildedJsonForHighChart = buildJsonForHIghChart(frequencyList);
        LOG.debug("builded json for highchart.js :" + buildedJsonForHighChart);
        return buildedJsonForHighChart;
    }

    public ArrayNode getThisMonthStargazersFrequencyPerProject(String projectName) throws InterruptedException, ExecutionException, URISyntaxException, IOException, ClassNotFoundException {
        TreeMap<LocalDate, Integer> stargazersFrequencyMap = this.buildStargazersFrequencyMap(getMonthStargazersList(projectName));
        List<Integer> frequencyList = parseMonthFrequencyMapToFrequencyLIst(stargazersFrequencyMap);
        ArrayNode buildedJsonForHighChart = buildJsonForHIghChart(frequencyList);
        LOG.debug("builded json for highchart.js :" + buildedJsonForHighChart);
        return buildedJsonForHighChart;
    }

    public List<LocalDate> getWeekStargazersList(String projectName) throws URISyntaxException, IOException, ExecutionException, InterruptedException {
        List<LocalDate> thisWeekAllStargazersDateList = new LinkedList<>();
        StargazersIterator stargazersIterator = new StargazersIterator(projectName, template);
        while (stargazersIterator.hasNext()) {
            List<JsonNode> stargazerPagesBatch = stargazersIterator.next(5);

            List<LocalDate> stargazersFrequency = StreamSupport.stream(stargazerPagesBatch.spliterator(), false)
                    .map(e -> e.get(0).get("starred_at"))//get element "starred_at from each JSON inside the node
                    .map(e -> Utils.parseTimestamp(e.textValue()))
                    .filter(Utils::isWithinThisWeekRange)
                    .collect(Collectors.toList());
            LOG.debug(stargazersFrequency.toString());

            thisWeekAllStargazersDateList.addAll(stargazersFrequency);

            boolean batchContainStargazersOutOfRange = StreamSupport.stream(stargazerPagesBatch.spliterator(), false)
                    .map(e -> e.get(0).get("starred_at"))//get element "starred_at from each JSON inside the node
                    .map(e -> Utils.parseTimestamp(e.textValue()))
                    .anyMatch((e) -> !Utils.isWithinThisWeekRange(e));
            if (batchContainStargazersOutOfRange) break;
        }

        LOG.debug("finish parsing stargazers" + thisWeekAllStargazersDateList.toString());
        return thisWeekAllStargazersDateList;
    }

    public List<LocalDate> getMonthStargazersList(String projectName) throws URISyntaxException, ExecutionException, InterruptedException {
        List<LocalDate> thisMonthAllStargazersDateList = new LinkedList<>();
        StargazersIterator stargazersIterator = new StargazersIterator(projectName, template);
        while (stargazersIterator.hasNext()) {
            List<JsonNode> stargazerPagesBatch = stargazersIterator.next(5);

            List<LocalDate> stargazersFrequency = StreamSupport.stream(stargazerPagesBatch.spliterator(), false)
                    .map(e -> e.get(0).get("starred_at"))//get element "starred_at from each JSON inside the node
                    .map(e -> Utils.parseTimestamp(e.textValue()))
                    .filter(Utils::isWithinThisMonthRange)
                    .collect(Collectors.toList());
            LOG.debug(stargazersFrequency.toString());

            thisMonthAllStargazersDateList.addAll(stargazersFrequency);

            boolean batchContainStargazersOutOfRange = StreamSupport.stream(stargazerPagesBatch.spliterator(), false)
                    .map(e -> e.get(0).get("starred_at"))//get element "starred_at from each JSON inside the node
                    .map(e -> Utils.parseTimestamp(e.textValue()))
                    .anyMatch((e) -> !Utils.isWithinThisMonthRange(e));
            if (batchContainStargazersOutOfRange) break;
        }

        LOG.debug("finish parsing stargazers" + thisMonthAllStargazersDateList.toString());
        return thisMonthAllStargazersDateList;
    }

    public TreeMap<LocalDate, Integer> buildStargazersFrequencyMap(List<LocalDate> stargazersList) throws IOException, URISyntaxException, ExecutionException, InterruptedException {
        //temporary set
        Set<LocalDate> stargazersDateSet = stargazersList.stream().collect(Collectors.toSet());
        Map<LocalDate, Integer> stargazersFrequencyMap = stargazersDateSet.stream().collect(Collectors
                .toMap(Function.identity(), e -> Collections.frequency(stargazersList, e)));
        TreeMap<LocalDate, Integer> localDateIntegerNavigableMap = new TreeMap<>(stargazersFrequencyMap);
        LOG.debug("stargazers week/month frequency map:" + localDateIntegerNavigableMap.toString());
        return localDateIntegerNavigableMap;
    }

    public ArrayNode buildJsonForHIghChart(List<Integer> stargazersFrequencyList) throws IOException, ClassNotFoundException {
        ArrayNode outputJson = JsonNodeFactory.instance.arrayNode();
        outputJson.addObject().put("name", "Stars").putPOJO("data", stargazersFrequencyList);
        return outputJson;
    }

    public List<Integer> parseWeekStargazersMapFrequencyToWeekFrequencyList(TreeMap<LocalDate, Integer> weekStargazersFrequenyMap) {
        LOG.debug("parseWeekStargazersMapFrequencyToWeekFrequencyList");
        List<Integer> output = new ArrayList<>();
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            LOG.debug(dayOfWeek.toString());
            Optional<LocalDate> optional = weekStargazersFrequenyMap.keySet().stream()
                    .filter(e -> DayOfWeek.from(e).equals(dayOfWeek))
                    .findFirst();
            if (optional.isPresent()) {
                LOG.debug("match " + optional.get() + " with frequency " + weekStargazersFrequenyMap.get(optional.get()));
                output.add(weekStargazersFrequenyMap.get(optional.get()));
            } else {
                LOG.debug("no match from " + weekStargazersFrequenyMap.keySet());
                LOG.debug("add 0");
                output.add(0);
            }

        }
        LOG.debug("Output is" + output.toString());
        return output;
    }

    public List<Integer> parseMonthFrequencyMapToFrequencyLIst(TreeMap<LocalDate, Integer> mockWeekStargazersFrequencyMap) throws IOException {
        int lastDayOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
        LOG.debug(String.valueOf(lastDayOfMonth));
        List<Integer> monthStargazersFrequency = new ArrayList<>(lastDayOfMonth);
        for (int dayOfMonth = 1; dayOfMonth < lastDayOfMonth + 1; dayOfMonth++) {
            LOG.debug("day of month: " + dayOfMonth);
            Optional<Integer> frequency = Optional.empty();
            for (LocalDate localDate : mockWeekStargazersFrequencyMap.keySet()) {
                if (dayOfMonth == localDate.getDayOfMonth()) {
                    frequency = Optional.of(mockWeekStargazersFrequencyMap.get(localDate));
                }
            }
            if (frequency.isPresent()) {
                monthStargazersFrequency.add(frequency.get());
            } else {
                monthStargazersFrequency.add(0);
            }
            LOG.debug(monthStargazersFrequency.toString());
        }
        return monthStargazersFrequency;
    }

}
