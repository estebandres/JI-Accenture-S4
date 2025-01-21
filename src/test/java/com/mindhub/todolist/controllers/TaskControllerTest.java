package com.mindhub.todolist.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindhub.todolist.dtos.*;
import com.mindhub.todolist.models.AppUser;
import com.mindhub.todolist.models.Task;
import com.mindhub.todolist.models.TaskStatus;
import com.mindhub.todolist.repositories.AppUserRepository;
import com.mindhub.todolist.repositories.TaskRepository;
import com.mindhub.todolist.security.JwtAuthenticationFilter;
import com.mindhub.todolist.security.SecurityConfig;
import com.mindhub.todolist.services.AppUserService;
import com.mindhub.todolist.services.TaskService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.support.ReflectionSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
//@WebMvcTest(TaskController.class)
//@ComponentScan(basePackages = {"com.mindhub.todolist.services", "com.mindhub.todolist.repositories","com.mindhub.todolist.security"})
@AutoConfigureWebMvc
@ActiveProfiles("test")
class TaskControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    private final ObjectMapper jsonMapper = new ObjectMapper();

    @BeforeEach
    public void setup(){

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        SecurityContext securityContext = SecurityContextHolder.getContext();
        AppUser testUser = new AppUser("testino","password123456!","testino@gmail.com");
        testUser = appUserRepository.save(testUser);
        Task testTask = new Task("Test task","test task description", TaskStatus.PENDING);
        testTask = taskRepository.save(testTask);
        testUser.addTask(testTask);
        taskRepository.save(testTask);
    }

    @AfterEach
    public void cleanup(){
        taskRepository.deleteAll();
        appUserRepository.deleteAll();
    }
    @Test
    void contextLoads() {
    }

    @Test
    @WithMockUser(username = "testino@gmail.com", authorities = "USER")
    void given_user_with_tasks_when_get_tasks_then_return_tasks() throws Exception {
        MvcResult response = mockMvc.perform(get("/api/tasks/mine"))
                .andExpect(status().isOk()).andReturn();
        String jsonResponse = response.getResponse().getContentAsString();
        System.out.println(jsonResponse);
        List<GetTaskDTO> tasks = jsonMapper.readValue(jsonResponse, jsonMapper.getTypeFactory().constructCollectionType(List.class, GetTaskDTO.class));

        assertEquals(1, tasks.size());
        assertEquals("Test task", tasks.get(0).getTitle());
        assertEquals("test task description", tasks.get(0).getDescription());
        assertEquals(TaskStatus.PENDING, tasks.get(0).getStatus());
    }

    @Test
    @WithMockUser(username = "unexistent@gmail.com", authorities = "USER")
    void given_nonexistent_user_when_get_tasks_then_return_error() throws Exception {
    MvcResult response = mockMvc.perform(get("/api/tasks/mine"))
            .andExpect(status().isNotFound()).andReturn();

    String jsonResponse = response.getResponse().getContentAsString();
    assertTrue(jsonResponse.contains("STEVE REPORT: User with email: unexistent@gmail.com was not found."));
    }

    @Test
    @WithMockUser(username = "testino@gmail.com", authorities = "USER")
    void given_user_with_existent_task_when_get_task_with_id_then_return_the_task() throws Exception {
        Long taskId = taskRepository.findAll().get(0).getId(); // Assuming there's only one task created in the setup
        MvcResult response = mockMvc.perform(get("/api/tasks/mine/" + taskId))
                .andExpect(status().isOk()).andReturn();
        String jsonResponse = response.getResponse().getContentAsString();
        GetTaskDTO task = jsonMapper.readValue(jsonResponse, GetTaskDTO.class);

        assertEquals("Test task", task.getTitle());
        assertEquals("test task description", task.getDescription());
        assertEquals(TaskStatus.PENDING, task.getStatus());
    }

    @Test
    @WithMockUser(username = "testino@gmail.com", authorities = "USER")
    void given_user_with_nonexistent_task_when_get_task_with_id_then_return_error() throws Exception {
        MvcResult response = mockMvc.perform(get("/api/tasks/mine/999"))
                .andExpect(status().isNotFound()).andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        assertTrue(jsonResponse.contains("STEVE REPORT: Task with Id: 999 was not found."));
    }


}
