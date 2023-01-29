package com.example.renatojava.javasemester.database;

import com.example.renatojava.javasemester.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface UserData {

    static User getUserFromResult(ResultSet procedureSet) throws SQLException {

        String id = procedureSet.getString("ID");
        String password = procedureSet.getString("PASSWORD");
        String name = procedureSet.getString("NAME");
        String surname = procedureSet.getString("SURNAME");
        String role = procedureSet.getString("ROLE");
        String oib = procedureSet.getString("OIB");


        return new User(id, password, name, surname, role, oib);

    }

}
