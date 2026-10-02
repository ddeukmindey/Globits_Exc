
package com.globits.hr.dto;

import com.globits.hr.domain.PoliticalTheoryLevel;

public class PoliticalTheoryLevelDto extends BaseObjectDto {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String code;
    private Integer level;

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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public PoliticalTheoryLevelDto() {

    }

    public PoliticalTheoryLevelDto(PoliticalTheoryLevel entity) {

        if (entity != null) {
            this.setId(entity.getId());
            this.setCode(entity.getCode());
            this.setName(entity.getName());
            this.setLevel(entity.getLevel());
        }
    }
}
