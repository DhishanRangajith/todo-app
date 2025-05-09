package com.dra.todo_app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.dra.todo_app.constant.AppConstant;
import com.dra.todo_app.dto.TaskDto;
import com.dra.todo_app.dto.TaskStatusUpdateDto;
import com.dra.todo_app.repository.TaskRepository;
import com.dra.todo_app.service.TaskService;

@ExtendWith(MockitoExtension.class)
public class TaskContollerTest {
    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskContoller taskController;

    @Mock
    private TaskRepository taskRepository;

    @Test
    public void getTasks_shouldReturnTasks() {
        int status = 1;
        int page = 1;
        int size = 5;
        Page<TaskDto> taskPage = new PageImpl<>(Collections.singletonList(new TaskDto()));
        when(taskService.getTasksByStatus(status, page, size)).thenReturn(taskPage);
        ResponseEntity<Page<TaskDto>> response = taskController.getTasks(status, page, size);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskPage, response.getBody());
        verify(taskService, times(1)).getTasksByStatus(status, page, size);
    }

    @Test
    public void getTasks_shouldReturnTasksWithDefaults_whenParamsAreNull() {
        int defaultStatus = AppConstant.STATUS_ACTIVE;
        int defaultPage = AppConstant.DEFAULT_PAGE;
        int defaultSize = AppConstant.DEFAULT_PAGE_SIZE;

        Page<TaskDto> taskPage = new PageImpl<>(Collections.singletonList(new TaskDto()));
        when(taskService.getTasksByStatus(defaultStatus, defaultPage, defaultSize)).thenReturn(taskPage);

        ResponseEntity<Page<TaskDto>> response = taskController.getTasks(null, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskPage, response.getBody());
        verify(taskService, times(1)).getTasksByStatus(defaultStatus, defaultPage, defaultSize);
    }

    @Test
    public void createTask_shouldReturnCreatedResponse() {
        TaskDto input = new TaskDto();
        TaskDto saved = new TaskDto();
        when(taskService.createTask(input)).thenReturn(saved);

        ResponseEntity<TaskDto> response = taskController.createTask(input);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(saved, response.getBody());
        verify(taskService, times(1)).createTask(input);
    }

    @Test
    public void updateStatusOfTask_shouldReturnOkResponse() {
        Long taskId = 1L;
        TaskStatusUpdateDto request = new TaskStatusUpdateDto();
        request.setStatus(1);
        TaskDto updated = new TaskDto();
        updated.setId(taskId);
        updated.setStatus(1);
        when(taskService.updateStatusOfTask(taskId, request)).thenReturn(updated);
        ResponseEntity<TaskDto> response = taskController.updateStatusOfTask(taskId, request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updated, response.getBody());
        verify(taskService, times(1)).updateStatusOfTask(taskId, request);
    }

}
