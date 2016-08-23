package com.rhcloud.analytics4github.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

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


}
