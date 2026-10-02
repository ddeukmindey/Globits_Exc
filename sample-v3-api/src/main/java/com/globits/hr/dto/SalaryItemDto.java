package com.globits.hr.dto;

import com.globits.hr.domain.SalaryItem;

public class SalaryItemDto extends BaseObjectDto {
    private String name;
    private String code;
    private Integer type;
    private String formula;
    private Boolean isDefault;
    private Boolean isActive;
    private Double defaultValue;
    private Boolean isCheck = false;

    public Boolean getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(Boolean isCheck) {
        this.isCheck = isCheck;
    }

    public void setType(int type) {
        this.type = type;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Double getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Double defaultValue) {
        this.defaultValue = defaultValue;
    }

    public SalaryItemDto() {
    }

    public SalaryItemDto(SalaryItem si) {
        if (si != null) {
            this.setId(si.getId());
            this.setCode(si.getCode());
            this.setIsDefault(si.getIsDefault());
            this.setFormula(si.getFormula());
            this.setIsActive(si.getIsActive());
            this.setDefaultValue(si.getDefaultValue());
            this.setName(si.getName());
            this.setType(si.getType());
        }
    }
}
