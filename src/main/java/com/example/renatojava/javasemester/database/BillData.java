package com.example.renatojava.javasemester.database;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.entity.Bill;
import com.example.renatojava.javasemester.entity.Patient;
import com.example.renatojava.javasemester.util.DateFormatter;
import com.example.renatojava.javasemester.util.Notification;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface BillData {

    static void createBill(Patient patient, LocalDateTime time){
        try{
            Connection conn = Data.connectingToDatabase();

            String billCreated = DateFormatter.getDateTimeFormatted(time.toString());
            String birthDay = DateFormatter.getDateFormatted(patient.getDate().toString());

            PreparedStatement stmnt = conn.prepareStatement("INSERT INTO BILLS(NAME, SURNAME, OIB, GENDER, DEBT, PROCEDURES, DATE, BIRTH_DATE) VALUES ('" + patient.getName() + "', '" + patient.getSurname() + "', '" + patient.getOib() + "', '" + patient.getGender() +"', " + patient.getDebt() + ", '" + patient.getProcedures() + "', parsedatetime('" + billCreated + "', 'dd-MM-yyyy HH:mm'), parsedatetime('" + birthDay + "', 'dd-MM-yyyy'))");
            stmnt.executeUpdate();

            PatientData.removePatient(patient.getId());

            Notification.addedSuccessfully("Bill");

            conn.close();

        } catch (IOException | SQLException e) {
            Application.logger.info(e.getMessage(), e);
        }
    }
    static List<Bill> getAllBills(){
        List<Bill> billList = new ArrayList<>();

        try(Connection conn = Data.connectingToDatabase()) {
            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM BILLS"
            );

            while(proceduresResultSet.next()){
                Bill newBill = getBill(proceduresResultSet);
                billList.add(newBill);
            }
        } catch (SQLException e) {
            Application.logger.info(e.getMessage(), e);
        } catch (IOException e) {
            Application.logger.info(e.getMessage(), e);
        }
        return billList;
    }
    static Bill getBill(ResultSet set) throws SQLException {
        String name = set.getString("NAME");
        String surname = set.getString("SURNAME");
        String oib = set.getString("OIB");
        String gender = set.getString("GENDER");
        Double debt = set.getDouble("DEBT");
        String procedures = set.getString("PROCEDURES");
        LocalDateTime date = set.getTimestamp("DATE").toLocalDateTime();
        LocalDate birth_date = set.getTimestamp("BIRTH_DATE").toLocalDateTime().toLocalDate();

        return new Bill(new Patient(null, name, surname, gender, debt, procedures, oib, birth_date), date);
    }

}
