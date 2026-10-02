package com.globits.hr.dto;

import com.globits.hr.domain.PositionTitle;

public class PositionTitleDto extends BaseObjectDto {
    private static final long serialVersionUID = 1L;
    private String name;
    private String code;
    private String description;
    private Double positionCoefficient;
    private Integer type;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getPositionCoefficient() {
        return positionCoefficient;
    }

    public void setPositionCoefficient(Double positionCoefficient) {
        this.positionCoefficient = positionCoefficient;
    }

    public PositionTitleDto() {
    }

    public PositionTitleDto(PositionTitle title) {
        if (title != null) {
            this.setId(title.getId());
            this.setName(title.getName());
            this.setCode(title.getCode());
            this.setDescription(title.getDescription());
            this.setType(title.getType());
            this.setPositionCoefficient(title.getPositionCoefficient());
        }
    }
}