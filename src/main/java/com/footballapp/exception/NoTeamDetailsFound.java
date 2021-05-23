package com.footballapp.exception;

public class NoTeamDetailsFound extends RuntimeException {
    private String message;

    public NoTeamDetailsFound(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}