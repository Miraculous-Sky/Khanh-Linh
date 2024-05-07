package utils;

import conf.Config;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    private static final DateFormat dateFormat = new SimpleDateFormat(Config.DATE_FORMAT);

    public static String formatDate(Date date) {
        return dateFormat.format(date);
    }

    public static Date parseDate(String dateString) {
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }
}
