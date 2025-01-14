package com.mindhub.todolist.exceptions;

public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String email) {
        super("STEVE REPORT: User with email: " + email + " already exists.");
    }
}
