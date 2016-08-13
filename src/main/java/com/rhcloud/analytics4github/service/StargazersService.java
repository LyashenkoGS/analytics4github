package com.rhcloud.analytics4github.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.rhcloud.analytics4github.util.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author lyashenkogs
 */
@Service
public class StargazersService {
    Logger LOG = LoggerFactory.getLogger(StargazersService.class);
    @Autowired
    private RestTemplate template;

    public ArrayNode getThisWeekStargazersFrequencyPerProject(String projectName) throws IOException, URISyntaxException, ClassNotFoundException {
        TreeMap<LocalDate, Integer> weekStargazersFrequencyMap = getWeekStargazersFrequencyMap(projectName);
        List<Integer> frequencyList = parseWeekStargazersMapFrequencyToWeekFrequencyList(weekStargazersFrequencyMap);
        ArrayNode buildedJsonForHighChart = buildJsonForHIghChart(frequencyList);
        LOG.debug("builded json for highchart.js :" + buildedJsonForHighChart);
        return buildedJsonForHighChart;
    }

    public int getLastStargazersPageNumberByProjectName(String projectName) throws URISyntaxException {
        String URL = UriComponentsBuilder.fromHttpUrl("https://api.github.com/repos/")
                .path(projectName).path("/stargazers").build().encode()
                .toUriString();
        LOG.debug("URL to get the last stargazers page number:" + URL);
        ResponseEntity<JsonNode> stargazersPageResponseEntity = template.getForEntity(URL, JsonNode.class);
        String link = stargazersPageResponseEntity.getHeaders().getFirst("Link");
        LOG.debug("Link: " + link);
        LOG.debug("parse link by regexp");
        Pattern p = Pattern.compile("page=(\\d*)>; rel=\"last\"");
        int lastPageNum = 0;
        try {
            Matcher m = p.matcher(link);
            if (m.find()) {
                lastPageNum = Integer.valueOf(m.group(1));
                LOG.debug("parse result: " + lastPageNum);
            }
        } catch (NullPointerException npe) {
            //  npe.printStackTrace();
            LOG.info("Propably " + projectName + "stargazers consists from only one page");
            return 1;
        }
        return lastPageNum;
    }

    public List<LocalDate> getWeekStargazersList(String projectName) throws URISyntaxException, IOException {
        int lastStargazersPageNumber = getLastStargazersPageNumberByProjectName(projectName);
        List<LocalDate> thisWeekAllStargazersDateList = new LinkedList<>();
        //Todo add feature for 1 page stargazers
        for (int pageNumber = lastStargazersPageNumber; pageNumber > 1; pageNumber--) {
            //build url
            String basicURL = "https://api.github.com/repos/" + projectName + "/stargazers";
            UriComponents page = UriComponentsBuilder.fromHttpUrl(basicURL)
                    .queryParam("page", pageNumber).build();
            String URL = page.encode().toUriString();
            LOG.debug(URL);
            //sent request
            JsonNode stargazerJSON = template.getForObject(URL, JsonNode.class);
            LOG.debug(stargazerJSON.textValue());
            //iterate over stargazers page and put all elements into a list, if they passed a checking
            //todo break outer loop in more elegant way, or implement the Iterator pattern!
            JsonNode firstStargazerDateJson = stargazerJSON.get(0).get("starred_at");
            LOG.debug(firstStargazerDateJson.textValue());
            LocalDate timestamp = Utils.parseTimestamp(firstStargazerDateJson.textValue());
            //if first element timestamp is out this week range -> break the outer loop
            if (!Utils.isWithinThisWeekRange(timestamp)) {
                LOG.info("page number" + pageNumber + "contain stargazer date that is" +
                        "not in current week range");
                LOG.info("stop iteration over stargazers page!");
                break;
                //todo break outer loop in more elegant way, or implement the Iterator pattern!
            }
            List<LocalDate> stargazesFromPageList = StreamSupport.stream(stargazerJSON.spliterator(), false)
                    .map(e -> e.get("starred_at"))//get element "starred_at from each JSON inside the node
                    .map(e -> Utils.parseTimestamp(e.textValue()))
                    .filter(Utils::isWithinThisWeekRange)
                    .collect(Collectors.toList());
            LOG.debug(stargazesFromPageList.toString());
            thisWeekAllStargazersDateList.addAll(stargazesFromPageList);

        }
        LOG.debug("finish parsing stargazers" + thisWeekAllStargazersDateList.toString());
        return thisWeekAllStargazersDateList;
    }

    public TreeMap<LocalDate, Integer> getWeekStargazersFrequencyMap(String projectName) throws IOException, URISyntaxException {
        List<LocalDate> weekStargazersList = getWeekStargazersList(projectName);
        //temporary set
        Set<LocalDate> stargazersDateSet = weekStargazersList.stream().collect(Collectors.toSet());
        Map<LocalDate, Integer> stargazersFrequencyMap = stargazersDateSet.stream().collect(Collectors
                .toMap(Function.identity(), e -> Collections.frequency(weekStargazersList, e)));
        TreeMap<LocalDate, Integer> localDateIntegerNavigableMap = new TreeMap<>(stargazersFrequencyMap);
        LOG.debug("stargazers week frequency map:" + localDateIntegerNavigableMap.toString());
        return localDateIntegerNavigableMap;
    }

    public ArrayNode buildJsonForHIghChart(List<Integer> weekStargazersFrequencyList) throws IOException, ClassNotFoundException {
        ArrayNode outputJson = JsonNodeFactory.instance.arrayNode();
        outputJson.addObject().put("name", "Stars").putPOJO("data", weekStargazersFrequencyList);
        return outputJson;
    }

    public List<Integer> parseWeekStargazersMapFrequencyToWeekFrequencyList(TreeMap<LocalDate, Integer> mockWeekStargazersFrequenyMap) {
        LOG.debug("parseWeekStargazersMapFrequencyToWeekFrequencyList");
        List<Integer> output = new ArrayList<>();
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            LOG.debug(dayOfWeek.toString());
            Optional<LocalDate> optional = mockWeekStargazersFrequenyMap.keySet().stream()
                    .filter(e -> DayOfWeek.from(e).equals(dayOfWeek))
                    .findFirst();
            if (optional.isPresent()) {
                LOG.debug("match " + optional.get() + " with frequency " + mockWeekStargazersFrequenyMap.get(optional.get()));
                output.add(mockWeekStargazersFrequenyMap.get(optional.get()));
            } else {
                LOG.debug("no match from " + mockWeekStargazersFrequenyMap.keySet());
                LOG.debug("add 0");
                output.add(0);
            }

        }
        LOG.debug("Output is" + output.toString());
        return output;
    }


}
