package com.example.renatojava.javasemester.entity;

import com.example.renatojava.javasemester.Application;

import java.io.*;
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
        Map<T, T> items = new HashMap<>(read());
        items.put(oldObject, newObject);
        writeAll(items);
    }
    public void writeAll(Map<T,T> itemsToWrite) {
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(CHANGE_FILE_PATIENTS, false))){

            for(T object : itemsToWrite.keySet()){
                out.writeObject(object);
                out.writeObject(itemsToWrite.get(object));
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

    public Map<T, T> read(){
        Map<T, T> patientsChanged = new HashMap<>();
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(CHANGE_FILE_PATIENTS));
            while(true){
                patientsChanged.put((T)input.readObject(), (T) input.readObject());
            }


        } catch (IOException e) {
            Application.logger.info(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            Application.logger.info(e.getMessage(), e);
        }

        return patientsChanged;
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
