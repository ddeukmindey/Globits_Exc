package com.globits.hr.timesheet.service;

import com.globits.hr.dto.function.ProjectTimeWorkChartDto;
import com.globits.hr.dto.function.ProjectTimeWorkTableDto;
import com.globits.hr.timesheet.dto.SearchTotalTimeReportDto;
import org.springframework.data.domain.Page;

public interface ProjectTimeWorkTableService {
    Page<ProjectTimeWorkTableDto> getPageAllStaff();

    Page<ProjectTimeWorkTableDto> getPageStaff(SearchTotalTimeReportDto dto);

    ProjectTimeWorkChartDto getChartTimeWork(SearchTotalTimeReportDto dto);
}
