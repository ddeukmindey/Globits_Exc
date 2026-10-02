package com.globits.hr.exercise.dto;

import com.globits.core.dto.BaseObjectDto;
import com.globits.hr.exercise.domain.Task;

import java.util.Date;
import java.util.UUID;

public class TaskDto extends BaseObjectDto {
    private String name;
    private String description;
    private Date startTime;
    private Date endTime;
    private Integer priority;
    private Integer status;
    private UUID projectId;
    private String projectName;
    private UUID staffId;
    private String staffName;
    private UUID companyId;

    public TaskDto() {
    }

    public TaskDto(Task entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.name = entity.getName();
            this.description = entity.getDescription();
            this.startTime = entity.getStartTime();
            this.endTime = entity.getEndTime();
            this.priority = entity.getPriority();
            this.status = entity.getStatus();

            if (entity.getProject() != null) {
                this.projectId = entity.getProject().getId();
                this.projectName = entity.getProject().getName();
            }

            if (entity.getStaff() != null) {
                this.staffId = entity.getStaff().getId();
                this.staffName = entity.getStaff().getDisplayName();
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public void setProjectId(UUID projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public UUID getStaffId() {
        return staffId;
    }

    public void setStaffId(UUID staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }
}
