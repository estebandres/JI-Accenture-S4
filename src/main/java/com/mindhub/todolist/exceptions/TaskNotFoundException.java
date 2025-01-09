package com.mindhub.todolist.exceptions;

public class TaskNotFoundException extends Exception {
    public TaskNotFoundException(Long id) {
        super("STEVE REPORT: Task with Id: " + id + " was not found.");
    }
}
