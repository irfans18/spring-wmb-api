package com.enigma.wmb_api.util;

import com.enigma.wmb_api.constant.enums.ReportPeriod;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    public static String parseDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static Date parseDate(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        Date tempDate = new Date();
        try {
            tempDate = sdf.parse(sdf.format(date));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return tempDate;
    }

    public static DateRange getDateRange(ReportPeriod period){
        InstantRange instant = getInstantRange();
        Date endDate = Date.from(instant.endOfDay);
        Date startDate;

        startDate = switch (period) {
            case DAILY -> Date.from(instant.startOfDay);
            case WEEKLY -> Date.from(instant.startOfDay.minus(Duration.ofDays(7)));
            case MONTHLY -> Date.from(instant.startOfDay.minus(Duration.ofDays(30)));
        };

        return new DateRange(startDate, endDate);
    }

    private static InstantRange getInstantRange() {
        // Get the current date in the default time zone
        LocalDate today = LocalDate.now();

        // Get the start of the day (00:00) and end of the day (23:59:59) in local time
        Instant startOfDay = today.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Instant endOfDay = today.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant();

        // Convert LocalDateTime to Date
        // Date startDate = Date.from(startOfDay);
        // Date endDate = Date.from(endOfDay);
        return new InstantRange(startOfDay, endOfDay);
    }

    public record DateRange(Date startDate, Date endDate) {}

    private record InstantRange(Instant startOfDay, Instant endOfDay) {}
}
