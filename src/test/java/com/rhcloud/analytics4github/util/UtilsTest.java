package com.rhcloud.analytics4github.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

/**
 * @author lyashenkogs.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
//@TestPropertySource(locations = "classpath:application-test.properties")
public class UtilsTest {
    Logger logger = LoggerFactory.getLogger(UtilsTest.class);

    @Autowired
    RestTemplate template;

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

}
