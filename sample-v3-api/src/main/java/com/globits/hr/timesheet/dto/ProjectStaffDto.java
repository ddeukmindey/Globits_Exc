package com.globits.hr.timesheet.dto;

import com.globits.hr.dto.BaseObjectDto;
import com.globits.hr.dto.StaffDto;
import com.globits.hr.timesheet.domain.ProjectStaff;

public class ProjectStaffDto extends BaseObjectDto {
    private StaffDto staff;
    private ProjectDto project;

    public ProjectStaffDto() {
        super();
    }

    public ProjectStaffDto(ProjectStaff entity) {
        if (entity != null) {
            if (entity.getStaff() != null) {
                this.staff = new StaffDto(entity.getStaff(), false);
            }
            if (entity.getProject() != null) {
                this.project = new ProjectDto(entity.getProject(), false);
            }
        }
    }


    public StaffDto getStaff() {
        return staff;
    }

    public void setStaff(StaffDto staff) {
        this.staff = staff;
    }

    public ProjectDto getProject() {
        return project;
    }

    public void setProject(ProjectDto project) {
        this.project = project;
    }

}
