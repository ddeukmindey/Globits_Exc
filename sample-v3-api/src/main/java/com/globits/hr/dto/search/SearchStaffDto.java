package com.globits.hr.dto.search;

import java.util.UUID;

public class SearchStaffDto extends SearchDto {
    private UUID staffId;
    private UUID departmentId;
    private UUID employeeStatusId;
    private UUID civilServantTypeId;
    private Boolean isExportExcel = false;

    public UUID getStaffId() {
        return staffId;
    }

    public void setStaffId(UUID staffId) {
        this.staffId = staffId;
    }

    public UUID getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }

    public UUID getEmployeeStatusId() {
        return employeeStatusId;
    }

    public void setEmployeeStatusId(UUID employeeStatusId) {
        this.employeeStatusId = employeeStatusId;
    }

    public UUID getCivilServantTypeId() {
        return civilServantTypeId;
    }

    public void setCivilServantTypeId(UUID civilServantTypeId) {
        this.civilServantTypeId = civilServantTypeId;
    }

    public Boolean getIsExportExcel() {
        return isExportExcel;
    }

    public void setIsExportExcel(Boolean isExportExcel) {
        this.isExportExcel = isExportExcel;
    }
}
