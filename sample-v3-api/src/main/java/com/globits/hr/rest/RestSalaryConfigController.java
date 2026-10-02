package com.globits.hr.rest;

/*
 * Giang và Tuấn Anh
 */

import com.globits.core.Constants;
import com.globits.hr.HrConstants;
import com.globits.hr.dto.SalaryConfigDto;
import com.globits.hr.dto.search.SearchDto;
import com.globits.hr.service.SalaryConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/salaryConfig")
public class RestSalaryConfigController {
    @Autowired
    private SalaryConfigService salaryConfigService;

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<SalaryConfigDto> saveSalaryConfig(@RequestBody SalaryConfigDto dto) {
        SalaryConfigDto result = salaryConfigService.saveSalaryConfig(dto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<SalaryConfigDto> getSalaryConfig(@PathVariable UUID id) {
        SalaryConfigDto result = salaryConfigService.getSalaryConfig(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Integer> deleteMultiple(@RequestBody List<SalaryConfigDto> dtos) {
        int result = salaryConfigService.deleteSalaryConfig(dtos);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteOne(@PathVariable UUID id) {
        Boolean result = salaryConfigService.deleteSalaryConfig(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(value = "/searchByPage", method = RequestMethod.POST)
    public ResponseEntity<Page<SalaryConfigDto>> searchByPage(@RequestBody SearchDto searchDto) {
        Page<SalaryConfigDto> page = this.salaryConfigService.searchByPage(searchDto);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(value = "/checkCode", method = RequestMethod.GET)
    public ResponseEntity<Boolean> checkCode(@RequestParam(value = "id", required = false) UUID id, @RequestParam("code") String code) {
        Boolean result = salaryConfigService.checkCode(id, code);
        return new ResponseEntity<>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
