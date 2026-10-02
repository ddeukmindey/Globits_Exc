package com.globits.hr.timesheet.dto;

import com.globits.hr.dto.BaseObjectDto;
import com.globits.hr.timesheet.domain.ProjectActivity;

public class ProjectActivityDto extends BaseObjectDto {
    private ProjectDto project;
    private String code;
    private String name;

    public ProjectActivityDto() {
        super();
    }

    public ProjectActivityDto(ProjectActivity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.code = entity.getCode();
            this.name = entity.getName();
            if (entity.getProject() != null) {
                this.project = new ProjectDto(entity.getProject());
            }
        }
    }

    public ProjectActivityDto(ProjectActivity entity, boolean collapse) {
        if (entity != null) {
            this.id = entity.getId();
            this.code = entity.getCode();
            this.name = entity.getName();

        }
        if (collapse && entity != null && entity.getProject() != null) {
            this.project = new ProjectDto(entity.getProject());
        }
    }

    public ProjectDto getProject() {
        return project;
    }

    public void setProject(ProjectDto project) {
        this.project = project;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
