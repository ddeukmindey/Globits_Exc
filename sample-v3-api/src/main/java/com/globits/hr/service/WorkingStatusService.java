package com.globits.hr.service;
/*
 * Modify Giang 21/04/2018
 */

import com.globits.core.service.GenericService;
import com.globits.hr.domain.WorkingStatus;
import com.globits.hr.dto.WorkingStatusDto;
import com.globits.hr.dto.search.SearchDto;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface WorkingStatusService extends GenericService<WorkingStatus, UUID> {
    WorkingStatusDto saveOrUpdate(UUID id, WorkingStatusDto dto);

    Boolean deleteWorkingStatus(UUID id);

    WorkingStatusDto getWorkingStatus(UUID id);

    Page<WorkingStatusDto> searchByPage(SearchDto dto);

    Boolean checkCode(UUID id, String code);
}
