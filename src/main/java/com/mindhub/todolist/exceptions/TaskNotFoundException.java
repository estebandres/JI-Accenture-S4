package com.mindhub.todolist.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class TaskNotFoundException extends Exception {
    public static final String MESSAGE = "STEVE REPORT: Task with Id: %d was not found.";
    public static final HttpStatus ERROR_CODE = HttpStatus.NOT_FOUND;

    public TaskNotFoundException(Long id) {
        super(String.format(MESSAGE, id));
    }

    public static String getErrorCode(){
        return Integer.toString(ERROR_CODE.value());
    }
}
