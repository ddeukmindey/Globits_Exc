package com.globits.resourceserver.sample.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.globits.core.service.GenericService;
import com.globits.resourceserver.sample.dto.SearchDto;
import com.globits.resourceserver.sample.dto.ColorDto;
import com.globits.resourceserver.sample.model.Color;

@Service
public interface ColorService extends GenericService<Color, UUID> {

	Page<ColorDto> pagingColors(SearchDto dto);

	ColorDto getColor(UUID id);

	ColorDto createOrEditColor(UUID id, ColorDto dto);

	Boolean deleteColor(UUID id);

}
