package com.mindhub.todolist.services.impl;

import com.mindhub.todolist.dtos.GetTaskDTO;
import com.mindhub.todolist.dtos.CreateTaskUserDTO;
import com.mindhub.todolist.dtos.CreateTaskDTO;
import com.mindhub.todolist.exceptions.TaskNotFoundException;
import com.mindhub.todolist.exceptions.UserNotFoundException;
import com.mindhub.todolist.models.AppUser;

import com.mindhub.todolist.models.Task;
import com.mindhub.todolist.models.TaskStatus;
import com.mindhub.todolist.repositories.AppUserRepository;
import com.mindhub.todolist.repositories.TaskRepository;
import com.mindhub.todolist.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {


    private TaskService taskService;

    private TaskRepository taskRepository;

    private AppUserRepository appUserRepository;

    @BeforeEach
    void setup() {
        taskRepository = Mockito.mock(TaskRepository.class);
        appUserRepository = Mockito.mock(AppUserRepository.class);
        taskService = new TaskServiceImpl(taskRepository, appUserRepository);
    }

    @Test
    public void get_all_tasks_returns_task_dtos() {
        // Arrange
        Task task1 = new Task("Test Task 1", "Description 1", TaskStatus.PENDING);
        Task task2 = new Task("Test Task 2", "Description 2", TaskStatus.COMPLETED);
        List<Task> tasks = Arrays.asList(task1, task2);

        when(taskRepository.findAll()).thenReturn(tasks);

        // Act
        List<GetTaskDTO> result = taskService.getAllTasks();

        // Assert
        assertEquals(2, result.size());
        assertEquals(task1.getTitle(), result.get(0).getTitle());
        assertEquals(task2.getTitle(), result.get(1).getTitle());
        verify(taskRepository).findAll();
    }

    @Test
    public void test_create_task_with_valid_dto_and_existing_user() throws UserNotFoundException {
        CreateTaskUserDTO userDTO = new CreateTaskUserDTO(1L);
        CreateTaskDTO taskDTO = new CreateTaskDTO("Test Task", "Test Description", TaskStatus.PENDING, userDTO);
        AppUser user = new AppUser("testuser", "password", "test@test.com");
        Task savedTask = new Task("Test Task", "Test Description", TaskStatus.PENDING);
        savedTask.setUser(user);

        when(appUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        GetTaskDTO result = taskService.createTask(taskDTO);

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertEquals(TaskStatus.PENDING, result.getStatus());
        verify(taskRepository).save(any(Task.class));
        verify(appUserRepository).findById(1L);
    }

    @Test
    public void test_create_task_with_non_existent_user() {
        CreateTaskUserDTO userDTO = new CreateTaskUserDTO(999L);
        CreateTaskDTO taskDTO = new CreateTaskDTO("Test Task", "Test Description", TaskStatus.PENDING, userDTO);

        when(appUserRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            taskService.createTask(taskDTO);
        });

        verify(appUserRepository).findById(999L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    public void given_existent_user_and_task_when_update_task_then_update_success() throws UserNotFoundException, TaskNotFoundException {
        // GIVEN
        String email = "test@test.com";
        Long taskId = 1L;

        AppUser user = new AppUser("testuser", "password", email);
        Task task = new Task("Old Title", "Old Description", TaskStatus.PENDING);
        ReflectionTestUtils.setField(task, "id", taskId);
        user.addTask(task);

        CreateTaskDTO updateDTO = new CreateTaskDTO(
                "New Title",
                "New Description",
                TaskStatus.IN_PROGRESS,
                null
        );

        when(appUserRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // WHEN
        GetTaskDTO result = taskService.updateTaskForLoggedInUser(email, taskId, updateDTO);

        // THEN
        assertEquals(task.getId(), result.getId());
        assertEquals("New Title", result.getTitle());
        assertEquals("New Description", result.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, result.getStatus());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    public void given_nonexistent_user_when_update_task_then_exception_on_user() {
        // GIVEN
        String nonExistentEmail = "nonexistent@test.com";
        Long taskId = 1L;

        CreateTaskDTO updateDTO = new CreateTaskDTO(
                "New Title",
                "New Description",
                TaskStatus.PENDING,
                null
        );
        // WHEN
        when(appUserRepository.findByEmail(nonExistentEmail)).thenReturn(Optional.empty());

        // THEN
        assertThrows(UserNotFoundException.class, () ->
                taskService.updateTaskForLoggedInUser(nonExistentEmail, taskId, updateDTO)
        );

        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    public void given_existent_user_and_nonexistent_task_when_update_task_then_exception_on_task() {
        // GIVEN
        String email = "test@test.com";
        Long nonExistentTaskId = 999L;

        CreateTaskDTO updateDTO = new CreateTaskDTO(
                "New Title",
                "New Description",
                TaskStatus.PENDING,
                null
        );

        AppUser user = new AppUser("testuser", "password", email);
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task("Old Title", "Old Description", TaskStatus.PENDING);
        ReflectionTestUtils.setField(task1, "id", 1L);
        Task task2 = new Task("Second Title", "Second Description", TaskStatus.IN_PROGRESS);
        ReflectionTestUtils.setField(task2, "id", 2L);
        tasks.add(task1);
        tasks.add(task2);
        user.setTasks(tasks);
        when(appUserRepository.findByEmail(email)).thenReturn(Optional.of(user));


        // WHEN
        assertThrows(TaskNotFoundException.class, () ->
                taskService.updateTaskForLoggedInUser(email, nonExistentTaskId, updateDTO)
        );

        // THEN
        verify(taskRepository, never()).save(any(Task.class));
    }
}