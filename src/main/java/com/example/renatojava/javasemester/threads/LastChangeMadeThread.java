package com.example.renatojava.javasemester.threads;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.entity.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LastChangeMadeThread implements Runnable{
    @Override
    public void run() {
        ChangeWriter writer = new ChangeWriter();

        String doctorsLatest = String.valueOf(writer.readTimeDoctors().stream().reduce((first, second) -> second).get());
        String patientsLatest = String.valueOf(writer.readTimePatients().stream().reduce((first, second) -> second).get());
        String proceduresLatest = String.valueOf(writer.readTimeProcedures().stream().reduce((first, second) -> second).get());
        String roomsLatest = String.valueOf(writer.readTimeRooms().stream().reduce((first, second) -> second).get());

        List<String> latestStrings = List.of(doctorsLatest, patientsLatest, proceduresLatest, roomsLatest);
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Integer index = null;

        Date latestDate = null;

        for(String s : latestStrings){
            try {
                if(latestDate == null){
                    latestDate = sf.parse(s);
                    index = latestStrings.indexOf(s);
                }else{
                    if(sf.parse(s).after(latestDate)){
                        latestDate = sf.parse(s);
                        index = latestStrings.indexOf(s);
                    }
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        String change = "";

        switch (index){
            case 0:
                if(((Doctor) writer.readDoctors().get(writer.readDoctors().size() - 1)).getDoctorFullName().contains("-")){
                    change = "doctor removed.";
                }else{
                    change = "doctor updated: " + ((Doctor) writer.readDoctors().get(writer.readDoctors().size() - 1)).getDoctorFullName();
                }
                break;
            case 1:
                if(((Patient)writer.readPatients().get(writer.readPatients().size() - 1)).getFullName().contains("-")){
                    change = "patient removed.";
                }else{
                    change = "patient updated: " + ((Patient)writer.readPatients().get(writer.readPatients().size() - 1)).getFullName();
                }
                break;
            case 2:
                if(((Procedure)writer.readProcedures().get(writer.readProcedures().size() - 1)).description().contains("-")){
                    change = "procedure removed.";
                }else{
                    change = "procedure updated: " + ((Procedure)writer.readProcedures().get(writer.readProcedures().size() - 1)).description();
                }
                break;
            case 3:
                if(((DoctorRoom)writer.readRooms().get(writer.readRooms().size() - 1)).getRoomName().contains("-")){
                    change = "room removed.";
                }else{
                    change = "doctor room: " + ((DoctorRoom)writer.readRooms().get(writer.readRooms().size() - 1)).getRoomName();
                }
                break;
        }

        Application.getStage().setTitle("Latest change, " + change + " Time of change: " + latestDate);
    }
}
