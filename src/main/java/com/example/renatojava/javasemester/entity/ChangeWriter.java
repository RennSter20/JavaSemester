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

    public void writeChange() {

        try{
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(CHANGE_FILE_PATIENTS, true));
            out.writeObject(oldObject);
            out.writeObject(newObject);
            out.close();
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

    public Map<Patient, Patient> readChanges(){
        Map<Patient, Patient> patientsChanged = new HashMap<>();
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(CHANGE_FILE_PATIENTS));

            Patient oldPatient;
            Patient newPatient;

            while(true){
                oldPatient = (Patient) in.readObject();
                newPatient = (Patient) in.readObject();
                patientsChanged.put(oldPatient, newPatient);
            }

        } catch (IOException e) {
            Application.logger.info(e.getMessage(), e.getStackTrace());
        } catch (ClassNotFoundException e) {
            Application.logger.info(e.getMessage(), e.getStackTrace());
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
