package com.globits.hr.timesheet.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;
import com.globits.hr.domain.Staff;

@XmlRootElement
@Table(name = "tbl_project_staff")
@Entity
public class ProjectStaff extends BaseObject {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name="staff_id")
	private Staff staff;
	
	@ManyToOne
	@JoinColumn(name="project_id")
	private Project project;

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
}
