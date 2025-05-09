package com.dra.todo_app.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.dra.todo_app.dto.TaskDto;
import com.dra.todo_app.dto.TaskStatusUpdateDto;
import com.dra.todo_app.entity.TaskEntity;
import com.dra.todo_app.exception.NotFoundException;
import com.dra.todo_app.repository.TaskRepository;
import com.dra.todo_app.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{

    private final TaskRepository taskRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Page<TaskDto> getTasksByStatus(int status, int page, int pageSize){
        Pageable pageable = PageRequest.of(page-1, pageSize, Sort.by("createdAt").descending());
        Page<TaskEntity> entityData = this.taskRepository.findAllByStatus(status, pageable);
        List<TaskDto> data = entityData.getContent().stream()
                                        .map(x -> objectMapper.convertValue(x, TaskDto.class))
                                        .collect(Collectors.toList());
        return new PageImpl<>(data, pageable, entityData.getTotalElements());
    }

    @Override
    public TaskDto createTask(TaskDto taskDto){
        TaskEntity dataEntity = objectMapper.convertValue(taskDto, TaskEntity.class);
        TaskEntity savedDataEntity = this.taskRepository.save(dataEntity);
        TaskDto data = objectMapper.convertValue(savedDataEntity, TaskDto.class);
        return data;
    }

    @Override
    public TaskDto updateStatusOfTask(Long id, TaskStatusUpdateDto taskStatusUpdateDto){
        Optional<TaskEntity> dataEntityOpt = this.taskRepository.findById(id);
        if(dataEntityOpt.isEmpty()){
            throw new NotFoundException("To Do task not found with the given id");
        }
        TaskEntity entityData = dataEntityOpt.get();
        entityData.setStatus(taskStatusUpdateDto.getStatus());
        TaskEntity savedEntityData = this.taskRepository.save(entityData);
        TaskDto data = objectMapper.convertValue(savedEntityData, TaskDto.class);
        return data;
    }



}
