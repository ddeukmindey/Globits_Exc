package com.globits.hr.rest;

import com.globits.core.Constants;
import com.globits.hr.HrConstants;
import com.globits.hr.dto.SalaryItemDto;
import com.globits.hr.dto.SearchSalaryItemDto;
import com.globits.hr.dto.search.SearchDto;
import com.globits.hr.service.SalaryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/salaryitem")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RestSalaryItemController {
	@Autowired
	private SalaryItemService salaryItemService;

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<SalaryItemDto> saveSalaryItem(@RequestBody SalaryItemDto dto) {
		SalaryItemDto result = salaryItemService.saveSalaryItem(dto);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<SalaryItemDto> getSalaryItem(@PathVariable UUID id) {
		SalaryItemDto result = salaryItemService.getSalaryItem(id);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteOne(@PathVariable UUID id) {
		Boolean result = salaryItemService.deleteSalaryItem(id);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "search/{pageIndex}/{pageSize}", method = RequestMethod.POST)
	public ResponseEntity<Page<SalaryItemDto>> searchSalaryItem(@RequestBody SearchSalaryItemDto dto,
			@PathVariable int pageIndex, @PathVariable int pageSize) {
		Page<SalaryItemDto> result = salaryItemService.searchSalaryItem(dto, pageIndex, pageSize);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value="/searchByPage", method = RequestMethod.POST)
	public ResponseEntity<Page<SalaryItemDto>> searchByPage(@RequestBody SearchDto searchDto) {
		Page<SalaryItemDto> page =  this.salaryItemService.searchByPage(searchDto);
		return new ResponseEntity<>(page, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/checkCode",method = RequestMethod.GET)
	public ResponseEntity<Boolean> checkCode(@RequestParam(value = "id", required=false) UUID id, @RequestParam("code") String code) {
		Boolean result = salaryItemService.checkCode(id,code);
		return new ResponseEntity<>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}
}
