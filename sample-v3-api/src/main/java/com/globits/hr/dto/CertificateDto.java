package com.globits.hr.dto;

import com.globits.hr.domain.Certificate;

public class CertificateDto extends BaseObjectDto {
    private String code;
    private String name;
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

    public CertificateDto() {
    }

    public CertificateDto(Certificate entity) {
        if (entity != null) {
            setId(entity.getId());
            this.name = entity.getName();
            this.code = entity.getCode();
            this.type = entity.getType();
        }
    }
}
