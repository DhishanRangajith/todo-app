package com.dra.todo_app.entity;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.dra.todo_app.constant.AppConstant;
import com.dra.todo_app.repository.TaskRepository;

@SpringBootTest
@ActiveProfiles("test")
public class TaskEntityAuditTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void shouldSetTimestampsOnPersist() {
        TaskEntity task = new TaskEntity();
        task.setTitle("Audit Test");
        task.setStatus(AppConstant.STATUS_ACTIVE);

        TaskEntity saved = taskRepository.save(task);

        assertNotNull(saved.getCreatedAt(), "createdAt should not be null");
        assertNotNull(saved.getUpdatedAt(), "updatedAt should not be null");
    }

    @Test
    void shouldUpdateTimestampOnUpdate() throws InterruptedException {
        TaskEntity task = new TaskEntity();
        task.setTitle("Update Test");
        task.setStatus(AppConstant.STATUS_ACTIVE);
        TaskEntity saved = taskRepository.save(task);

        Thread.sleep(1000);
        saved.setTitle("Updated Title");
        TaskEntity updated = taskRepository.save(saved);

        assertTrue(updated.getUpdatedAt().isAfter(updated.getCreatedAt()));
    }
}