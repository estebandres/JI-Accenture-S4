package com.mindhub.todolist.dtos;

import jakarta.validation.constraints.NotNull;

public record CreateTaskUserDTO(
        @NotNull
        Long id
){}
