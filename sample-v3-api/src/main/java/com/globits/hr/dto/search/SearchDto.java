package com.globits.hr.dto.search;

import java.util.UUID;

public class SearchDto {
    private UUID id;
    private int pageIndex;
    private int pageSize;
    private String keyword;
    private Boolean voided;
    private String orderBy;
    private Integer academicTitleLevel;
    private UUID academicTitleId;
    private Integer educationDegreeLevel;
    private String gender;
    private UUID departmentId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Boolean getVoided() {
        return voided;
    }

    public void setVoided(Boolean voided) {
        this.voided = voided;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getAcademicTitleLevel() {
        return academicTitleLevel;
    }

    public void setAcademicTitleLevel(Integer academicTitleLevel) {
        this.academicTitleLevel = academicTitleLevel;
    }

    public UUID getAcademicTitleId() {
        return academicTitleId;
    }

    public void setAcademicTitleId(UUID academicTitleId) {
        this.academicTitleId = academicTitleId;
    }

    public Integer getEducationDegreeLevel() {
        return educationDegreeLevel;
    }

    public void setEducationDegreeLevel(Integer educationDegreeLevel) {
        this.educationDegreeLevel = educationDegreeLevel;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public UUID getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }
}
