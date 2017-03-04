package onethreeseven.common.util;

import org.junit.Assert;
import org.junit.Test;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Testing parsing date/time using {@link TimeUtil}.
 * @author Luke Bermingham
 */
public class TimeUtilTest {

    @Test
    public void parseDate1() throws Exception {
        LocalDateTime expected = LocalDateTime.of(1994, 6, 17, 13, 0, 2);
        LocalDateTime actual = TimeUtil.parseDate("17/06/1994 13:00:02");
        Assert.assertTrue(ChronoUnit.SECONDS.between(expected, actual) == 0);
    }

    @Test
    public void parseDate2() throws Exception {
        LocalDateTime expected = LocalDateTime.of(2012, 8, 16, 14, 0, 0);
        LocalDateTime actual = TimeUtil.parseDate("16/08/2012 2:00 PM");
        Assert.assertTrue(ChronoUnit.SECONDS.between(expected, actual) == 0);
    }

    @Test
    public void parseDate3() throws Exception {
        LocalDateTime expected = LocalDateTime.of(2007, 4, 12, 16, 39, 48);
        LocalDateTime actual = TimeUtil.parseDate("2007-04-12 16:39:48");
        Assert.assertTrue(ChronoUnit.SECONDS.between(expected, actual) == 0);
    }

    @Test
    public void parseDate4() throws Exception {
        LocalDateTime expected = LocalDateTime.of(1993, 5, 13, 16, 39, 48);
        LocalDateTime actual = TimeUtil.parseDate("19930513 16:39:48");
        Assert.assertTrue(ChronoUnit.SECONDS.between(expected, actual) == 0);
    }

    @Test
    public void parseDate5() throws Exception {
        LocalDateTime expected = LocalDateTime.of(2011, 12, 3, 10, 15, 30);
        LocalDateTime actual = TimeUtil.parseDate("2011-12-03T10:15:30+01:00");
        Assert.assertTrue(ChronoUnit.SECONDS.between(expected, actual) == 0);
    }

    @Test
    public void parseDate6() throws Exception {
        LocalDateTime expected = LocalDateTime.of(2012, 11, 9, 21, 51, 2);
        LocalDateTime actual = TimeUtil.parseDate("2012-11-09T21:51:02Z");
        Assert.assertTrue(ChronoUnit.SECONDS.between(expected, actual) == 0);
    }


}