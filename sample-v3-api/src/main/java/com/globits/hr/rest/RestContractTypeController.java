package com.globits.hr.rest;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.globits.hr.dto.ContractTypeDto;
import com.globits.hr.dto.search.SearchDto;
import com.globits.hr.service.ContractTypeService;

@RestController
@RequestMapping("/api/contractType")
@CrossOrigin(value = "*")
public class RestContractTypeController {
    @Autowired
    ContractTypeService contractTypeService;

    @PostMapping(value = "/searchByPage")
    public ResponseEntity<Page<ContractTypeDto>> searchByPage(@RequestBody SearchDto dto) {
        Page<ContractTypeDto> result = contractTypeService.searchByPage(dto);
        return new ResponseEntity<>(result, result != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "")
    public ResponseEntity<ContractTypeDto> create(@RequestBody ContractTypeDto dto) {
        ContractTypeDto result = contractTypeService.saveOrUpdate(dto, null);
        return new ResponseEntity<>(result, result != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ContractTypeDto> update(@RequestBody ContractTypeDto dto, @PathVariable("id") UUID id) {
        ContractTypeDto result = contractTypeService.saveOrUpdate(dto, id);
        return new ResponseEntity<>(result, result != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ContractTypeDto> getOne(@PathVariable("id") UUID id) {
        ContractTypeDto result = contractTypeService.getOne(id);
        return new ResponseEntity<>(result, result != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Boolean> deleteOne(@PathVariable("id") UUID id) {
        contractTypeService.deleteOne(id);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @RequestMapping(value = "/checkCode", method = RequestMethod.GET)
    public ResponseEntity<Boolean> checkCode(@RequestParam(value = "id", required = false) UUID id,
                                             @RequestParam("code") String code) {
        Boolean result = contractTypeService.checkCode(id, code);
        return new ResponseEntity<>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
