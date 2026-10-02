package com.globits.hr.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;

import com.globits.core.domain.Department;
import com.globits.hr.domain.HRDepartment;

public class HRDepartmentDto extends BaseObjectDto {
    private static final long serialVersionUID = 1L;
    private String name;
    private String code;
    private HRDepartmentDto parent;
    private List<HRDepartmentDto> subDepartment;
    private String displayOrder;
    private UUID parentId;
    private List<HRDepartmentDto> children = new ArrayList<HRDepartmentDto>();
    private String description;
    private String func;
    private String industryBlock;
    private Date foundedDate;
    private String foundedNumber;
    private String departmentDisplayCode;//Số hiệu phòng ban
    private String establishDecisionCode;//Số quyết định thành lập
    private Date establishDecisionDate;//Ngày quyết định thành lập
    private String shortName;
    private String parentCode;
    private Integer departmentType;

    public String getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(String displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public String getIndustryBlock() {
        return industryBlock;
    }

    public void setIndustryBlock(String industryBlock) {
        this.industryBlock = industryBlock;
    }

    public Date getFoundedDate() {
        return foundedDate;
    }

    public void setFoundedDate(Date foundedDate) {
        this.foundedDate = foundedDate;
    }

    public String getFoundedNumber() {
        return foundedNumber;
    }

    public void setFoundedNumber(String foundedNumber) {
        this.foundedNumber = foundedNumber;
    }

    public HRDepartmentDto() {
        super();
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

    public HRDepartmentDto getParent() {
        return parent;
    }

    public void setParent(HRDepartmentDto parent) {
        this.parent = parent;
    }

    public List<HRDepartmentDto> getSubDepartment() {
        return subDepartment;
    }

    public void setSubDepartment(List<HRDepartmentDto> subDepartment) {
        this.subDepartment = subDepartment;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    public List<HRDepartmentDto> getChildren() {
        return children;
    }

    public void setChildren(List<HRDepartmentDto> children) {
        this.children = children;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDepartmentDisplayCode() {
        return departmentDisplayCode;
    }

    public void setDepartmentDisplayCode(String departmentDisplayCode) {
        this.departmentDisplayCode = departmentDisplayCode;
    }

    public String getEstablishDecisionCode() {
        return establishDecisionCode;
    }

    public void setEstablishDecisionCode(String establishDecisionCode) {
        this.establishDecisionCode = establishDecisionCode;
    }

    public Date getEstablishDecisionDate() {
        return establishDecisionDate;
    }

    public void setEstablishDecisionDate(Date establishDecisionDate) {
        this.establishDecisionDate = establishDecisionDate;
    }

    public Integer getDepartmentType() {
        return departmentType;
    }

    public void setDepartmentType(Integer departmentType) {
        this.departmentType = departmentType;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public HRDepartmentDto(HRDepartment entity, boolean isGetParent) {
        if (entity != null) {
            this.setId(entity.getId());
            this.name = entity.getName();
            this.code = entity.getCode();
            this.displayOrder = entity.getDisplayOrder();
            this.func = entity.getFunc();
            this.industryBlock = entity.getIndustryBlock();
            this.foundedDate = entity.getFoundedDate();
            this.foundedNumber = entity.getFoundedNumber();
            this.displayOrder = entity.getDisplayOrder();
            this.description = entity.getDescription();
            this.establishDecisionDate = entity.getEstablishDecisionDate();
            this.establishDecisionCode = entity.getEstablishDecisionCode();
            this.departmentDisplayCode = entity.getDepartmentDisplayCode();
            this.departmentType = entity.getDepartmentType();
            this.shortName = entity.getShortName();
            if (entity.getParent() != null) {
                if (isGetParent) {
                    this.parent = new HRDepartmentDto();
                    this.parent.setId(entity.getParent().getId());
                    this.parent.setName(entity.getParent().getName());
                    this.parent.setCode(entity.getParent().getCode());
                    this.parent.setDisplayOrder(entity.getDisplayOrder());
                }
                this.setParentId(this.parent.getId());
            }
            if (entity.getSubDepartments() != null && entity.getSubDepartments().size() > 0) {
                this.children = new ArrayList<>();
                for (Department child : entity.getSubDepartments()) {
                    HRDepartment hRDepartment = (HRDepartment) child;
                    this.children.add(new HRDepartmentDto(hRDepartment, true));
                }
            }

        }
    }

    public HRDepartmentDto(HRDepartment entity) {
        this(entity, true);
    }
}
