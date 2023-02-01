package com.example.renatojava.javasemester.threads;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.entity.*;
import com.example.renatojava.javasemester.exceptions.NoRecentChangesException;
import com.example.renatojava.javasemester.util.ChangeWriter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LastChangeThread implements Runnable{

    private ChangeWriter writer;

    public LastChangeThread(ChangeWriter writer) {
        this.writer = writer;
    }

    @Override
    public void run() {
        try{

            List<String> doctorsTime = writer.readTimeDoctors();
            List<String> patientsTime = writer.readTimePatients();
            List<String> proceduresTime = writer.readTimeProcedures();
            List<String> roomsTime = writer.readTimeRooms();
            Boolean allEmpty = true;

            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Integer index = null;
            Date latestDate = null;


            if(doctorsTime.size() > 0){
                allEmpty = false;
                if(latestDate == null){
                    latestDate = sf.parse(doctorsTime.get(doctorsTime.size() - 1));
                    index = 0;
                }
            }
            if(patientsTime.size() > 0){
                allEmpty = false;
                if(latestDate == null){
                    latestDate = sf.parse(patientsTime.get(patientsTime.size() - 1));
                    index = 1;
                }else if(latestDate.before(sf.parse(patientsTime.get(patientsTime.size() - 1)))){
                    latestDate = sf.parse(patientsTime.get(patientsTime.size() - 1));
                    index = 1;
                }
            }
            if(proceduresTime.size() > 0){
                allEmpty = false;
                if(latestDate == null){
                    latestDate = sf.parse(proceduresTime.get(proceduresTime.size() - 1));
                    index = 2;
                }else if(latestDate.before(sf.parse(proceduresTime.get(proceduresTime.size() - 1)))){
                    latestDate = sf.parse(proceduresTime.get(proceduresTime.size() - 1));
                    index = 2;
                }
            }
            if(roomsTime.size() > 0){
                allEmpty = false;
                if(latestDate == null){
                    latestDate = sf.parse(roomsTime.get(roomsTime.size() - 1));
                    index = 3;
                }else if(latestDate.before(sf.parse(roomsTime.get(roomsTime.size() - 1)))){
                    latestDate = sf.parse(roomsTime.get(roomsTime.size() - 1));
                    index = 3;
                }
            }
            if(allEmpty) throw new NoRecentChangesException("Hospital Java                                                                  No recent change!");

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
                Application.getStage().setTitle("Hospital Java                                                                                      Latest change, " + change + " ---> Time of change: " + latestDate);
        }catch (NoRecentChangesException e){
            Application.getStage().setTitle(e.getMessage());
        } catch (ParseException e) {
            Application.logger.error(e.getMessage(), e);
        }


    }
}
