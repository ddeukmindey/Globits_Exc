package com.globits.hr.rest;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import com.globits.core.Constants;
import com.globits.hr.HrConstants;
import com.globits.hr.dto.DepartmentsTreeDto;
import com.globits.hr.dto.PositionTitleDto;
import com.globits.hr.dto.search.SearchDto;
import com.globits.hr.service.PositionTitleService;

@RestController
@RequestMapping("/api/positionTitle")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RestPositionTitleController {
    @Autowired
    private PositionTitleService titleService;

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PositionTitleDto> getDocument(@PathVariable("id") UUID id) {
        PositionTitleDto dto = titleService.getTitle(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(value = "/{titleId}", method = RequestMethod.DELETE)
    public Boolean removeTitle(@PathVariable("titleId") String titleId) {
        return titleService.removeTitle(UUID.fromString(titleId));
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> deleteTitles(@RequestBody PositionTitleDto[] list) {
        Boolean deleted = titleService.deleteMultiple(list);
        if (deleted) {
            return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Boolean.FALSE, HttpStatus.BAD_REQUEST);
        }
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(value = "/searchByPage", method = RequestMethod.POST)
    public ResponseEntity<Page<PositionTitleDto>> searchByPage(@RequestBody SearchDto searchDto) {
        Page<PositionTitleDto> page = this.titleService.searchByPage(searchDto);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(value = "/checkCode", method = RequestMethod.GET)
    public ResponseEntity<Boolean> checkCode(@RequestParam(value = "id", required = false) UUID id,
                                             @RequestParam("code") String code) {
        Boolean result = titleService.checkCode(id, code);
        return new ResponseEntity<>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(value = "/getByRoot/{pageIndex}/{pageSize}", method = RequestMethod.GET)
    public ResponseEntity<Page<DepartmentsTreeDto>> getByRoot(@PathVariable("pageIndex") int pageIndex,
                                                              @PathVariable("pageSize") int pageSize) {
        Page<DepartmentsTreeDto> page = this.titleService.getByRoot(pageIndex, pageSize);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<PositionTitleDto> save(@RequestBody PositionTitleDto dto) {
        PositionTitleDto result = titleService.saveOrUpdate(null, dto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<PositionTitleDto> update(@RequestBody PositionTitleDto dto, @PathVariable UUID id) {
        PositionTitleDto result = titleService.saveOrUpdate(id, dto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
