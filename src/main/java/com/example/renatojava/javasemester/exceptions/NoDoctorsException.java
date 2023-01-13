package com.example.renatojava.javasemester.exceptions;

public class NoDoctorsException extends RuntimeException{

    public NoDoctorsException() {
    }

    public NoDoctorsException(String message) {
        super(message);
    }

    public NoDoctorsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoDoctorsException(Throwable cause) {
        super(cause);
    }
}
