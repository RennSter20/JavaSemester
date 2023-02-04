package com.example.renatojava.javasemester.database;

import com.example.renatojava.javasemester.Application;
import okhttp3.Request;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;
import java.util.TreeMap;

public interface Data {

   String DATABASE_FILE = "database.properties";
   String API_FILE = "api.properties";
    String COUNTRIES = "dat\\countries.txt";

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

    static void putCountries(){
        Application.responseMap = new TreeMap<>();
        try(Scanner scanner = new Scanner(new FileReader(COUNTRIES))){
            while(scanner.hasNextLine()){
                Application.responseMap.put(scanner.nextLine(), null);
            }
        } catch (FileNotFoundException e) {
            Application.logger.error(e.getMessage(), e);
        }
    }

}
