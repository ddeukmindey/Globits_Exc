package com.globits.hr.service;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.Staff;
import com.globits.hr.dto.PositionStaffDto;
import com.globits.hr.dto.StaffDto;
import com.globits.hr.dto.StaffSearchDto;
import com.globits.hr.dto.function.PositionTitleStaffDto;
import com.globits.hr.dto.search.SearchStaffDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface StaffService extends GenericService<Staff, UUID> {
    Page<StaffDto> findByPageBasicInfo(int pageIndex, int pageSize);

    StaffDto createStaffAndAccountByCode(StaffDto staff, UUID id);

    StaffDto getStaff(UUID staffId);

    Page<PositionStaffDto> findTeacherByDepartment(UUID departmentId, int pageIndex, int pageSize);

    StaffDto createStaffFromDto(StaffDto staffDto);

    Page<StaffDto> findPageByCode(String textSearch, int pageIndex, int pageSize);

    StaffDto deleteStaff(UUID id);

    List<StaffDto> getAll();

    Boolean deleteMultiple(Staff[] staffs);

    Page<StaffDto> searchStaff(StaffSearchDto dto, int pageSize, int pageIndex);

    int saveListStaff(List<StaffDto> dtos);

    Boolean validateStaffCode(String staffCode, UUID staffId);

    Boolean validateUserName(String userName, UUID userId);

    Page<StaffDto> searchByPage(SearchStaffDto dto);

    List<UUID> getAllDepartmentIdByParentId(UUID parentId);

    StaffDto savePositionStaff(PositionTitleStaffDto dto);

    Staff getByCode(String code);

    StaffDto createStaffSimple(StaffDto staffDto);

    List<StaffDto> saveImportStaff(List<StaffDto> list);

    Boolean checkIdNumber(StaffDto dto);

    StaffDto updateStaffImage(UUID id, String imagePath);

}
