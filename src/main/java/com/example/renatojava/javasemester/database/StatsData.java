package com.example.renatojava.javasemester.database;

import com.example.renatojava.javasemester.entity.Stats;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public interface StatsData {

    static Stats getCurrentStats(){
        try(Connection conn = Data.connectingToDatabase()) {

            Statement sqlStatement = conn.createStatement();
            ResultSet proceduresResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM STATS WHERE ID=0"
            );

            Stats statsToReturn = null;
            while(proceduresResultSet.next()){
                statsToReturn = new Stats(proceduresResultSet.getInt("id"), proceduresResultSet.getInt("patients"), proceduresResultSet.getDouble("debt"), proceduresResultSet.getInt("doctors"), proceduresResultSet.getInt("bills"));
            }

            return statsToReturn;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
