/*
 * Created by TA2 & Giang on 23/4/2018.
 */

package com.globits.hr.service;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.SalaryIncrementType;
import com.globits.hr.dto.SalaryIncrementTypeDto;
import com.globits.hr.dto.search.SearchDto;
import org.springframework.data.domain.Page;

import java.util.UUID;


public interface SalaryIncrementTypeService extends GenericService<SalaryIncrementType, UUID> {
    SalaryIncrementTypeDto saveSalaryIncrementType(SalaryIncrementTypeDto dto);

    Boolean deleteSalaryIncrementType(UUID id);

    SalaryIncrementTypeDto getSalaryIncrementType(UUID id);

    Page<SalaryIncrementTypeDto> searchByPage(SearchDto dto);

    Boolean checkCode(UUID id, String code);
}
