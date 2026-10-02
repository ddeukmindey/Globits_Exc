package com.globits.hr.rest;

import com.globits.core.Constants;
import com.globits.core.dto.DepartmentTreeDto;
import com.globits.core.service.DepartmentService;
import com.globits.hr.HrConstants;
import com.globits.hr.domain.Staff;
import com.globits.hr.dto.PositionStaffDto;
import com.globits.hr.dto.StaffDto;
import com.globits.hr.dto.StaffSearchDto;
import com.globits.hr.dto.search.SearchStaffDto;
import com.globits.hr.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/staff")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RestStaffController {
    @Autowired
    private StaffService staffService;
    @Autowired
    private DepartmentService departmentService;

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN, "ROLE_STUDENT_MANAGERMENT", "ROLE_EDUCATION_MANAGERMENT", "ROLE_FINANCIAL_MANAGERMENT", "ROLE_EXAM_MANAGERMENT"})
    @RequestMapping(value = "/departmenttree", method = RequestMethod.GET)
    public List<DepartmentTreeDto> getTreeData() {
        return departmentService.getTreeData();
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN, "ROLE_STUDENT_MANAGERMENT", "ROLE_EDUCATION_MANAGERMENT", "ROLE_FINANCIAL_MANAGERMENT", "ROLE_EXAM_MANAGERMENT"})
    @RequestMapping(value = "/{staffId}", method = RequestMethod.GET)
    public StaffDto getStaff(@PathVariable("staffId") String staffId) {
        return staffService.getStaff(UUID.fromString(staffId));
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> deleteStaffs(@RequestBody Staff[] staffs) {
        Boolean deleted = staffService.deleteMultiple(staffs);
        if (deleted) {
            return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Boolean.FALSE, HttpStatus.BAD_REQUEST);
        }
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN, "ROLE_STUDENT_MANAGERMENT", "ROLE_EDUCATION_MANAGERMENT", "ROLE_FINANCIAL_MANAGERMENT", "ROLE_EXAM_MANAGERMENT"})
    @RequestMapping(value = "/{pageIndex}/{pageSize}", method = RequestMethod.GET)
    public Page<StaffDto> getStaffs(@PathVariable int pageIndex, @PathVariable int pageSize) {
        return staffService.findByPageBasicInfo(pageIndex, pageSize);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(method = RequestMethod.POST)
    public StaffDto saveStaff(@RequestBody StaffDto staff) {
        return staffService.createStaffAndAccountByCode(staff, null);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(value = "/{staffId}", method = RequestMethod.PUT)
    public StaffDto updateItem(@PathVariable("staffId") UUID staffId, @RequestBody StaffDto staff) {
        return staffService.createStaffAndAccountByCode(staff, staffId);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(value = "/{staffId}", method = RequestMethod.DELETE)
    public StaffDto removeStaff(@PathVariable("staffId") UUID staffId) {
        return staffService.deleteStaff(staffId);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN, "ROLE_STUDENT_MANAGERMENT", "ROLE_EDUCATION_MANAGERMENT", "ROLE_FINANCIAL_MANAGERMENT", "ROLE_EXAM_MANAGERMENT"})
    @RequestMapping(value = "/department/{departmentId}/{pageIndex}/{pageSize}", method = RequestMethod.GET)
    public Page<PositionStaffDto> findTeacherByDepartment(@PathVariable("departmentId") UUID departmentId,
                                                          @PathVariable int pageIndex, @PathVariable int pageSize) {
        return staffService.findTeacherByDepartment(departmentId, pageIndex, pageSize);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN, "ROLE_STUDENT_MANAGERMENT", "ROLE_EDUCATION_MANAGERMENT", "ROLE_FINANCIAL_MANAGERMENT", "ROLE_EXAM_MANAGERMENT"})
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<StaffDto>> getAllStaffs() {
        List<StaffDto> staffs = staffService.getAll();
        return new ResponseEntity<>(staffs, HttpStatus.OK);
    }

    @RequestMapping(value = "/find/{pageIndex}/{pageSize}", method = RequestMethod.POST)
    public ResponseEntity<Page<StaffDto>> findStaffs(@RequestBody StaffSearchDto dto, @PathVariable int pageIndex,
                                                     @PathVariable int pageSize) {
        Page<StaffDto> page = staffService.searchStaff(dto, pageSize, pageIndex);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @RequestMapping(value = "/staffcode/{textSearch}/{pageIndex}/{pageSize}", method = RequestMethod.GET)
    public Page<StaffDto> getStaffsByCode(@PathVariable String textSearch, @PathVariable int pageIndex,
                                          @PathVariable int pageSize) {
        return staffService.findPageByCode(textSearch, pageIndex, pageSize);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<StaffDto> getSimpleSearch(String textSearch) {
        int pageIndex = 1, pageSize = 10;
        Page<StaffDto> page = staffService.findPageByCode(textSearch, pageIndex, pageSize);
        return page.getContent();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/validatestaffcode/{staffId}")
    public Boolean validateStaffCode(@RequestParam String staffCode, @PathVariable UUID staffId) {
        return staffService.validateStaffCode(staffCode, staffId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/validateusername/{userId}")
    public Boolean validateUserName(@RequestParam String userName, @PathVariable("userId") UUID userId) {
        return staffService.validateUserName(userName, userId);
    }

    @RequestMapping(value = "/searchByPage", method = RequestMethod.POST)
    public ResponseEntity<Page<StaffDto>> searchByPage(@RequestBody SearchStaffDto searchDto) {
        Page<StaffDto> page = this.staffService.searchByPage(searchDto);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @RequestMapping(value = "/checkIdNumber", method = RequestMethod.POST)
    public ResponseEntity<Boolean> checkIdNumber(@RequestBody StaffDto dto) {
        Boolean rs = staffService.checkIdNumber(dto);
        return new ResponseEntity<>(rs, rs == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/imagePath/{id}")
    public StaffDto updateStaffImage(@RequestParam String imagePath, @PathVariable UUID id) {
        return staffService.updateStaffImage(id, imagePath);
    }
}
