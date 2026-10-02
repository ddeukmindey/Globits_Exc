package com.globits.hr.timesheet.dto;

import java.util.Date;

import com.globits.hr.dto.BaseObjectDto;
import com.globits.hr.dto.StaffDto;
import com.globits.hr.timesheet.domain.TimeSheet;
import com.globits.hr.timesheet.domain.TimeSheetDetail;

public class TimeSheetDetailDto extends BaseObjectDto {
    private TimeSheetDto timeSheet;
    private Date startTime;
    private Date endTime;
    private double duration;
    private String workingItemTitle;
    private StaffDto employee;

    public TimeSheetDto getTimeSheet() {
        return timeSheet;
    }

    public void setTimeSheet(TimeSheetDto timeSheet) {
        this.timeSheet = timeSheet;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getWorkingItemTitle() {
        return workingItemTitle;
    }

    public void setWorkingItemTitle(String workingItemTitle) {
        this.workingItemTitle = workingItemTitle;
    }

    public StaffDto getEmployee() {
        return employee;
    }

    public void setEmployee(StaffDto employee) {
        this.employee = employee;
    }

    public TimeSheetDetail toEntity(TimeSheetDetailDto dto, TimeSheetDetail entity) {
        entity.setId(dto.getId());
        entity.setWorkingItemTitle(dto.getWorkingItemTitle());
        entity.setDuration(dto.getDuration());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        return entity;
    }

    public TimeSheetDetailDto() {

    }

    public TimeSheetDetailDto(TimeSheetDetail timeSheetDetail) {
        if (timeSheetDetail != null) {
            this.setId(timeSheetDetail.getId());
            this.startTime = timeSheetDetail.getStartTime();
            this.endTime = timeSheetDetail.getEndTime();
            this.duration = timeSheetDetail.getDuration();
            this.workingItemTitle = timeSheetDetail.getWorkingItemTitle();
            if (timeSheetDetail.getEmployee() != null) {
                this.employee = new StaffDto(timeSheetDetail.getEmployee());
            }
            if (timeSheetDetail.getTimeSheet() != null) {
                TimeSheet sheet = timeSheetDetail.getTimeSheet();
                if (sheet != null) {
                    TimeSheetDto timeSheetDto = new TimeSheetDto();
                    timeSheetDto.setStartTime(sheet.getStartTime());
                    timeSheetDto.setEndTime(sheet.getEndTime());
                    this.timeSheet = timeSheetDto;
                }
            }
        }
    }
}
