package com.globits.hr.dto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.globits.core.dto.AdministrativeUnitDto;
import com.globits.core.dto.CountryDto;
import com.globits.core.dto.EthnicsDto;
import com.globits.core.dto.PersonDto;
import com.globits.core.dto.ProfessionDto;
import com.globits.core.dto.ReligionDto;
import com.globits.hr.domain.PersonCertificate;
import com.globits.hr.domain.PositionStaff;
import com.globits.hr.domain.StaffInsuranceHistory;
import com.globits.hr.domain.Staff;
import com.globits.hr.domain.StaffEducationHistory;
import com.globits.hr.domain.StaffFamilyRelationship;
import com.globits.hr.domain.StaffLabourAgreement;
import com.globits.hr.domain.StaffSalaryHistory;
import com.globits.security.dto.UserDto;

public class StaffDto extends PersonDto {
    private String staffCode;
    private Date contractDate;
    private Date startDate;// Ngày bắt đầu công việc
    private Date recruitmentDate;// Ngày tuyển dụng
    private HRDepartmentDto department;
    private EducationDegreeDto educationDegree;// Trình độ học vấn cao nhất
    private CivilServantTypeDto civilServantType;
    private Set<PositionStaffDto> positions = new HashSet<>();
    private Integer currentWorkingStatus;// Trạng thái công việc hiện tại
    private String salaryCoefficient;// Hệ số lương
    private String socialInsuranceNumber;// Số sổ bảo hiểm xã hội
    private String jobTitle;// Tên công việc hiện tại
    private ProfessionalDegreeDto professionalDegree;// Trình độ chuyên môn cao nhất
    private InformaticDegreeDto informaticDegree;
    private PoliticalTheoryLevelDto politicalTheoryLevel;// Trình độ lý luận chính trị
    private Date salaryStartDate;// Ngày hưởng bậc lương hiện thời
    private Set<StaffLabourAgreementDto> agreements = new HashSet<>();
    private Set<StaffEducationHistoryDto> educationHistory;// Quá trình đào tạo
    private Set<StaffFamilyRelationshipDto> familyRelationships;// Quan hệ gia đình
    private Set<StaffSalaryHistoryDto> salaryHistory;// Quá trình lương
    private Set<StaffInsuranceHistoryDto> stafInsuranceHistory;// Quá trình bảo hiểm xh
    private Set<PersonCertificateDto> personCertificate;// Trinh do hoc van
    private UserDto user;
    private String specializedName;// Chuyên ngành đào tạo (chuyên ngành chính)
    private String foreignLanguageName;// Ngữ thành thạo nhất
    private Integer graduationYear;// Năm tốt nghiệp
    //
    private String highestPosition;// Chức vụ cao nhất
    private Date dateOfReceivingPosition;// Ngày nhận Chức vụ cao nhất
    private String professionalTitles;// Chức danh chuyên môn
    private String allowanceCoefficient;// Hệ số phụ cấp
    private Date dateOfReceivingAllowance;// Ngày hưởng phụ cấp
    private ProfessionDto profession;// Công việc được giao
    private String salaryLeve;// Bậc lương
    private LabourAgreementTypeDto labourAgreementType; // Loại hợp đồng
    private EducationDegreeDto computerSkill; // Trình độ tin học
    private EducationDegreeDto englishLevel; // Trình độ tiếng anh
    private CertificateDto englishCertificate; // Chứng chỉ tiếng anh
    private Boolean ethnicLanguage;// Tiếng dân tộc
    private Boolean physicalEducationTeacher;// Giáo viên thể dục
    private String highSchoolEducation;// Giáo dục phổ thông
    private String formsOfTraining;// Hình thức đào tạo
    private String trainingPlaces;// Nơi đào tạo
    private String trainingCountry;// Nơi đào tạo
    private String qualification;// Trình độ chuyên môn
    private AcademicTitleDto academicRank; // Học vị
    private EducationDegreeDto degree; // học hàm
    private EmployeeStatusDto status; // Trạng thái nhân viên
    private String certificationScore;// Điểm chứng chỉ
    private String yearOfCertification;// Năm cấp chứng chỉ
    private String note;//
    private EducationDegreeDto otherLanguageProficiency;// Trình độ ngoại ngữ khác
    private EducationDegreeDto studying;// Đang theo học
    private String yearOfRecognitionDegree;// Năm công nhận học vị
    private String yearOfRecognitionAcademicRank;// Năm công nhận học hàm
    private String currentCell; // for import excel
    private CivilServantCategoryDto civilServantCategory;// Ngạch công chức
    private CivilServantGradeDto grade;// Bậc công chức
    private String permanentResidence;// Hộ khẩu thường trú
    private String currentResidence;
    private String positionDecisionNumber;// Số quyết định chức vụ

    // import
    private String nationCode; // for import excel
    private String ethnicsCode; // for import excel
    private String religionCode; // for import excel
    private String statusCode; // for import excel
    private String departmentCode; // for import excel
    private String labourAgreementTypeCode; // for import excel
    private String civilServantCategoryCode; // for import excel
    private String civilServantTypeCode; // for import excel
    private String politicalTheoryLevelCode; // for import excel
    private String professionCode; // for import excel
    private String computerSkillCode; // for import excel
    private String englishLevelCode; // for import excel
    private String englishCertificateCode; // for import excel
    private String otherLanguageProficiencyCode; // for import excel
    private String academicRankCode; // for import excel
    private String degreeCode; // for import excel
    private String nationalityCode; // for import excel
    private String academicTitleCode; // for import excel
    private String educationDegreeCode; // for import excel
    private String wards; // nguyên quán

    public StaffDto() {
    }

    public StaffDto(UUID id, String staffCode, String displayName, String gender) {
        this.setId(id);
        this.setStaffCode(staffCode);
        this.setDisplayName(displayName);
        this.setGender(gender);
    }

    public StaffDto(Staff entity) {
        super(entity);
        if (entity == null) {
            return;
        }
        id = entity.getId();
        staffCode = entity.getStaffCode();
        firstName = entity.getFirstName();
        lastName = entity.getLastName();
        displayName = entity.getDisplayName();
        gender = entity.getGender();
        birthDate = entity.getBirthDate();
        birthPlace = entity.getBirthPlace();
        contractDate = entity.getContractDate();
        recruitmentDate = entity.getRecruitmentDate();
        startDate = entity.getStartDate();
        setMaritalStatus(entity.getMaritalStatus());
        jobTitle = entity.getJobTitle();
        if (entity.getCivilServantType() != null) {
            civilServantType = new CivilServantTypeDto(entity.getCivilServantType());
        }
        currentWorkingStatus = entity.getCurrentWorkingStatus();
        salaryCoefficient = entity.getSalaryCoefficient();
        socialInsuranceNumber = entity.getSocialInsuranceNumber();
        salaryStartDate = entity.getSalaryStartDate();
        graduationYear = entity.getGraduationYear();
        foreignLanguageName = entity.getForeignLanguageName();
        specializedName = entity.getSpecializedName();
        highestPosition = entity.getHighestPosition();
        dateOfReceivingPosition = entity.getDateOfReceivingPosition();
        professionalTitles = entity.getProfessionalTitles();
        allowanceCoefficient = entity.getAllowanceCoefficient();
        dateOfReceivingAllowance = entity.getDateOfReceivingAllowance();
        salaryLeve = entity.getSalaryLeve();
        ethnicLanguage = entity.getEthnicLanguage();
        physicalEducationTeacher = entity.getPhysicalEducationTeacher();
        formsOfTraining = entity.getFormsOfTraining();
        trainingPlaces = entity.getTrainingCountry();
        trainingCountry = entity.getTrainingPlaces();
        highSchoolEducation = entity.getHighSchoolEducation();
        qualification = entity.getQualification();
        certificationScore = entity.getCertificationScore();
        yearOfCertification = entity.getYearOfCertification();
        yearOfRecognitionDegree = entity.getYearOfRecognitionDegree();
        yearOfRecognitionAcademicRank = entity.getYearOfRecognitionAcademicRank();
        permanentResidence = entity.getPermanentResidence();
        note = entity.getNote();
        positionDecisionNumber = entity.getPositionDecisionNumber();
        currentResidence = entity.getCurrentResidence();
        wards = entity.getWards();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            if (entity.getIdNumberIssueDate().before(sdf.parse("01-01-1900"))
                    || entity.getIdNumberIssueDate().after(sdf.parse("01-01-2100"))) {
                this.idNumberIssueDate = null;
            }
            if (entity.getBirthDate().before(sdf.parse("01-01-1900"))
                    || entity.getBirthDate().after(sdf.parse("01-01-2100"))) {
                this.birthDate = null;
            } else {
                birthDate = entity.getBirthDate();
            }
            if (entity.getContractDate().before(sdf.parse("01-01-1900"))
                    || entity.getContractDate().after(sdf.parse("01-01-2100"))) {
                this.contractDate = null;
            } else {
                contractDate = entity.getContractDate();
            }
            if (entity.getRecruitmentDate().before(sdf.parse("01-01-1900"))
                    || entity.getRecruitmentDate().after(sdf.parse("01-01-2100"))) {
                this.recruitmentDate = null;
            } else {
                recruitmentDate = entity.getRecruitmentDate();
            }
            if (entity.getStartDate().before(sdf.parse("01-01-1900"))
                    || entity.getStartDate().after(sdf.parse("01-01-2100"))) {
                this.startDate = null;
            } else {
                startDate = entity.getStartDate();
            }
            if (entity.getSalaryStartDate().before(sdf.parse("01-01-1900"))
                    || entity.getSalaryStartDate().after(sdf.parse("01-01-2100"))) {
                this.salaryStartDate = null;
            } else {
                salaryStartDate = entity.getSalaryStartDate();
            }
            if (entity.getDateOfReceivingPosition().before(sdf.parse("01-01-1900"))
                    || entity.getDateOfReceivingPosition().after(sdf.parse("01-01-2100"))) {
                this.dateOfReceivingPosition = null;
            } else {
                dateOfReceivingPosition = entity.getDateOfReceivingPosition();
            }
            if (entity.getDateOfReceivingAllowance().before(sdf.parse("01-01-1900"))
                    || entity.getDateOfReceivingAllowance().after(sdf.parse("01-01-2100"))) {
                this.dateOfReceivingAllowance = null;
            } else {
                dateOfReceivingAllowance = entity.getDateOfReceivingAllowance();
            }
        } catch (Exception e) {
        }
        if (entity.getOtherLanguageProficiency() != null) {
            otherLanguageProficiency = new EducationDegreeDto(entity.getOtherLanguageProficiency());
        }
        if (entity.getCivilServantCategory() != null) {
            civilServantCategory = new CivilServantCategoryDto(entity.getCivilServantCategory());
        }
        if (entity.getGrade() != null) {
            grade = new CivilServantGradeDto(entity.getGrade());
        }
        if (entity.getStudying() != null) {
            studying = new EducationDegreeDto(entity.getStudying());
        }
        if (entity.getStatus() != null) {
            status = new EmployeeStatusDto(entity.getStatus());
        }
        if (entity.getProfession() != null) {
            profession = new ProfessionDto(entity.getProfession());
        }
        if (entity.getComputerSkill() != null) {
            computerSkill = new EducationDegreeDto(entity.getComputerSkill());
        }
        if (entity.getEnglishCertificate() != null) {
            englishCertificate = new CertificateDto(entity.getEnglishCertificate());
        }
        if (entity.getEnglishLevel() != null) {
            englishLevel = new EducationDegreeDto(entity.getEnglishLevel());
        }
        if (entity.getDegree() != null) {
            degree = new EducationDegreeDto(entity.getDegree());
        }
        if (entity.getAcademicRank() != null) {
            academicRank = new AcademicTitleDto(entity.getAcademicRank());
        }
        if (entity.getDepartment() != null) {
            this.setDepartment(new HRDepartmentDto(entity.getDepartment()));
        }
        if (entity.getEthnics() != null) {
            ethnics = new EthnicsDto(entity.getEthnics());
        }
        if (entity.getNationality() != null) {
            nationality = new CountryDto(entity.getNationality());
        }
        if (entity.getNativeVillage() != null) {
            nativeVillage = new AdministrativeUnitDto(entity.getNativeVillage());
        }
        if (entity.getReligion() != null) {
            religion = new ReligionDto(entity.getReligion());
        }

        if (entity.getEducationDegree() != null) {
            educationDegree = new EducationDegreeDto(entity.getEducationDegree());
        }

        if (entity.getProfessionalDegree() != null) {
            professionalDegree = new ProfessionalDegreeDto(entity.getProfessionalDegree());
        }
        if (entity.getInformaticDegree() != null) {
            informaticDegree = new InformaticDegreeDto(entity.getInformaticDegree());
        }
        if (entity.getPoliticalTheoryLevel() != null) {
            politicalTheoryLevel = new PoliticalTheoryLevelDto(entity.getPoliticalTheoryLevel());
        }
        if (entity.getLabourAgreementType() != null) {
            labourAgreementType = new LabourAgreementTypeDto(entity.getLabourAgreementType());
        }

        if (entity.getPositions() != null) {
            List<PositionStaffDto> list = new ArrayList<>();
            for (PositionStaff e : entity.getPositions()) {
                list.add(new PositionStaffDto(e));
            }

            if (list.size() > 0) {
                positions.addAll(list);
            }
        }
        if (entity.getAgreements() != null) {
            this.agreements = new HashSet<>();
            for (StaffLabourAgreement agreement : entity.getAgreements()) {
                this.agreements.add(new StaffLabourAgreementDto(agreement));
            }
        }

        if (entity.getStafInsuranceHistory() != null) {
            this.stafInsuranceHistory = new HashSet<>();
            for (StaffInsuranceHistory staffInsuranceHistory : entity.getStafInsuranceHistory()) {
                this.stafInsuranceHistory.add(new StaffInsuranceHistoryDto(staffInsuranceHistory));
            }
        }
        if (entity.getFamilyRelationships() != null) {
            this.familyRelationships = new HashSet<>();
            for (StaffFamilyRelationship familyRelationship : entity.getFamilyRelationships()) {
                this.familyRelationships.add(new StaffFamilyRelationshipDto(familyRelationship));
            }
        }

        if (entity.getEducationHistory() != null) {
            this.educationHistory = new HashSet<>();
            for (StaffEducationHistory history : entity.getEducationHistory()) {
                this.educationHistory.add(new StaffEducationHistoryDto(history));
            }
        }

        if (entity.getSalaryHistory() != null && !entity.getSalaryHistory().isEmpty()) {
            this.salaryHistory = new HashSet<>();
            for (StaffSalaryHistory history : entity.getSalaryHistory()) {
                this.salaryHistory.add(new StaffSalaryHistoryDto(history));
            }
        }

        if (entity.getPersonCertificate() != null) {
            this.personCertificate = new HashSet<>();
            for (PersonCertificate history : entity.getPersonCertificate()) {
                this.personCertificate.add(new PersonCertificateDto(history));
            }
        }

        if (entity.getUser() != null) {
            user = new UserDto(entity.getUser());
        }
    }

    public StaffDto(Staff entity, Boolean simple) {
        super(entity);
        if (entity == null) {
            return;
        }
        id = entity.getId();
        staffCode = entity.getStaffCode();
        firstName = entity.getFirstName();
        lastName = entity.getLastName();
        displayName = entity.getDisplayName();
        gender = entity.getGender();
        birthDate = entity.getBirthDate();
        birthPlace = entity.getBirthPlace();

        setMaritalStatus(entity.getMaritalStatus());
        jobTitle = entity.getJobTitle();
        currentWorkingStatus = entity.getCurrentWorkingStatus();
        salaryCoefficient = entity.getSalaryCoefficient();
        socialInsuranceNumber = entity.getSocialInsuranceNumber();

        graduationYear = entity.getGraduationYear();
        foreignLanguageName = entity.getForeignLanguageName();
        specializedName = entity.getSpecializedName();
        highestPosition = entity.getHighestPosition();

        professionalTitles = entity.getProfessionalTitles();
        allowanceCoefficient = entity.getAllowanceCoefficient();

        salaryLeve = entity.getSalaryLeve();
        formsOfTraining = entity.getFormsOfTraining();
        trainingPlaces = entity.getTrainingCountry();
        trainingCountry = entity.getTrainingPlaces();
        highSchoolEducation = entity.getHighSchoolEducation();
        qualification = entity.getQualification();
        yearOfRecognitionDegree = entity.getYearOfRecognitionDegree();
        yearOfRecognitionAcademicRank = entity.getYearOfRecognitionAcademicRank();
        currentResidence = entity.getCurrentResidence();
        positionDecisionNumber = entity.getPositionDecisionNumber();
        if (entity.getDepartment() != null) {
            this.setDepartment(new HRDepartmentDto(entity.getDepartment()));
        }
        if (entity.getUser() != null) {
            user = new UserDto(entity.getUser());
        }
        if (entity.getLabourAgreementType() != null) {
            this.setLabourAgreementType(new LabourAgreementTypeDto(entity.getLabourAgreementType()));
        }
    }

    @Override
    public Staff toEntity() {
        Staff staff = new Staff();
        staff.setId(id);
        staff.setStaffCode(staffCode);
        staff.setFirstName(staffCode);
        staff.setLastName(staffCode);
        staff.setGender(gender);
        staff.setBirthDate(birthDate);
        staff.setBirthPlace(birthPlace);
        staff.setGender(gender);

        if (!positions.isEmpty()) {
            List<PositionStaff> list = new ArrayList<>();
            for (PositionStaffDto _d : positions) {
                list.add(_d.toEntity());
            }
            staff.getPositions().addAll(list);
        }
        return staff;
    }

    public Set<StaffLabourAgreementDto> getAgreements() {
        return agreements;
    }

    public void setAgreements(Set<StaffLabourAgreementDto> agreements) {
        this.agreements = agreements;
    }

    public String getSocialInsuranceNumber() {
        return socialInsuranceNumber;
    }

    public void setSocialInsuranceNumber(String socialInsuranceNumber) {
        this.socialInsuranceNumber = socialInsuranceNumber;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public String getSalaryCoefficient() {
        return salaryCoefficient;
    }

    public void setSalaryCoefficient(String salaryCoefficient) {
        this.salaryCoefficient = salaryCoefficient;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public Set<PositionStaffDto> getPositions() {
        return positions;
    }

    public void setPositions(Set<PositionStaffDto> positions) {
        this.positions = positions;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public String getCurrentCell() {
        return currentCell;
    }

    public void setCurrentCell(String currentCell) {
        this.currentCell = currentCell;
    }

    public Date getContractDate() {
        return contractDate;
    }

    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getRecruitmentDate() {
        return recruitmentDate;
    }

    public void setRecruitmentDate(Date recruitmentDate) {
        this.recruitmentDate = recruitmentDate;
    }

    public HRDepartmentDto getDepartment() {
        return department;
    }

    public void setDepartment(HRDepartmentDto department) {
        this.department = department;
    }

    public EducationDegreeDto getEducationDegree() {
        return educationDegree;
    }

    public void setEducationDegree(EducationDegreeDto educationDegree) {
        this.educationDegree = educationDegree;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public CivilServantTypeDto getCivilServantType() {
        return civilServantType;
    }

    public void setCivilServantType(CivilServantTypeDto civilServantType) {
        this.civilServantType = civilServantType;
    }

    public Set<StaffFamilyRelationshipDto> getFamilyRelationships() {
        return familyRelationships;
    }

    public void setFamilyRelationships(Set<StaffFamilyRelationshipDto> familyRelationships) {
        this.familyRelationships = familyRelationships;
    }

    public Set<StaffEducationHistoryDto> getEducationHistory() {
        return educationHistory;
    }

    public void setEducationHistory(Set<StaffEducationHistoryDto> educationHistory) {
        this.educationHistory = educationHistory;
    }

    public Set<StaffSalaryHistoryDto> getSalaryHistory() {
        return salaryHistory;
    }

    public void setSalaryHistory(Set<StaffSalaryHistoryDto> salaryHistory) {
        this.salaryHistory = salaryHistory;
    }

    public Integer getCurrentWorkingStatus() {
        return currentWorkingStatus;
    }

    public void setCurrentWorkingStatus(Integer currentWorkingStatus) {
        this.currentWorkingStatus = currentWorkingStatus;
    }

    public ProfessionalDegreeDto getProfessionalDegree() {
        return professionalDegree;
    }

    public void setProfessionalDegree(ProfessionalDegreeDto professionalDegree) {
        this.professionalDegree = professionalDegree;
    }

    public InformaticDegreeDto getInformaticDegree() {
        return informaticDegree;
    }

    public void setInformaticDegree(InformaticDegreeDto informaticDegree) {
        this.informaticDegree = informaticDegree;
    }

    public PoliticalTheoryLevelDto getPoliticalTheoryLevel() {
        return politicalTheoryLevel;
    }

    public void setPoliticalTheoryLevel(PoliticalTheoryLevelDto politicalTheoryLevel) {
        this.politicalTheoryLevel = politicalTheoryLevel;
    }

    public Date getSalaryStartDate() {
        return salaryStartDate;
    }

    public void setSalaryStartDate(Date salaryStartDate) {
        this.salaryStartDate = salaryStartDate;
    }

    public String getSpecializedName() {
        return specializedName;
    }

    public void setSpecializedName(String specializedName) {
        this.specializedName = specializedName;
    }

    public String getForeignLanguageName() {
        return foreignLanguageName;
    }

    public void setForeignLanguageName(String foreignLanguageName) {
        this.foreignLanguageName = foreignLanguageName;
    }

    public Integer getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(Integer graduationYear) {
        this.graduationYear = graduationYear;
    }

    public String getHighestPosition() {
        return highestPosition;
    }

    public void setHighestPosition(String highestPosition) {
        this.highestPosition = highestPosition;
    }

    public Date getDateOfReceivingPosition() {
        return dateOfReceivingPosition;
    }

    public void setDateOfReceivingPosition(Date dateOfReceivingPosition) {
        this.dateOfReceivingPosition = dateOfReceivingPosition;
    }

    public String getProfessionalTitles() {
        return professionalTitles;
    }

    public void setProfessionalTitles(String professionalTitles) {
        this.professionalTitles = professionalTitles;
    }

    public String getAllowanceCoefficient() {
        return allowanceCoefficient;
    }

    public void setAllowanceCoefficient(String allowanceCoefficient) {
        this.allowanceCoefficient = allowanceCoefficient;
    }

    public Date getDateOfReceivingAllowance() {
        return dateOfReceivingAllowance;
    }

    public void setDateOfReceivingAllowance(Date dateOfReceivingAllowance) {
        this.dateOfReceivingAllowance = dateOfReceivingAllowance;
    }

    public ProfessionDto getProfession() {
        return profession;
    }

    public void setProfession(ProfessionDto profession) {
        this.profession = profession;
    }

    public String getSalaryLeve() {
        return salaryLeve;
    }

    public void setSalaryLeve(String salaryLeve) {
        this.salaryLeve = salaryLeve;
    }

    public LabourAgreementTypeDto getLabourAgreementType() {
        return labourAgreementType;
    }

    public void setLabourAgreementType(LabourAgreementTypeDto labourAgreementType) {
        this.labourAgreementType = labourAgreementType;
    }

    public Set<StaffInsuranceHistoryDto> getStafInsuranceHistory() {
        return stafInsuranceHistory;
    }

    public void setStafInsuranceHistory(Set<StaffInsuranceHistoryDto> stafInsuranceHistory) {
        this.stafInsuranceHistory = stafInsuranceHistory;
    }

    public EducationDegreeDto getComputerSkill() {
        return computerSkill;
    }

    public void setComputerSkill(EducationDegreeDto computerSkill) {
        this.computerSkill = computerSkill;
    }

    public EducationDegreeDto getEnglishLevel() {
        return englishLevel;
    }

    public void setEnglishLevel(EducationDegreeDto englishLevel) {
        this.englishLevel = englishLevel;
    }

    public CertificateDto getEnglishCertificate() {
        return englishCertificate;
    }

    public void setEnglishCertificate(CertificateDto englishCertificate) {
        this.englishCertificate = englishCertificate;
    }

    public Boolean getEthnicLanguage() {
        return ethnicLanguage;
    }

    public void setEthnicLanguage(Boolean ethnicLanguage) {
        this.ethnicLanguage = ethnicLanguage;
    }

    public Boolean getPhysicalEducationTeacher() {
        return physicalEducationTeacher;
    }

    public void setPhysicalEducationTeacher(Boolean physicalEducationTeacher) {
        this.physicalEducationTeacher = physicalEducationTeacher;
    }

    public String getHighSchoolEducation() {
        return highSchoolEducation;
    }

    public void setHighSchoolEducation(String highSchoolEducation) {
        this.highSchoolEducation = highSchoolEducation;
    }

    public String getFormsOfTraining() {
        return formsOfTraining;
    }

    public void setFormsOfTraining(String formsOfTraining) {
        this.formsOfTraining = formsOfTraining;
    }

    public String getTrainingPlaces() {
        return trainingPlaces;
    }

    public void setTrainingPlaces(String trainingPlaces) {
        this.trainingPlaces = trainingPlaces;
    }

    public String getTrainingCountry() {
        return trainingCountry;
    }

    public void setTrainingCountry(String trainingCountry) {
        this.trainingCountry = trainingCountry;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public EducationDegreeDto getDegree() {
        return degree;
    }

    public void setDegree(EducationDegreeDto degree) {
        this.degree = degree;
    }

    public AcademicTitleDto getAcademicRank() {
        return academicRank;
    }

    public void setAcademicRank(AcademicTitleDto academicRank) {
        this.academicRank = academicRank;
    }

    public EmployeeStatusDto getStatus() {
        return status;
    }

    public void setStatus(EmployeeStatusDto status) {
        this.status = status;
    }

    public String getCertificationScore() {
        return certificationScore;
    }

    public void setCertificationScore(String certificationScore) {
        this.certificationScore = certificationScore;
    }

    public String getYearOfCertification() {
        return yearOfCertification;
    }

    public void setYearOfCertification(String yearOfCertification) {
        this.yearOfCertification = yearOfCertification;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public EducationDegreeDto getOtherLanguageProficiency() {
        return otherLanguageProficiency;
    }

    public void setOtherLanguageProficiency(EducationDegreeDto otherLanguageProficiency) {
        this.otherLanguageProficiency = otherLanguageProficiency;
    }

    public EducationDegreeDto getStudying() {
        return studying;
    }

    public void setStudying(EducationDegreeDto studying) {
        this.studying = studying;
    }

    public String getYearOfRecognitionDegree() {
        return yearOfRecognitionDegree;
    }

    public void setYearOfRecognitionDegree(String yearOfRecognitionDegree) {
        this.yearOfRecognitionDegree = yearOfRecognitionDegree;
    }

    public String getYearOfRecognitionAcademicRank() {
        return yearOfRecognitionAcademicRank;
    }

    public void setYearOfRecognitionAcademicRank(String yearOfRecognitionAcademicRank) {
        this.yearOfRecognitionAcademicRank = yearOfRecognitionAcademicRank;
    }

    public CivilServantCategoryDto getCivilServantCategory() {
        return civilServantCategory;
    }

    public void setCivilServantCategory(CivilServantCategoryDto civilServantCategory) {
        this.civilServantCategory = civilServantCategory;
    }

    public CivilServantGradeDto getGrade() {
        return grade;
    }

    public void setGrade(CivilServantGradeDto grade) {
        this.grade = grade;
    }

    public String getNationCode() {
        return nationCode;
    }

    public void setNationCode(String nationCode) {
        this.nationCode = nationCode;
    }

    public String getEthnicsCode() {
        return ethnicsCode;
    }

    public void setEthnicsCode(String ethnicsCode) {
        this.ethnicsCode = ethnicsCode;
    }

    public String getReligionCode() {
        return religionCode;
    }

    public void setReligionCode(String religionCode) {
        this.religionCode = religionCode;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getLabourAgreementTypeCode() {
        return labourAgreementTypeCode;
    }

    public void setLabourAgreementTypeCode(String labourAgreementTypeCode) {
        this.labourAgreementTypeCode = labourAgreementTypeCode;
    }

    public String getCivilServantCategoryCode() {
        return civilServantCategoryCode;
    }

    public void setCivilServantCategoryCode(String civilServantCategoryCode) {
        this.civilServantCategoryCode = civilServantCategoryCode;
    }

    public String getCivilServantTypeCode() {
        return civilServantTypeCode;
    }

    public void setCivilServantTypeCode(String civilServantTypeCode) {
        this.civilServantTypeCode = civilServantTypeCode;
    }

    public String getPoliticalTheoryLevelCode() {
        return politicalTheoryLevelCode;
    }

    public void setPoliticalTheoryLevelCode(String politicalTheoryLevelCode) {
        this.politicalTheoryLevelCode = politicalTheoryLevelCode;
    }

    public String getProfessionCode() {
        return professionCode;
    }

    public void setProfessionCode(String professionCode) {
        this.professionCode = professionCode;
    }

    public String getComputerSkillCode() {
        return computerSkillCode;
    }

    public void setComputerSkillCode(String computerSkillCode) {
        this.computerSkillCode = computerSkillCode;
    }

    public String getEnglishLevelCode() {
        return englishLevelCode;
    }

    public void setEnglishLevelCode(String englishLevelCode) {
        this.englishLevelCode = englishLevelCode;
    }

    public String getEnglishCertificateCode() {
        return englishCertificateCode;
    }

    public void setEnglishCertificateCode(String englishCertificateCode) {
        this.englishCertificateCode = englishCertificateCode;
    }

    public String getOtherLanguageProficiencyCode() {
        return otherLanguageProficiencyCode;
    }

    public void setOtherLanguageProficiencyCode(String otherLanguageProficiencyCode) {
        this.otherLanguageProficiencyCode = otherLanguageProficiencyCode;
    }

    public String getAcademicRankCode() {
        return academicRankCode;
    }

    public void setAcademicRankCode(String academicRankCode) {
        this.academicRankCode = academicRankCode;
    }

    public String getDegreeCode() {
        return degreeCode;
    }

    public void setDegreeCode(String degreeCode) {
        this.degreeCode = degreeCode;
    }

    public String getNationalityCode() {
        return nationalityCode;
    }

    public void setNationalityCode(String nationalityCode) {
        this.nationalityCode = nationalityCode;
    }

    public Set<PersonCertificateDto> getPersonCertificate() {
        return personCertificate;
    }

    public void setPersonCertificate(Set<PersonCertificateDto> personCertificate) {
        this.personCertificate = personCertificate;
    }

    public String getAcademicTitleCode() {
        return academicTitleCode;
    }

    public void setAcademicTitleCode(String academicTitleCode) {
        this.academicTitleCode = academicTitleCode;
    }

    public String getEducationDegreeCode() {
        return educationDegreeCode;
    }

    public void setEducationDegreeCode(String educationDegreeCode) {
        this.educationDegreeCode = educationDegreeCode;
    }

    public String getPermanentResidence() {
        return permanentResidence;
    }

    public void setPermanentResidence(String permanentResidence) {
        this.permanentResidence = permanentResidence;
    }

    public String getCurrentResidence() {
        return currentResidence;
    }

    public void setCurrentResidence(String currentResidence) {
        this.currentResidence = currentResidence;
    }

    public String getPositionDecisionNumber() {
        return positionDecisionNumber;
    }

    public void setPositionDecisionNumber(String positionDecisionNumber) {
        this.positionDecisionNumber = positionDecisionNumber;
    }

    public String getWards() {
        return wards;
    }

    public void setWards(String wards) {
        this.wards = wards;
    }
}
