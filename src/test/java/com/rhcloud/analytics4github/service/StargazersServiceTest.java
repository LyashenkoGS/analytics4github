package com.rhcloud.analytics4github.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;

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
    private StargazersService service;


    //Util method
    private TreeMap<LocalDate, Integer> getFrequencyMapFromFile(String name) throws IOException, ClassNotFoundException {
        TreeMap<LocalDate, Integer> weekStargazersFrequency = null;
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(name).getFile());
        FileInputStream fileIn = new FileInputStream(file);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        weekStargazersFrequency = (TreeMap<LocalDate, Integer>) in.readObject();
        in.close();
        fileIn.close();
        LOG.debug("got MockWeekStargazersFrequencyMap from a file " + file.getName() + " :" + weekStargazersFrequency.toString());
        return weekStargazersFrequency;
    }


    /**
     * Parse  TreeMap<LocalDate, Integer> mockWeekStargazersFrequenyMap
     * to JSON for HighChart.js library.
     * Output format represent frequency by day of a week. From monday
     * to sunday.
     */
    @Test
    public void testParseWeekMapToFrequencyList() throws IOException, ClassNotFoundException {
        //given stargazersFrequencyMap and JSON that the Map should became after parsing
        final TreeMap<LocalDate, Integer> mockWeekStargazersFrequencyMap = this.getFrequencyMapFromFile("weekStargazersFrequencyMap.ser");
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
    public void testParseMonthMapToFrequencyList() throws IOException, ClassNotFoundException {
        final TreeMap<LocalDate, Integer> mockWeekStargazersFrequencyMap = this.getFrequencyMapFromFile("monthStargazersFrequencyMap.ser");
        final JsonNode etalonFrequencyListJSON = new ObjectMapper().readTree(new ClassPathResource("monthStargazersFrequencyForHighChart.json")
                .getInputStream())
                .get(0).get("data");
        LOG.debug("etalon frequency: " + etalonFrequencyListJSON.toString());
        //parse map to month frequency List
        List<Integer> monthFrequencyList = service.parseMonthFrequencyMapToFrequencyLIst(mockWeekStargazersFrequencyMap);
        JsonNode monthStargazersFrequncyJSON = new ObjectMapper().readTree(monthFrequencyList.toString());
        assertEquals(etalonFrequencyListJSON.size(), monthStargazersFrequncyJSON.size());
        assertEquals(etalonFrequencyListJSON, monthStargazersFrequncyJSON);

    }


}
