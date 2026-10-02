package com.globits.hr.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;
/**
 * Chứng chỉ
 */
@XmlRootElement
@Table(name = "tbl_certificate")
@Entity
public class Certificate extends BaseObject{
	private static final long serialVersionUID = 1L;
	@Column(name="code")
	private String code;
	@Column(name="name")
	private String name;
	@Column(name="type")
	private Integer type;
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
}
