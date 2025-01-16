package com.mindhub.todolist.controllers;

import com.mindhub.todolist.models.AppUser;
import com.mindhub.todolist.repositories.AppUserRepository;
import com.mindhub.todolist.repositories.TaskRepository;
import com.mindhub.todolist.security.JwtAuthenticationFilter;
import com.mindhub.todolist.security.SecurityConfig;
import com.mindhub.todolist.services.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.support.ReflectionSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(TaskController.class)
@ComponentScan(basePackages = {"com.mindhub.todolist.services", "com.mindhub.todolist.security"})
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AppUserRepository appUserRepository;

    @MockitoBean
    private TaskRepository taskRepository;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void contextLoads() {

    }

    @Test
    //@WithMockedUser(username = "testuser", password = "password", authority = "ADMIN")
    void test_get_all_users() throws Exception {
        AppUser user = new AppUser();
        when(appUserRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk());
    }
}