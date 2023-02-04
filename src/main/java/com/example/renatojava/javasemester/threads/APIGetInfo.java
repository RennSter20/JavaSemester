package com.example.renatojava.javasemester.threads;

import com.example.renatojava.javasemester.api.APIManager;

public final class APIGetInfo implements Runnable, APIManager {

    private String country;

    public APIGetInfo(String country) {
        this.country = country;
    }

    @Override
    public void run() {
    }
}
