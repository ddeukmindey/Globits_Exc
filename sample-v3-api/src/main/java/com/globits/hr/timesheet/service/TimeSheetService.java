package com.globits.hr.timesheet.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.hr.dto.SearchReportDto;
import com.globits.hr.dto.StaffDto;
import com.globits.hr.dto.StaffShiftWorkDto;
import com.globits.hr.dto.SynthesisReportOfStaffDto;
import com.globits.hr.dto.WorkingStatusDto;
import com.globits.hr.timesheet.domain.TimeSheet;
import com.globits.hr.timesheet.dto.SearchTimeSheetDto;
import com.globits.hr.timesheet.dto.TimeSheetDetailDto;
import com.globits.hr.timesheet.dto.TimeSheetDto;

public interface TimeSheetService extends GenericService<TimeSheet, UUID> {
    TimeSheetDto findTimeSheetById(UUID id);

    Boolean deleteTimeSheetById(UUID id);

    Boolean deleteTimeSheets(List<TimeSheetDto> list);

    Page<StaffDto> findPageByName(String textSearch, int pageIndex, int pageSize);

    Page<TimeSheetDto> getAllByWorkingDate(Date workingDate, int pageIndex, int pageSize);

    Page<TimeSheetDetailDto> getTimeSheetDetailByTimeSheetID(UUID id, int pageIndex, int pageSize);

    Boolean confirmTimeSheets(List<TimeSheetDto> list);

    Page<TimeSheetDto> findPageByStaff(String textSearch, int pageIndex, int pageSize);

    Page<TimeSheetDto> searchByDto(SearchTimeSheetDto searchTimeSheetDto, int pageIndex, int pageSize);

    Page<SynthesisReportOfStaffDto> reportWorkingStatus(SearchReportDto searchReportDto, int pageIndex, int pageSize);

    Page<TimeSheetDto> searchByPage(SearchTimeSheetDto dto);

    TimeSheetDto saveOrUpdate(UUID id, TimeSheetDto dto);

    String updateStatus(UUID id, UUID workingStatusId);
}
