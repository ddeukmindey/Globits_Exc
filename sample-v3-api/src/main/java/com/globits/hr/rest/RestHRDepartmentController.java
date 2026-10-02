package com.globits.hr.rest;

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
import com.globits.hr.dto.HRDepartmentDto;
import com.globits.hr.dto.search.SearchDto;
import com.globits.hr.service.HRDepartmentService;

@RestController
@RequestMapping("/api/hRDepartment")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RestHRDepartmentController {
    @Autowired
    private HRDepartmentService hRDepartmentService;

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<HRDepartmentDto> save(@RequestBody HRDepartmentDto dto) {
        HRDepartmentDto result = hRDepartmentService.saveOrUpdate(null, dto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<HRDepartmentDto> update(@RequestBody HRDepartmentDto dto, @PathVariable UUID id) {
        HRDepartmentDto result = hRDepartmentService.saveOrUpdate(id, dto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<HRDepartmentDto> getOne(@PathVariable UUID id) {
        HRDepartmentDto result = hRDepartmentService.getHRDepartment(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteOne(@PathVariable UUID id) {
        Boolean result = hRDepartmentService.deleteHRDepartment(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(value = "/searchByPage", method = RequestMethod.POST)
    public ResponseEntity<Page<HRDepartmentDto>> searchByPage(@RequestBody SearchDto searchDto) {
        Page<HRDepartmentDto> page = this.hRDepartmentService.searchByPage(searchDto);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(value = "/checkCode", method = RequestMethod.GET)
    public ResponseEntity<Boolean> checkCode(@RequestParam(value = "id", required = false) UUID id,
                                             @RequestParam("code") String code) {
        Boolean result = hRDepartmentService.checkCode(id, code);
        return new ResponseEntity<>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(value = "/pagingDepartments", method = RequestMethod.POST)
    public ResponseEntity<Page<HRDepartmentDto>> pagingDepartments(@RequestBody SearchDto searchDto) {
        Page<HRDepartmentDto> page = this.hRDepartmentService.pagingDepartments(searchDto);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
