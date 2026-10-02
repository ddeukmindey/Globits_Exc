package com.globits.hr.timesheet.service;

import com.globits.hr.timesheet.dto.SearchTotalTimeReportDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.globits.hr.dto.function.TotalTimeReportDto;
@Service
public interface TotalTimeReportService {
	Page<TotalTimeReportDto> totalTimeReport(SearchTotalTimeReportDto dto);
}
