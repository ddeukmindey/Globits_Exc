package com.globits.hr.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.PositionStaff;
import com.globits.hr.dto.PositionStaffDto;
import com.globits.hr.dto.function.PositionTitleStaffDto;

@Service
public interface PositionStaffService extends GenericService<PositionStaff, UUID> {
	PositionStaffDto saveImportStaffEducationHistory(PositionTitleStaffDto dto);
}
