package com.globits.hr.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.globits.core.dto.PersonDto;
import com.globits.hr.domain.PersonCertificate;

public class PersonCertificateDto extends BaseObjectDto {
    private PersonDto person;
    private CertificateDto certificate;
    private Date issueDate;
    private String level;
    private String name;
    private String personCode;
    private String certificateType;

    public PersonDto getPerson() {
        return person;
    }

    public void setPerson(PersonDto person) {
        this.person = person;
    }

    public CertificateDto getCertificate() {
        return certificate;
    }

    public void setCertificate(CertificateDto certificate) {
        this.certificate = certificate;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PersonCertificateDto() {

    }

    public PersonCertificateDto(PersonCertificate entity) {
        if (entity != null) {
            this.setId(entity.getId());
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            try {
                if (entity.getIssueDate().before(sdf.parse("01-01-1900")) || entity.getIssueDate().after(sdf.parse("01-01-2100"))) {
                    this.issueDate = null;
                } else {
                    this.issueDate = entity.getIssueDate();
                }
            } catch (Exception e) {
            }
            this.level = entity.getLevel();
            this.name = entity.getName();
            if (entity.getPerson() != null) {
                this.person = new PersonDto(entity.getPerson());
            }
            if (entity.getCertificate() != null) {
                this.certificate = new CertificateDto(entity.getCertificate());
            }
        }
    }
}
