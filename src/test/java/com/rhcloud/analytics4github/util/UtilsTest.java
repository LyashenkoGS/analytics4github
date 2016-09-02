package com.rhcloud.analytics4github.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;

/**
 * @author lyashenkogs.
 */
@RunWith(JUnit4.class)
//@TestPropertySource(locations = "classpath:application-test.properties")
public class UtilsTest {
    private static Logger LOG = LoggerFactory.getLogger(UtilsTest.class);

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
    public void testIsWithinThisMonthRange() {
     /*   //given timestamps
      //Todo: generate timestamps dynamically, cause the test will broke in some time
        LocalDate beforeThisMonthTimestamp = Utils.parseTimestamp("2016-06-15T16:38:45Z");
        LocalDate thisMonthBeginTimestamp = Utils.parseTimestamp("2016-08-01T16:38:45Z");
        LocalDate thisMonthMiddleTimestamp = Utils.parseTimestamp("2016-08-14T16:38:45Z");
        LocalDate thisMonthEndTimestamp = Utils.parseTimestamp("2016-08-31T16:38:45Z");
        LocalDate afterThisMonthTimestamp = Utils.parseTimestamp("2016-09-01T16:38:45Z");

        //when timestamps are within this week range
        List<LocalDate> localDatesList = new LinkedList<>();
        localDatesList.add(thisMonthBeginTimestamp);
        localDatesList.add(thisMonthMiddleTimestamp);
        localDatesList.add(thisMonthEndTimestamp);

        //then return true
        localDatesList.forEach(e -> assertTrue(Utils.isWithinThisMonthRange(e)));
        //else false
        assertFalse(Utils.isWithinThisMonthRange(beforeThisMonthTimestamp));
        assertFalse(Utils.isWithinThisMonthRange(afterThisMonthTimestamp));*/
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
        List<Integer> frequencyList = Utils.parseWeekStargazersMapFrequencyToWeekFrequencyList(mockWeekStargazersFrequencyMap);
        //then  it must match the etalonFrequencyList
        JsonNode frequencyListJSON = new ObjectMapper().readTree(frequencyList.toString());
        assertEquals(frequencyListJSON, etalonFrequencyListJSON);
    }


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

    /*@Test TODO:Depends on the current month size. Fail on month with size other then 31 days.
    public void testParseMonthMapToFrequencyList() throws IOException, ClassNotFoundException {
        final TreeMap<LocalDate, Integer> mockWeekStargazersFrequencyMap = this.getFrequencyMapFromFile("monthStargazersFrequencyMap.ser");
        final JsonNode etalonFrequencyListJSON = new ObjectMapper().readTree(new ClassPathResource("monthStargazersFrequencyForHighChart.json")
                .getInputStream())
                .get(0).get("data");
        LOG.debug("etalon frequency: " + etalonFrequencyListJSON.toString());
        //parse map to month frequency List
        List<Integer> monthFrequencyList = Utils.parseMonthFrequencyMapToFrequencyLIst(mockWeekStargazersFrequencyMap);
        JsonNode monthStargazersFrequncyJSON = new ObjectMapper().readTree(monthFrequencyList.toString());
        assertEquals(etalonFrequencyListJSON.size(), monthStargazersFrequncyJSON.size());
        assertEquals(etalonFrequencyListJSON, monthStargazersFrequncyJSON);
    }*/

    @Test
    public void testGetThisMonthBeginInstant() {
        //format should be like: 2016-08-01T00:00:01Z
        LOG.debug(Utils.getThisMonthBeginInstant().toString());
    }
}
