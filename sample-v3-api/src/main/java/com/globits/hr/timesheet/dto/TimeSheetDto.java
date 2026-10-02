package com.globits.hr.timesheet.dto;

import java.util.*;

import com.globits.hr.domain.TimeSheetStaff;
import com.globits.hr.dto.ShiftWorkDto;
import com.globits.hr.timesheet.domain.TimeSheetLabel;
import org.joda.time.LocalDateTime;

import com.globits.hr.dto.BaseObjectDto;
import com.globits.hr.dto.StaffDto;
import com.globits.hr.dto.WorkingStatusDto;
import com.globits.hr.timesheet.domain.TimeSheet;
import com.globits.hr.timesheet.domain.TimeSheetDetail;

public class TimeSheetDto extends BaseObjectDto {

    private Date workingDate;

    private double totalHours;

    private WorkingStatusDto workingStatus;

    private Date startTime;

    private Date endTime;

    private List<StaffDto> timeSheetStaff;

    private List<TimeSheetDetailDto> details;

    private List<LabelDto> labels;

    private ShiftWorkDto shiftWork;

    private Integer approveStatus;

    private ProjectDto project;

    // private ProjectActivityDto activity;

    private String description;

    private Integer priority;

    private LocalDateTime createDate;

    
    public TimeSheetDto() {

    }

    public TimeSheetDto(TimeSheet timeSheet) {
        if (timeSheet != null) {
            this.setId(timeSheet.getId());
            this.workingDate = timeSheet.getWorkingDate();
            this.startTime = timeSheet.getStartTime();
            this.endTime = timeSheet.getEndTime();
            this.totalHours = timeSheet.getTotalHours();
            this.approveStatus = timeSheet.getApproveStatus();
            this.description = timeSheet.getDescription();
            this.createDate = timeSheet.getCreateDate();
            this.priority = timeSheet.getPriority();
            if (timeSheet.getWorkingStatus() != null) {
                this.workingStatus = new WorkingStatusDto(timeSheet.getWorkingStatus());
            }
            if (timeSheet.getProject() != null) {
                this.project = new ProjectDto(timeSheet.getProject(), false);
            }
            // if (timeSheet.getActivity() != null) {
            //     this.activity = new ProjectActivityDto(timeSheet.getActivity(), false);
            // }

            Set<TimeSheetStaff> timeSheetStaffs = timeSheet.getTimeSheetStaffSet();
            if (timeSheetStaffs != null && !timeSheetStaffs.isEmpty()) {
                this.timeSheetStaff = new ArrayList<>();
                for (TimeSheetStaff sheetStaff : timeSheetStaffs) {
                    this.timeSheetStaff.add(new StaffDto(sheetStaff.getStaff(), true));
                }
            }

            if (timeSheet.getDetails() != null && !timeSheet.getDetails().isEmpty()) {
                this.details = new ArrayList<>();
                for (TimeSheetDetail detail : timeSheet.getDetails()) {
                    TimeSheetDetailDto detailDto = new TimeSheetDetailDto();
                    detailDto.setId(detail.getId());
                    detailDto.setDuration(detail.getDuration());
                    detailDto.setEndTime(detail.getEndTime());
                    detailDto.setStartTime(detail.getStartTime());
                    detailDto.setWorkingItemTitle(detail.getWorkingItemTitle());
                    detailDto.setEmployee(new StaffDto(detail.getEmployee(), false));
                    details.add(detailDto);
                }
            }

            if (timeSheet.getLabels() != null) {
                this.labels = new ArrayList<>();
                for (TimeSheetLabel timeSheetLabel : timeSheet.getLabels()) {
                    this.labels.add(new LabelDto(timeSheetLabel.getLabel(), false));
                }
            }

            if (timeSheet.getShiftWork() != null) {
                this.shiftWork = new ShiftWorkDto(timeSheet.getShiftWork());
            }
        }
    }

    public TimeSheetDto(TimeSheet timeSheet, boolean collapse) {
        if (timeSheet != null) {
            this.setId(timeSheet.getId());
            this.workingDate = timeSheet.getWorkingDate();
            this.startTime = timeSheet.getStartTime();
            this.endTime = timeSheet.getEndTime();
            this.totalHours = timeSheet.getTotalHours();
            this.approveStatus = timeSheet.getApproveStatus();
            this.description = timeSheet.getDescription();
            this.createDate = timeSheet.getCreateDate();
            this.priority = timeSheet.getPriority();

            // if (timeSheet.getActivity() != null) {
            //     this.activity = new ProjectActivityDto(timeSheet.getActivity(), false);
            // }

            if (timeSheet.getWorkingStatus() != null) {
                this.workingStatus = new WorkingStatusDto(timeSheet.getWorkingStatus());
            }
            if (timeSheet.getProject() != null) {
                this.project = new ProjectDto(timeSheet.getProject(), false);
            }

            Set<TimeSheetStaff> timeSheetStaffs = timeSheet.getTimeSheetStaffSet();
            if (timeSheetStaffs != null) {
                this.timeSheetStaff = new ArrayList<>();
                for (TimeSheetStaff sheetStaff : timeSheetStaffs) {
                    StaffDto staffDto = new StaffDto(sheetStaff.getStaff(), true);
                    this.timeSheetStaff.add(staffDto);
                }
            }

            if (timeSheet.getLabels() != null) {
                this.labels = new ArrayList<>();
                for (TimeSheetLabel timeSheetLabel : timeSheet.getLabels()) {
                    this.labels.add(new LabelDto(timeSheetLabel.getLabel()));
                }
            }

            if (timeSheet.getDetails() != null && !timeSheet.getDetails().isEmpty()) {
                this.details = new ArrayList<>();
                for (TimeSheetDetail detail : timeSheet.getDetails()) {
                    TimeSheetDetailDto detailDto = new TimeSheetDetailDto();
                    detailDto.setId(detail.getId());
                    detailDto.setDuration(detail.getDuration());
                    detailDto.setEndTime(detail.getEndTime());
                    detailDto.setStartTime(detail.getStartTime());
                    detailDto.setWorkingItemTitle(detail.getWorkingItemTitle());
                    if (detail.getEmployee() != null) {
                        detailDto.setEmployee(new StaffDto(detail.getEmployee(), false));
                    }
                    details.add(detailDto);
                }
            }

            if (timeSheet.getShiftWork() != null) {
                this.shiftWork = new ShiftWorkDto(timeSheet.getShiftWork());
            }
        }
    }

    public List<LabelDto> getLabels() {
        return labels;
    }

    public void setLabels(List<LabelDto> labels) {
        this.labels = labels;
    }

    public ShiftWorkDto getShiftWork() {
        return shiftWork;
    }

    public void setShiftWork(ShiftWorkDto shiftWork) {
        this.shiftWork = shiftWork;
    }

    public Date getWorkingDate() {
        return workingDate;
    }

    public void setWorkingDate(Date workingDate) {
        this.workingDate = workingDate;
    }

    public double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(double totalHours) {
        this.totalHours = totalHours;
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

    public List<TimeSheetDetailDto> getDetails() {
        return details;
    }

    public void setDetails(List<TimeSheetDetailDto> details) {
        this.details = details;
    }

    public WorkingStatusDto getWorkingStatus() {
        return workingStatus;
    }

    public void setWorkingStatus(WorkingStatusDto workingStatus) {
        this.workingStatus = workingStatus;
    }

    public Integer getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(Integer approveStatus) {
        this.approveStatus = approveStatus;
    }

    public ProjectDto getProject() {
        return project;
    }

    public void setProject(ProjectDto project) {
        this.project = project;
    }

    // public ProjectActivityDto getActivity() {
    //     return activity;
    // }

    // public void setActivity(ProjectActivityDto activity) {
    //     this.activity = activity;
    // }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public List<StaffDto> getTimeSheetStaff() {
        return timeSheetStaff;
    }

    public void setTimeSheetStaff(List<StaffDto> timeSheetStaff) {
        this.timeSheetStaff = timeSheetStaff;
    }
    
}
