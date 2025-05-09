package com.dra.todo_app.service;

import org.springframework.data.domain.Page;

import com.dra.todo_app.dto.TaskDto;
import com.dra.todo_app.dto.TaskStatusUpdateDto;

public interface TaskService {
    Page<TaskDto> getTasksByStatus(int status, int page, int pageSize);
    TaskDto createTask(TaskDto taskDto);
    TaskDto updateStatusOfTask(Long id, TaskStatusUpdateDto taskStatusUpdateDto);
}
