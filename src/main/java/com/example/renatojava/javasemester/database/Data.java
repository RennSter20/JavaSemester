package com.example.renatojava.javasemester.database;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public interface Data {

    String DATABASE_FILE = "database.properties";

    static Connection connectingToDatabase() throws IOException, SQLException {
        Connection conn;
        try{
            Properties properties = new Properties();
            properties.load(new FileReader(DATABASE_FILE));
            String url = properties.getProperty("url");
            String user = properties.getProperty("user");
            String pass = properties.getProperty("pass");
            conn = DriverManager.getConnection(url, user,pass);
        }catch (IOException e){
            throw new IOException("Error while reading properties file for DB.", e);
        }catch (SQLException e){
            throw new SQLException("Error while connecting to database.", e);
        }
        return conn;
    }

}
