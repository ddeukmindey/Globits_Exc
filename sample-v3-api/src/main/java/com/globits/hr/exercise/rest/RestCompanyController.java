package com.globits.hr.exercise.rest;

import com.globits.hr.dto.search.SearchDto;
import com.globits.hr.exercise.dto.CompanyDto;
import com.globits.hr.exercise.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/company")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RestCompanyController {

    @Autowired
    private CompanyService companyService;

    @RequestMapping(value = {"/searchByPage", "/search-by-page"}, method = RequestMethod.POST)
    public ResponseEntity<Page<CompanyDto>> searchByPage(@RequestBody SearchDto dto) {
        Page<CompanyDto> result = companyService.searchByPage(dto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<CompanyDto> getById(@PathVariable("id") UUID id) {
        CompanyDto dto = companyService.getById(id);
        if (dto != null) {
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<CompanyDto> save(@RequestBody CompanyDto dto) {
        CompanyDto result = companyService.saveOrUpdate(dto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<CompanyDto> update(@PathVariable("id") UUID id, @RequestBody CompanyDto dto) {
        dto.setId(id);
        CompanyDto result = companyService.saveOrUpdate(dto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> delete(@PathVariable("id") UUID id) {
        Boolean result = companyService.deleteById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = {"/checkCode", "/check-code"}, method = RequestMethod.GET)
    public ResponseEntity<Boolean> checkCode(@RequestParam(value = "id", required = false) UUID id,
                                             @RequestParam("code") String code) {
        Boolean result = companyService.checkCode(id, code);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
