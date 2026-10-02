package com.globits.hr.timesheet.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.globits.hr.dto.search.SearchDto;
import com.globits.hr.timesheet.dto.ProjectDto;

@Service
public interface ProjectService {
    ProjectDto saveOrUpdate(UUID id, ProjectDto dto);

    Boolean delete(UUID id);

    ProjectDto getProject(UUID id);

    Page<ProjectDto> searchByPage(SearchDto dto);

    Boolean checkCode(UUID id, String code);

    Page<ProjectDto> getPageProjectByUsername(SearchDto dto);
}
