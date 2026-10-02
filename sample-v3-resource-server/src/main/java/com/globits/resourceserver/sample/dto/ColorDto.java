package com.globits.resourceserver.sample.dto;

import com.globits.core.dto.BaseObjectDto;
import com.globits.resourceserver.sample.model.Color;

public class ColorDto extends BaseObjectDto {
	private String code;
	private String value;

	public ColorDto() {
	}

	public ColorDto(Color entity) {
		if (entity != null) {
			this.id = entity.getId();
			this.code = entity.getCode();
			this.value = entity.getValue();
		}

	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
