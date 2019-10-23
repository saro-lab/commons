package me.saro.commons;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * date util
 * @author		PARK Yong Seo
 * @since		5.0
 */
public class Dates {

    public static Map<String, DateTimeFormatter> formatters = new HashMap<>();
    public static Map<String, SimpleDateFormat> simpleDateFormats = new HashMap<>();

    public static ZonedDateTime toZonedDateTime(Calendar calendar) {
        var tz = calendar.getTimeZone();
        return ZonedDateTime.ofInstant(calendar.toInstant(), tz != null ? tz.toZoneId() : ZoneId.systemDefault());
    }

    public static LocalDateTime toLocalDateTime(Calendar calendar) {
        var tz = calendar.getTimeZone();
        return LocalDateTime.ofInstant(calendar.toInstant(), tz != null ? tz.toZoneId() : ZoneId.systemDefault());
    }

    public static LocalDate toLocalDate(Calendar calendar) {
        var tz = calendar.getTimeZone();
        return LocalDate.ofInstant(calendar.toInstant(), tz != null ? tz.toZoneId() : ZoneId.systemDefault());
    }

    public static LocalTime toLocalTime(Calendar calendar) {
        var tz = calendar.getTimeZone();
        return LocalTime.ofInstant(calendar.toInstant(), tz != null ? tz.toZoneId() : ZoneId.systemDefault());
    }

    public static ZonedDateTime parseZonedDateTime(String date, String format, ZoneId zone) {
        return ZonedDateTime.parse(date, toFormatter(format).withZone(zone));
    }

    public static ZonedDateTime parseZonedDateTime(String date, String format) {
        return ZonedDateTime.parse(date, toFormatter(format));
    }

    public static LocalDateTime parseLocalDateTime(String date, String format) {
        return LocalDateTime.parse(date, toFormatter(format));
    }

    public static LocalDate parseLocalDate(String date, String format) {
        return LocalDate.parse(date, toFormatter(format));
    }

    public static LocalTime parseLocalTime(String date, String format) {
        return LocalTime.parse(date, toFormatter(format));
    }

    public static Calendar parseCalendar(String date, String format) {
        var calendar = Calendar.getInstance();
        calendar.setTime(parseDate(date, format));
        return calendar;
    }

    public static Date parseDate(String date, String format) {
        try {
            return toSimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            throw new DateTimeParseException(e.getMessage(), date, -1, e);
        }
    }

    public static synchronized DateTimeFormatter toFormatter(String format) {
        DateTimeFormatter formatter;
        if ((formatter = formatters.get(format)) == null) {
            formatters.put(format, (formatter = DateTimeFormatter.ofPattern(format)));
        }
        return formatter;
    }

    public static synchronized SimpleDateFormat toSimpleDateFormat(String format) {
        SimpleDateFormat simpleDateFormat;
        if ((simpleDateFormat = simpleDateFormats.get(format)) == null) {
            simpleDateFormats.put(format, (simpleDateFormat = new SimpleDateFormat(format)));
        }
        // SimpleDateFormat is not thread-safe
        // solution is return cloned object
        return (SimpleDateFormat)simpleDateFormat.clone();
    }

    public static Calendar toCalendar(Date date) {
        var calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Calendar toCalendar(ZonedDateTime zonedDateTime) {
        var calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(
                zonedDateTime.getYear(), zonedDateTime.getMonthValue() - 1, zonedDateTime.getDayOfMonth(),
                zonedDateTime.getHour(), zonedDateTime.getMinute(), zonedDateTime.getSecond()
        );
        calendar.set(Calendar.MILLISECOND, Math.round(zonedDateTime.getNano() / 1_000_000));
        calendar.setTimeZone(TimeZone.getTimeZone(zonedDateTime.getZone()));
        return calendar;
    }

    public static Calendar toCalendar(LocalDateTime localDateTime) {
        var calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(
                localDateTime.getYear(), localDateTime.getMonthValue() - 1, localDateTime.getDayOfMonth(),
                localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond()
        );
        calendar.set(Calendar.MILLISECOND, localDateTime.getNano() / 1_000_000);
        return calendar;
    }

    public static Calendar toCalendar(LocalDate localDate) {
        var calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(localDate.getYear(), localDate.getMonthValue() - 1, localDate.getDayOfMonth());
        return calendar;
    }

    public static Calendar toCalendar(LocalTime localTime) {
        var calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.HOUR, localTime.getHour());
        calendar.set(Calendar.MINUTE, localTime.getMinute());
        calendar.set(Calendar.SECOND, localTime.getSecond());
        calendar.set(Calendar.MILLISECOND, localTime.getNano() / 1_000_000);
        return calendar;
    }
}
