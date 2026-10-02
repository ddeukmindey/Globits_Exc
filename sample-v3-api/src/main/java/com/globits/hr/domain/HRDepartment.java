package com.globits.hr.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.util.Date;

import com.globits.core.domain.Department;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("HRDepartment")
public class HRDepartment extends Department {
    private static final long serialVersionUID = 1L;
    @Column(name = "description")
    private String description;

    @Column(name = "func")
    private String func;

    @Column(name = "industry_block")
    private String industryBlock;

    @Column(name = "founded_date")
    private Date foundedDate;

    @Column(name = "founded_number")
    private String foundedNumber;

    @Column(name = "department_display_code")
    private String departmentDisplayCode;//Số hiệu phòng ban

    @Column(name = "establish_decision_code")
    private String establishDecisionCode;//Số quyết định thành lập

    @Column(name = "establish_decision_date")
    private Date establishDecisionDate;//Ngày quyết định thành lập

  
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


}
