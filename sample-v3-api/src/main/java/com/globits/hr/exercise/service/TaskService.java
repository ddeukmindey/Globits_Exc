package com.globits.hr.exercise.service;

import com.globits.core.service.GenericService;
import com.globits.hr.exercise.domain.Task;
import com.globits.hr.exercise.dto.TaskDto;
import com.globits.hr.exercise.dto.search.TaskSearchDto;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public interface TaskService extends GenericService<Task, UUID> {
    Page<TaskDto> searchByPage(TaskSearchDto dto);
    TaskDto getById(UUID id);
    TaskDto saveOrUpdate(TaskDto dto);
    Boolean deleteById(UUID id);
    void exportExcel(TaskSearchDto dto, HttpServletResponse response) throws IOException;
}
