package com.mindhub.todolist.services.impl;

import com.mindhub.todolist.dtos.CreateTaskDTO;
import com.mindhub.todolist.dtos.GetTaskDTO;
import com.mindhub.todolist.models.AppUser;
import com.mindhub.todolist.models.Task;
import com.mindhub.todolist.models.TaskStatus;
import com.mindhub.todolist.exceptions.TaskNotFoundException;
import com.mindhub.todolist.exceptions.UserNotFoundException;
import com.mindhub.todolist.repositories.AppUserRepository;
import com.mindhub.todolist.repositories.TaskRepository;
import com.mindhub.todolist.services.TaskService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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

    public List<GetTaskDTO> getAllTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status).stream()
                .map(GetTaskDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public GetTaskDTO getTaskById(Long id) throws TaskNotFoundException {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        AppUser user = task.getUser();
        return new GetTaskDTO(task);
    }

    @Override
    public GetTaskDTO createTask(CreateTaskDTO newTaskDTO) throws UserNotFoundException {
        Task task = new Task();
        task.setTitle(newTaskDTO.title());
        task.setDescription(newTaskDTO.description());
        task.setStatus(newTaskDTO.status());

        AppUser user = appUserRepository.findById(newTaskDTO.user().id())
                .orElseThrow(() -> new UserNotFoundException(newTaskDTO.user().id()));
        task.setUser(user);

        Task savedTask = taskRepository.save(task);
        return new GetTaskDTO(savedTask);
    }

    @Override
    public GetTaskDTO updateTask(Long id, CreateTaskDTO updateTaskDTO) throws TaskNotFoundException {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        task.setTitle(updateTaskDTO.title());
        task.setDescription(updateTaskDTO.description());
        task.setStatus(updateTaskDTO.status());
        Task updatedTask = taskRepository.save(task);
        return new GetTaskDTO(updatedTask);
    }

    @Override
    public void deleteTask(Long id) throws TaskNotFoundException {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }

    @Override
    public List<GetTaskDTO> getLoggedInUserTasks(String email) throws UserNotFoundException {
        AppUser user = this.appUserRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        return user.getTasks().stream().map(GetTaskDTO::new).collect(Collectors.toList());
    }

    @Override
    public GetTaskDTO createTaskForLoggedInUser(CreateTaskDTO createTaskDTO, String email) throws UserNotFoundException {
        AppUser user = this.appUserRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        Task task = new Task();
        task.setTitle(createTaskDTO.title());
        task.setDescription(createTaskDTO.description());
        task.setStatus(createTaskDTO.status());
        Task savedTask = taskRepository.save(task);
        user.addTask(savedTask);
        appUserRepository.save(user);
        return new GetTaskDTO(savedTask);
    }

    @Override
    public GetTaskDTO updateTaskForLoggedInUser(String email, Long id, CreateTaskDTO createTaskDTO) throws UserNotFoundException, TaskNotFoundException {
//        if (createTaskDTO.title()==null){throw new BadRequestException("Title missing.");}
//        if (createTaskDTO.status()==null){throw new BadRequestException("Status missing.");}
//        if (createTaskDTO.description()==null){throw new BadRequestException("Description missing.");}
        AppUser user = this.appUserRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        return user.getTasks().stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .map(task -> {
                    task.setTitle(createTaskDTO.title());
                    task.setDescription(createTaskDTO.description());
                    task.setStatus(createTaskDTO.status());
                    Task savedTask = taskRepository.save(task);
                    return new GetTaskDTO(savedTask);
                }).orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Override
    public GetTaskDTO getLoggedInUserTaskById(String email, Long id) throws UserNotFoundException, TaskNotFoundException {
        AppUser user = this.appUserRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        return user.getTasks().stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .map(GetTaskDTO::new).orElseThrow(() -> new TaskNotFoundException(id));
    }
}
