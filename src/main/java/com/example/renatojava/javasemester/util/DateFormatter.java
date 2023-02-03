package com.example.renatojava.javasemester.util;

import com.example.renatojava.javasemester.Application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public interface DateFormatter {

     static String getDateTimeFormatted(String start){
         SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
         SimpleDateFormat desiredFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
         String formattedDate = null;
         try{
             Date date = originalFormat.parse(start);
             formattedDate = desiredFormat.format(date);
         }catch (ParseException e){
             Application.logger.error(e.getMessage(), e);
         }
        return formattedDate;
    }

    static String getDateFormatted(String start){
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat desiredFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = null;
        try{
            Date date = originalFormat.parse(start);
            formattedDate = desiredFormat.format(date);
        }catch (ParseException e){
            Application.logger.error(e.getMessage(), e);
        }
        return formattedDate;
    }

    static String getTimeWithSeconds(String start){
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat desiredFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = null;
        try{
            Date date = originalFormat.parse(start);
            formattedDate = desiredFormat.format(date);
        }catch (ParseException e){
            Application.logger.error(e.getMessage(), e);
        }
        return formattedDate;
    }

    static boolean isEqualToday(String date){
         String today = getDateFormatted(LocalDate.now().toString());
         String dateToCheck = getDateFormatted(date);
        return dateToCheck.equals(today);
    }

}
