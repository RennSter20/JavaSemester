package com.example.renatojava.javasemester.entity;

import com.example.renatojava.javasemester.Application;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ChangeWriter<T> {

    private static final String CHANGE_FILE_PATIENTS = "dat\\changesPatients.dat";
    private static final String CHANGE_FILE_TIME_PATIENTS = "dat\\changesTimePatients.txt";


    private static final String CHANGE_FILE_DOCTORS = "dat\\changesDoctors.dat";
    private static final String CHANGE_FILE_TIME_DOCTORS = "dat\\changesTimeDoctors.txt";
    private T oldObject, newObject;

    public ChangeWriter(T oldObject, T newObject) {
        this.oldObject = oldObject;
        this.newObject = newObject;
    }

    public ChangeWriter() {
    }

    public void addChange(){
        List<T> items = null;
        if(oldObject instanceof Patient){
            items = new ArrayList<>(readPatients());
        }else if(oldObject instanceof Doctor){
            items = new ArrayList<>(readDoctors());
        }


        items.add(oldObject);
        items.add(newObject);
        writeAll(items);
    }
    public void writeAll(List<T> itemsToWrite) {
        try{
            if(itemsToWrite.get(0) instanceof Patient){
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(CHANGE_FILE_PATIENTS, false));
                for(T object : itemsToWrite){
                    out.writeObject(object);
                }

                out.close();
                out.flush();

                FileWriter myWriter = new FileWriter(CHANGE_FILE_TIME_PATIENTS, true);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();


                myWriter.write(dtf.format(now) + "\n");
                myWriter.close();
                myWriter.flush();

            }else if( itemsToWrite.get(0) instanceof Doctor){
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(CHANGE_FILE_DOCTORS, false));
                for(T object : itemsToWrite){
                    out.writeObject(object);
                }

                out.close();
                out.flush();

                FileWriter myWriter = new FileWriter(CHANGE_FILE_TIME_DOCTORS, true);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();


                myWriter.write(dtf.format(now) + "\n");
                myWriter.close();
                myWriter.flush();

            }

        } catch (FileNotFoundException e) {
            Application.logger.info(e.getMessage(), e.getStackTrace());
        } catch (IOException e) {
            Application.logger.info(e.getMessage(), e.getStackTrace());
        }





    }

    public List<T> readPatients(){
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

    public List<T> readDoctors(){
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

    public List<String> readTimePatients(){
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

    public List<String> readTimeDoctors(){
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
}
