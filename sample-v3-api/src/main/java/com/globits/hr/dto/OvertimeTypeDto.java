/*
 * Created by TA & Giang on 22/4/2018.
 */

package com.globits.hr.dto;

import com.globits.hr.domain.OvertimeType;

public class OvertimeTypeDto extends BaseObjectDto{

	private static final long serialVersionUID = 1L;
	private String name;
	private String code;
	
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
	public OvertimeTypeDto(){}
	
	public OvertimeTypeDto(OvertimeType ov){
		
		if (ov!=null) {
			this.setId(ov.getId());
			this.setCode(ov.getCode());			
			this.setName(ov.getName());
		}
	}
}
