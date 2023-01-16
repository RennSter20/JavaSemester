package com.example.renatojava.javasemester.entity;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public interface StatsChanger {

    static void changeStats(List<String> sql){
        try{
            Connection veza = Data.connectingToDatabase();

            String addOn = sql.stream().collect(Collectors.joining(" , "));

            PreparedStatement stmnt = veza.prepareStatement("UPDATE STATS SET " + addOn + " WHERE ID=0");
            stmnt.executeUpdate();

            veza.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
