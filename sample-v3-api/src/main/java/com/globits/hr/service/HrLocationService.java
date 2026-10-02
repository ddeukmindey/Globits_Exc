package com.globits.hr.service;

import com.globits.core.domain.Location;
import com.globits.core.dto.LocationDto;
import com.globits.core.service.GenericService;
import com.globits.hr.dto.search.SearchDto;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface HrLocationService extends GenericService<Location, UUID> {

	Page<LocationDto> searchByPage(SearchDto dto);

	LocationDto saveOne(LocationDto dto, UUID id);

	void remove(UUID id);

	Boolean checkCode(UUID id,String code);

	public LocationDto getLocation(UUID id);

}
