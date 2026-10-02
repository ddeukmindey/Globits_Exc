package com.globits.hr.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import com.globits.hr.dto.HRDepartmentDto;
import com.globits.hr.dto.search.SearchDto;

public interface HRDepartmentService {
    HRDepartmentDto saveOrUpdate(UUID id, HRDepartmentDto dto);

    Boolean deleteHRDepartment(UUID id);

    HRDepartmentDto getHRDepartment(UUID id);

    Page<HRDepartmentDto> searchByPage(SearchDto dto);

    Boolean checkCode(UUID id, String code);

    Page<HRDepartmentDto> pagingDepartments(SearchDto dto);
}
