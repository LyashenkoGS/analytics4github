package com.rhcloud.analytics4github.util;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.rhcloud.analytics4github.config.GitHubApiEndpoints;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

/**
 * Common utilities for complex operation on objects representing date and time.
 *
 * @author lyashenkogs.
 */
public class Utils {
    private static Logger LOG = LoggerFactory.getLogger(Utils.class);

    /**
     * Assert that given Date is in the range from current monday to sunday
     * include border values.
     * Must return true for all days from this week include monday and sunday.
     */
    public static boolean isWithinThisWeekRange(LocalDate timestamp) {
        LOG.debug("Check is the " + timestamp + " is within this week range");
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(previousOrSame(MONDAY));
        LocalDate sunday = today.with(nextOrSame(SUNDAY));
        boolean isWithinThisWeekRange = (timestamp.isAfter(monday.minusDays(1))) && timestamp.isBefore(sunday.plusDays(1));
        LOG.debug(String.valueOf(isWithinThisWeekRange));
        return isWithinThisWeekRange;
    }

    public static boolean isWithinThisMonthRange(LocalDate timestamp) {
        LOG.debug("Check is the " + timestamp + " is within this month range");
        LocalDate monthStart = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate monthEnd = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        boolean isWithinThisMonthRange = ((timestamp.isAfter(monthStart) || timestamp.isEqual(monthStart))
                && (timestamp.isBefore(monthEnd) || timestamp.isEqual(monthEnd)));
        LOG.debug(String.valueOf(isWithinThisMonthRange));
        return isWithinThisMonthRange;
    }

    /**
     * @param timestamp String representing date and time in ISO 8601 YYYY-MM-DDTHH:MM:SSZ
     * @return LocalDate parsed from the given @param. Example (2007-12-03)
     */
    public static LocalDate parseTimestamp(String timestamp) {
        return LocalDate.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME);
    }

    public static Instant getThisMonthBeginInstant() {
        LocalDateTime localDate = LocalDateTime.now().withSecond(0).withHour(0).withMinute(0)
                .with(TemporalAdjusters.firstDayOfMonth()).truncatedTo(ChronoUnit.SECONDS);
        Instant instant = localDate.toInstant(ZoneOffset.UTC);
        return instant;
    }

    public static Instant getThisWeekBeginInstant() {
        LocalDateTime localDate = LocalDateTime.now().withSecond(0).withHour(0).withMinute(0)
                .with((MONDAY)).truncatedTo(ChronoUnit.SECONDS);
        return localDate.toInstant(ZoneOffset.UTC);
    }

    public static ArrayNode buildJsonForHIghChart(List<Integer> stargazersFrequencyList) throws IOException, ClassNotFoundException {
        ArrayNode outputJson = JsonNodeFactory.instance.arrayNode();
        outputJson.addObject().put("name", "Stars").putPOJO("data", stargazersFrequencyList);
        return outputJson;
    }

    public static List<Integer> parseWeekStargazersMapFrequencyToWeekFrequencyList(TreeMap<LocalDate, Integer> weekStargazersFrequenyMap) {
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

    public static List<Integer> parseMonthFrequencyMapToFrequencyLIst(TreeMap<LocalDate, Integer> mockWeekStargazersFrequencyMap) throws IOException {
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

    public static TreeMap<LocalDate, Integer> buildStargazersFrequencyMap(List<LocalDate> stargazersList) throws IOException, URISyntaxException, ExecutionException, InterruptedException {
        //temporary set
        Set<LocalDate> stargazersDateSet = stargazersList.stream().collect(Collectors.toSet());
        Map<LocalDate, Integer> stargazersFrequencyMap = stargazersDateSet.stream().collect(Collectors
                .toMap(Function.identity(), e -> Collections.frequency(stargazersList, e)));
        TreeMap<LocalDate, Integer> localDateIntegerNavigableMap = new TreeMap<>(stargazersFrequencyMap);
        LOG.debug("stargazers week/month frequency map:" + localDateIntegerNavigableMap.toString());
        return localDateIntegerNavigableMap;
    }

    public static int getLastPageNumber(String repository, RestTemplate restTemplate, GitHubApiEndpoints githubEndpoint, String author, Instant since) {
        String URL;
        if (since != null) {
            URL = UriComponentsBuilder.fromHttpUrl("https://api.github.com/repos/")
                    .path(repository).path("/" + githubEndpoint.toString().toLowerCase())
                    .queryParam("since", since)
                    .build().encode()
                    .toUriString();
        } else if (author != null) {
            URL = UriComponentsBuilder.fromHttpUrl("https://api.github.com/repos/")
                    .path(repository).path("/" + githubEndpoint.toString().toLowerCase())
                    .queryParam("author", author)
                    .build().encode()
                    .toUriString();
        } else {
            URL = UriComponentsBuilder.fromHttpUrl("https://api.github.com/repos/")
                    .path(repository).path("/" + githubEndpoint.toString().toLowerCase())
                    .build().encode()
                    .toUriString();
        }
        LOG.debug("URL to get the last commits page number:" + URL);
        HttpHeaders headers = restTemplate.headForHeaders(URL);
        String link = headers.getFirst("Link");
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
            LOG.info("Propably " + repository + "commits consists from only one page");
            return 1;
        }
        return lastPageNum;

    }
}