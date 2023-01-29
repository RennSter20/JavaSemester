package com.example.renatojava.javasemester.threads;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.entity.*;
import com.example.renatojava.javasemester.exceptions.NoRecentChangesException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LastChangeThread implements Runnable{
    @Override
    public void run() {
        ChangeWriter writer = new ChangeWriter();

        try{
            List<String> doctorsTime = writer.readTimeDoctors();
            List<String> patientsTime = writer.readTimePatients();
            List<String> proceduresTime = writer.readTimeProcedures();
            List<String> roomsTime = writer.readTimeRooms();

            Boolean allEmpty = true;

            String doctorLatest = null;
            String patientLatest = null;
            String procedureLatest = null;
            String roomLatest = null;

            List<String> latestStrings = new ArrayList<>();

            if(doctorsTime.size() > 0){
                allEmpty = false;
                doctorLatest = doctorsTime.get(doctorsTime.size() - 1);
                latestStrings.add(doctorLatest);
            }
            if(patientsTime.size() > 0){
                allEmpty = false;
                patientLatest = patientsTime.get(patientsTime.size() - 1);
                latestStrings.add(patientLatest);
            }
            if(proceduresTime.size() > 0){
                allEmpty = false;
                procedureLatest = proceduresTime.get(proceduresTime.size() - 1);
                latestStrings.add(procedureLatest);
            }
            if(roomsTime.size() > 0){
                allEmpty = false;
                roomLatest = roomsTime.get(roomsTime.size() - 1);
                latestStrings.add(roomLatest);
            }
            if(allEmpty) throw new NoRecentChangesException("No recent change!");

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
                Application.getStage().setTitle("Latest change, " + change + " ---> Time of change: " + latestDate);

        }catch (NoRecentChangesException e){
            Application.getStage().setTitle(e.getMessage());
        }


    }
}
