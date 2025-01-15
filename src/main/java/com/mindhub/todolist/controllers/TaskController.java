package com.mindhub.todolist.controllers;

import com.mindhub.todolist.dtos.CreateTaskDTO;
import com.mindhub.todolist.dtos.GetTaskDTO;
import com.mindhub.todolist.models.TaskStatus;
import com.mindhub.todolist.exceptions.TaskNotFoundException;
import com.mindhub.todolist.exceptions.UserNotFoundException;
import com.mindhub.todolist.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.*;
import jakarta.validation.constraints.NotNull;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Get all tasks", description = "Retrieve a list of all tasks.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GetTaskDTO.class))))
    })
    @GetMapping
    public List<GetTaskDTO> getAllTasks(@Parameter(description = "Task Status", required = false, example = "PENDING")
                                            @RequestParam(required = false) TaskStatus status) {
        if (status != null) {
            return taskService.getAllTasksByStatus(status);
        }
        return taskService.getAllTasks();
    }

    @Operation(summary = "Get task by ID", description = "Retrieve a task by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetTaskDTO.class))),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/{id}")
    public GetTaskDTO getTaskById(@NotNull @Parameter(description = "ID of the task to retrieve", required = true, example = "12")
                                      @PathVariable Long id) throws TaskNotFoundException {
        return taskService.getTaskById(id);
    }

    @Operation(summary = "Create a new task", description = "Add a new task to the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetTaskDTO.class))),
            @ApiResponse(responseCode = "404", description = "Owner user not found")
    })
    @PostMapping
    public GetTaskDTO createTask(@Valid @RequestBody CreateTaskDTO newTaskDTO) throws UserNotFoundException {
        return taskService.createTask(newTaskDTO);
    }

    @Operation(summary = "Update an existing task", description = "Update the details of an existing task.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetTaskDTO.class))),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content(mediaType = "text/plain"))
    })
    @PutMapping("/{id}")
    public GetTaskDTO updateTask(@NotNull @PathVariable Long id,
                                 @Valid @RequestBody CreateTaskDTO updateTaskDTO) throws TaskNotFoundException {
        return taskService.updateTask(id, updateTaskDTO);
    }

    @Operation(summary = "Delete a task", description = "Remove a task from the system by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("/{id}")
    public void deleteTask(@NotNull @PathVariable Long id) throws TaskNotFoundException {
        taskService.deleteTask(id);
    }


    @Operation(summary = "Get all tasks of the logged-in user", description = "Retrieve all tasks of the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks found and returned",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GetTaskDTO.class)))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "text/plain"))
    })
    @GetMapping("/mine")
    public List<GetTaskDTO> getAllMyTasks(Authentication authentication) throws UserNotFoundException {
        return taskService.getLoggedInUserTasks(authentication.getName());
    }


    @Operation(summary = "Get a specific task of the logged-in user", description = "Get an existing task of the currently authenticated user by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task found and returned",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetTaskDTO.class))),
            @ApiResponse(responseCode = "404", description = "User or task not found", content = @Content(mediaType = "text/plain"))
    })
    @GetMapping("/mine/{id}")
    public GetTaskDTO getMyTask(Authentication authentication,
                                @NotNull @PathVariable Long id) throws UserNotFoundException, TaskNotFoundException {
        return taskService.getLoggedInUserTaskById(authentication.getName(),id);
    }

    @Operation(summary = "Create a new task for the logged-in user", description = "Add a new task to the system for the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetTaskDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "text/plain"))
    })
    @PostMapping("mine")
    public GetTaskDTO createMineTask(Authentication authentication,
                                     @Valid @RequestBody CreateTaskDTO createTaskDTO) throws UserNotFoundException {
        return taskService.createTaskForLoggedInUser(createTaskDTO, authentication.getName());
    }


    @Operation(summary = "Update an existing task for the logged-in user", description = "Modify an existing task for the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = GetTaskDTO.class))),
            @ApiResponse(responseCode = "404", description = "User or task not found", content = @Content(mediaType = "text/plain"))
    })
    @PutMapping("mine/{id}")
    public GetTaskDTO updateMineTask(Authentication authentication,
                                     @NotNull @PathVariable Long id,
                                     @Valid @RequestBody CreateTaskDTO createTaskDTO) throws UserNotFoundException, TaskNotFoundException {
        return taskService.updateTaskForLoggedInUser(authentication.getName(),id,createTaskDTO);
    }
}