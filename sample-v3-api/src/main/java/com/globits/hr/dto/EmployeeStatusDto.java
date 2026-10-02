package com.globits.hr.dto;

import com.globits.hr.domain.EmployeeStatus;

public class EmployeeStatusDto extends BaseObjectDto {
    private static final long serialVersionUID = 1L;
    private String code;
    private String name;
    private String languageKey;

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

    public String getLanguageKey() {
        return languageKey;
    }

    public void setLanguageKey(String languageKey) {
        this.languageKey = languageKey;
    }

    public EmployeeStatusDto() {

    }

    public EmployeeStatusDto(EmployeeStatus entity) {
        if (entity != null) {
            this.setCode(entity.getCode());
            this.setName(entity.getName());
            this.setId(entity.getId());
            this.setLanguageKey(entity.getLanguageKey());
        }
    }
}
