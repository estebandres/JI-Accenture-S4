package com.mindhub.todolist.repositories;

import com.mindhub.todolist.entities.Task;
import com.mindhub.todolist.entities.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(TaskStatus status);
}