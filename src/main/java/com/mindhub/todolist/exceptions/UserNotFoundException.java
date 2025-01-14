package com.mindhub.todolist.exceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(Long id) {
        super("STEVE REPORT: User with Id: " + id + " was not found.");
    }
    public UserNotFoundException(String email) {super("STEVE REPORT: User with email: " + email + " was not found.");}
}
