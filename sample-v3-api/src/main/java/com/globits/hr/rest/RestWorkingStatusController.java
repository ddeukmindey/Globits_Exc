package com.globits.hr.rest;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.globits.core.Constants;
import com.globits.hr.HrConstants;
import com.globits.hr.dto.WorkingStatusDto;
import com.globits.hr.dto.search.SearchDto;
import com.globits.hr.service.WorkingStatusService;

@RestController
@RequestMapping("/api/workingstatus")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RestWorkingStatusController {
	@Autowired
	WorkingStatusService workingStatusService;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<WorkingStatusDto> save(@RequestBody WorkingStatusDto dto) {
		WorkingStatusDto result = workingStatusService.saveOrUpdate(null, dto);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<WorkingStatusDto> update(@RequestBody WorkingStatusDto dto, @PathVariable UUID id) {
		WorkingStatusDto result = workingStatusService.saveOrUpdate(id, dto);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<WorkingStatusDto> getOne(@PathVariable UUID id) {
		WorkingStatusDto result = workingStatusService.getWorkingStatus(id);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteOne(@PathVariable UUID id) {
		Boolean result = workingStatusService.deleteWorkingStatus(id);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
	@RequestMapping(value = "/search-by-page", method = RequestMethod.POST)
	public ResponseEntity<Page<WorkingStatusDto>> searchByPage(@RequestBody SearchDto searchDto) {
		Page<WorkingStatusDto> page = workingStatusService.searchByPage(searchDto);
		return new ResponseEntity<>(page, HttpStatus.OK);
	}

	@Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
	@RequestMapping(value = "/check-code", method = RequestMethod.GET)
	public ResponseEntity<Boolean> checkCode(@RequestParam(value = "id", required = false) UUID id,
											 @RequestParam("code") String code) {
		Boolean result = workingStatusService.checkCode(id, code);
		return new ResponseEntity<>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}
}
