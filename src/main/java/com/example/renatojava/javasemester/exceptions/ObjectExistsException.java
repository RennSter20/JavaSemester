package com.example.renatojava.javasemester.exceptions;

public class ObjectExistsException extends Exception{

    public ObjectExistsException() {
    }

    public ObjectExistsException(String message) {
        super(message);
    }

    public ObjectExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectExistsException(Throwable cause) {
        super(cause);
    }
}
