package com.globits.hr.dto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.globits.hr.domain.StaffLabourAgreement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaffLabourAgreementDto extends BaseObjectDto {
    private static final Logger logger = LoggerFactory.getLogger(StaffLabourAgreementDto.class);
    private StaffDto staff;
    private Date startDate;
    private Date endDate;
    private Date signedDate;
    private Integer agreementStatus;
    private LabourAgreementTypeDto labourAgreementType;
    private List<StaffLabourAgreementAttachmentDto> attachments = new ArrayList<>();
    private Boolean isCurrent;
    private String staffCode;
    private String contractTypeCode;
    private Date recruitmentDate;
    private Date contractDate;

    public StaffDto getStaff() {
        return staff;
    }

    public void setStaff(StaffDto staff) {
        this.staff = staff;
    }

    public LabourAgreementTypeDto getLabourAgreementType() {
        return labourAgreementType;
    }

    public void setLabourAgreementType(LabourAgreementTypeDto labourAgreementType) {
        this.labourAgreementType = labourAgreementType;
    }

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

    public Date getSignedDate() {
        return signedDate;
    }

    public void setSignedDate(Date signedDate) {
        this.signedDate = signedDate;
    }

    public Integer getAgreementStatus() {
        return agreementStatus;
    }

    public void setAgreementStatus(Integer agreementStatus) {
        this.agreementStatus = agreementStatus;
    }

    public List<StaffLabourAgreementAttachmentDto> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<StaffLabourAgreementAttachmentDto> attachments) {
        this.attachments = attachments;
    }

    public Boolean getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(Boolean isCurrent) {
        this.isCurrent = isCurrent;
    }

    public Boolean getCurrent() {
        return isCurrent;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public void setCurrent(Boolean current) {
        isCurrent = current;
    }

    public String getContractTypeCode() {
        return contractTypeCode;
    }

    public void setContractTypeCode(String contractTypeCode) {
        this.contractTypeCode = contractTypeCode;
    }

    public Date getRecruitmentDate() {
        return recruitmentDate;
    }

    public void setRecruitmentDate(Date recruitmentDate) {
        this.recruitmentDate = recruitmentDate;
    }

    public Date getContractDate() {
        return contractDate;
    }

    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    public StaffLabourAgreementDto() {

    }

    public StaffLabourAgreementDto(StaffLabourAgreement agreement) {
        if (agreement == null) {
            return;
        }
        setId(agreement.getId());
        this.agreementStatus = agreement.getAgreementStatus();
        this.isCurrent = agreement.getIsCurrent();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            if (agreement.getStartDate().before(sdf.parse("01-01-1900")) || agreement.getStartDate().after(sdf.parse("01-01-2100"))) {
                this.startDate = null;
            } else {
                this.startDate = agreement.getStartDate();
            }
            if (agreement.getEndDate().before(sdf.parse("01-01-1900")) || agreement.getEndDate().after(sdf.parse("01-01-2100"))) {
                this.endDate = null;
            } else {
                this.endDate = agreement.getEndDate();
            }
            if (agreement.getSignedDate().before(sdf.parse("01-01-1900")) || agreement.getSignedDate().after(sdf.parse("01-01-2100"))) {
                this.signedDate = null;
            } else {
                this.signedDate = agreement.getSignedDate();
            }
        } catch (Exception e) {
            logger.error("ERROR : {}", e.getMessage(), e);
        }
        if (agreement.getStaff() != null) {
            this.staff = new StaffDto(agreement.getStaff(), false);
        }
        if (agreement.getLabourAgreementType() != null) {
            this.labourAgreementType = new LabourAgreementTypeDto();
            this.labourAgreementType.setId(agreement.getLabourAgreementType().getId());
            this.labourAgreementType.setName(agreement.getLabourAgreementType().getName());
            labourAgreementType = new LabourAgreementTypeDto(agreement.getLabourAgreementType());
        }
    }
}
