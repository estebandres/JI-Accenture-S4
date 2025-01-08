package com.mindhub.todolist.services.impl;

import com.mindhub.todolist.dtos.CreateTaskDTO;
import com.mindhub.todolist.dtos.GetTaskDTO;
import com.mindhub.todolist.entities.AppUser;
import com.mindhub.todolist.entities.Task;
import com.mindhub.todolist.repositories.AppUserRepository;
import com.mindhub.todolist.repositories.TaskRepository;
import com.mindhub.todolist.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final AppUserRepository appUserRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, AppUserRepository appUserRepository) {
        this.taskRepository = taskRepository;
        this.appUserRepository = appUserRepository;
    }

    @Override
    public List<GetTaskDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(GetTaskDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public GetTaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        AppUser user = task.getUser();
        return new GetTaskDTO(task);
    }

    @Override
    public GetTaskDTO createTask(CreateTaskDTO newTaskDTO) {
        Task task = new Task();
        task.setTitle(newTaskDTO.title());
        task.setDescription(newTaskDTO.description());
        task.setStatus(newTaskDTO.status());

        // Assigning a default user (e.g., first user for simplicity)
        AppUser user = appUserRepository.findById(newTaskDTO.user().id()).orElseThrow(() -> new RuntimeException("User not found"));
        task.setUser(user);

        Task savedTask = taskRepository.save(task);
        return new GetTaskDTO(savedTask);
    }

    @Override
    public GetTaskDTO updateTask(Long id, GetTaskDTO getTaskDTO) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setTitle(getTaskDTO.getTitle());
        task.setDescription(getTaskDTO.getDescription());
        task.setStatus(getTaskDTO.getStatus());
        Task updatedTask = taskRepository.save(task);
        return new GetTaskDTO(updatedTask);
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found");
        }
        taskRepository.deleteById(id);
    }
}
