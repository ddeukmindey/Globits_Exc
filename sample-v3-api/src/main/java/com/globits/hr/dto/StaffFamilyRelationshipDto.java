package com.globits.hr.dto;

import com.globits.hr.domain.StaffFamilyRelationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class StaffFamilyRelationshipDto {
    private static final Logger logger = LoggerFactory.getLogger(StaffFamilyRelationshipDto.class);
    private UUID id;
    private StaffDto staff;
    private FamilyRelationshipDto familyRelationship;
    private String fullName;
    private Date birthDate;
    private String profession;
    private String address;
    private String description;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public StaffDto getStaff() {
        return staff;
    }

    public void setStaff(StaffDto staff) {
        this.staff = staff;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StaffFamilyRelationshipDto() {
        super();
    }

    public FamilyRelationshipDto getFamilyRelationship() {
        return familyRelationship;
    }

    public void setFamilyRelationship(FamilyRelationshipDto familyRelationship) {
        this.familyRelationship = familyRelationship;
    }

    public StaffFamilyRelationshipDto(StaffFamilyRelationship staffFamilyRelationship) {
        super();
        if (staffFamilyRelationship != null) {
            this.id = staffFamilyRelationship.getId();
            if (staffFamilyRelationship.getStaff() != null) {
                this.staff = new StaffDto(staffFamilyRelationship.getStaff(), false);
            }
            if (staffFamilyRelationship.getFamilyRelationship() != null) {
                this.familyRelationship = new FamilyRelationshipDto(staffFamilyRelationship.getFamilyRelationship());
            }
            this.fullName = staffFamilyRelationship.getFullName();
            this.profession = staffFamilyRelationship.getProfession();
            this.address = staffFamilyRelationship.getAddress();
            this.description = staffFamilyRelationship.getDescription();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            try {
                if (staffFamilyRelationship.getBirthDate().before(sdf.parse("01-01-1900")) || staffFamilyRelationship.getBirthDate().after(sdf.parse("01-01-2100"))) {
                    this.birthDate = null;
                } else {
                    this.birthDate = staffFamilyRelationship.getBirthDate();
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    public StaffFamilyRelationshipDto(StaffFamilyRelationship staffFamilyRelationship, Boolean getStaff) {
        super();
        if (familyRelationship != null) {
            this.id = familyRelationship.getId();
            this.fullName = staffFamilyRelationship.getFullName();
            this.birthDate = staffFamilyRelationship.getBirthDate();
            this.profession = staffFamilyRelationship.getProfession();
            this.address = staffFamilyRelationship.getAddress();
            this.description = staffFamilyRelationship.getDescription();
        }
    }
}
