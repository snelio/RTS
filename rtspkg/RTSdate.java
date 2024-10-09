package rtspkg;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

/*
 *
 *
 */

public class RTSdate {
    private static final Pattern DATE_PATTERN = Pattern.compile("^\\{d}4-\\{d}2-\\{2}$");

    public static Boolean isValidDate (String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setLenient(false);
        try {
            simpleDateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            System.err.println(e.toString());
            return false;
        }
    }

    public static Boolean isValidDateFormat (String dateFormat) {
        return DATE_PATTERN.matcher(dateFormat).matches() ;
    }

    public static String addDaysToDate (String strDate, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try{
            cal.setTime(sdf.parse(strDate));
        }catch(ParseException e){
            System.err.println(e.toString());
        }        
        // add days number to calendar date
        cal.add(Calendar.DAY_OF_MONTH, days);  
        String newDate = sdf.format(cal.getTime());  
        return newDate;
    }

}
