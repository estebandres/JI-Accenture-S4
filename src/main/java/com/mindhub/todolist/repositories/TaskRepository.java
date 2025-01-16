package com.mindhub.todolist.repositories;

import com.mindhub.todolist.models.Task;
import com.mindhub.todolist.models.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(TaskStatus status);
}