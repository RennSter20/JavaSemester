package com.example.renatojava.javasemester.exceptions;

public class NoPricesException extends RuntimeException{

    public NoPricesException() {
    }

    public NoPricesException(String message) {
        super(message);
    }

    public NoPricesException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoPricesException(Throwable cause) {
        super(cause);
    }

    public NoPricesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
