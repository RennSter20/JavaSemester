package com.example.renatojava.javasemester.util;

public class DateFormatter {

    private String start;

    public DateFormatter(String start) {
        this.start = start;
    }

    public String getDateTimeFormatted(){
        String fullDateTime = start.substring(8,10) + "-" + start.substring(5,7) + "-" + start.substring(0,4) + " " + start.substring(11,13) + ":" + start.substring(14,16);
        return fullDateTime;
    }

    public String getDateFormatted(){
        return start.substring(8,10) + "-" + start.substring(5,7) + "-" + start.substring(0,4);
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }
}
