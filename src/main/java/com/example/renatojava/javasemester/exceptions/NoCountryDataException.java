package com.example.renatojava.javasemester.exceptions;

public class NoCountryDataException extends RuntimeException{

    public NoCountryDataException() {
    }

    public NoCountryDataException(String message) {
        super(message);
    }

    public NoCountryDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoCountryDataException(Throwable cause) {
        super(cause);
    }
}
