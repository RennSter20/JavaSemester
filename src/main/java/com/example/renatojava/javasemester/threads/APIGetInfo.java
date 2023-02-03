package com.example.renatojava.javasemester.threads;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.api.APIManager;
import com.example.renatojava.javasemester.api.APIResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class APIGetInfo implements Runnable{
    @Override
    public void run() {
        try {
            List<String> countries = APIManager.avaibleCountries();
            Map<String, APIResponse> results = new TreeMap();

            for(String country : countries){
                if(!country.equals("World")){
                    results.put(country, APIManager.getCountryInfo(country));
                }else{
                    results.put(country, APIManager.getWorldInfo());
                }
            }
            Application.responseMap = results;

        } catch (IOException e) {
            Application.logger.error(e.getMessage(), e);
        }
    }
}
