package onethreeseven.common.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.function.Predicate;

/**
 * A single, immutable temporal category, such as, "Morning".
 * @author Luke Bermingham
 */
public final class TimeCategory {

    private static final HashMap<String, TimeCategory> categoryPool = new HashMap<>();
    public static TimeCategory getCategory(String categoryName){
        return TimeCategory.categoryPool.get(categoryName);
    }

    private final Predicate<LocalDateTime> containmentFunction;

    /**
     * Each TimeCategory is given a unique string name that can be used
     * to retrieve it.
     */
    public final String name;

    private TimeCategory(Predicate<LocalDateTime> containmentFunction, String name){
        this.containmentFunction = containmentFunction;
        this.name = name;
        TimeCategory.categoryPool.put(this.name, this);
    }

    public boolean contains(LocalDateTime query){
        return this.containmentFunction.test(query);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeCategory that = (TimeCategory) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    ///////////////////
    //Parts of a day
    ///////////////////

    public static TimeCategory SMALL_HOURS = new TimeCategory(localDateTime -> {
        int hour = localDateTime.getHour();
        return hour >= 0 && hour <= 6;
    }, "SMALL_HOURS");

    public static TimeCategory MORNING = new TimeCategory(localDateTime -> {
        int hour = localDateTime.getHour();
        return hour > 6 && hour <= 9;
    }, "MORNING");

    public static TimeCategory BEFORE_NOON = new TimeCategory(localDateTime ->  {
        int hour = localDateTime.getHour();
        return hour > 9 && hour <= 12;
    }, "BEFORE_NOON");

    public static TimeCategory AFTERNOON = new TimeCategory(localDateTime -> {
        int hour = localDateTime.getHour();
        return hour > 12 && hour <= 18;
    }, "AFTERNOON");

    public static TimeCategory EVENING = new TimeCategory(localDateTime -> {
        int hour = localDateTime.getHour();
        return hour > 18 && hour <= 24;
    }, "EVENING");

}
