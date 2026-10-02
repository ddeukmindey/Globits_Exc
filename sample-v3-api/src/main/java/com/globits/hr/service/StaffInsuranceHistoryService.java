package com.globits.hr.service;

import java.util.UUID;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.StaffInsuranceHistory;
import com.globits.hr.dto.StaffInsuranceHistoryDto;

public interface StaffInsuranceHistoryService extends GenericService<StaffInsuranceHistory, UUID> {
    StaffInsuranceHistoryDto saveStaffInsuranceHistory(StaffInsuranceHistoryDto dto, UUID id);

    StaffInsuranceHistoryDto saveOrUpdate(StaffInsuranceHistoryDto dto, UUID id);
}
