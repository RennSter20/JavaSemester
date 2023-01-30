package com.example.renatojava.javasemester.entity;

import com.example.renatojava.javasemester.Application;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
    private T oldObject, newObject;


    public ChangeWriter(T oldObject, T newObject) {
        this.oldObject = oldObject;
        this.newObject = newObject;
    }

    public ChangeWriter() {
    }

    public synchronized void addChange(String role){

        List<T> items = null;

        if(oldObject instanceof Patient){
            items = new ArrayList<>(readPatients());
        }else if(oldObject instanceof Doctor){
            items = new ArrayList<>(readDoctors());
        }else if(oldObject instanceof DoctorRoom){
            items = new ArrayList<>(readRooms());
        }else if(oldObject instanceof Procedure){
            items = new ArrayList<>(readProcedures());
        }

        items.add(oldObject);
        items.add(newObject);

        writeAll(items, role);
    }
    public synchronized void writeAll(List<T> itemsToWrite, String role) {
        try{
            if(itemsToWrite.get(0) instanceof Patient){
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(CHANGE_FILE_PATIENTS, false));
                for(T object : itemsToWrite){
                    out.writeObject(object);
                }

                out.close();
                out.flush();


                FileWriter timePatientsWriter = new FileWriter(CHANGE_FILE_TIME_PATIENTS, true);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
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

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
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

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
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

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();


                timeRoomsWriter.write(dtf.format(now) + "\n");
                timeRoomsWriter.close();

                FileWriter roleDoctorsWriter = new FileWriter(CHANGE_FILE_PROCEDURES_ROLE, true);
                roleDoctorsWriter.write(role + "\n");
                roleDoctorsWriter.close();
            }

        } catch (FileNotFoundException e) {
            Application.logger.info(e.getMessage(), e.getStackTrace());
        } catch (IOException e) {
            Application.logger.info(e.getMessage(), e.getStackTrace());
        }


    }

    public synchronized List<T> readPatients(){
        List<T> first = new ArrayList<>();
        List<T> second = new ArrayList<>();
        List<T> finalList = new ArrayList<>();
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(CHANGE_FILE_PATIENTS));
            while(true){
                first.add((T)input.readObject());
                second.add((T)input.readObject());
            }


        } catch (IOException e) {
            Application.logger.info(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            Application.logger.info(e.getMessage(), e);
        }
        for(int i = 0;i< first.size();i++){
            finalList.add(first.get(i));
            finalList.add(second.get(i));
        }
        return finalList;
    }
    public synchronized List<T> readDoctors(){
        List<T> first = new ArrayList<>();
        List<T> second = new ArrayList<>();
        List<T> finalList = new ArrayList<>();
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(CHANGE_FILE_DOCTORS));
            while(true){
                first.add((T)input.readObject());
                second.add((T)input.readObject());
            }


        } catch (IOException e) {
            Application.logger.info(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            Application.logger.info(e.getMessage(), e);
        }
        for(int i = 0;i< first.size();i++){
            finalList.add(first.get(i));
            finalList.add(second.get(i));
        }
        return finalList;
    }
    public synchronized List<T> readRooms(){
        List<T> first = new ArrayList<>();
        List<T> second = new ArrayList<>();
        List<T> finalList = new ArrayList<>();
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(CHANGE_FILE_ROOMS));
            while(true){
                first.add((T)input.readObject());
                second.add((T)input.readObject());
            }


        } catch (IOException e) {
            Application.logger.info(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            Application.logger.info(e.getMessage(), e);
        }
        for(int i = 0;i< first.size();i++){
            finalList.add(first.get(i));
            finalList.add(second.get(i));
        }
        return finalList;
    }
    public synchronized List<T> readProcedures(){
        List<T> first = new ArrayList<>();
        List<T> second = new ArrayList<>();
        List<T> finalList = new ArrayList<>();
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(CHANGE_FILE_PROCEDURES));
            while(true){
                first.add((T)input.readObject());
                second.add((T)input.readObject());
            }


        } catch (IOException e) {
            Application.logger.info(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            Application.logger.info(e.getMessage(), e);
        }
        for(int i = 0;i< first.size();i++){
            finalList.add(first.get(i));
            finalList.add(second.get(i));
        }
        return finalList;
    }

    public synchronized List<String> readTimePatients(){
        List<String> changesTime = new ArrayList<>();

        try(Scanner scanner = new Scanner(new File(CHANGE_FILE_TIME_PATIENTS))){
            while(scanner.hasNextLine()){
                String time = scanner.nextLine();
                changesTime.add(time);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return changesTime;
    }
    public synchronized List<String> readTimeDoctors(){
        List<String> changesTime = new ArrayList<>();

        try(Scanner scanner = new Scanner(new File(CHANGE_FILE_TIME_DOCTORS))){
            while(scanner.hasNextLine()){
                String time = scanner.nextLine();
                changesTime.add(time);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return changesTime;
    }
    public synchronized List<String> readTimeRooms(){
        List<String> changesTime = new ArrayList<>();

        try(Scanner scanner = new Scanner(new File(CHANGE_FILE_TIME_ROOMS))){
            while(scanner.hasNextLine()){
                String time = scanner.nextLine();
                changesTime.add(time);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return changesTime;
    }
    public synchronized List<String> readTimeProcedures() {
        List<String> changesTime = new ArrayList<>();

        try(Scanner scanner = new Scanner(new File(CHANGE_FILE_TIME_PROCEDURES))){
            while(scanner.hasNextLine()){
                String time = scanner.nextLine();
                changesTime.add(time);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return changesTime;
    }


    public synchronized List<String> readRoleChangeDoctors(){
        List<String> changesRole = new ArrayList<>();

        try(Scanner scanner = new Scanner(new File(CHANGE_FILE_DOCTORS_ROLE))){
            while(scanner.hasNextLine()){
                String role = scanner.nextLine();
                changesRole.add(role);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return changesRole;
    }
    public synchronized List<String> readRoleChangePatients(){
        List<String> changesRole = new ArrayList<>();

        try(Scanner scanner = new Scanner(new File(CHANGE_FILE_PATIENTS_ROLE))){
            while(scanner.hasNextLine()){
                String role = scanner.nextLine();
                changesRole.add(role);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return changesRole;
    }
    public synchronized List<String> readRoleChangeRooms(){
        List<String> changesRole = new ArrayList<>();

        try(Scanner scanner = new Scanner(new File(CHANGE_FILE_ROOMS_ROLE))){
            while(scanner.hasNextLine()){
                String role = scanner.nextLine();
                changesRole.add(role);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return changesRole;
    }
    public synchronized List<String> readRoleChangeProcedures(){
        List<String> changesRole = new ArrayList<>();

        try(Scanner scanner = new Scanner(new File(CHANGE_FILE_PROCEDURES_ROLE))){
            while(scanner.hasNextLine()){
                String role = scanner.nextLine();
                changesRole.add(role);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return changesRole;
    }


}
