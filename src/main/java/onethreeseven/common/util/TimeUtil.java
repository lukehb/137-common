package onethreeseven.common.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.function.Function;

/**
 * A utilities for dealing with dates/times.
 * @author Luke Bermingham
 */
public final class TimeUtil {

    private static final DateTimeFormatter fmt1 =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH); //17/06/1994 13:00:02;

    private static final DateTimeFormatter fmt2 =
            DateTimeFormatter.ofPattern("dd/MM/yyyy h:mm a", Locale.ENGLISH); //16/08/2012 2:00 PM"

    private static final DateTimeFormatter fmt3 =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH); //2007-04-12 16:39:48

    private static  final DateTimeFormatter fmt4 =
            DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss", Locale.ENGLISH); //19930513 16:39:48

    private static final DateTimeFormatter fmt5 =
            DateTimeFormatter.ISO_DATE_TIME; //2011-12-03T10:15:30+01:00

    private static final ArrayList<Function<String, LocalDateTime>> formatters = new ArrayList<>();
    static {
        formatters.add(s -> LocalDateTime.parse(s, fmt1));
        formatters.add(s -> LocalDateTime.parse(s, fmt2));
        formatters.add(s -> LocalDateTime.parse(s, fmt3));
        formatters.add(s -> LocalDateTime.parse(s, fmt4));
        formatters.add(s -> LocalDateTime.parse(s, fmt5));

        //parse an epoch millis timestamp
        formatters.add(s -> LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.valueOf(s)), ZoneId.systemDefault()));
    }

    private TimeUtil() {
    }

    /**
     * Parse a given date string using our available date formats, also rearranges the date formatters
     * so if we used one successfully it goes to the top (in the hope it gets used again).
     * @param toParse the date string to parse
     * @return the parsed date string as a DateTime or if not formatter worked - null
     */
    public static LocalDateTime parseDate(String toParse) {


        for (int i = 0; i < formatters.size(); i++) {
            Function<String, LocalDateTime> strategy = formatters.get(i);
            try {
                LocalDateTime dateTime = strategy.apply(toParse);
                //it worked, so swap position in array so next date check will use this first
                if (i != 0) {
                    //add first position at current
                    Collections.swap(formatters, i, 0);
                }
                return dateTime;
            } catch (Exception ignore) {

            }
        }
        return null;
    }

}
