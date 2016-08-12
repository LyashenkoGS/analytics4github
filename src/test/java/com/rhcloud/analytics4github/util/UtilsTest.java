package com.rhcloud.analytics4github.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author lyashenkogs.
 */
@RunWith(JUnit4.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class UtilsTest {
    Logger logger = LoggerFactory.getLogger(UtilsTest.class);

    @Test
    public void testIsWithinThisWeekRange() {
     /*   //Todo: generate timestamps dynamically, cause the test will broke in some time
        //given timestamps
        LocalDate beforeThisWeekTimestamp = Utils.parseTimestamp("2016-06-15T16:38:45Z");
        LocalDate thisThursdayTimestamp = Utils.parseTimestamp("2016-08-11T16:38:45Z");
        LocalDate thisMondayTimestamp = Utils.parseTimestamp("2016-08-11T16:38:45Z");
        LocalDate thisSundayTimestamp = Utils.parseTimestamp("2016-08-14T16:38:45Z");
        //when timestamps are within this week range
        List<LocalDate> localDatesList = new LinkedList<>();
        localDatesList.add(thisMondayTimestamp);
        localDatesList.add(thisSundayTimestamp);
        localDatesList.add(thisThursdayTimestamp);
        //then return true
        localDatesList.forEach(e -> assertTrue(Utils.isWithinThisWeekRange(e)));
        //else false
        assertFalse(Utils.isWithinThisWeekRange(beforeThisWeekTimestamp));*/
    }

    @Test
    public void testGetWeekStargazers() throws URISyntaxException, InterruptedException, IOException {
     //   getWeekStargazersList("FallibleInc/security-guide-for-developers");
    }

    public void getWeekStargazersList(String projectName) throws URISyntaxException, IOException {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/vnd.github.v3.star+json");

        int lastStargazersPageNumber = getLastStargazersPageNumberByProjectName(projectName);
        List<LocalDate> thisWeekStargazersDateList = new LinkedList<>();
        for (int pageNumber = lastStargazersPageNumber; pageNumber > 1; pageNumber--) {
            //build url
            String basicURL = "https://api.github.com/repos/"+projectName+"/stargazers";
            UriComponents page = UriComponentsBuilder.fromHttpUrl(basicURL)
                    .queryParam("page", pageNumber).build();
            String completedURI = page.encode().toUriString();
            logger.debug(completedURI);
            //sent request
            RequestEntity lastPageRequestEntity = new RequestEntity(headers, HttpMethod.GET, new URI(completedURI));
            ResponseEntity<String> lastPageResponseEntity = template.exchange(lastPageRequestEntity, String.class);
            logger.debug(lastPageResponseEntity.getBody());

            //iterate over stargazers page and put all elements into a list, if they passed a checking
            JsonNode stargazerJSON = new ObjectMapper().readTree(lastPageResponseEntity.getBody());
            //todo break outer loop in more elegant way!
            JsonNode firstStargazerDateJson = stargazerJSON.get(0).get("starred_at");
            logger.debug(firstStargazerDateJson.textValue());
            LocalDate timestamp = Utils.parseTimestamp(firstStargazerDateJson.textValue());
            //if first element timestamp is out this week range -> break the outer loop
            if (!Utils.isWithinThisWeekRange(timestamp)) {
                logger.info("page number" + pageNumber + "contain stargazer date that is" +
                        "not in current week range");
                logger.info("stop iteration over stargazers page!");
                break;
                //todo break outer loop in more elegant way!
            }
            List<LocalDate> stargazesFromPageList = StreamSupport.stream(stargazerJSON.spliterator(), false)
                    .map(e -> e.get("starred_at"))//get element "starred_at from each JSON inside the node
                    .map(e -> Utils.parseTimestamp(e.textValue()))
                    .filter(Utils::isWithinThisWeekRange)
                    .collect(Collectors.toList());
            logger.debug(stargazesFromPageList.toString());
            thisWeekStargazersDateList.addAll(stargazesFromPageList);

        }
        logger.debug("finish parsing stargezers" + thisWeekStargazersDateList.toString());
    }


    public int getLastStargazersPageNumberByProjectName(String projectName) throws URISyntaxException {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/vnd.github.v3.star+json");
        String buildedURL = UriComponentsBuilder.fromHttpUrl("https://api.github.com/repos/")
                .path(projectName).path("/stargazers").build().encode()
                .toUriString();
        logger.debug("URL to get the last stargazers page number:" + buildedURL);
        RequestEntity requestEntity = new RequestEntity(headers, HttpMethod.GET, new URI(buildedURL));
        ResponseEntity<String> responseEntity = template.exchange(requestEntity, String.class);
        String link = responseEntity.getHeaders().getFirst("Link");
        logger.debug("Link: " + link);
        logger.debug("parse link by regexp");
        Pattern p = Pattern.compile("page=(\\d*)>; rel=\"last\"");
        Matcher m = p.matcher(link);
        int lastPageNum = 0;
        if (m.find()) {
            lastPageNum = Integer.valueOf(m.group(1));
            logger.debug("parse result: " + lastPageNum);
        }
        return lastPageNum;
    }


}
