package com.globits.hr.timesheet.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.globits.hr.dto.search.SearchDto;
import com.globits.hr.timesheet.dto.ProjectActivityDto;

@Service
public interface ProjectActivityService {
	ProjectActivityDto saveOrUpdate(UUID id, ProjectActivityDto dto);

	Boolean delete(UUID id);

	ProjectActivityDto getProjectActivity(UUID id);

	Page<ProjectActivityDto> searchByPage(SearchDto dto);

	Boolean checkCode(UUID id, String code);

}
