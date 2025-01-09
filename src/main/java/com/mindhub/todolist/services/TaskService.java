package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.CreateTaskDTO;
import com.mindhub.todolist.dtos.GetTaskDTO;
import com.mindhub.todolist.entities.TaskStatus;
import com.mindhub.todolist.exceptions.TaskNotFoundException;
import com.mindhub.todolist.exceptions.UserNotFoundException;

import java.util.List;

public interface TaskService {
    List<GetTaskDTO> getAllTasks();
    List<GetTaskDTO> getAllTasksByStatus(TaskStatus status);
    GetTaskDTO getTaskById(Long id) throws TaskNotFoundException;
    GetTaskDTO createTask(CreateTaskDTO newTaskDTO) throws UserNotFoundException;
    GetTaskDTO updateTask(Long id, CreateTaskDTO updateTaskDTO) throws TaskNotFoundException;
    void deleteTask(Long id) throws TaskNotFoundException;
}
