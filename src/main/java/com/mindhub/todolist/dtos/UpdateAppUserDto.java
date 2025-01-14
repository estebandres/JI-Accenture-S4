package com.mindhub.todolist.dtos;

import java.io.Serializable;

/**
 * DTO for {@link com.mindhub.todolist.models.AppUser}
 */
public record UpdateAppUserDto(String username, String password, String email) implements Serializable {
}