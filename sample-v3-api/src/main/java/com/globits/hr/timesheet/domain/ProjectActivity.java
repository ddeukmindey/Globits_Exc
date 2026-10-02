package com.globits.hr.timesheet.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;
@XmlRootElement
@Table(name = "tbl_project_activity")
@Entity
public class ProjectActivity extends BaseObject {
	@ManyToOne
	@JoinColumn(name="project_id")
	private Project project;
	
	@Column(name = "code")
	private String code;
	
	@Column(name = "name")
	private String name;
	
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
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
