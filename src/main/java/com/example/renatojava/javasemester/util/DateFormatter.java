package com.example.renatojava.javasemester.util;

import java.time.LocalDate;

public interface DateFormatter {

     static String getDateTimeFormatted(String start){
        String fullDateTime = start.substring(8,10) + "-" + start.substring(5,7) + "-" + start.substring(0,4) + " " + start.substring(11,13) + ":" + start.substring(14,16);
        return fullDateTime;
    }

    static String getDateFormatted(String start){
        return start.substring(8,10) + "-" + start.substring(5,7) + "-" + start.substring(0,4);
    }

    static boolean isEqualToday(String date){
         String today = LocalDate.now().toString();
         String dateToCheck = date.substring(0,4) + "-" + date.substring(5,7) + "-" + date.substring(8,10);
        return dateToCheck.equals(today);
    }

}
