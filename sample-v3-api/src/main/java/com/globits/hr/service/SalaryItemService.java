package com.globits.hr.service;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.SalaryItem;
import com.globits.hr.dto.SalaryItemDto;
import com.globits.hr.dto.SearchSalaryItemDto;
import com.globits.hr.dto.search.SearchDto;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface SalaryItemService extends GenericService<SalaryItem, UUID> {
    SalaryItemDto saveSalaryItem(SalaryItemDto dto);

    Boolean deleteSalaryItem(UUID id);

    SalaryItemDto getSalaryItem(UUID id);

    Page<SalaryItemDto> searchSalaryItem(SearchSalaryItemDto dto, int pageIndex, int pageSize);

    Page<SalaryItemDto> searchByPage(SearchDto dto);

    Boolean checkCode(UUID id, String code);
}
