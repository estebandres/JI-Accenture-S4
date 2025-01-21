package com.mindhub.todolist.dtos;

import com.mindhub.todolist.models.Task;
import com.mindhub.todolist.models.TaskStatus;

public class GetTaskDTO {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;

    public GetTaskDTO(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.status = task.getStatus();
    }

    public GetTaskDTO() {}
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }


    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }
}
