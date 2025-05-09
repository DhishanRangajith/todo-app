package com.dra.todo_app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dra.todo_app.constant.AppConstant;
import com.dra.todo_app.dto.TaskDto;
import com.dra.todo_app.dto.TaskStatusUpdateDto;
import com.dra.todo_app.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("tasks")
@RequiredArgsConstructor
public class TaskContoller {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<Page<TaskDto>> getTasks(
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        //Set default values
        status = status != null ? status : AppConstant.STATUS_ACTIVE;
        page = page != null ? page : AppConstant.DEFAULT_PAGE;
        size = size != null ? size : AppConstant.DEFAULT_PAGE_SIZE;

        Page<TaskDto> tasks = this.taskService.getTasksByStatus(status, page, size);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@Valid @RequestBody TaskDto data) {
        TaskDto savedData = this.taskService.createTask(data);
        return  new ResponseEntity<>(savedData, HttpStatus.CREATED);
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskDto> updateStatusOfTask(@PathVariable Long id, @Valid @RequestBody TaskStatusUpdateDto data){
        TaskDto savedData = this.taskService.updateStatusOfTask(id, data);
        return ResponseEntity.ok(savedData);
    }
    

}
