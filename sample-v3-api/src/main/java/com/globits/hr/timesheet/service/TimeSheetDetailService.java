package com.globits.hr.timesheet.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.hr.timesheet.domain.TimeSheetDetail;
import com.globits.hr.timesheet.dto.TimeSheetDetailDto;

public interface TimeSheetDetailService extends GenericService<TimeSheetDetail, UUID> {
    TimeSheetDetailDto saveTimeSheetDetail(TimeSheetDetailDto dto);

    Page<TimeSheetDetailDto> getPage(int pageSize, int pageIndex);

    Boolean deleteTimeSheetDetails(List<TimeSheetDetailDto> list);

    TimeSheetDetailDto findTimeSheetDetailById(UUID id);
}
