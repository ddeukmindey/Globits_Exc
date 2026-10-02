package com.globits.hr.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.globits.hr.domain.StaffInsuranceHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaffInsuranceHistoryDto extends BaseObjectDto {
    private static final Logger logger = LoggerFactory.getLogger(StaffInsuranceHistoryDto.class);
    private Date startDate;//Ngày bắt đầu mức đóng
    private Date endDate;//Ngày kết thúc mức đóng
    private String note;//Ghi chú
    private Double salaryCofficient;//Hệ số lương đó bảo hiểm xã hội
    private Double insuranceSalary;//Mức lương đóng bảo hiểm xã hội
    private Double staffPercentage;//Tỷ lệ cá nhân đóng bảo hiểm xã hội
    private Double orgPercentage;//Tỷ lệ đơn vị đóng bảo hiểm xã hội
    private Double staffInsuranceAmount;//Số tiền cá nhân đóng
    private Double orgInsuranceAmount;//Số tiền đơn vị đóng
    private String socialInsuranceBookCode;//Số sổ bảo hiểm xã hội
    private String professionName;//Mã ngạch viên chức khi đóng BHCH --> Dữ liệu mang tính lịch sử, có thể không sử dụng sau này
    private String departmentName;//Trực thuộc phòng ban nào khi đóng mức BHXH này --> Dữ liệu mang tính lịch sử, có thể không sử dụng sau này
    private Double allowanceCoefficient;//Hệ số phụ cấp --> Dữ liệu mang tính lịch sử, có thể không sử dụng sau này
    private StaffDto staff;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Double getSalaryCofficient() {
        return salaryCofficient;
    }

    public void setSalaryCofficient(Double salaryCofficient) {
        this.salaryCofficient = salaryCofficient;
    }

    public Double getInsuranceSalary() {
        return insuranceSalary;
    }

    public void setInsuranceSalary(Double insuranceSalary) {
        this.insuranceSalary = insuranceSalary;
    }

    public Double getStaffPercentage() {
        return staffPercentage;
    }

    public void setStaffPercentage(Double staffPercentage) {
        this.staffPercentage = staffPercentage;
    }

    public Double getOrgPercentage() {
        return orgPercentage;
    }

    public void setOrgPercentage(Double orgPercentage) {
        this.orgPercentage = orgPercentage;
    }

    public Double getStaffInsuranceAmount() {
        return staffInsuranceAmount;
    }

    public void setStaffInsuranceAmount(Double staffInsuranceAmount) {
        this.staffInsuranceAmount = staffInsuranceAmount;
    }

    public Double getOrgInsuranceAmount() {
        return orgInsuranceAmount;
    }

    public void setOrgInsuranceAmount(Double orgInsuranceAmount) {
        this.orgInsuranceAmount = orgInsuranceAmount;
    }

    public String getSocialInsuranceBookCode() {
        return socialInsuranceBookCode;
    }

    public void setSocialInsuranceBookCode(String socialInsuranceBookCode) {
        this.socialInsuranceBookCode = socialInsuranceBookCode;
    }

    public String getProfessionName() {
        return professionName;
    }

    public void setProfessionName(String professionName) {
        this.professionName = professionName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Double getAllowanceCoefficient() {
        return allowanceCoefficient;
    }

    public void setAllowanceCoefficient(Double allowanceCoefficient) {
        this.allowanceCoefficient = allowanceCoefficient;
    }

    public StaffDto getStaff() {
        return staff;
    }

    public void setStaff(StaffDto staff) {
        this.staff = staff;
    }

    public StaffInsuranceHistoryDto() {
        super();
    }

    public StaffInsuranceHistoryDto(StaffInsuranceHistory entity) {
        if (entity != null) {
            this.note = entity.getNote();
            this.salaryCofficient = entity.getSalaryCofficient();
            this.insuranceSalary = entity.getInsuranceSalary();
            this.staffPercentage = entity.getStaffPercentage();
            this.orgPercentage = entity.getOrgPercentage();
            this.staffInsuranceAmount = entity.getStaffInsuranceAmount();
            this.orgInsuranceAmount = entity.getOrgInsuranceAmount();
            this.allowanceCoefficient = entity.getAllowanceCoefficient();
            this.departmentName = entity.getDepartmentName();
            this.professionName = entity.getProfessionName();
            this.socialInsuranceBookCode = entity.getSocialInsuranceBookCode();
            if (entity.getStaff() != null) {
                this.staff = new StaffDto(entity.getStaff(), false);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            try {
                if (entity.getStartDate().before(sdf.parse("01-01-1900")) || entity.getStartDate().after(sdf.parse("01-01-2100"))) {
                    this.startDate = null;
                } else {
                    this.startDate = entity.getStartDate();
                }
                if (entity.getEndDate().before(sdf.parse("01-01-1900")) || entity.getEndDate().after(sdf.parse("01-01-2100"))) {
                    this.endDate = null;
                } else {
                    this.endDate = entity.getEndDate();
                }
            } catch (Exception e) {
                logger.error("ERROR : {}", e.getMessage(), e);
            }
        }
    }
}
