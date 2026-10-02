package com.globits.hr.exercise.dto;

import com.globits.core.dto.BaseObjectDto;
import com.globits.hr.exercise.domain.Company;

public class CompanyDto extends BaseObjectDto {
    private String name;
    private String code;
    private String address;

    public CompanyDto() {
    }

    public CompanyDto(Company entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.name = entity.getName();
            this.code = entity.getCode();
            this.address = entity.getAddress();
        }
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
