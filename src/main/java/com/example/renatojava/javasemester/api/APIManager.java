package com.example.renatojava.javasemester.api;

import com.example.renatojava.javasemester.Application;
import com.example.renatojava.javasemester.database.Data;
import com.example.renatojava.javasemester.threads.APIGetInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeMap;

public sealed interface APIManager permits APIGetInfo {

    String COUNTRIES = "dat\\countries.txt";

    static APIResponse getCountryInfo(String country) throws IOException {

        String url = "https://covid-19-statistics.p.rapidapi.com/reports?region_name=" + country;

        OkHttpClient client = new OkHttpClient();

        Request request = Data.connectingToApi(url);

        Response response = client.newCall(request).execute();
        String jsonString = response.body().string();
        JSONObject jsonObject = new JSONObject(jsonString);

        JSONArray jsonArray = jsonObject.getJSONArray("data");

        APIResponse apiResponse = new APIResponse.Builder()
                .withDate(jsonArray.getJSONObject(0).getString("date"))
                .withTotalCases(jsonArray.getJSONObject(0).getInt("confirmed"))
                .withTotalDeaths(jsonArray.getJSONObject(0).getInt("deaths"))
                .withNewCasesDay(jsonArray.getJSONObject(0).getInt("confirmed_diff"))
                .lastUpdatedFullTime(jsonArray.getJSONObject(0).getString("last_update"))
                .build();

        return apiResponse;
    }

    static APIResponse getWorldInfo() throws IOException{
        String url = "https://covid-19-statistics.p.rapidapi.com/reports/total";

        OkHttpClient client = new OkHttpClient();

        Request request = Data.connectingToApi(url);

        Response response = client.newCall(request).execute();
        String jsonString = response.body().string();
        JSONObject jsonObject = new JSONObject(jsonString);

        JSONObject mainJSON = jsonObject.getJSONObject("data");

        APIResponse apiResponse = new APIResponse.Builder()
                .withDate(mainJSON.getString("date"))
                .withTotalCases(mainJSON.getInt("confirmed"))
                .withTotalDeaths(mainJSON.getInt("deaths"))
                .withNewCasesDay(mainJSON.getInt("confirmed_diff"))
                .lastUpdatedFullTime(mainJSON.getString("last_update"))
                .build();

        return apiResponse;
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
