package com.example.renatojava.javasemester.api;

import com.example.renatojava.javasemester.database.Data;
import com.example.renatojava.javasemester.menus.MenuScreenController;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public sealed interface APIManager permits MenuScreenController {

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




}
