package com.globits.hr.timesheet.dto;

import java.util.Date;
import java.util.UUID;

public class SearchTimeSheetDto {
    private UUID id;
    private int pageIndex;
    private int pageSize;
    private String keyword;
    private Date workingDate;
    private String staffCode;
    private String displayName;
    private String codeAndName;
    private UUID projectId;
    private UUID staffId;
    private UUID shiftWorkId;
    private Date fromDate;
    private Date toDate;
    private UUID workingStatusId;
    private Integer priority;
    private boolean isExportExcel;

    public UUID getShiftWorkId() {
        return shiftWorkId;
    }

    public void setShiftWorkId(UUID shiftWorkId) {
        this.shiftWorkId = shiftWorkId;
    }

    public Date getWorkingDate() {
        return workingDate;
    }

    public void setWorkingDate(Date workingDate) {
        this.workingDate = workingDate;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCodeAndName() {
        return codeAndName;
    }

    public void setCodeAndName(String codeAndName) {
        this.codeAndName = codeAndName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public void setProjectId(UUID projectId) {
        this.projectId = projectId;
    }

    public UUID getStaffId() {
        return staffId;
    }

    public void setStaffId(UUID staffId) {
        this.staffId = staffId;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public UUID getWorkingStatusId() {
        return workingStatusId;
    }

    public void setWorkingStatusId(UUID workingStatusId) {
        this.workingStatusId = workingStatusId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public boolean getIsExportExcel() {
        return isExportExcel;
    }

    public void setExportExcel(boolean isExportExcel) {
        this.isExportExcel = isExportExcel;
    }

}
