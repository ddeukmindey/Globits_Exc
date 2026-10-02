package com.globits.hr.exercise.service;

import com.globits.core.service.GenericService;
import com.globits.hr.exercise.domain.Company;
import com.globits.hr.exercise.dto.CompanyDto;
import com.globits.hr.dto.search.SearchDto;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface CompanyService extends GenericService<Company, UUID> {
    Page<CompanyDto> searchByPage(SearchDto dto);
    CompanyDto getById(UUID id);
    CompanyDto saveOrUpdate(CompanyDto dto);
    Boolean deleteById(UUID id);
    Boolean checkCode(UUID id, String code);
}
