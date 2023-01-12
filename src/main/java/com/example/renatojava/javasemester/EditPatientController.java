package com.example.renatojava.javasemester;

import com.example.renatojava.javasemester.entity.Patient;

public class EditPatientController {

    private Patient patientToEdit;

    public void initializePatient(Patient p){
        patientToEdit = p;
    }
    public void initialize(){
        System.out.println(patientToEdit.getName());
    }
}
