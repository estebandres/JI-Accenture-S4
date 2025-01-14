package com.mindhub.todolist.dtos;

import com.mindhub.todolist.models.TaskStatus;

public record CreateTaskDTO(Long id,
                            String title,
                            String description,
                            TaskStatus status,
                            CreateTaskUserDTO user) {
}
