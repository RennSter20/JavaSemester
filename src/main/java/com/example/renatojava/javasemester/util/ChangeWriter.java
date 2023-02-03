package com.example.renatojava.javasemester.util;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.entity.*;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ChangeWriter<T>{

    private static final String CHANGE_FILE_PATIENTS = "dat\\changes\\patients\\changesPatients.dat";
    private static final String CHANGE_FILE_TIME_PATIENTS = "dat\\changes\\patients\\changesTimePatients.txt";
    private static final String CHANGE_FILE_PATIENTS_ROLE = "dat\\changes\\patients\\changesPatientsRole.txt";

    private static final String CHANGE_FILE_DOCTORS = "dat\\changes\\doctors\\changesDoctors.dat";
    private static final String CHANGE_FILE_TIME_DOCTORS = "dat\\changes\\doctors\\changesTimeDoctors.txt";
    private static final String CHANGE_FILE_DOCTORS_ROLE = "dat\\changes\\doctors\\changesDoctorsRole.txt";


    private static final String CHANGE_FILE_ROOMS = "dat\\changes\\rooms\\changesRooms.dat";
    private static final String CHANGE_FILE_TIME_ROOMS = "dat\\changes\\rooms\\changesTimeRooms.txt";
    private static final String CHANGE_FILE_ROOMS_ROLE = "dat\\changes\\rooms\\changesRoomsRole.txt";


    private static final String CHANGE_FILE_PROCEDURES = "dat\\changes\\procedures\\changesProcedures.dat";
    private static final String CHANGE_FILE_PROCEDURES_ROLE = "dat\\changes\\procedures\\changesProceduresRole.txt";
    private static final String CHANGE_FILE_TIME_PROCEDURES = "dat\\changes\\procedures\\changesTimeProcedures.txt";


    private static final String CHANGE_FILE_CHECKUPS = "dat\\changes\\checkups\\changesCheckups.dat";
    private static final String CHANGE_FILE_CHECKUPS_ROLE = "dat\\changes\\checkups\\changesCheckupsRole.txt";
    private static final String CHANGE_FILE_TIME_CHECKUPS = "dat\\changes\\checkups\\changesTimeCheckups.txt";


    private static final String CHANGE_FILE_USERS = "dat\\changes\\users\\changesUsers.dat";
    private static final String CHANGE_FILE_TIME_USERS = "dat\\changes\\users\\changesTimeUsers.txt";
    private static final String CHANGE_FILE_USERS_ROLE = "dat\\changes\\users\\changesUsersRole.txt";

    private Change change;


    public ChangeWriter(Change change) {
        this.change = change;
    }
    public ChangeWriter(){}


    public void addChange(String role){

        List items = null;

        if(change.getOldObject() instanceof Patient){
            items = readPatients();
        }else if(change.getOldObject() instanceof Doctor){
            items = readDoctors();
        }else if(change.getOldObject() instanceof DoctorRoom){
            items = readRooms();
        }else if(change.getOldObject() instanceof Procedure){
            items = readProcedures();
        }else if(change.getOldObject() instanceof User){
            items = readUsers();
        }else if(change.getOldObject() instanceof ActiveCheckup){
            items = readCheckups();
        }

        items.add(change.getOldObject());
        items.add(change.getNewObject());

        writeAll(items, role);
    }
    public void writeAll(List<T> itemsToWrite, String role) {
        try{
            if(itemsToWrite.get(0) instanceof Patient){
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(CHANGE_FILE_PATIENTS, false));
                for(T object : itemsToWrite){
                    out.writeObject(object);
                }
                out.close();
                out.flush();

                FileWriter timePatientsWriter = new FileWriter(CHANGE_FILE_TIME_PATIENTS, true);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                timePatientsWriter.write(dtf.format(now) + "\n");
                timePatientsWriter.close();


                FileWriter roleDoctorsWriter = new FileWriter(CHANGE_FILE_PATIENTS_ROLE, true);
                roleDoctorsWriter.write(role + "\n");
                roleDoctorsWriter.close();


            }
            else if( itemsToWrite.get(0) instanceof Doctor){
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(CHANGE_FILE_DOCTORS, false));
                for(T object : itemsToWrite){
                    out.writeObject(object);
                }
                out.close();
                out.flush();

                FileWriter timeDoctorsWriter = new FileWriter(CHANGE_FILE_TIME_DOCTORS, true);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                timeDoctorsWriter.write(dtf.format(now) + "\n");
                timeDoctorsWriter.close();

                FileWriter roleDoctorsWriter = new FileWriter(CHANGE_FILE_DOCTORS_ROLE, true);
                roleDoctorsWriter.write(role + "\n");
                roleDoctorsWriter.close();

            }else if(itemsToWrite.get(0) instanceof DoctorRoom){
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(CHANGE_FILE_ROOMS, false));
                for(T object : itemsToWrite){
                    out.writeObject(object);
                }
                out.close();

                FileWriter timeRoomsWriter = new FileWriter(CHANGE_FILE_TIME_ROOMS, true);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                timeRoomsWriter.write(dtf.format(now) + "\n");
                timeRoomsWriter.close();

                FileWriter roleDoctorsWriter = new FileWriter(CHANGE_FILE_ROOMS_ROLE, true);
                roleDoctorsWriter.write(role + "\n");
                roleDoctorsWriter.close();

            }else if(itemsToWrite.get(0) instanceof Procedure){
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(CHANGE_FILE_PROCEDURES, false));
                for(T object : itemsToWrite){
                    out.writeObject(object);
                }
                out.close();

                FileWriter timeRoomsWriter = new FileWriter(CHANGE_FILE_TIME_PROCEDURES, true);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                timeRoomsWriter.write(dtf.format(now) + "\n");
                timeRoomsWriter.close();

                FileWriter roleDoctorsWriter = new FileWriter(CHANGE_FILE_PROCEDURES_ROLE, true);
                roleDoctorsWriter.write(role + "\n");
                roleDoctorsWriter.close();

            }else if(itemsToWrite.get(0) instanceof User){
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(CHANGE_FILE_USERS, false));
                for(T object : itemsToWrite){
                    out.writeObject(object);
                }
                out.close();

                FileWriter timeRoomsWriter = new FileWriter(CHANGE_FILE_TIME_USERS, true);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                timeRoomsWriter.write(dtf.format(now) + "\n");
                timeRoomsWriter.close();

                FileWriter roleDoctorsWriter = new FileWriter(CHANGE_FILE_USERS_ROLE, true);
                roleDoctorsWriter.write(role + "\n");
                roleDoctorsWriter.close();
            }else if(itemsToWrite.get(0) instanceof ActiveCheckup){
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(CHANGE_FILE_CHECKUPS, false));
                for(T object : itemsToWrite){
                    out.writeObject(object);
                }
                out.close();

                FileWriter timeRoomsWriter = new FileWriter(CHANGE_FILE_TIME_CHECKUPS, true);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                timeRoomsWriter.write(dtf.format(now) + "\n");
                timeRoomsWriter.close();

                FileWriter roleDoctorsWriter = new FileWriter(CHANGE_FILE_CHECKUPS_ROLE, true);
                roleDoctorsWriter.write(role + "\n");
                roleDoctorsWriter.close();
            }

        } catch (IOException e) {
            Application.logger.info(e.getMessage(), e);
        }
    }

    public List<Patient> readPatients(){
        List<Patient> finalList = new ArrayList<>();
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(CHANGE_FILE_PATIENTS));
            while(true){
                finalList.add((Patient)input.readObject());
                finalList.add((Patient)input.readObject());
            }


        } catch (IOException e) {
            Application.logger.info(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            Application.logger.info(e.getMessage(), e);
        }
        return finalList;
    }
    public List<Doctor> readDoctors(){
        List<Doctor> finalList = new ArrayList<>();
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(CHANGE_FILE_DOCTORS));
            while(true){
                finalList.add((Doctor)input.readObject());
                finalList.add((Doctor)input.readObject());
            }

        } catch (IOException e) {
            Application.logger.info(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            Application.logger.info(e.getMessage(), e);
        }
        return finalList;
    }
    public List<DoctorRoom> readRooms(){
        List<DoctorRoom> finalList = new ArrayList<>();
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(CHANGE_FILE_ROOMS));
            while(true){
                finalList.add((DoctorRoom)input.readObject());
                finalList.add((DoctorRoom)input.readObject());
            }


        } catch (IOException e) {
            Application.logger.info(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            Application.logger.info(e.getMessage(), e);
        }
        return finalList;
    }
    public List<Procedure> readProcedures(){
        List<Procedure> finalList = new ArrayList<>();
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(CHANGE_FILE_PROCEDURES));
            while(true){
                finalList.add((Procedure)input.readObject());
                finalList.add((Procedure)input.readObject());
            }
        } catch (IOException e) {
            Application.logger.info(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            Application.logger.info(e.getMessage(), e);
        }
        return finalList;
    }
    public List<ActiveCheckup> readCheckups(){
        List<ActiveCheckup> finalList = new ArrayList<>();
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(CHANGE_FILE_CHECKUPS));
            while(true){
                finalList.add((ActiveCheckup)input.readObject());
                finalList.add((ActiveCheckup)input.readObject());
            }

        } catch (IOException e) {
            Application.logger.info(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            Application.logger.info(e.getMessage(), e);
        }

        return finalList;
    }
    public List<User> readUsers(){

        List<User> finalList = new ArrayList<>();
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(CHANGE_FILE_USERS));
            while(true){
                finalList.add((User)input.readObject());
                finalList.add((User)input.readObject());
            }

        } catch (IOException e) {
            Application.logger.info(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            Application.logger.info(e.getMessage(), e);
        }

        return finalList;
    }

    public List<String> readTimePatients(){
        List<String> changesTime = new ArrayList<>();

        try(Scanner scanner = new Scanner(new File(CHANGE_FILE_TIME_PATIENTS))){
            while(scanner.hasNextLine()){
                String time = scanner.nextLine();
                changesTime.add(time);
            }
        } catch (FileNotFoundException e) {
            Application.logger.info(e.getMessage(), e);
        }
        return changesTime;
    }
    public List<String> readTimeDoctors(){
        List<String> changesTime = new ArrayList<>();

        try(Scanner scanner = new Scanner(new File(CHANGE_FILE_TIME_DOCTORS))){
            while(scanner.hasNextLine()){
                String time = scanner.nextLine();
                changesTime.add(time);
            }
        } catch (FileNotFoundException e) {
            Application.logger.info(e.getMessage(), e);
        }
        return changesTime;
    }
    public List<String> readTimeRooms(){
        List<String> changesTime = new ArrayList<>();

        try(Scanner scanner = new Scanner(new File(CHANGE_FILE_TIME_ROOMS))){
            while(scanner.hasNextLine()){
                String time = scanner.nextLine();
                changesTime.add(time);
            }
        } catch (FileNotFoundException e) {
            Application.logger.info(e.getMessage(), e);
        }
        return changesTime;
    }
    public List<String> readTimeProcedures() {
        List<String> changesTime = new ArrayList<>();

        try(Scanner scanner = new Scanner(new File(CHANGE_FILE_TIME_PROCEDURES))){
            while(scanner.hasNextLine()){
                String time = scanner.nextLine();
                changesTime.add(time);
            }
        } catch (FileNotFoundException e) {
            Application.logger.info(e.getMessage(), e);
        }
        return changesTime;
    }
    public List<String> readTimeCheckups(){
        List<String> changesTime = new ArrayList<>();

        try(Scanner scanner = new Scanner(new File(CHANGE_FILE_TIME_CHECKUPS))){
            while(scanner.hasNextLine()){
                String time = scanner.nextLine();
                changesTime.add(time);
            }
        } catch (FileNotFoundException e) {
            Application.logger.error(e.getMessage(), e);
        }
        return changesTime;
    }
    public List<String> readTimeUsers(){
        List<String> changesTime = new ArrayList<>();

        try(Scanner scanner = new Scanner(new File(CHANGE_FILE_TIME_USERS))){
            while(scanner.hasNextLine()){
                String time = scanner.nextLine();
                changesTime.add(time);
            }
        } catch (FileNotFoundException e) {
            Application.logger.error(e.getMessage(), e);
        }
        return changesTime;
    }

    public List<String> readRoleChangeDoctors(){
        List<String> changesRole = new ArrayList<>();

        try(Scanner scanner = new Scanner(new File(CHANGE_FILE_DOCTORS_ROLE))){
            while(scanner.hasNextLine()){
                String role = scanner.nextLine();
                changesRole.add(role);
            }
        } catch (FileNotFoundException e) {
            Application.logger.info(e.getMessage(), e);
        }
        return changesRole;
    }
    public List<String> readRoleChangePatients(){
        List<String> changesRole = new ArrayList<>();

        try(Scanner scanner = new Scanner(new File(CHANGE_FILE_PATIENTS_ROLE))){
            while(scanner.hasNextLine()){
                String role = scanner.nextLine();
                changesRole.add(role);
            }
        } catch (FileNotFoundException e) {
            Application.logger.info(e.getMessage(), e);
        }
        return changesRole;
    }
    public List<String> readRoleChangeRooms(){
        List<String> changesRole = new ArrayList<>();

        try(Scanner scanner = new Scanner(new File(CHANGE_FILE_ROOMS_ROLE))){
            while(scanner.hasNextLine()){
                String role = scanner.nextLine();
                changesRole.add(role);
            }
        } catch (FileNotFoundException e) {
            Application.logger.info(e.getMessage(), e);
        }
        return changesRole;
    }
    public List<String> readRoleChangeProcedures(){
        List<String> changesRole = new ArrayList<>();

        try(Scanner scanner = new Scanner(new File(CHANGE_FILE_PROCEDURES_ROLE))){
            while(scanner.hasNextLine()){
                String role = scanner.nextLine();
                changesRole.add(role);
            }
        } catch (FileNotFoundException e) {
            Application.logger.info(e.getMessage(), e);
        }
        return changesRole;
    }
    public List<String> readRoleChangeCheckups(){
        List<String> changesRole = new ArrayList<>();

        try(Scanner scanner = new Scanner(new File(CHANGE_FILE_CHECKUPS_ROLE))){
            while(scanner.hasNextLine()){
                String scan = scanner.nextLine();
                changesRole.add(scan);
            }
        } catch (FileNotFoundException e) {
            Application.logger.error(e.getMessage(), e);
        }
        return changesRole;
    }
    public List<String> readRoleChangeUsers(){
        List<String> changesRole = new ArrayList<>();

        try(Scanner scanner = new Scanner(new File(CHANGE_FILE_USERS_ROLE))){
            while(scanner.hasNextLine()){
                String scan = scanner.nextLine();
                changesRole.add(scan);
            }
        } catch (FileNotFoundException e) {
            Application.logger.error(e.getMessage(), e);
        }
        return changesRole;
    }


    public Change getChange() {
        return change;
    }

    public void setChange(Change change) {
        this.change = change;
    }
}
