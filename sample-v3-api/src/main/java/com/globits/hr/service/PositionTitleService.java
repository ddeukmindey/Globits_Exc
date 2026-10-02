package com.globits.hr.service;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.PositionTitle;
import com.globits.hr.dto.DepartmentsTreeDto;
import com.globits.hr.dto.PositionTitleDto;
import com.globits.hr.dto.search.SearchDto;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface PositionTitleService extends GenericService<PositionTitle, UUID> {
	PositionTitleDto saveTitle(PositionTitleDto dto);

	PositionTitleDto getTitle(UUID id);

	Boolean removeTitle(UUID id);
	
	Boolean deleteMultiple(PositionTitleDto[] dtos);
	
	Boolean checkCode (UUID id,String code);
	
	Page<PositionTitleDto> searchByPage(SearchDto dto);

	Page<DepartmentsTreeDto>getByRoot(int pageIndex, int pageSize);
	
	PositionTitleDto saveOrUpdate(UUID id,PositionTitleDto dto);
}
