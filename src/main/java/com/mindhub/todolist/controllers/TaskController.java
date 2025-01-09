package com.mindhub.todolist.controllers;

import com.mindhub.todolist.dtos.CreateTaskDTO;
import com.mindhub.todolist.dtos.GetTaskDTO;
import com.mindhub.todolist.entities.TaskStatus;
import com.mindhub.todolist.exceptions.TaskNotFoundException;
import com.mindhub.todolist.exceptions.UserNotFoundException;
import com.mindhub.todolist.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = GetTaskDTO.class))))
    })
    @GetMapping
    public List<GetTaskDTO> getAllTasks(@RequestParam(required = false) TaskStatus status) {
        if (status != null) {
            return taskService.getAllTasksByStatus(status);
        }
        return taskService.getAllTasks();
    }

    @Operation(summary = "Get task by ID", description = "Retrieve a task by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = GetTaskDTO.class))),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/{id}")
    public GetTaskDTO getTaskById(@PathVariable Long id) throws TaskNotFoundException {
        return taskService.getTaskById(id);
    }

    @Operation(summary = "Create a new task", description = "Add a new task to the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully",
                    content = @Content(schema = @Schema(implementation = GetTaskDTO.class)))
    })
    @PostMapping
    public GetTaskDTO createTask(@RequestBody CreateTaskDTO newTaskDTO) throws UserNotFoundException {
        return taskService.createTask(newTaskDTO);
    }

    @Operation(summary = "Update an existing task", description = "Update the details of an existing task.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully",
                    content = @Content(schema = @Schema(implementation = GetTaskDTO.class))),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PutMapping("/{id}")
    public GetTaskDTO updateTask(@PathVariable Long id, @RequestBody CreateTaskDTO updateTaskDTO) throws TaskNotFoundException {
        return taskService.updateTask(id, updateTaskDTO);
    }

    @Operation(summary = "Delete a task", description = "Remove a task from the system by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) throws TaskNotFoundException {
        taskService.deleteTask(id);
    }
}