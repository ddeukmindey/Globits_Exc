package com.globits.resourceserver.hr.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;

import com.globits.resourceserver.sample.dto.ColorDto;
import com.globits.resourceserver.sample.dto.SearchDto;
import com.globits.resourceserver.sample.service.ColorService;
import com.globits.security.domain.User;
import com.globits.core.utils.SecurityUtils;
import com.globits.resourceserver.sample.dao.ColorRepository;
import com.globits.resourceserver.sample.model.Color;

//@CrossOrigin(origins = "http://localhost:8080", allowedHeaders = "Authorization")
//@CrossOrigin(origins = "*", allowedHeaders = "Authorization")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/colors")
public class ColorsRestController {

	@Autowired
	ColorRepository repository;

	@Autowired
	private ColorService colorService;

	@RequestMapping(value = "/pagingColors", method = RequestMethod.POST)
	public ResponseEntity<Page<ColorDto>> searchRiskBehaviorByDto(@RequestBody SearchDto dto) {
		String userName = SecurityUtils.getCurrentUserName();
		Page<ColorDto> result = colorService.pagingColors(dto);
		return new ResponseEntity<Page<ColorDto>>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<ColorDto> getColor(@PathVariable UUID id) {
		ColorDto result = colorService.getColor(id);
		return new ResponseEntity<ColorDto>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<ColorDto> createColor(@RequestBody ColorDto dto) {
		ColorDto result = colorService.createOrEditColor(null, dto);
		return new ResponseEntity<ColorDto>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<ColorDto> editColor(@RequestBody ColorDto dto, @PathVariable("id") UUID id) {
		ColorDto result = colorService.createOrEditColor(id, dto);
		return new ResponseEntity<ColorDto>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteColor(@PathVariable("id") String id) {
		Boolean result = colorService.deleteColor(UUID.fromString(id));
		return new ResponseEntity<Boolean>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}
}
