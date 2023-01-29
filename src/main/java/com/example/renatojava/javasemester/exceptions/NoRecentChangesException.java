package com.example.renatojava.javasemester.exceptions;

public class NoRecentChangesException extends RuntimeException{

    public NoRecentChangesException() {
    }

    public NoRecentChangesException(String message) {
        super(message);
    }

    public NoRecentChangesException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoRecentChangesException(Throwable cause) {
        super(cause);
    }
}
