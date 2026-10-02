package com.globits.hr.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.Person;
import com.globits.core.domain.Profession;
import com.globits.hr.timesheet.domain.TimeSheetDetail;

@Entity
@Table(name = "tbl_staff")
@PrimaryKeyJoinColumn(name = "id", foreignKey = @ForeignKey(name = "staff_person", value = ConstraintMode.NO_CONSTRAINT))
@DiscriminatorValue("1")
@XmlRootElement
public class Staff extends Person {
    private static final long serialVersionUID = 6014783475303579207L;

    @Column(name = "staff_code", nullable = true, unique = true)
    private String staffCode;//Mã nhân viên
    @Column(name = "contract_date")
    private Date contractDate;//Ngày hợp đồng

    @Column(name = "start_date")
    private Date startDate;//Ngày bắt đầu công việc
    @Column(name = "recruitment_date")
    private Date recruitmentDate;//Ngày tuyển dụng
    @Column(name = "contract_number")
    private String contractNumber;//Số hợp đồng hiện thời

    @ManyToOne
    @JoinColumn(name = "civil_servant_type_id")
    private CivilServantType civilServantType;//Loại công chức

    @Column(name = "current_working_status", nullable = true)
    private Integer currentWorkingStatus;//Trạng thái công việc hiện tại
    @ManyToOne
    @JoinColumn(name = "department_id")
    private HRDepartment department;//Phòng ban hiện thời
    @Column(name = "social_insurance_number", nullable = true)
    private String socialInsuranceNumber;//Số sổ bảo hiểm xã hội
    @OneToMany(mappedBy = "staff", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PositionStaff> positions = new HashSet<PositionStaff>();//Quá trình chức vụ

    @OneToMany(mappedBy = "staff", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StaffLabourAgreement> agreements;//Hợp đồng

    @OneToMany(mappedBy = "staff", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StaffSalaryHistory> salaryHistory;//Quá trình lương

    @OneToMany(mappedBy = "staff", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StaffSalaryProperty> staffSalaryProperties;//Thuộc tính lương

    @OneToMany(mappedBy = "staff", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StaffEducationHistory> educationHistory;//Quá trình đào tạo

    @OneToMany(mappedBy = "staff", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StaffFamilyRelationship> familyRelationships;//Quan hệ gia đình

    @OneToMany(mappedBy = "staff", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StaffInsuranceHistory> stafInsuranceHistory;//Quá trình bảo hiểm xh

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PersonCertificate> personCertificate;//Trinh do hoc van

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TimeSheetDetail> details;

    @OneToMany(mappedBy = "staff", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TimeSheetStaff> timeSheets;

    @ManyToOne
    @JoinColumn(name = "education_degree_id")
    private EducationDegree educationDegree;//Trình độ học vấn

    @ManyToOne
    @JoinColumn(name = "professional_degree_id")
    private ProfessionalDegree professionalDegree;//Trình độ chuyên môn cao nhất
    @ManyToOne
    @JoinColumn(name = "informatic_degree_id")
    private InformaticDegree informaticDegree;//Bằng cấp

    @ManyToOne
    @JoinColumn(name = "political_theory_level")
    private PoliticalTheoryLevel politicalTheoryLevel;//Trình độ lý luận chính trị

    @Column(name = "job_title")
    private String jobTitle;//Tên công việc hiện tại

    @Column(name = "salary_coefficient")
    private String salaryCoefficient;//Hệ số lương

    @Column(name = "salary_leve")
    private String salaryLeve;//Bậc lương

    @Column(name = "salary_start_date")
    private Date salaryStartDate;//Ngày hưởng bậc lương hiện thời

    @Column(name = "specializedName")
    private String specializedName;//Chuyên ngành đào tạo (chuyên ngành chính)

    @Column(name = "foreign_language_name")
    private String foreignLanguageName;//Ngữ thành thạo nhất

    @Column(name = "graduation_year")
    private Integer graduationYear;//Năm tốt nghiệp

    @Column(name = "permanent_residence")
    private String permanentResidence;//Hộ khẩu thường trú

    //
    @Column(name = "highest_position")
    private String highestPosition;//Chức vụ cao nhất

    @Column(name = "date_of_receiving_position")
    private Date dateOfReceivingPosition;//Ngày nhận Chức vụ cao nhất


    @Column(name = "professional_titles")
    private String professionalTitles;//Chức danh chuyên môn

    @Column(name = "allowance_coefficient")
    private String allowanceCoefficient;//Hệ số phụ cấp

    @Column(name = "date_of_receiving_allowance")
    private Date dateOfReceivingAllowance;//Ngày hưởng phụ cấp

    @ManyToOne
    @JoinColumn(name = "profession_id")
    private Profession profession;//Công việc được giao

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "labour_agreement_type_id")
    private LabourAgreementType labourAgreementType; //Loại hợp đồng

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "education_degree_computer_skill")
    private EducationDegree computerSkill; //Trình độ tin học

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "education_degree_english_level")
    private EducationDegree englishLevel; //Trình độ tiếng anh

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "english_certificate")
    private Certificate englishCertificate; //Chứng chỉ tiếng anh

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "degree")
    private EducationDegree degree; //Học vị

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status")
    private EmployeeStatus status; //Trạng thái nhân viên

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "academic_rank")
    private AcademicTitle academicRank; //học hàm
    @Column(name = "ethnic_language")
    private Boolean ethnicLanguage;//Tiếng dân tộc

    @Column(name = "physical_education_teacher")
    private Boolean physicalEducationTeacher;//Giáo viên thể dục
    @Column(name = "high_school_education")
    private String highSchoolEducation;//Giáo dục phổ thông
    @Column(name = "forms_of_training")
    private String formsOfTraining;//Hình thức đào tạo
    @Column(name = "training_places")
    private String trainingPlaces;//Nơi đào tạo
    @Column(name = "training_country")
    private String trainingCountry;//Nơi đào tạo
    @Column(name = "qualification")
    private String qualification;//Trình độ chuyên môn

    @Column(name = "certification_score")
    private String certificationScore;//Điểm chứng chỉ
    @Column(name = "year_of_certification")
    private String yearOfCertification;//Năm cấp chứng chỉ
    @Column(name = "year_of_recognition_degree")
    private String yearOfRecognitionDegree;//Năm công nhận học vị

    @Column(name = "year_of_recognition_academicRank")
    private String yearOfRecognitionAcademicRank;//Năm công nhận học hàm

    @Column(name = "note")
    private String note;//Năm cấp chứng chỉ

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Other_language_proficiency")
    private EducationDegree otherLanguageProficiency;//Trình độ ngoại ngữ khác
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "studying")
    private EducationDegree studying;//Đang theo học

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "civil_servant_category_id")
    private CivilServantCategory civilServantCategory;//Ngạch công chức
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "grade_id")
    private CivilServantGrade grade;//Bậc công chức

    @Column(name = "current_residence")
    private String currentResidence;//Năm cấp chứng chỉ

    @Column(name = "position_decision_number")
    private String positionDecisionNumber;//Số quyết định chức vụ

    @Column(name = "wards")
    private String wards;//Nguyên quán

    public Set<TimeSheetDetail> getDetails() {
        return details;
    }

    public void setDetails(Set<TimeSheetDetail> details) {
        this.details = details;
    }

    public Integer getCurrentWorkingStatus() {
        return currentWorkingStatus;
    }

    public void setCurrentWorkingStatus(Integer currentWorkingStatus) {
        this.currentWorkingStatus = currentWorkingStatus;
    }

    public Date getContractDate() {
        return contractDate;
    }

    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    public Date getRecruitmentDate() {
        return recruitmentDate;
    }

    public void setRecruitmentDate(Date recruitmentDate) {
        this.recruitmentDate = recruitmentDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public CivilServantType getCivilServantType() {
        return civilServantType;
    }

    public void setCivilServantType(CivilServantType civilServantType) {
        this.civilServantType = civilServantType;
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

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getSalaryCoefficient() {
        return salaryCoefficient;
    }

    public void setSalaryCoefficient(String salaryCoefficient) {
        this.salaryCoefficient = salaryCoefficient;
    }

    public Date getSalaryStartDate() {
        return salaryStartDate;
    }

    public void setSalaryStartDate(Date salaryStartDate) {
        this.salaryStartDate = salaryStartDate;
    }

    public Set<PositionStaff> getPositions() {
        return positions;
    }

    public void setPositions(Set<PositionStaff> positions) {
        this.positions = positions;
    }

    public Set<StaffLabourAgreement> getAgreements() {
        return agreements;
    }

    public void setAgreements(Set<StaffLabourAgreement> agreements) {
        this.agreements = agreements;
    }

    public Set<StaffSalaryHistory> getSalaryHistory() {
        return salaryHistory;
    }

    public void setSalaryHistory(Set<StaffSalaryHistory> salaryHistory) {
        this.salaryHistory = salaryHistory;
    }

    public Set<StaffSalaryProperty> getStaffSalaryProperties() {
        return staffSalaryProperties;
    }

    public void setStaffSalaryProperties(Set<StaffSalaryProperty> staffSalaryProperties) {
        this.staffSalaryProperties = staffSalaryProperties;
    }

    public Set<StaffEducationHistory> getEducationHistory() {
        return educationHistory;
    }

    public void setEducationHistory(Set<StaffEducationHistory> educationHistory) {
        this.educationHistory = educationHistory;
    }

    public Set<StaffFamilyRelationship> getFamilyRelationships() {
        return familyRelationships;
    }

    public void setFamilyRelationships(Set<StaffFamilyRelationship> familyRelationships) {
        this.familyRelationships = familyRelationships;
    }

    public HRDepartment getDepartment() {
        return department;
    }

    public void setDepartment(HRDepartment department) {
        this.department = department;
    }

    public EducationDegree getEducationDegree() {
        return educationDegree;
    }

    public void setEducationDegree(EducationDegree educationDegree) {
        this.educationDegree = educationDegree;
    }

    public ProfessionalDegree getProfessionalDegree() {
        return professionalDegree;
    }

    public void setProfessionalDegree(ProfessionalDegree professionalDegree) {
        this.professionalDegree = professionalDegree;
    }

    public Integer getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(Integer graduationYear) {
        this.graduationYear = graduationYear;
    }

    public String getPermanentResidence() {
        return permanentResidence;
    }

    public void setPermanentResidence(String permanentResidence) {
        this.permanentResidence = permanentResidence;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
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

    public InformaticDegree getInformaticDegree() {
        return informaticDegree;
    }

    public void setInformaticDegree(InformaticDegree informaticDegree) {
        this.informaticDegree = informaticDegree;
    }

    public PoliticalTheoryLevel getPoliticalTheoryLevel() {
        return politicalTheoryLevel;
    }

    public void setPoliticalTheoryLevel(PoliticalTheoryLevel politicalTheoryLevel) {
        this.politicalTheoryLevel = politicalTheoryLevel;
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

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public String getSalaryLeve() {
        return salaryLeve;
    }

    public void setSalaryLeve(String salaryLeve) {
        this.salaryLeve = salaryLeve;
    }

    public LabourAgreementType getLabourAgreementType() {
        return labourAgreementType;
    }

    public void setLabourAgreementType(LabourAgreementType labourAgreementType) {
        this.labourAgreementType = labourAgreementType;
    }

    public Set<StaffInsuranceHistory> getStafInsuranceHistory() {
        return stafInsuranceHistory;
    }

    public void setStafInsuranceHistory(Set<StaffInsuranceHistory> stafInsuranceHistory) {
        this.stafInsuranceHistory = stafInsuranceHistory;
    }

    public EducationDegree getComputerSkill() {
        return computerSkill;
    }

    public void setComputerSkill(EducationDegree computerSkill) {
        this.computerSkill = computerSkill;
    }

    public EducationDegree getEnglishLevel() {
        return englishLevel;
    }

    public void setEnglishLevel(EducationDegree englishLevel) {
        this.englishLevel = englishLevel;
    }

    public Certificate getEnglishCertificate() {
        return englishCertificate;
    }

    public void setEnglishCertificate(Certificate englishCertificate) {
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

    public EducationDegree getDegree() {
        return degree;
    }

    public void setDegree(EducationDegree degree) {
        this.degree = degree;
    }

    public AcademicTitle getAcademicRank() {
        return academicRank;
    }

    public void setAcademicRank(AcademicTitle academicRank) {
        this.academicRank = academicRank;
    }

    public EmployeeStatus getStatus() {
        return status;
    }

    public void setStatus(EmployeeStatus status) {
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

    public EducationDegree getOtherLanguageProficiency() {
        return otherLanguageProficiency;
    }

    public void setOtherLanguageProficiency(EducationDegree otherLanguageProficiency) {
        this.otherLanguageProficiency = otherLanguageProficiency;
    }

    public EducationDegree getStudying() {
        return studying;
    }

    public void setStudying(EducationDegree studying) {
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

    public CivilServantCategory getCivilServantCategory() {
        return civilServantCategory;
    }

    public void setCivilServantCategory(CivilServantCategory civilServantCategory) {
        this.civilServantCategory = civilServantCategory;
    }

    public CivilServantGrade getGrade() {
        return grade;
    }

    public void setGrade(CivilServantGrade grade) {
        this.grade = grade;
    }

    public Set<PersonCertificate> getPersonCertificate() {
        return personCertificate;
    }

    public void setPersonCertificate(Set<PersonCertificate> personCertificate) {
        this.personCertificate = personCertificate;
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

    public Set<TimeSheetStaff> getTimeSheets() {
        return timeSheets;
    }

    public void setTimeSheets(Set<TimeSheetStaff> timeSheetStaffSet) {
        this.timeSheets = timeSheetStaffSet;
    }

    public Staff() {

    }
}
