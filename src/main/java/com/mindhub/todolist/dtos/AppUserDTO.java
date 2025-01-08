package com.mindhub.todolist.dtos;

import com.mindhub.todolist.entities.AppUser;

import java.util.List;
import java.util.stream.Collectors;

public class AppUserDTO {
    private Long id;
    private String username;
    private String email;
    private List<GetTaskDTO> tasks;

    public AppUserDTO(AppUser appUser) {
        this.id = appUser.getId();
        this.username = appUser.getUsername();
        this.email = appUser.getEmail();
        this.tasks = appUser.getTasks().stream().map(GetTaskDTO::new).collect(Collectors.toList());
    }

    public AppUserDTO(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public List<GetTaskDTO> getTasks() {
        return tasks;
    }

}
