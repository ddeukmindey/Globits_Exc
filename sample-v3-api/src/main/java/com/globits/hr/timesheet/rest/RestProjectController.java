package com.globits.hr.timesheet.rest;

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
import com.globits.hr.dto.search.SearchDto;
import com.globits.hr.timesheet.dto.ProjectDto;
import com.globits.hr.timesheet.service.ProjectService;

@RestController
@RequestMapping("/api/project")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RestProjectController {
    @Autowired
    ProjectService projectService;


    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ProjectDto> save(@RequestBody ProjectDto dto) {
        ProjectDto result = projectService.saveOrUpdate(null, dto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ProjectDto> save(@RequestBody ProjectDto dto, @PathVariable UUID id) {
        ProjectDto result = projectService.saveOrUpdate(id, dto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ProjectDto> getCertificate(@PathVariable UUID id) {
        ProjectDto result = projectService.getProject(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> delete(@PathVariable UUID id) {
        Boolean result = projectService.delete(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(value = "/search-by-page", method = RequestMethod.POST)
    public ResponseEntity<Page<ProjectDto>> searchByPage(@RequestBody SearchDto searchDto) {
        Page<ProjectDto> page = projectService.searchByPage(searchDto);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(value = "/check-code", method = RequestMethod.GET)
    public ResponseEntity<Boolean> checkCode(@RequestParam(value = "id", required = false) UUID id,
                                             @RequestParam("code") String code) {
        Boolean result = projectService.checkCode(id, code);
        return new ResponseEntity<>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @Secured({Constants.ROLE_ADMIN})
    @RequestMapping(value = "/get-page-project-by-username", method = RequestMethod.POST)
    public ResponseEntity<Page<ProjectDto>> getPageProjectByUsername(@RequestBody SearchDto searchDto) {
        Page<ProjectDto> page = projectService.getPageProjectByUsername(searchDto);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
