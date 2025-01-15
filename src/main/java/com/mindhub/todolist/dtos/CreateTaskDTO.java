package com.mindhub.todolist.dtos;

import com.mindhub.todolist.models.TaskStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTaskDTO(
        @NotNull
        @NotEmpty
        @Size(max = 20)
        String title,
        @NotNull
        @Size(max = 100)
        @NotEmpty
        String description,
        @NotNull
        TaskStatus status,
        @NotNull
        CreateTaskUserDTO user
) {}
