package utils;

import conf.SystemConfig;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Converter {
    private static final DateFormat dateFormat = new SimpleDateFormat(SystemConfig.DATE_FORMAT);

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

    public static double parseDouble(String number) {
        try {
            return Double.parseDouble(number);
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }
}
