package com.example.renatojava.javasemester.database;

import okhttp3.Request;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public interface Data {

   String DATABASE_FILE = "database.properties";
   String API_FILE = "api.properties";

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

    static Request connectingToApi(String url) throws IOException {
            try{
                Properties properties = new Properties();
                properties.load(new FileReader(API_FILE));
                String key = properties.getProperty("X-RapidAPI-Key");
                String host = properties.getProperty("X-RapidAPI-Host");

                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .addHeader("X-RapidAPI-Key", key)
                        .addHeader("X-RapidAPI-Host", host)
                        .build();

                return request;

            }catch (IOException e){
                throw new IOException("Error while reading properties file for API call.", e);
            }
    }

}
