package com.mindhub.todolist.exceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(Long id) {
        super("STEVE REPORT: User with Id: " + id + " was not found.");
    }
}
