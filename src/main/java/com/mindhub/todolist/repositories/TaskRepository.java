package com.mindhub.todolist.repositories;

import com.mindhub.todolist.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    // Custom query methods if needed
}