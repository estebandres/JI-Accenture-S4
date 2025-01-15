package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.CreateTaskDTO;
import com.mindhub.todolist.dtos.GetTaskDTO;
import com.mindhub.todolist.models.TaskStatus;
import com.mindhub.todolist.exceptions.TaskNotFoundException;
import com.mindhub.todolist.exceptions.UserNotFoundException;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface TaskService {
    List<GetTaskDTO> getAllTasks();
    List<GetTaskDTO> getAllTasksByStatus(TaskStatus status);
    GetTaskDTO getTaskById(Long id) throws TaskNotFoundException;
    GetTaskDTO createTask(@Valid CreateTaskDTO newTaskDTO) throws UserNotFoundException;
    GetTaskDTO updateTask(Long id, @Valid CreateTaskDTO updateTaskDTO) throws TaskNotFoundException;
    void deleteTask(Long id) throws TaskNotFoundException;
    List<GetTaskDTO> getLoggedInUserTasks(String email) throws UserNotFoundException;
    GetTaskDTO createTaskForLoggedInUser(@Valid CreateTaskDTO createTaskDTO, String email) throws UserNotFoundException;
    GetTaskDTO updateTaskForLoggedInUser(String email, Long id, @Valid CreateTaskDTO createTaskDTO) throws UserNotFoundException, TaskNotFoundException, BadRequestException;
    GetTaskDTO getLoggedInUserTaskById(String email, Long id) throws UserNotFoundException, TaskNotFoundException;
}
