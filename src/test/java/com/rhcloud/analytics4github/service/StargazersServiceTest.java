package com.rhcloud.analytics4github.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.min;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

/**
 * @author lyashenkogs
 */
@RunWith(SpringRunner.class)
@RestClientTest(StargazersService.class)
@AutoConfigureWebClient(registerRestTemplate = true)
//because RestTempalate is @Autowired into the service
@TestPropertySource(locations = "classpath:application-test.properties")
public class StargazersServiceTest {
    private static Logger LOG = LoggerFactory.getLogger(StargazersServiceTest.class);

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private StargazersService service;

    //Util method
    private TreeMap<LocalDate, Integer> getMockWeekStargazersFrequencyMap() throws IOException, ClassNotFoundException {
        TreeMap<LocalDate, Integer> weekStargazersFrequency = null;
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("weekStargazersFrequencyMap.ser").getFile());
        FileInputStream fileIn = new FileInputStream(file);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        weekStargazersFrequency = (TreeMap<LocalDate, Integer>) in.readObject();
        in.close();
        fileIn.close();
        LOG.debug("got MockWeekStargazersFrequencyMap from a file " + file.getName() + " :" + weekStargazersFrequency.toString());
        return weekStargazersFrequency;
    }

    @Test
    public void testServiceAccessGithubAtLeasOnce() throws IOException, URISyntaxException, ClassNotFoundException {
        server.expect(min(1), requestTo("https://api.github.com/repos/FallibleInc/security-guide-for-developers/stargazers"))
                .andRespond(MockRestResponseCreators.withSuccess());
        service.getThisWeekStargazersFrequencyPerProject("FallibleInc/security-guide-for-developers");
        server.verify();
    }


    /**
     * Parse  TreeMap<LocalDate, Integer> mockWeekStargazersFrequenyMap
     * to JSON for HighChart.js library.
     * Output format represent frequency by day of a week. From monday
     * to sunday.
     */
    @Test
    public void testParseMapToFrequencyList() throws IOException, ClassNotFoundException {
        //given stargazersFrequencyMap and JSON that the Map should became after parsing
        final TreeMap<LocalDate, Integer> mockWeekStargazersFrequencyMap = getMockWeekStargazersFrequencyMap();
        final JsonNode etalonFrequencyListJSON = new ObjectMapper().readTree(new ClassPathResource("weekStargazersFrequencyForHIghChart.json")
                .getInputStream())
                .get(0).get("data"); //get [390,470,349,189,143,33,0]
        //when parse mockWeekStargazersFrequencyMap to frequencyList
        List<Integer> frequencyList = service.parseWeekStargazersMapFrequencyToWeekFrequencyList(mockWeekStargazersFrequencyMap);
        //then  it must match the etalonFrequencyList
        JsonNode frequencyListJSON = new ObjectMapper().readTree(frequencyList.toString());
        assertEquals(frequencyListJSON, etalonFrequencyListJSON);
    }

    @Test
    public void testBuildJsonForHighChart() throws IOException, ClassNotFoundException {
        LOG.debug("testBuildJsonForHighChart");
        final JsonNode etalonOutputJson = new ObjectMapper().readTree(new ClassPathResource("weekStargazersFrequencyForHIghChart.json")
                .getInputStream());
        ArrayNode outputJson = service.buildJsonForHIghChart(service.parseWeekStargazersMapFrequencyToWeekFrequencyList(getMockWeekStargazersFrequencyMap()));
        LOG.debug("output json:" + outputJson);
        LOG.debug("etalon json:" + etalonOutputJson);

        //todo find a way to compare object properly, ignoring whitespaces in an array
        // [{"name":"Stars","data":[390, 470, 349, 189, 143, 33, 0]}]
        //[{"name":"Stars","data":[390,470,349,189,143,33,0]}]
        //assertEquals(outputJson.get(0).get("data").size(),(etalonOutputJson.get(0).get("data").size()));
    }


}
