package com.globits.hr.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.SalaryConfig;
import com.globits.hr.dto.SalaryConfigDto;
import com.globits.hr.dto.search.SearchDto;

public interface SalaryConfigService extends GenericService<SalaryConfig, UUID> {
    SalaryConfigDto saveSalaryConfig(SalaryConfigDto dto);

    Boolean deleteSalaryConfig(UUID id);

    int deleteSalaryConfig(List<SalaryConfigDto> dtos);

    SalaryConfigDto getSalaryConfig(UUID id);

    Page<SalaryConfigDto> searchByPage(SearchDto dto);

    Boolean checkCode(UUID id, String code);
}
