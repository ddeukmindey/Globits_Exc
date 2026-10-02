
package com.globits.hr.dto;

import com.globits.hr.domain.ProfessionalDegree;

public class ProfessionalDegreeDto extends BaseObjectDto {
	private static final long serialVersionUID = 1L;
	private Long id;
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
	public ProfessionalDegreeDto(){
		
	}
	public ProfessionalDegreeDto(ProfessionalDegree entity){
		if (entity !=null) {
			this.setId(entity.getId());
			this.setCode(entity.getCode());			
			this.setName(entity.getName());
		}
	}
}
