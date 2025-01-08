package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.CreateTaskDTO;
import com.mindhub.todolist.dtos.GetTaskDTO;

import java.util.List;

public interface TaskService {
    List<GetTaskDTO> getAllTasks();
    GetTaskDTO getTaskById(Long id);
    GetTaskDTO createTask(CreateTaskDTO newTaskDTO);
    GetTaskDTO updateTask(Long id, GetTaskDTO getTaskDTO);
    void deleteTask(Long id);
}
