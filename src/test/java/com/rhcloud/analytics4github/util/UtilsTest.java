package com.rhcloud.analytics4github.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

}
