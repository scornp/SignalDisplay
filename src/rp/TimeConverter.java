package rp;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: scornp
 * Date: 17-Mar-2007
 * Time: 00:03:50
 * This class is to do the time conversions for signal analysis
 */
public class TimeConverter {

    //todo tidy up the method names globally

    /**
     * This routine accepts a date in the string format yyyyMMddhhmmss[.ext]
     * and converts it into a Calender object.
     *
     * @param strTime
     * @return
     */
    public static Calendar getCalendarX(String strTime) {
        Calendar testCalendar = Calendar.getInstance();
        Calendar testStartDate = (Calendar) testCalendar.clone();

        int yy = new Integer(strTime.substring(0, 4));
        int MM = new Integer(strTime.substring(4, 6));
        int dd = new Integer(strTime.substring(6, 8));
        int hh = new Integer(strTime.substring(8, 10));
        int mm = new Integer(strTime.substring(10, 12));
        int ss = new Integer(strTime.substring(12, 14));

        testStartDate.set(yy, --MM, dd, hh, mm, ss);

        return testStartDate;
    }

    /**
     * This routine accepts a date in the string format yyyyMMddhhmmss[.ext]
     * and returns the time in milli seconds since 00/01/1970
     * @param strTime
     * @return
     */
        public static long getCalendarXInMills(String strTime) {
        Calendar testCalendar = Calendar.getInstance();
        Calendar testStartDate = (Calendar) testCalendar.clone();

        int yy = new Integer(strTime.substring(0, 4));
        int MM = new Integer(strTime.substring(4, 6));
        int dd = new Integer(strTime.substring(6, 8));
        int hh = new Integer(strTime.substring(8, 10));
        int mm = new Integer(strTime.substring(10, 12));
        int ss = new Integer(strTime.substring(12, 14));

        testStartDate.set(yy, --MM, dd, hh, mm, ss);

        return testStartDate.getTimeInMillis();
    }


    public static Date getDateFromMills(long timeInMilliSec){
        Calendar testCalendar = Calendar.getInstance();
        Calendar testStartDate = (Calendar) testCalendar.clone();

         testStartDate.setTimeInMillis(timeInMilliSec);

        return testStartDate.getTime();
    }
}
