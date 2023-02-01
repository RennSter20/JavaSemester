package com.example.renatojava.javasemester.threads;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.api.APIManager;

import java.io.IOException;

public class APICountries implements Runnable{

    @Override
    public void run() {
        try {
            Application.countries = APIManager.avaibleCountries().keySet().stream().toList();
        } catch (IOException e) {
            Application.logger.error(e.getMessage(), e);
        }
    }
}
