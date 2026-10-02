package com.globits.hr.exercise.rest;

import com.globits.hr.exercise.dto.TaskDto;
import com.globits.hr.exercise.dto.search.TaskSearchDto;
import com.globits.hr.exercise.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/task")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RestTaskController {

    @Autowired
    private TaskService taskService;

    @RequestMapping(value = {"/searchByPage", "/search-by-page"}, method = RequestMethod.POST)
    public ResponseEntity<Page<TaskDto>> searchByPage(@RequestBody TaskSearchDto dto) {
        Page<TaskDto> result = taskService.searchByPage(dto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<TaskDto> getById(@PathVariable("id") UUID id) {
        TaskDto dto = taskService.getById(id);
        if (dto != null) {
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<TaskDto> save(@RequestBody TaskDto dto) {
        TaskDto result = taskService.saveOrUpdate(dto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<TaskDto> update(@PathVariable("id") UUID id, @RequestBody TaskDto dto) {
        dto.setId(id);
        TaskDto result = taskService.saveOrUpdate(dto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> delete(@PathVariable("id") UUID id) {
        Boolean result = taskService.deleteById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = {"/exportExcel", "/export-excel"}, method = RequestMethod.POST)
    public void exportExcel(@RequestBody TaskSearchDto dto, HttpServletResponse response) throws IOException {
        taskService.exportExcel(dto, response);
    }
}
