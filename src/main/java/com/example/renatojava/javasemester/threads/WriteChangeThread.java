package com.example.renatojava.javasemester.threads;

import com.example.renatojava.javasemester.entity.ChangeWriter;

import java.util.Random;

public class WriteChangeThread implements Runnable{

    private ChangeWriter changeWriter;
    private String role;

    public WriteChangeThread(ChangeWriter changeWriter, String role) {
        this.changeWriter = changeWriter;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public ChangeWriter getChangeWriter() {
        return changeWriter;
    }

    public void setChangeWriter(ChangeWriter changeWriter) {
        this.changeWriter = changeWriter;
    }

    @Override
    public void run() {
        //TEST THREAD SLEEP
        Random generator = new Random();
        try{
            Thread.sleep(generator.nextInt(50000));
        }catch (InterruptedException e){

        }
        writeAChange();
    }

    public synchronized void writeAChange(){
        changeWriter.addChange(role);
    }
}
