package com.example.renatojava.javasemester.entity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public interface CheckObjects {

    static Boolean checkIfPatientExists(String oib){
        List<Patient> patientsList = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/production", "student", "student");

            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM PATIENTS WHERE OIB=" + oib
            );

            while(proceduresResultSet.next()){
                Patient newPatient = getPatient(proceduresResultSet);
                patientsList.add(newPatient);
            }

            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(patientsList.size() != 0){
            return true;
        }
        return false;
    }
    static Patient getPatient(ResultSet procedureSet) throws SQLException{

        String name = procedureSet.getString("name");
        String surname = procedureSet.getString("surname");
        String gender = procedureSet.getString("gender");
        String debt = procedureSet.getString("debt");
        String procedures = procedureSet.getString("procedures");
        String oib = procedureSet.getString("oib");


        return new Patient.Builder().withName(name).withSurname(surname).withGender(gender).withOIB(oib).withDebt(Double.valueOf(debt)).withProcedures(procedures).build();

    }

}
