package com.dra.todo_app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.dra.todo_app.constant.AppConstant;
import com.dra.todo_app.dto.TaskDto;
import com.dra.todo_app.dto.TaskStatusUpdateDto;
import com.dra.todo_app.entity.TaskEntity;
import com.dra.todo_app.exception.NotFoundException;
import com.dra.todo_app.repository.TaskRepository;
import com.dra.todo_app.service.impl.TaskServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    public void getTasksByStatus_shouldReturnTasks() {
        int status = 1;
        int page = 1;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<TaskEntity> taskEntities = new PageImpl<>(Collections.singletonList(new TaskEntity()));

        when(taskRepository.findAllByStatus(eq(status), eq(pageable))).thenReturn(taskEntities);
        when(objectMapper.convertValue(any(), eq(TaskDto.class))).thenReturn(new TaskDto());

        Page<TaskDto> result = taskService.getTasksByStatus(status, page, pageSize);

        assertEquals(1, result.getContent().size());
        verify(taskRepository, times(1)).findAllByStatus(eq(status), eq(pageable));
    }

    @Test
    void createTask_shouldSaveAndReturnTask() {
        TaskDto inputDto = new TaskDto();
        TaskEntity taskEntity = new TaskEntity();
        TaskEntity savedEntity = new TaskEntity();
        TaskDto outputDto = new TaskDto();

        when(objectMapper.convertValue(inputDto, TaskEntity.class)).thenReturn(taskEntity);
        when(taskRepository.save(taskEntity)).thenReturn(savedEntity);
        when(objectMapper.convertValue(savedEntity, TaskDto.class)).thenReturn(outputDto);

        TaskDto result = taskService.createTask(inputDto);

        assertNotNull(result);
        assertEquals(outputDto, result);

        verify(objectMapper).convertValue(inputDto, TaskEntity.class);
        verify(taskRepository).save(taskEntity);
        verify(objectMapper).convertValue(savedEntity, TaskDto.class);
    }

    @Test
    void updateStatusOfTask_shouldUpdateStatusAndReturnDto() {
        Long taskId = 1L;
        TaskStatusUpdateDto statusDto = new TaskStatusUpdateDto();
        statusDto.setStatus(1);

        TaskEntity existingEntity = new TaskEntity();
        existingEntity.setId(taskId);
        existingEntity.setStatus(AppConstant.STATUS_INACTIVE);

        TaskEntity updatedEntity = new TaskEntity();
        updatedEntity.setId(taskId);
        updatedEntity.setStatus(AppConstant.STATUS_ACTIVE);

        TaskDto expectedDto = new TaskDto();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingEntity));
        when(taskRepository.save(existingEntity)).thenReturn(updatedEntity);
        when(objectMapper.convertValue(updatedEntity, TaskDto.class)).thenReturn(expectedDto);

        TaskDto result = taskService.updateStatusOfTask(taskId, statusDto);
        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(existingEntity);
        verify(objectMapper).convertValue(updatedEntity, TaskDto.class);
    }

    @Test
    void updateStatusOfTask_shouldThrowNotFoundException_whenTaskNotFound() {

        Long taskId = 999L;
        TaskStatusUpdateDto statusDto = new TaskStatusUpdateDto();
        statusDto.setStatus(AppConstant.STATUS_ACTIVE);

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> taskService.updateStatusOfTask(taskId, statusDto)
        );

        assertEquals("To Do task not found with the given id", exception.getMessage());
        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).save(any());
        verify(objectMapper, never()).convertValue(any(), eq(TaskDto.class));
    }
    
}
