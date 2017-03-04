package onethreeseven.common.model;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;

/**
 * Attempts to transform a {@link java.time.LocalDateTime}
 * to one of the provided {@link TimeCategory}.
 * @author Luke Bermingham
 */
public class TimeCategoryPool {

    private final TimeCategory[] potentialCategories;

    public TimeCategoryPool(@Nonnull TimeCategory... timeCategories){
        if(timeCategories.length == 0){
            throw new IllegalArgumentException("Must have at least one time category.");
        }
        this.potentialCategories = timeCategories;
    }

    public TimeCategory resolve(LocalDateTime query){
        for (int i = 0; i < potentialCategories.length; i++) {
            TimeCategory potentialCategory = potentialCategories[i];
            if (potentialCategory.contains(query)) {
                //move the one that matched into the first position (for next time)
                potentialCategories[i] = potentialCategories[0];
                potentialCategories[0] = potentialCategory;
                return potentialCategory;
            }
        }
        throw new IllegalArgumentException("There was no time category for your query: " + query);
    }

    ////////////////////////////
    ////Pre-made transformers
    ////////////////////////////

    public static final TimeCategoryPool TIMES_OF_DAY = new TimeCategoryPool(
            TimeCategory.AFTERNOON,
            TimeCategory.BEFORE_NOON,
            TimeCategory.MORNING,
            TimeCategory.SMALL_HOURS,
            TimeCategory.EVENING
    );


}
