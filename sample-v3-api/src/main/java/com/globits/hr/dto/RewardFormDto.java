package com.globits.hr.dto;

import com.globits.hr.domain.RewardForm;

public class RewardFormDto extends BaseObjectDto {
    private static final long serialVersionUID = 1L;
    private String name;
    private String languageKey;
    private String code;
    private Integer rewardType;
    private Integer formal;
    private String description;
    private Integer evaluateYear;
    private Integer evaluateLevel;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getRewardType() {
        return rewardType;
    }

    public void setRewardType(Integer rewardType) {
        this.rewardType = rewardType;
    }

    public Integer getFormal() {
        return formal;
    }

    public void setFormal(Integer formal) {
        this.formal = formal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEvaluateYear() {
        return evaluateYear;
    }

    public void setEvaluateYear(Integer evaluateYear) {
        this.evaluateYear = evaluateYear;
    }

    public Integer getEvaluateLevel() {
        return evaluateLevel;
    }

    public void setEvaluateLevel(Integer evaluateLevel) {
        this.evaluateLevel = evaluateLevel;
    }

    public RewardFormDto() {
        super();
    }

    public RewardFormDto(RewardForm entity) {
        super();
        if (entity != null) {
            this.setId(entity.getId());
            this.setName(entity.getName());
            this.setLanguageKey(entity.getLanguageKey());
            this.setCode(entity.getCode());
            this.setRewardType(entity.getRewardType());
            this.setFormal(entity.getFormal());
            this.setDescription(entity.getDescription());
            this.setEvaluateYear(entity.getEvaluateYear());
            this.setEvaluateLevel(entity.getEvaluateLevel());
        }
    }
}
