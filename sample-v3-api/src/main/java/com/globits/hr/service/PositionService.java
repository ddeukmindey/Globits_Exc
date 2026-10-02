package com.globits.hr.service;

import java.util.UUID;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.Position;
import com.globits.hr.dto.PositionDto;

public interface PositionService extends GenericService<Position, UUID> {
	PositionDto findByID(UUID id);

	PositionDto updatePosition(PositionDto staff, UUID positionId);

}
