package com.globits.hr.rest;

import com.globits.core.Constants;
import com.globits.hr.HrConstants;
import com.globits.hr.dto.ShiftWorkDto;
import com.globits.hr.dto.search.SearchDto;
import com.globits.hr.service.ShiftWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/shiftwork")
public class RestShiftWorkController {
    @Autowired
    private ShiftWorkService shiftWorkService;

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN, "ROLE_STUDENT_MANAGERMENT", "ROLE_EDUCATION_MANAGERMENT", "ROLE_FINANCIAL_MANAGERMENT", "ROLE_EXAM_MANAGERMENT"})
    @PostMapping("/search-by-page")
    public ResponseEntity<Page<ShiftWorkDto>> searchByPage(@RequestBody SearchDto dto) {
        Page<ShiftWorkDto> pageShiftWork = shiftWorkService.searchByPage(dto);
        return new ResponseEntity<>(pageShiftWork, HttpStatus.OK);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(value = "/check-code", method = RequestMethod.GET)
    public ResponseEntity<Boolean> checkCode(@RequestParam(value = "id", required = false) UUID id, @RequestParam("code") String code) {
        Boolean result = shiftWorkService.checkCode(id, code);
        return new ResponseEntity<>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ShiftWorkDto> save(@RequestBody ShiftWorkDto dto) {
        ShiftWorkDto result = shiftWorkService.saveOrUpdate(null, dto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ShiftWorkDto> update(@RequestBody ShiftWorkDto dto, @PathVariable UUID id) {
        ShiftWorkDto result = shiftWorkService.saveOrUpdate(id, dto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        shiftWorkService.remove(UUID.fromString(id));
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN, "ROLE_STUDENT_MANAGERMENT", "ROLE_EDUCATION_MANAGERMENT", "ROLE_FINANCIAL_MANAGERMENT", "ROLE_EXAM_MANAGERMENT"})
    @GetMapping("/{id}")
    public ResponseEntity<ShiftWorkDto> getShiftWorkById(@PathVariable String id) {
        ShiftWorkDto result = shiftWorkService.getById(UUID.fromString(id));
        return new ResponseEntity<>(result, result != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
