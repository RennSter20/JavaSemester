package com.example.renatojava.javasemester.entity;

import com.example.renatojava.javasemester.Application;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ChangeWriter<T> {

    private static final String CHANGE_FILE_PATIENTS = "dat\\changesPatients.dat";
    private static final String CHANGE_FILE_TIME = "dat\\changesTime.txt";
    private T oldObject, newObject;

    public ChangeWriter(T oldObject, T newObject) {
        this.oldObject = oldObject;
        this.newObject = newObject;
    }

    public ChangeWriter() {
    }

    public void addChange(){
        List<T> items = new ArrayList<>(read());

        items.add(oldObject);
        items.add(newObject);
        writeAll(items);
    }
    public void writeAll(List<T> itemsToWrite) {
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(CHANGE_FILE_PATIENTS, false))){

            for(T object : itemsToWrite){
                out.writeObject(object);
            }

            out.close();
            out.flush();
        } catch (FileNotFoundException e) {
            Application.logger.info(e.getMessage(), e.getStackTrace());
        } catch (IOException e) {
            Application.logger.info(e.getMessage(), e.getStackTrace());
        }

        try{
            FileWriter myWriter = new FileWriter(CHANGE_FILE_TIME, true);

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();


            myWriter.write(dtf.format(now) + "\n");
            myWriter.close();
            myWriter.flush();

        } catch (IOException e) {
            Application.logger.info(e.getMessage(), e.getStackTrace());
        }

    }

    public List<T> read(){
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

    public List<String> readTime(){
        List<String> changesTime = new ArrayList<>();

        try(Scanner scanner = new Scanner(new File(CHANGE_FILE_TIME))){
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
