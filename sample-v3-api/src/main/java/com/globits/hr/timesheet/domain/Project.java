package com.globits.hr.timesheet.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;
import com.globits.hr.domain.PersonCertificate;

@XmlRootElement
@Table(name = "tbl_project")
@Entity
public class Project extends BaseObject {
    private static final long serialVersionUID = 1L;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "workload")
    private Double workload;//Tổng số giờ làm việc theo kế hoạch

    @Column(name = "actual_workload")
    private Double actualWorkload;//Tổng số giờ làm việc hiện thời (tính tổng giờ của Timesheet)

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProjectStaff> projectStaff;

    @OneToMany(mappedBy="project", cascade=CascadeType.ALL, orphanRemoval = true)
	private Set<Label> labels;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getWorkload() {
        return workload;
    }

    public void setWorkload(Double workload) {
        this.workload = workload;
    }

    public Double getActualWorkload() {
        return actualWorkload;
    }

    public void setActualWorkload(Double actualWorkload) {
        this.actualWorkload = actualWorkload;
    }

    public Set<ProjectStaff> getProjectStaff() {
        return projectStaff;
    }

    public void setProjectStaff(Set<ProjectStaff> projectStaff) {
        this.projectStaff = projectStaff;
    }

    public Set<Label> getLabels() {
        return labels;
    }

    public void setLabels(Set<Label> labels) {
        this.labels = labels;
    }

    
}
