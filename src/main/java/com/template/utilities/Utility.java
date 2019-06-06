package com.template.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {

    public static String getJSON(Object object) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(object);
    }

    public static Date parseDate(String dateString) {
        //2017-09-23
        if (dateString == null) return null;
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return fmt.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            System.err.print("Could not parse datetime: " + dateString);
            return null;
        }
    }

    public static Date parseDateTime(String dateString) {
        if (dateString == null) return null;
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (dateString.contains("T")) dateString = dateString.replace('T', ' ');
        if (dateString.contains("Z")) dateString = dateString.replace("Z", "+0000");
        else {
            int lastIndexOfColon = dateString.lastIndexOf(':');
            dateString = dateString.substring(0, lastIndexOfColon + 3 );
        }
        try {
            return fmt.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            System.err.print("Could not parse datetime: " + dateString);
            return null;
        }
    }

    public static void main(String args[]) throws Exception {
        parseDateTime("2017-09-18T22:16:42.000+0000");
    }
}
