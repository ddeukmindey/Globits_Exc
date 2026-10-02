/*
 * TA va Giang làm
 */

package com.globits.hr.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.AcademicTitle;
import com.globits.hr.dto.AcademicTitleDto;
import com.globits.hr.dto.search.SearchDto;

public interface AcademicTitleService extends GenericService<AcademicTitle, UUID> {
	Boolean deleteAcademicTitle(UUID id);

	AcademicTitleDto getAcademicTitle(UUID id);

	Page<AcademicTitleDto> searchByPage(SearchDto dto);

	Boolean checkCode(UUID id, String code);

	AcademicTitleDto saveOrUpdate(UUID id, AcademicTitleDto dto);
}
