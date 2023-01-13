package com.example.renatojava.javasemester.exceptions;

public class NoProceduresException extends RuntimeException{

    public NoProceduresException() {
    }

    public NoProceduresException(String message) {
        super(message);
    }

    public NoProceduresException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoProceduresException(Throwable cause) {
        super(cause);
    }

}
