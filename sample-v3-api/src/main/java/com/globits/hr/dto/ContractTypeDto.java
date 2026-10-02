package com.globits.hr.dto;

import com.globits.hr.domain.ContractType;

public class ContractTypeDto extends BaseObjectDto {
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private String code;
	
	private String languageKey;

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

	public String getLanguageKey() {
		return languageKey;
	}

	public void setLanguageKey(String languageKey) {
		this.languageKey = languageKey;
	}
	
	public ContractTypeDto() {}
	
	public ContractTypeDto(ContractType entity) {
		if (entity != null) {
			this.setId(entity.getId());
			this.setCode(entity.getCode());
			this.setName(entity.getName());
			this.setLanguageKey(entity.getLanguageKey());
		}
	}
}
