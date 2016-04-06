package com.idg.csv.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by agermenos on 4/6/16.
 */
public class Formatter {
    public static Date formatDate(String token, String format){
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {

            Date date = formatter.parse(token);
            return date;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
