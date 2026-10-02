package com.globits.hr.service.impl;

import com.globits.core.domain.*;
import com.globits.core.dto.*;
import com.globits.core.repository.*;
import com.globits.core.service.CountryService;
import com.globits.core.service.EthnicsService;
import com.globits.core.service.ReligionService;
import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.core.utils.SecurityUtils;
import com.globits.hr.domain.*;
import com.globits.hr.dto.*;
import com.globits.hr.dto.function.PositionTitleStaffDto;
import com.globits.hr.dto.loginkeycloak.UserKeyCloackDto;
import com.globits.hr.dto.search.SearchStaffDto;
import com.globits.hr.repository.*;
import com.globits.hr.service.LabourAgreementTypeService;
import com.globits.hr.service.StaffLabourAgreementService;
import com.globits.hr.service.StaffService;
import com.globits.hr.service.UserExtService;
import com.globits.security.domain.Role;
import com.globits.security.domain.User;
import com.globits.security.dto.RoleDto;
import com.globits.security.dto.UserDto;
import com.globits.security.repository.RoleRepository;
import com.globits.security.repository.UserRepository;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class StaffServiceImpl extends GenericServiceImpl<Staff, UUID> implements StaffService {
    private static final Logger logger = LoggerFactory.getLogger(StaffServiceImpl.class);
    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PositionStaffRepository positionStaffRepository;
    @Autowired
    PersonAddressRepository personAddressRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private ReligionRepository religionRepository;

    @Autowired
    private EthnicsRepository ethnicsRepository;
    @Autowired
    AdministrativeUnitRepository administrativeUnitRepository;
    @Autowired
    HRDepartmentRepository hRDepartmentRepository;

    @Autowired
    HRDepartmentRepository departmentRepository;

    @Autowired
    private PositionTitleRepository positionTitleRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ContractTypeRepository contractTypeRepository;

    @Autowired
    StaffLabourAgreementService staffLabourAgreement;

    @Autowired
    LabourAgreementTypeService labourAgreementTypeService;
    @Autowired
    LabourAgreementTypeRepository labourAgreementTypeRepository;
    @Autowired
    PositionRepository positionRepository;
    @Autowired
    StaffFamilyRelationshipRepository staffFamilyRelationshipRepository;
    @Autowired
    StaffLabourAgreementRepository labourAgreementRepository;
    @Autowired
    StaffEducationHistoryRepository staffEducationHistoryRepository;
    @Autowired
    StaffSalaryHistoryRepository staffSalaryHistoryRepository;
    @Autowired
    ProfessionalDegreeRepository professionalDegreeRepository;
    @Autowired
    InformaticDegreeRepository informaticDegreeRepository;
    @Autowired
    PoliticalTheoryLevelRepository politicalTheoryLevelRepository;
    @Autowired
    ProfessionRepository professionRepository;
    @Autowired
    StaffInsuranceHistoryRepository staffInsuranceHistoryRepository;
    @Autowired
    AcademicTitleRepository academicTitleRepository;
    @Autowired
    EducationDegreeRepository educationDegreeRepository;
    @Autowired
    EmployeeStatusRepository employeeStatusRepository;
    @Autowired
    CertificateRepository certificateRepository;
    @Autowired
    CivilServantTypeRepository civilServantTypeRepository;
    @Autowired
    CivilServantCategoryRepository civilServantCategoryRepository;
    @Autowired
    CivilServantGradeRepository civilServantGradeRepository;
    @Autowired
    HrSpecialityRepository hrSpecialityRepository;
    @Autowired
    HrEducationTypeRepository hrEducationTypeRepository;

    @Autowired
    CountryService countryService;
    @Autowired
    EthnicsService ethnicsService;
    @Autowired
    ReligionService religionService;
    @Autowired
    FamilyRelationshipRepository familyRelationshipRepository;
    @Autowired
    PersonCertificateRepository personCertificateRepository;
    @Autowired
    SalaryIncrementTypeRepository salaryIncrementTypeRepository;
    @Autowired
    private UserExtService userExtService;

    @Override
    public StaffDto getStaff(UUID staffId) {
        Optional<Staff> optionalStaff = staffRepository.findById(staffId);
        if (optionalStaff.isPresent()) {
            Staff staff = optionalStaff.get();
            StaffDto retStaff = new StaffDto(staff);
            if (retStaff.getBirthDate() != null) {
                logger.info(new SimpleDateFormat("dd-MM-yyy").format(retStaff.getBirthDate()));
            }
            return retStaff;
        }
        return null;
    }

    @Override
    public Page<StaffDto> findByPageBasicInfo(int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        return staffRepository.findByPageBasicInfo(pageable);
    }

    @Override
    @Transactional
    public StaffDto createStaffAndAccountByCode(StaffDto staffDto, UUID id) {
        if (staffDto == null) {
            return null;
        }
        if (staffDto.getFirstName() != null && !validateStringName(staffDto.getFirstName())) {
            return null;
        }
        if (staffDto.getLastName() != null && !validateStringName(staffDto.getLastName())) {
            return null;
        }
        if (staffDto.getDisplayName() != null && !validateStringName(staffDto.getDisplayName())) {
            return null;
        }
        String currentUserName = "Unknown User";
        LocalDateTime currentDate = new LocalDateTime();
        User user = null;
        boolean isRoleUser = false;
        boolean isRoleAdmin = false;
        boolean isRoleManager = false;

        UserDto userDto = userExtService.getCurrentUserName();
        if (userDto != null && userDto.getRoles() != null && userDto.getRoles().size() > 0) {
            for (RoleDto item : userDto.getRoles()) {
                if (item.getName() != null && "ROLE_ADMIN".equals(item.getName())) {
                    isRoleAdmin = true;
                }
                if (item.getName() != null && "HR_MANAGER".equals(item.getName())) {
                    isRoleManager = true;
                }
                if (item.getName() != null && "HR_USER".equals(item.getName())) {
                    isRoleUser = true;
                }
            }
        }
        if (isRoleAdmin) {
            isRoleUser = false;
        } else {
            if (isRoleManager) {
                isRoleUser = false;
            }
        }

        Staff staff = null;
        if (id != null) {
            if (staffDto.getId() != null && !staffDto.getId().equals(id)) {
                return null;
            }
        }

        if (staffDto.getId() != null && id != null) {
            Optional<Staff> optional = staffRepository.findById(id);
            if (optional.isPresent()) {
                staff = optional.get();
                if (isRoleUser) {
                    if (userDto.getUsername() != null && !userDto.getUsername().equals(staff.getStaffCode())) {
                        return null;
                    }
                }
            }
        }

        if (staff == null && staffDto.getStaffCode() != null) {
            List<Staff> list = staffRepository.getByCode(staffDto.getStaffCode());
            if (list != null && list.size() > 0) {
                staff = list.get(0);
            }
        }
        if (staff == null) {
            staff = new Staff();
        }
        if (staffDto.getStaffCode() != null) {
            staff.setStaffCode(staffDto.getStaffCode());
            user = userRepository.findByUsername(staffDto.getStaffCode());
        }
        if (staffDto.getFirstName() != null)
            staff.setFirstName(normalize(staffDto.getFirstName()));
        if (staffDto.getLastName() != null)
            staff.setLastName(normalize(staffDto.getLastName()));
        if (staffDto.getBirthDate() != null)
            staff.setBirthDate(staffDto.getBirthDate());
        if (staffDto.getBirthPlace() != null)
            staff.setBirthPlace(staffDto.getBirthPlace());
        if (staffDto.getGender() != null)
            staff.setGender(staffDto.getGender());
        if (staffDto.getPhoto() != null)
            staff.setPhoto(staffDto.getPhoto());
        if (staffDto.getDisplayName() != null)
            staff.setDisplayName(normalize(staffDto.getDisplayName()));
        if (staffDto.getPhoneNumber() != null)
            staff.setPhoneNumber(staffDto.getPhoneNumber());
        if (staffDto.getMaritalStatus() != null) {
            staff.setMaritalStatus(staffDto.getMaritalStatus());
        }
        staff.setCurrentWorkingStatus(staffDto.getCurrentWorkingStatus());
        staff.setSalaryCoefficient(staffDto.getSalaryCoefficient());
        staff.setSocialInsuranceNumber(staffDto.getSocialInsuranceNumber());
        staff.setJobTitle(staffDto.getJobTitle());
        staff.setHighestPosition(staffDto.getHighestPosition());
        staff.setDateOfReceivingPosition(staffDto.getDateOfReceivingPosition());
        staff.setPositionDecisionNumber(staffDto.getPositionDecisionNumber());
        staff.setDateOfReceivingAllowance(staffDto.getDateOfReceivingAllowance());
        staff.setProfessionalTitles(staffDto.getProfessionalTitles());
        staff.setAllowanceCoefficient(staffDto.getAllowanceCoefficient());
        staff.setSalaryLeve(staffDto.getSalaryLeve());
        staff.setEthnicLanguage(staffDto.getEthnicLanguage());
        staff.setPhysicalEducationTeacher(staffDto.getPhysicalEducationTeacher());
        staff.setSpecializedName(staffDto.getSpecializedName());
        staff.setFormsOfTraining(staffDto.getFormsOfTraining());
        staff.setTrainingCountry(staffDto.getTrainingCountry());
        staff.setTrainingPlaces(staffDto.getTrainingPlaces());
        staff.setHighSchoolEducation(staffDto.getHighSchoolEducation());
        staff.setQualification(staffDto.getQualification());
        staff.setCertificationScore(staffDto.getCertificationScore());
        staff.setYearOfCertification(staffDto.getYearOfCertification());
        staff.setNote(staffDto.getNote());
        staff.setYearOfRecognitionDegree(staffDto.getYearOfRecognitionDegree());
        staff.setYearOfRecognitionAcademicRank(staffDto.getYearOfRecognitionAcademicRank());
        staff.setImagePath(staffDto.getImagePath());
        staff.setPermanentResidence(staffDto.getPermanentResidence());
        staff.setCurrentResidence(staffDto.getCurrentResidence());
        staff.setWards(staffDto.getWards());
        if (staffDto.getOtherLanguageProficiency() != null) {
            EducationDegree otherLanguageProficiency = null;
            Optional<EducationDegree> optional = educationDegreeRepository
                    .findById(staffDto.getOtherLanguageProficiency().getId());
            if (optional.isPresent()) {
                otherLanguageProficiency = optional.get();
            }
            staff.setOtherLanguageProficiency(otherLanguageProficiency);
        }
        if (staffDto.getStudying() != null) {
            EducationDegree studying = null;
            Optional<EducationDegree> optional = educationDegreeRepository.findById(staffDto.getStudying().getId());
            if (optional.isPresent()) {
                studying = optional.get();
            }
            staff.setStudying(studying);
        }
        if (staffDto.getCivilServantCategory() != null) {
            CivilServantCategory civilServantCategory = null;
            Optional<CivilServantCategory> optional = civilServantCategoryRepository
                    .findById(staffDto.getCivilServantCategory().getId());
            if (optional.isPresent()) {
                civilServantCategory = optional.get();
            }
            staff.setCivilServantCategory(civilServantCategory);
        } else {
            staff.setCivilServantCategory(null);
        }
        if (staffDto.getGrade() != null) {
            CivilServantGrade grade = null;
            Optional<CivilServantGrade> optional = civilServantGradeRepository.findById(staffDto.getGrade().getId());
            if (optional.isPresent()) {
                grade = optional.get();
            }
            staff.setGrade(grade);
        } else {
            staff.setGrade(null);
        }
        if (staffDto.getProfession() != null) {
            Profession profession = null;
            Optional<Profession> optional = professionRepository.findById(staffDto.getProfession().getId());
            if (optional.isPresent()) {
                profession = optional.get();
            }
            staff.setProfession(profession);
        } else {
            staff.setProfession(null);
        }
        if (staffDto.getStatus() != null) {
            EmployeeStatus status = null;
            Optional<EmployeeStatus> optional = employeeStatusRepository.findById(staffDto.getStatus().getId());
            if (optional.isPresent()) {
                status = optional.get();
            }
            staff.setStatus(status);
        } else {
            staff.setStatus(null);
        }

        if (staffDto.getEthnics() != null) {
            Ethnics ethnics = null;
            Optional<Ethnics> optional = ethnicsRepository.findById(staffDto.getEthnics().getId());
            if (optional.isPresent()) {
                ethnics = optional.get();
            }
            staff.setEthnics(ethnics);
        } else {
            staff.setEthnics(null);
        }
        if (staffDto.getComputerSkill() != null) {
            EducationDegree computerSkill = null;
            Optional<EducationDegree> optional = educationDegreeRepository
                    .findById(staffDto.getComputerSkill().getId());
            if (optional.isPresent()) {
                computerSkill = optional.get();
            }
            staff.setComputerSkill(computerSkill);
        } else {
            staff.setComputerSkill(null);
        }
        if (staffDto.getEnglishLevel() != null) {
            EducationDegree englishLevel = null;
            Optional<EducationDegree> optional = educationDegreeRepository.findById(staffDto.getEnglishLevel().getId());
            if (optional.isPresent()) {
                englishLevel = optional.get();
            }
            staff.setEnglishLevel(englishLevel);
        } else {
            staff.setEnglishLevel(null);
        }
        if (staffDto.getEnglishCertificate() != null) {
            Certificate englishCertificate = null;
            Optional<Certificate> optional = certificateRepository.findById(staffDto.getEnglishCertificate().getId());
            if (optional.isPresent()) {
                englishCertificate = optional.get();
            }
            staff.setEnglishCertificate(englishCertificate);
        } else {
            staff.setEnglishCertificate(null);
        }
        if (staffDto.getAcademicRank() != null) {
            AcademicTitle academicRank = null;
            Optional<AcademicTitle> optional = academicTitleRepository.findById(staffDto.getAcademicRank().getId());
            if (optional.isPresent()) {
                academicRank = optional.get();
            }
            staff.setAcademicRank(academicRank);
        } else {
            staff.setAcademicRank(null);
        }
        if (staffDto.getDegree() != null) {
            EducationDegree degree = null;
            Optional<EducationDegree> optional = educationDegreeRepository.findById(staffDto.getDegree().getId());
            if (optional.isPresent()) {
                degree = optional.get();
            }
            staff.setDegree(degree);
        } else {
            staff.setDegree(null);
        }

        if (staffDto.getNationality() != null) {
            Country nationality = null;
            Optional<Country> optional = countryRepository.findById(staffDto.getNationality().getId());
            if (optional.isPresent()) {
                nationality = optional.get();
            }
            staff.setNationality(nationality);
        } else {
            staff.setNationality(null);
        }
        if (staffDto.getEmail() != null) {
            staff.setEmail(staffDto.getEmail());
        } else {
            staff.setEmail(null);
        }

        if (staffDto.getDepartment() != null) {
            if (staffDto.getDepartment().getId() != null) {
                HRDepartment department = null;
                Optional<HRDepartment> optional = hRDepartmentRepository.findById(staffDto.getDepartment().getId());
                if (optional.isPresent()) {
                    department = optional.get();
                }
                staff.setDepartment(department);
            }
        } else {
            staff.setDepartment(null);
        }
        if (staffDto.getNativeVillage() != null) {
            AdministrativeUnit nativeVillage = null;
            Optional<AdministrativeUnit> optional = administrativeUnitRepository
                    .findById(staffDto.getNativeVillage().getId());
            if (optional.isPresent()) {
                nativeVillage = optional.get();
            }
            staff.setNativeVillage(nativeVillage);
        } else {
            staff.setNativeVillage(null);
        }

        if (staffDto.getReligion() != null) {
            if (staffDto.getReligion().getId() != null) {
                Religion religion = null;
                Optional<Religion> optional = religionRepository.findById(staffDto.getReligion().getId());
                if (optional.isPresent()) {
                    religion = optional.get();
                }
                staff.setReligion(religion);
            }
        } else {
            staff.setReligion(null);
        }

        if (staffDto.getProfessionalDegree() != null) {
            ProfessionalDegree professionalDegree = null;
            Optional<ProfessionalDegree> optional = professionalDegreeRepository
                    .findById(staffDto.getProfessionalDegree().getId());
            if (optional.isPresent()) {
                professionalDegree = optional.get();
            }
            staff.setProfessionalDegree(professionalDegree);
        } else {
            staff.setProfessionalDegree(null);
        }

        if (staffDto.getInformaticDegree() != null) {
            InformaticDegree informaticDegree = null;
            Optional<InformaticDegree> optional = informaticDegreeRepository
                    .findById(staffDto.getInformaticDegree().getId());
            if (optional.isPresent()) {
                informaticDegree = optional.get();
            }
            staff.setInformaticDegree(informaticDegree);
        } else {
            staff.setInformaticDegree(null);
        }

        if (staffDto.getPoliticalTheoryLevel() != null) {
            PoliticalTheoryLevel politicalTheoryLevel = null;
            Optional<PoliticalTheoryLevel> optional = politicalTheoryLevelRepository
                    .findById(staffDto.getPoliticalTheoryLevel().getId());
            if (optional.isPresent()) {
                politicalTheoryLevel = optional.get();
            }
            staff.setPoliticalTheoryLevel(politicalTheoryLevel);
        } else {
            staff.setPoliticalTheoryLevel(null);
        }

        if (staffDto.getLabourAgreementType() != null && staffDto.getLabourAgreementType().getId() != null) {

            LabourAgreementType labourAgreementType = null;
            Optional<LabourAgreementType> optional = labourAgreementTypeRepository
                    .findById(staffDto.getLabourAgreementType().getId());
            if (optional.isPresent()) {
                labourAgreementType = optional.get();
            }
            staff.setLabourAgreementType(labourAgreementType);
        } else {
            staff.setLabourAgreementType(null);
        }

        staff.setIdNumber(staffDto.getIdNumber());
        staff.setIdNumberIssueBy(staffDto.getIdNumberIssueBy());
        staff.setIdNumberIssueDate(staffDto.getIdNumberIssueDate());
        if (staffDto.getMaritalStatus() != null) {
            staff.setMaritalStatus(staffDto.getMaritalStatus());
        } else {
            staff.setMaritalStatus(null);
        }
        if (staffDto.getContractDate() != null) {
            staff.setContractDate(staffDto.getContractDate());
        } else {
            staff.setContractDate(null);
        }
        if (staffDto.getJobTitle() != null) {
            staff.setJobTitle(staffDto.getJobTitle());
        } else {
            staff.setJobTitle(null);
        }
        if (staffDto.getCivilServantType() != null) {
            CivilServantType type = null;
            Optional<CivilServantType> optional = civilServantTypeRepository
                    .findById(staffDto.getCivilServantType().getId());
            if (optional.isPresent()) {
                type = optional.get();
            }
            staff.setCivilServantType(type);
        } else {
            staff.setCivilServantType(null);
        }
        staff.setStartDate(staffDto.getStartDate());
        staff.setRecruitmentDate(staffDto.getRecruitmentDate());
        staff.setSalaryStartDate(staffDto.getSalaryStartDate());
        staff.setGraduationYear(staffDto.getGraduationYear());
        staff.setForeignLanguageName(staffDto.getForeignLanguageName());

        if (user == null) {
            user = new User();
            if (staffDto.getStaffCode() != null) {
                user.setUsername(staffDto.getStaffCode());
                String password = "123456";
                user.setPassword(password);
                user.setCreateDate(currentDate);
                user.setCreatedBy(currentUserName);
                if (staffDto.getEmail() != null) {
                    user.setEmail(staffDto.getEmail());
                }
                HashSet<Role> roles = new HashSet<>();
                Role role;
                role = roleRepository.findByName("HR_USER");
                if (role != null) {
                    roles.add(role);
                }
                user.getRoles().clear();
                user.getRoles().addAll(roles);
                user.setPerson(staff);
                UserDto dto = new UserDto(user);
                ResponseEntity<UserKeyCloackDto> response = userExtService.creatUserKeyCloak(dto);
                logger.info("Status code value of ResponseEntity UserKeyClockDto: {}", response.getStatusCodeValue());
                password = SecurityUtils.getHashPassword("123456");
                user.setPassword(password);
                user = userRepository.save(user);
                staff.setUser(user);
            }
        }
        if (staffDto.getAddress() != null && staffDto.getAddress().size() > 0) {
            HashSet<PersonAddress> temp = new HashSet<>();
            for (PersonAddressDto paDto : staffDto.getAddress()) {
                PersonAddress pa = null;
                if (paDto != null && paDto.getId() != null) {
                    Optional<PersonAddress> optional = personAddressRepository.findById(paDto.getId());
                    if (optional.isPresent()) {
                        pa = optional.get();
                    }
                }
                if (pa == null) {
                    pa = new PersonAddress();
                }
                if (paDto != null) {
                    pa.setType(paDto.getType());
                    pa.setAddress(paDto.getAddress());
                }
                pa.setPerson(staff);
                temp.add(pa);
            }
            if (staff.getAddress() == null) {
                staff.setAddress(new HashSet<>());
            }
            staff.getAddress().clear();
            staff.getAddress().addAll(temp);
        } else {
            if (staff.getAddress() != null) {
                staff.getAddress().clear();
            }
        }
        if (staffDto.getPositions() != null && staffDto.getPositions().size() > 0) {
            HashSet<PositionStaff> positions = new HashSet<>();
            for (PositionStaffDto psDto : staffDto.getPositions()) {
                PositionStaff newPs = null;
                if (psDto.getId() != null) {
                    Optional<PositionStaff> optional = positionStaffRepository.findById(psDto.getId());
                    if (optional.isPresent()) {
                        newPs = optional.get();
                    }
                }
                if (newPs == null) {
                    newPs = new PositionStaff();
                }
                newPs.setCurrent(psDto.isCurrent());
                newPs.setMainPosition(psDto.getMainPosition());
                if (psDto.getPosition() != null && psDto.getPosition().getId() != null) {
                    PositionTitle positionTitle = null;
                    Optional<PositionTitle> optional = positionTitleRepository.findById(psDto.getPosition().getId());
                    if (optional.isPresent()) {
                        positionTitle = optional.get();
                    }
                    newPs.setPosition(positionTitle);
                }
                if (psDto.getDepartment() != null && psDto.getDepartment().getId() != null) {
                    HRDepartment department = null;
                    Optional<HRDepartment> optional = departmentRepository.findById(psDto.getDepartment().getId());
                    if (optional.isPresent()) {
                        department = optional.get();
                    }
                    newPs.setHrDepartment(department);
                }

                newPs.setFromDate(psDto.getFromDate());
                newPs.setToDate(psDto.getToDate());
                newPs.setStaff(staff);
                positions.add(newPs);
            }
            if (staffDto.getPositions() != null) {
                staff.getPositions().clear();
                staff.getPositions().addAll(positions);
            } else {
                staff.setPositions(positions);
            }
        } else if (staffDto.getPositions() != null) {
            staff.getPositions().clear();
        }

        Set<StaffLabourAgreement> listAgreements = new HashSet<>();
        if (staffDto.getAgreements() != null && staffDto.getAgreements().size() > 0) {
            for (StaffLabourAgreementDto agr : staffDto.getAgreements()) {
                if (agr != null) {
                    StaffLabourAgreement staffLabourAgreement = null;
                    if (agr.getId() != null) {
                        Optional<StaffLabourAgreement> optional = labourAgreementRepository.findById(agr.getId());
                        if (optional.isPresent()) {
                            staffLabourAgreement = optional.get();
                        }
                    }
                    if (staffLabourAgreement == null) {
                        staffLabourAgreement = new StaffLabourAgreement();
                    }
                    staffLabourAgreement.setStartDate(agr.getStartDate());
                    staffLabourAgreement.setEndDate(agr.getEndDate());
                    staffLabourAgreement.setSignedDate(agr.getSignedDate());
                    staffLabourAgreement.setAgreementStatus(agr.getAgreementStatus());

                    if (agr.getLabourAgreementType() != null && agr.getLabourAgreementType().getId() != null) {
                        LabourAgreementType type = null;
                        Optional<LabourAgreementType> optional = labourAgreementTypeRepository
                                .findById(agr.getLabourAgreementType().getId());
                        if (optional.isPresent()) {
                            type = optional.get();
                        }
                        staffLabourAgreement.setLabourAgreementType(type);
                    }
                    staffLabourAgreement.setStaff(staff);
                    listAgreements.add(staffLabourAgreement);
                }
            }
        }
        if (listAgreements.size() > 0) {
            if (staff.getAgreements() == null) {
                staff.setAgreements(listAgreements);
            } else {
                staff.getAgreements().clear();
                staff.getAgreements().addAll(listAgreements);
            }
        } else {
            if (staff.getAgreements() != null) {
                staff.getAgreements().clear();
            }
        }

        Set<StaffFamilyRelationship> listFamilyRelationship = new HashSet<>();
        if (staffDto.getFamilyRelationships() != null && staffDto.getFamilyRelationships().size() > 0) {
            for (StaffFamilyRelationshipDto staffFamilyRelationshipDto : staffDto.getFamilyRelationships()) {
                if (staffFamilyRelationshipDto != null) {
                    StaffFamilyRelationship staffFamilyRelationship = null;
                    if (staffFamilyRelationshipDto.getId() != null) {
                        Optional<StaffFamilyRelationship> optional = staffFamilyRelationshipRepository
                                .findById(staffFamilyRelationshipDto.getId());
                        if (optional.isPresent()) {
                            staffFamilyRelationship = optional.get();
                        }
                    }
                    if (staffFamilyRelationship == null) {
                        staffFamilyRelationship = new StaffFamilyRelationship();
                    }
                    if (staffFamilyRelationshipDto.getFamilyRelationship() != null
                            && staffFamilyRelationshipDto.getFamilyRelationship().getId() != null) {
                        FamilyRelationship familyRelationship = null;
                        Optional<FamilyRelationship> optional = familyRelationshipRepository
                                .findById(staffFamilyRelationshipDto.getFamilyRelationship().getId());
                        if (optional.isPresent()) {
                            familyRelationship = optional.get();
                        }
                        staffFamilyRelationship.setFamilyRelationship(familyRelationship);

                    }
                    staffFamilyRelationship.setDescription(staffFamilyRelationshipDto.getDescription());
                    staffFamilyRelationship.setAddress(staffFamilyRelationshipDto.getAddress());
                    staffFamilyRelationship.setProfession(staffFamilyRelationshipDto.getProfession());
                    staffFamilyRelationship.setFullName(staffFamilyRelationshipDto.getFullName());
                    staffFamilyRelationship.setBirthDate(staffFamilyRelationshipDto.getBirthDate());
                    staffFamilyRelationship.setStaff(staff);
                    listFamilyRelationship.add(staffFamilyRelationship);
                }
            }
        }
        if (listFamilyRelationship.size() > 0) {
            if (staff.getFamilyRelationships() == null) {
                staff.setFamilyRelationships(listFamilyRelationship);
            } else {
                staff.getFamilyRelationships().clear();
                staff.getFamilyRelationships().addAll(listFamilyRelationship);
            }
        } else {
            if (staff.getFamilyRelationships() != null) {
                staff.getFamilyRelationships().clear();
            }
        }

        Set<StaffEducationHistory> ListEducationHistory = new HashSet<>();
        if (staffDto.getEducationHistory() != null && staffDto.getEducationHistory().size() > 0) {
            for (StaffEducationHistoryDto staffEducationHistoryDto : staffDto.getEducationHistory()) {
                if (staffEducationHistoryDto != null) {
                    StaffEducationHistory staffEducationHistory = null;
                    if (staffEducationHistoryDto.getId() != null) {
                        Optional<StaffEducationHistory> optional = staffEducationHistoryRepository
                                .findById(staffEducationHistoryDto.getId());
                        if (optional.isPresent()) {
                            staffEducationHistory = optional.get();
                        }
                    }
                    if (staffEducationHistory == null) {
                        staffEducationHistory = new StaffEducationHistory();
                    }
                    staffEducationHistory.setStartDate(staffEducationHistoryDto.getStartDate());
                    staffEducationHistory.setEndDate(staffEducationHistoryDto.getEndDate());
                    staffEducationHistory.setStatus(staffEducationHistoryDto.getStatus());
                    staffEducationHistory.setDescription(staffEducationHistoryDto.getDescription());
                    staffEducationHistory.setSchoolName(staffEducationHistoryDto.getSchoolName());
                    staffEducationHistory.setStaff(staff);
                    ListEducationHistory.add(staffEducationHistory);
                }
            }
        }
        if (ListEducationHistory.size() > 0) {
            if (staff.getEducationHistory() == null) {
                staff.setEducationHistory(ListEducationHistory);
            } else {
                staff.getEducationHistory().clear();
                staff.getEducationHistory().addAll(ListEducationHistory);
            }
        } else {
            if (staff.getEducationHistory() != null) {
                staff.getEducationHistory().clear();
            }
        }

        Set<StaffSalaryHistory> ListStaffSalaryHistory = new HashSet<>();
        if (staffDto.getSalaryHistory() != null && staffDto.getSalaryHistory().size() > 0) {
            for (StaffSalaryHistoryDto staffSalaryHistoryDto : staffDto.getSalaryHistory()) {
                if (staffSalaryHistoryDto != null) {
                    StaffSalaryHistory staffSalaryHistory = null;
                    if (staffSalaryHistoryDto.getId() != null) {
                        Optional<StaffSalaryHistory> optional = staffSalaryHistoryRepository
                                .findById(staffSalaryHistoryDto.getId());
                        if (optional.isPresent()) {
                            staffSalaryHistory = optional.get();
                        }
                    }
                    if (staffSalaryHistory == null) {
                        staffSalaryHistory = new StaffSalaryHistory();
                    }
                    staffSalaryHistory.setDecisionCode(staffSalaryHistoryDto.getDecisionCode());
                    staffSalaryHistory.setDecisionDate(staffSalaryHistoryDto.getDecisionDate());
                    staffSalaryHistory.setCoefficient(staffSalaryHistoryDto.getCoefficient());
                    staffSalaryHistory.setCoefficientOverLevel(staffSalaryHistoryDto.getCoefficientOverLevel());
                    staffSalaryHistory.setPercentage(staffSalaryHistoryDto.getPercentage());
                    staffSalaryHistory.setStaffTypeCode(staffSalaryHistoryDto.getStaffTypeCode());
                    if (staffSalaryHistoryDto.getSalaryIncrementType() != null) {
                        SalaryIncrementType salaryType = null;
                        Optional<SalaryIncrementType> optional = salaryIncrementTypeRepository
                                .findById(staffSalaryHistoryDto.getSalaryIncrementType().getId());
                        if (optional.isPresent()) {
                            salaryType = optional.get();
                        }
                        staffSalaryHistory.setSalaryIncrementType(salaryType);

                    }

                    staffSalaryHistory.setStaff(staff);
                    ListStaffSalaryHistory.add(staffSalaryHistory);
                }
            }
        }
        if (ListStaffSalaryHistory.size() > 0) {
            if (staff.getSalaryHistory() == null) {
                staff.setSalaryHistory(ListStaffSalaryHistory);
            } else {
                staff.getSalaryHistory().clear();
                staff.getSalaryHistory().addAll(ListStaffSalaryHistory);
            }
        } else {
            if (staff.getSalaryHistory() != null) {
                staff.getSalaryHistory().clear();
            }
        }

        Set<StaffInsuranceHistory> ListStaffInsuranceHistory = new HashSet<>();
        if (staffDto.getStafInsuranceHistory() != null && staffDto.getStafInsuranceHistory().size() > 0) {
            for (StaffInsuranceHistoryDto staffInsuranceHistoryDto : staffDto.getStafInsuranceHistory()) {
                if (staffInsuranceHistoryDto != null) {
                    StaffInsuranceHistory staffInsuranceHistory = null;
                    if (staffInsuranceHistoryDto.getId() != null) {
                        Optional<StaffInsuranceHistory> optional = staffInsuranceHistoryRepository
                                .findById(staffInsuranceHistoryDto.getId());
                        if (optional.isPresent()) {
                            staffInsuranceHistory = optional.get();
                        }
                    }
                    if (staffInsuranceHistory == null) {
                        staffInsuranceHistory = new StaffInsuranceHistory();
                    }
                    staffInsuranceHistory.setStartDate(staffInsuranceHistoryDto.getStartDate());
                    staffInsuranceHistory.setEndDate(staffInsuranceHistoryDto.getEndDate());
                    staffInsuranceHistory.setNote(staffInsuranceHistoryDto.getNote());
                    staffInsuranceHistory.setSalaryCofficient(staffInsuranceHistoryDto.getSalaryCofficient());
                    staffInsuranceHistory.setInsuranceSalary(staffInsuranceHistoryDto.getInsuranceSalary());
                    staffInsuranceHistory.setStaffPercentage(staffInsuranceHistoryDto.getStaffPercentage());
                    staffInsuranceHistory.setOrgPercentage(staffInsuranceHistoryDto.getOrgPercentage());
                    staffInsuranceHistory.setStaffInsuranceAmount(staffInsuranceHistoryDto.getStaffInsuranceAmount());
                    staffInsuranceHistory.setOrgInsuranceAmount(staffInsuranceHistoryDto.getOrgInsuranceAmount());
                    staffInsuranceHistory.setStaff(staff);
                    ListStaffInsuranceHistory.add(staffInsuranceHistory);
                }
            }
        }
        if (ListStaffInsuranceHistory.size() > 0) {
            if (staff.getStafInsuranceHistory() == null) {
                staff.setStafInsuranceHistory(ListStaffInsuranceHistory);
            } else {
                staff.getStafInsuranceHistory().clear();
                staff.getStafInsuranceHistory().addAll(ListStaffInsuranceHistory);
            }
        } else {
            if (staff.getStafInsuranceHistory() != null) {
                staff.getStafInsuranceHistory().clear();
            }
        }

        Set<PersonCertificate> personCertificates = new HashSet<>();
        if (staffDto.getPersonCertificate() != null && staffDto.getPersonCertificate().size() > 0) {
            for (PersonCertificateDto personCertificateDto : staffDto.getPersonCertificate()) {
                if (personCertificateDto != null) {
                    PersonCertificate personCertificate = null;
                    if (personCertificateDto.getId() != null) {
                        Optional<PersonCertificate> optional = personCertificateRepository
                                .findById(personCertificateDto.getId());
                        if (optional.isPresent()) {
                            personCertificate = optional.get();
                        }
                    }
                    if (personCertificate == null) {
                        personCertificate = new PersonCertificate();
                    }
                    if (personCertificateDto.getCertificate() != null
                            && personCertificateDto.getCertificate().getId() != null) {
                        Certificate certificate = null;
                        Optional<Certificate> optional = certificateRepository
                                .findById(personCertificateDto.getCertificate().getId());
                        if (optional.isPresent()) {
                            certificate = optional.get();
                        }
                        personCertificate.setCertificate(certificate);
                    }
                    personCertificate.setLevel(personCertificateDto.getLevel());
                    personCertificate.setIssueDate(personCertificateDto.getIssueDate());
                    personCertificate.setName(personCertificateDto.getName());
                    personCertificate.setPerson(staff);
                    personCertificates.add(personCertificate);
                }
            }
        }
        if (personCertificates.size() > 0) {
            if (staff.getPersonCertificate() == null) {
                staff.setPersonCertificate(personCertificates);
            } else {
                staff.getPersonCertificate().clear();
                staff.getPersonCertificate().addAll(personCertificates);
            }
        } else {
            if (staff.getPersonCertificate() != null) {
                staff.getPersonCertificate().clear();
            }
        }
        staff = staffRepository.save(staff);
        return new StaffDto(staff);
    }

    private boolean validateStringName(String strName) {
        for (int i = 0; i < strName.length(); i++) {
            if (!Character.isLetter(strName.charAt(i)) && !Character.isWhitespace(strName.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private String normalize(String strName) {
        strName = strName.trim();
        while (strName.contains("  ")) {
            strName = strName.replace("  ", " ");
        }
        String[] arrStr = strName.split(" ");
        StringBuilder kq = new StringBuilder();
        for (String s : arrStr) {
            kq.append(s.substring(0, 1).toUpperCase());
            kq.append(s.substring(1).toLowerCase());
            kq.append(" ");
        }
        return kq.toString().trim();
    }

    @Override
    public StaffDto createStaffSimple(StaffDto staffDto) {
        if (staffDto == null) {
            return null;
        }
        String currentUserName = "Unknown User";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LocalDateTime currentDate = new LocalDateTime();
        User modifiedUser;
        if (authentication != null) {
            modifiedUser = (User) authentication.getPrincipal();
            currentUserName = modifiedUser.getUsername();
        }
        User user = new User();
        Staff staff = null;
        Date date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy");
        String dateInString = "01-01-1900";
        try {
            date = formatter.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (staffDto.getId() != null) {
            Optional<Staff> optional = staffRepository.findById(staffDto.getId());
            if (optional.isPresent()) {
                staff = optional.get();
            }
        }

        if (staff == null && staffDto.getStaffCode() != null) {
            List<Staff> list = staffRepository.getByCode(staffDto.getStaffCode());
            if (list != null && list.size() > 0) {
                staff = list.get(0);
            }
        }
        if (staff == null) {
            staff = new Staff();
        }
        if (staffDto.getStaffCode() != null)
            staff.setStaffCode(staffDto.getStaffCode());
        if (staffDto.getFirstName() != null)
            staff.setFirstName(staffDto.getFirstName());
        if (staffDto.getLastName() != null)
            staff.setLastName(staffDto.getLastName());
        if (staffDto.getBirthDate() != null && !staffDto.getBirthDate().before(date)) {
            staff.setBirthDate(staffDto.getBirthDate());
        }
        if (staffDto.getBirthPlace() != null)
            staff.setBirthPlace(staffDto.getBirthPlace());
        if (staffDto.getGender() != null)
            staff.setGender(staffDto.getGender());
        if (staffDto.getPhoto() != null)
            staff.setPhoto(staffDto.getPhoto());
        if (staffDto.getDisplayName() != null)
            staff.setDisplayName(staffDto.getDisplayName());
        if (staffDto.getPhoneNumber() != null)
            staff.setPhoneNumber(staffDto.getPhoneNumber());
        if (staffDto.getMaritalStatus() != null) {
            staff.setMaritalStatus(staffDto.getMaritalStatus());
        }
        staff.setCurrentWorkingStatus(staffDto.getCurrentWorkingStatus());
        staff.setSalaryCoefficient(staffDto.getSalaryCoefficient());
        staff.setSocialInsuranceNumber(staffDto.getSocialInsuranceNumber());
        staff.setJobTitle(staffDto.getJobTitle());
        staff.setHighestPosition(staffDto.getHighestPosition());
        staff.setDateOfReceivingPosition(staffDto.getDateOfReceivingPosition());
        staff.setDateOfReceivingAllowance(staffDto.getDateOfReceivingAllowance());
        staff.setProfessionalTitles(staffDto.getProfessionalTitles());
        staff.setAllowanceCoefficient(staffDto.getAllowanceCoefficient());
        staff.setSalaryLeve(staffDto.getSalaryLeve());
        staff.setEthnicLanguage(staffDto.getEthnicLanguage());
        staff.setPhysicalEducationTeacher(staffDto.getPhysicalEducationTeacher());
        staff.setSpecializedName(staffDto.getSpecializedName());
        staff.setFormsOfTraining(staffDto.getFormsOfTraining());
        staff.setTrainingCountry(staffDto.getTrainingCountry());
        staff.setTrainingPlaces(staffDto.getTrainingPlaces());
        staff.setHighSchoolEducation(staffDto.getHighSchoolEducation());
        staff.setQualification(staffDto.getQualification());
        staff.setCertificationScore(staffDto.getCertificationScore());
        staff.setYearOfCertification(staffDto.getYearOfCertification());
        staff.setNote(staffDto.getNote());
        staff.setYearOfRecognitionDegree(staffDto.getYearOfRecognitionDegree());
        staff.setYearOfRecognitionAcademicRank(staffDto.getYearOfRecognitionAcademicRank());
        if (staffDto.getOtherLanguageProficiency() != null) {
            EducationDegree otherLanguageProficiency = null;
            Optional<EducationDegree> optional = educationDegreeRepository
                    .findById(staffDto.getOtherLanguageProficiency().getId());
            if (optional.isPresent()) {
                otherLanguageProficiency = optional.get();
            }
            staff.setOtherLanguageProficiency(otherLanguageProficiency);
        }
        if (staffDto.getStudying() != null) {
            EducationDegree studying = null;
            Optional<EducationDegree> optional = educationDegreeRepository.findById(staffDto.getStudying().getId());
            if (optional.isPresent()) {
                studying = optional.get();
            }
            staff.setStudying(studying);
        }
        if (staffDto.getCivilServantCategory() != null) {
            CivilServantCategory civilServantCategory = null;
            Optional<CivilServantCategory> optional = civilServantCategoryRepository
                    .findById(staffDto.getCivilServantCategory().getId());
            if (optional.isPresent()) {
                civilServantCategory = optional.get();
            }
            staff.setCivilServantCategory(civilServantCategory);
        }
        if (staffDto.getGrade() != null) {
            CivilServantGrade grade = null;
            Optional<CivilServantGrade> optional = civilServantGradeRepository.findById(staffDto.getGrade().getId());
            if (optional.isPresent()) {
                grade = optional.get();
            }
            staff.setGrade(grade);
        }
        if (staffDto.getProfession() != null) {
            Profession profession = null;
            Optional<Profession> optional = professionRepository.findById(staffDto.getProfession().getId());
            if (optional.isPresent()) {
                profession = optional.get();
            }
            staff.setProfession(profession);
        }
        if (staffDto.getStatus() != null) {
            EmployeeStatus status = null;
            Optional<EmployeeStatus> optional = employeeStatusRepository.findById(staffDto.getStatus().getId());
            if (optional.isPresent()) {
                status = optional.get();
            }
            staff.setStatus(status);
        }

        if (staffDto.getEthnics() != null) {
            Ethnics ethnics = null;
            Optional<Ethnics> optional = ethnicsRepository.findById(staffDto.getEthnics().getId());
            if (optional.isPresent()) {
                ethnics = optional.get();
            }
            staff.setEthnics(ethnics);
        }
        if (staffDto.getComputerSkill() != null) {
            EducationDegree computerSkill = null;
            Optional<EducationDegree> optional = educationDegreeRepository
                    .findById(staffDto.getComputerSkill().getId());
            if (optional.isPresent()) {
                computerSkill = optional.get();
            }
            staff.setComputerSkill(computerSkill);
        }
        if (staffDto.getEnglishLevel() != null) {
            EducationDegree englishLevel = null;
            Optional<EducationDegree> optional = educationDegreeRepository.findById(staffDto.getEnglishLevel().getId());
            if (optional.isPresent()) {
                englishLevel = optional.get();
            }
            staff.setEnglishLevel(englishLevel);
        }
        if (staffDto.getEnglishCertificate() != null) {
            Certificate englishCertificate = null;
            Optional<Certificate> optional = certificateRepository.findById(staffDto.getEnglishCertificate().getId());
            if (optional.isPresent()) {
                englishCertificate = optional.get();
            }
            staff.setEnglishCertificate(englishCertificate);
        }
        if (staffDto.getAcademicRank() != null) {
            AcademicTitle academicRank = null;
            Optional<AcademicTitle> optional = academicTitleRepository.findById(staffDto.getAcademicRank().getId());
            if (optional.isPresent()) {
                academicRank = optional.get();
            }
            staff.setAcademicRank(academicRank);
        }
        if (staffDto.getDegree() != null) {
            EducationDegree degree = null;
            Optional<EducationDegree> optional = educationDegreeRepository.findById(staffDto.getDegree().getId());
            if (optional.isPresent()) {
                degree = optional.get();
            }
            staff.setDegree(degree);
        }

        if (staffDto.getNationality() != null) {
            Country nationality = null;
            Optional<Country> optional = countryRepository.findById(staffDto.getNationality().getId());
            if (optional.isPresent()) {
                nationality = optional.get();
            }
            staff.setNationality(nationality);
        }
        if (staffDto.getEmail() != null)
            staff.setEmail(staffDto.getEmail());

        if (staffDto.getDepartment() != null && staffDto.getDepartment().getId() != null) {
            HRDepartment department = null;
            Optional<HRDepartment> optional = hRDepartmentRepository.findById(staffDto.getDepartment().getId());
            if (optional.isPresent()) {
                department = optional.get();
            }
            staff.setDepartment(department);
        }
        if (staffDto.getNativeVillage() != null) {
            AdministrativeUnit nativeVillage = null;
            Optional<AdministrativeUnit> optional = administrativeUnitRepository
                    .findById(staffDto.getNativeVillage().getId());
            if (optional.isPresent()) {
                nativeVillage = optional.get();
            }
            staff.setNativeVillage(nativeVillage);
        }

        if (staffDto.getReligion() != null) {
            Religion religion = null;
            Optional<Religion> optional = religionRepository.findById(staffDto.getReligion().getId());
            if (optional.isPresent()) {
                religion = optional.get();
            }
            staff.setReligion(religion);
        }

        if (staffDto.getProfessionalDegree() != null) {
            ProfessionalDegree professionalDegree = null;
            Optional<ProfessionalDegree> optional = professionalDegreeRepository
                    .findById(staffDto.getProfessionalDegree().getId());
            if (optional.isPresent()) {
                professionalDegree = optional.get();
            }
            staff.setProfessionalDegree(professionalDegree);
        }

        if (staffDto.getInformaticDegree() != null) {
            InformaticDegree informaticDegree = null;
            Optional<InformaticDegree> optional = informaticDegreeRepository
                    .findById(staffDto.getInformaticDegree().getId());
            if (optional.isPresent()) {
                informaticDegree = optional.get();
            }
            staff.setInformaticDegree(informaticDegree);
        }

        if (staffDto.getPoliticalTheoryLevel() != null) {
            PoliticalTheoryLevel politicalTheoryLevel = null;
            Optional<PoliticalTheoryLevel> optional = politicalTheoryLevelRepository
                    .findById(staffDto.getPoliticalTheoryLevel().getId());
            if (optional.isPresent()) {
                politicalTheoryLevel = optional.get();
            }
            staff.setPoliticalTheoryLevel(politicalTheoryLevel);
        }

        if (staffDto.getLabourAgreementType() != null) {
            LabourAgreementType labourAgreementType = null;
            Optional<LabourAgreementType> optional = labourAgreementTypeRepository
                    .findById(staffDto.getLabourAgreementType().getId());
            if (optional.isPresent()) {
                labourAgreementType = optional.get();
            }
            staff.setLabourAgreementType(labourAgreementType);
        }

        if (staffDto.getIdNumber() != null)
            staff.setIdNumber(staffDto.getIdNumber());
        if (staffDto.getIdNumberIssueBy() != null)
            staff.setIdNumberIssueBy(staffDto.getIdNumberIssueBy());
        if (staffDto.getIdNumberIssueDate() != null)
            staff.setIdNumberIssueDate(staffDto.getIdNumberIssueDate());
        if (staffDto.getMaritalStatus() != null) {
            staff.setMaritalStatus(staffDto.getMaritalStatus());
        }
        if (staffDto.getContractDate() != null) {
            staff.setContractDate(staffDto.getContractDate());
        }
        if (staffDto.getJobTitle() != null) {
            staff.setJobTitle(staffDto.getJobTitle());
        }
        if (staffDto.getCivilServantType() != null) {
            CivilServantType type = null;
            Optional<CivilServantType> optional = civilServantTypeRepository
                    .findById(staffDto.getCivilServantType().getId());
            if (optional.isPresent()) {
                type = optional.get();
            }
            staff.setCivilServantType(type);
        }
        staff.setStartDate(staffDto.getStartDate());
        staff.setRecruitmentDate(staffDto.getRecruitmentDate());
        staff.setSalaryStartDate(staffDto.getSalaryStartDate());
        staff.setGraduationYear(staffDto.getGraduationYear());
        staff.setForeignLanguageName(staffDto.getForeignLanguageName());

        if (staffDto.getUser() != null) {
            user.setUsername(staffDto.getUser().getUsername());
            String password = SecurityUtils.getHashPassword(staffDto.getUser().getPassword());
            user.setPassword(password);
            user.setPassword(password);
            user.setCreateDate(currentDate);
            user.setCreatedBy(currentUserName);
            user.setEmail(staffDto.getUser().getEmail());
            if (staffDto.getUser() != null && staffDto.getUser().getRoles() != null) {
                HashSet<Role> roles = new HashSet<>();
                if (user.getRoles() == null) {
                    user.setRoles(new HashSet<>());
                }
                for (RoleDto rDto : staffDto.getUser().getRoles()) {
                    Role role = null;
                    Optional<Role> optional = roleRepository.findById(rDto.getId());
                    if (optional.isPresent()) {
                        role = optional.get();
                    }
                    if (role != null) {
                        roles.add(role);
                    }
                }
                user.getRoles().clear();
                user.getRoles().addAll(roles);
            }

            user.setPerson(staff);
            staff.setUser(user);
        }
        staff = staffRepository.save(staff);
        return new StaffDto(staff);
    }

    @Override
    public List<StaffDto> saveImportStaff(List<StaffDto> list) {
        List<StaffDto> ret = new ArrayList<>();
        for (StaffDto staffDto : list) {
            if (staffDto.getNationalityCode() != null && StringUtils.hasText(staffDto.getNationalityCode())) {
                staffDto.setNationality(new CountryDto(countryService.findByCode(staffDto.getNationalityCode())));
            }
            if (staffDto.getEthnicsCode() != null && StringUtils.hasText(staffDto.getEthnicsCode())) {
                Ethnics entity = ethnicsService.findByCode(staffDto.getEthnicsCode());
                if (entity != null) {
                    staffDto.setEthnics(new EthnicsDto(entity));
                }
            }
            if (staffDto.getReligionCode() != null && StringUtils.hasText(staffDto.getReligionCode())) {
                Religion entity = religionService.findByCode(staffDto.getReligionCode());
                if (entity != null) {
                    staffDto.setReligion(new ReligionDto(entity));
                }
            }
            if (staffDto.getStatusCode() != null && StringUtils.hasText(staffDto.getStatusCode())) {
                List<EmployeeStatusDto> listEmployeeStatusDto = employeeStatusRepository
                        .getByName(staffDto.getStatusCode());
                if (listEmployeeStatusDto != null && listEmployeeStatusDto.size() > 0) {
                    staffDto.setStatus(listEmployeeStatusDto.get(0));
                } else {
                    staffDto.setStatus(null);
                }

            }
            if (staffDto.getDepartmentCode() != null && StringUtils.hasText(staffDto.getDepartmentCode())) {
                List<HRDepartment> listHRDepartment = hRDepartmentRepository.findByCode(staffDto.getDepartmentCode());
                if (listHRDepartment != null && listHRDepartment.size() > 0) {
                    staffDto.setDepartment(new HRDepartmentDto(listHRDepartment.get(0)));
                } else {
                    staffDto.setDepartment(null);
                }
            }
            if (staffDto.getCivilServantTypeCode() != null && StringUtils.hasText(staffDto.getCivilServantTypeCode())) {
                List<CivilServantType> listCivilServantType = civilServantTypeRepository
                        .findByCode(staffDto.getCivilServantTypeCode());
                if (listCivilServantType != null && listCivilServantType.size() > 0) {
                    staffDto.setCivilServantType(new CivilServantTypeDto(listCivilServantType.get(0)));
                } else {
                    staffDto.setCivilServantType(null);
                }
            }
            if (staffDto.getCivilServantCategoryCode() != null
                    && StringUtils.hasText(staffDto.getCivilServantCategoryCode())) {
                List<CivilServantCategory> listCivilServantCategory = civilServantCategoryRepository
                        .findByCode(staffDto.getCivilServantCategoryCode());
                if (listCivilServantCategory != null && listCivilServantCategory.size() > 0) {
                    staffDto.setCivilServantCategory(new CivilServantCategoryDto(listCivilServantCategory.get(0)));
                } else {
                    staffDto.setCivilServantCategory(null);
                }
            }
            if (staffDto.getLabourAgreementTypeCode() != null
                    && StringUtils.hasText(staffDto.getLabourAgreementTypeCode())) {
                List<LabourAgreementType> listLabourAgreementType = labourAgreementTypeRepository
                        .findByCode(staffDto.getLabourAgreementTypeCode());
                if (listLabourAgreementType != null && listLabourAgreementType.size() > 0) {
                    staffDto.setLabourAgreementType(new LabourAgreementTypeDto(listLabourAgreementType.get(0)));
                } else {
                    staffDto.setLabourAgreementType(null);
                }
            }
            if (staffDto.getProfessionCode() != null && StringUtils.hasText(staffDto.getProfessionCode())) {
                List<Profession> listProfession = professionRepository.findListByCode(staffDto.getProfessionCode());

                if (listProfession != null && listProfession.size() > 0) {
                    staffDto.setProfession(new ProfessionDto(listProfession.get(0)));
                } else {
                    staffDto.setProfession(null);
                }
            }
            if (staffDto.getAcademicTitleCode() != null && StringUtils.hasText(staffDto.getAcademicTitleCode())) {
                List<AcademicTitle> listAcademic = academicTitleRepository.findByCode(staffDto.getAcademicTitleCode());

                if (listAcademic != null && listAcademic.size() > 0) {
                    staffDto.setAcademicRank(new AcademicTitleDto(listAcademic.get(0)));
                } else {
                    staffDto.setAcademicRank(null);
                }
            }
            if (staffDto.getEducationDegreeCode() != null && StringUtils.hasText(staffDto.getEducationDegreeCode())) {
                List<EducationDegree> listEducation = educationDegreeRepository
                        .findByCode(staffDto.getEducationDegreeCode());

                if (listEducation != null && listEducation.size() > 0) {
                    staffDto.setDegree(new EducationDegreeDto(listEducation.get(0)));
                } else {
                    staffDto.setDegree(null);
                }
            }

            staffDto = this.createStaffSimple(staffDto);
            ret.add(staffDto);
        }
        return ret;
    }

    public Page<PositionStaffDto> findTeacherByDepartment(UUID departmentId, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        return positionStaffRepository.findTeacherByDepartment(departmentId, pageable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StaffDto deleteStaff(UUID id) {
        if (id == null) {
            throw new RuntimeException("Invalid argument!");
        }
        Staff entity = this.findById(id);
        if (entity != null) {
            staffRepository.delete(entity);
        } else {
            throw new RuntimeException("Invalid argument!");
        }
        return new StaffDto(entity);
    }

    @Override
    @Transactional
    public Boolean deleteMultiple(Staff[] staffs) {
        boolean ret = true;
        if (staffs == null || staffs.length <= 0) {
            return ret;
        }
        ArrayList<Staff> lstStaffs = new ArrayList<>();
        for (Staff st : staffs) {
            Staff entity = this.findById(st.getId());
            Person person = null;
            Optional<Person> optional = personRepository.findById(st.getId());
            if (optional.isPresent()) {
                person = optional.get();
            }
            if (entity == null || person == null) {
                throw new RuntimeException();
            }
            lstStaffs.add(entity);
        }
        staffRepository.deleteAllInBatch(lstStaffs);
        return ret;
    }

    @Override
    public List<StaffDto> getAll() {
        Iterator<Staff> itr = staffRepository.findAll().iterator();
        List<StaffDto> list = new ArrayList<>();
        while (itr.hasNext()) {
            list.add(new StaffDto(itr.next()));
        }
        return list;
    }

    @Override
    public Page<StaffDto> searchStaff(StaffSearchDto dto, int pageSize, int pageIndex) {
        DateTime date = DateTime.now();
        String sqlCount = "select count(distinct ps.staff.id) from PositionStaff ps where (1=1)";
        String sql = "select distinct ps.staff as s from PositionStaff ps where (1=1)";

        Department dep = null;
        if (dto.getDepartment() != null) {
            dep = departmentRepository.getById(dto.getDepartment().getId());
            sql += " and ps.department.linePath like :linePath";
            sqlCount += " and ps.department.linePath like :linePath";
        }
        if (dto.getIsMainPosition() != null) {
            sql += " and ps.mainPosition=:mainPosition";
            sqlCount += " and ps.mainPosition=:mainPosition";
        }

        if (dto.getKeyword() != null) {
            sql += " and ps.staff.displayName like :keyword";
            sqlCount += " and ps.staff.displayName like :keyword";
        }

        Query query = manager.createQuery(sql);
        Query qCount = manager.createQuery(sqlCount);

        if (dto.getDepartment() != null) {
            if (dep != null) {
                String linePath = dep.getLinePath() + "%";
                query.setParameter("linePath", linePath);
                qCount.setParameter("linePath", linePath);
            }
        }
        if (dto.getKeyword() != null) {
            String keyword = "%" + dto.getKeyword() + "%";
            query.setParameter("keyword", keyword);
            qCount.setParameter("keyword", keyword);
        }

        if (dto.getIsMainPosition() != null) {
            query.setParameter("mainPosition", dto.getIsMainPosition());
            qCount.setParameter("mainPosition", dto.getIsMainPosition());
        }

        int startPosition = (pageIndex - 1) * pageSize;
        query.setFirstResult(startPosition);
        query.setMaxResults(pageSize);
        List<Staff> entities = query.getResultList();
        DateTime endDate = DateTime.now();
        long diffInMillis = endDate.getMillis() - date.getMillis();
        logger.info("{}", diffInMillis);
        List<StaffDto> content = new ArrayList<>();
        for (Staff entity : entities) {
            content.add(new StaffDto(entity));
        }
        long count = (long) qCount.getSingleResult();
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        return new PageImpl<>(content, pageable, count);
    }

    @Transactional
    @Override
    public StaffDto createStaffFromDto(StaffDto staffDto) {
        LocalDateTime currentDate = LocalDateTime.now();
        String currentUserName = "Unknown User";

        Staff staff = null;
        if (staffDto.getId() != null) {
            staff = this.findById(staffDto.getId());
        }
        if (staff == null && staffDto.getStaffCode() != null) {
            List<Staff> listStaff = staffRepository.findByCode(staffDto.getStaffCode());
            if (listStaff != null && listStaff.size() > 0) {
                staff = listStaff.get(0);
            }
        }
        if (staff == null) {
            staff = new Staff();
            staff.setCreateDate(currentDate);
        }
        String displayName = null;
        if (staffDto.getLastName() != null) {
            staff.setLastName(staffDto.getLastName());
            displayName = staffDto.getLastName();
        }
        if (staffDto.getFirstName() != null) {
            staff.setFirstName(staffDto.getFirstName());
            displayName = displayName + " " + staffDto.getFirstName();
        }
        if (displayName != null) {
            staff.setDisplayName(displayName);
        }
        if (staffDto.getBirthDate() != null) {
            staff.setBirthDate(staffDto.getBirthDate());
        }
        if (staffDto.getGender() != null) {
            staff.setGender(staffDto.getGender());
        }
        if (staffDto.getBirthPlace() != null)
            staff.setBirthPlace(staffDto.getBirthPlace());
        if (staffDto.getEmail() != null) {
            staff.setEmail(staffDto.getEmail());
        }
        if (staffDto.getStaffCode() != null) {
            staff.setStaffCode(staffDto.getStaffCode());
        }
        if (staffDto.getPhoneNumber() != null) {
            staff.setPhoneNumber(staffDto.getPhoneNumber());
        }
        if (staffDto.getIdNumber() != null) {
            staff.setIdNumber(staffDto.getIdNumber());
        }
        if (staffDto.getIdNumberIssueBy() != null) {
            staff.setIdNumberIssueBy(staffDto.getIdNumberIssueBy());
        }
        if (staffDto.getIdNumberIssueDate() != null) {
            staff.setIdNumberIssueDate(staffDto.getIdNumberIssueDate());
        }

        if (staffDto.getNationality() != null) {
            Country country = null;
            if (staffDto.getNationality().getId() != null) {
                Optional<Country> optional = countryRepository.findById(staffDto.getNationality().getId());
                if (optional.isPresent()) {
                    country = optional.get();
                }
            }
            if (country == null) {
                if (staffDto.getNationality().getCode() != null) {
                    country = countryRepository.findByCode(staffDto.getNationality().getCode());
                }
            }
            staff.setNationality(country);
        }
        if (staffDto.getReligion() != null) {
            Religion religion = null;
            if (staffDto.getReligion().getId() != null) {
                Optional<Religion> optional = religionRepository.findById(staffDto.getReligion().getId());
                if (optional.isPresent()) {
                    religion = optional.get();
                }
            }
            if (religion == null) {
                if (staffDto.getReligion().getCode() != null) {
                    religion = religionRepository.findByCode(staffDto.getReligion().getCode());
                }
            }
            staff.setReligion(religion);
        }
        if (staffDto.getEthnics() != null) {
            Ethnics ethnics = null;
            if (staffDto.getEthnics().getId() != null) {
                Optional<Ethnics> optional = ethnicsRepository.findById(staffDto.getEthnics().getId());
                if (optional.isPresent()) {
                    ethnics = optional.get();
                }
            }
            if (ethnics == null) {
                if (staffDto.getEthnics().getCode() != null) {
                    ethnics = ethnicsRepository.findByCode(staffDto.getEthnics().getCode());
                }
            }
            staff.setEthnics(ethnics);
        }
        if (staffDto.getUser() != null) {
            User user = staff.getUser();
            if (user == null) {
                user = new User();
                user.setUsername(staffDto.getUser().getUsername());
            }
            String password = SecurityUtils.getHashPassword(staffDto.getUser().getPassword());
            if (password != null && password.length() > 0) {
                user.setPassword(password);
            }
            if (staffDto.getUser().getEmail() != null)
                user.setEmail(staffDto.getUser().getEmail());
            user.setPerson(staff);
            staff.setUser(user);
        }
        if (staffDto.getPositions() != null && staffDto.getPositions().size() > 0) {
            HashSet<PositionStaff> positions = new HashSet<>();
            for (PositionStaffDto ps : staffDto.getPositions()) {
                PositionStaff newPs = null;
                HRDepartment department = null;
                PositionTitle position = null;
                if (ps.getDepartment() != null && ps.getDepartment().getCode() != null) {
                    List<HRDepartment> list = departmentRepository.findByCode(ps.getDepartment().getCode());
                    if (list != null && list.size() > 0) {
                        department = list.get(0);
                    }
                }
                if (ps.getPosition() != null && ps.getPosition().getId() != null) {
                    Optional<PositionTitle> optional = positionTitleRepository.findById(ps.getPosition().getId());
                    if (optional.isPresent()) {
                        position = optional.get();
                    }
                }
                if (staff.getId() != null && position != null && department != null && position.getId() != null && department.getId() != null) {
                    List<PositionStaff> list = positionStaffRepository.findBy(staff.getId(), position.getId(),
                            department.getId());
                    if (list != null && list.size() > 0) {
                        newPs = list.get(0);
                    }
                }
                if (newPs == null) {
                    newPs = new PositionStaff();
                }
                newPs.setCreateDate(currentDate);
                newPs.setCreatedBy(currentUserName);
                newPs.setCurrent(ps.isCurrent());
                newPs.setMainPosition(ps.getMainPosition());
                if (position != null) {
                    newPs.setPosition(position);
                }
                if (department != null) {
                    newPs.setHrDepartment(department);
                }
                newPs.setStaff(staff);
                positions.add(newPs);
            }
            if (staffDto.getAgreements() != null && staffDto.getAgreements().size() > 0) {
                staff.setAgreements(new HashSet<>());
                for (StaffLabourAgreementDto agreementDto : staffDto.getAgreements()) {
                    StaffLabourAgreement agreement = new StaffLabourAgreement();
                    agreement.setAgreementStatus(agreementDto.getAgreementStatus());
                    agreement.setCreateDate(currentDate);
                    agreement.setCreatedBy(currentUserName);
                    agreement.setModifyDate(currentDate);
                    agreement.setModifiedBy(currentUserName);

                    agreement.setStartDate(agreementDto.getStartDate());

                    agreement.setSignedDate(agreementDto.getSignedDate());
                    agreement.setEndDate(agreementDto.getEndDate());
                    if (agreementDto.getLabourAgreementType() != null
                            && agreementDto.getLabourAgreementType().getId() != null) {
                        LabourAgreementType labourAgreementType = labourAgreementTypeService
                                .findById(agreementDto.getLabourAgreementType().getId());
                        agreement.setLabourAgreementType(labourAgreementType);
                    }
                    agreement.setStaff(staff);
                    staff.getAgreements().add(agreement);
                }
            }
            if (staff.getPositions() != null) {
                staff.getPositions().clear();
                staff.getPositions().addAll(positions);
            } else {
                staff.setPositions(positions);
            }
        } else if (staff.getPositions() != null) {
            staff.getPositions().clear();
        }
        logger.info("Display name - staff code: {}", staff.getDisplayName() + ", " + staff.getStaffCode());
        staff = staffRepository.save(staff);
        return new StaffDto(staff);
    }

    @Override
    public int saveListStaff(List<StaffDto> staffDtoList) {
        for (StaffDto dto : staffDtoList) {
            createStaffFromDto(dto);
        }
        return staffDtoList.size();
    }

    @Override
    public Page<StaffDto> findPageByCode(String textSearch, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        return staffRepository.findPageByCodeOrName(textSearch, pageable);
    }

    @Override
    public Boolean validateStaffCode(String staffCode, UUID staffId) {
        if (staffCode != null) {
            List<Staff> staffs = staffRepository.findAll();
            for (Staff staff : staffs) {
                if (staff.getStaffCode().equalsIgnoreCase(staffCode)) {
                    if (staffId != null && staff.getId() != null && staff.getId().equals(staffId)) {
                        return Boolean.TRUE;
                    }
                    return Boolean.FALSE;
                }
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean validateUserName(String userName, UUID userId) {
        if (userName != null) {
            List<Staff> staffs = staffRepository.findAll();
            for (Staff staff : staffs) {
                if (staff != null) {
                    if (staff.getUser() != null) {
                        if (staff.getUser().getUsername() != null
                                && staff.getUser().getUsername().equalsIgnoreCase(userName)) {
                            if (userId != null && staff.getUser() != null && staff.getUser().getId() != null) {
                                staff.getUser().getId();
                            }
                            return Boolean.FALSE;
                        }
                    }
                }
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Page<StaffDto> searchByPage(SearchStaffDto dto) {
        if (dto == null) {
            return null;
        }

        int pageIndex = dto.getPageIndex();
        int pageSize = dto.getPageSize();
        boolean isRoleUser = false;
        boolean isRoleAdmin = false;
        boolean isRoleManager = false;

        UserDto user = userExtService.getCurrentUserName();
        if (user != null && user.getRoles() != null && user.getRoles().size() > 0) {
            for (RoleDto item : user.getRoles()) {
                if (item.getName() != null && "ROLE_ADMIN".equals(item.getName())) {
                    isRoleAdmin = true;
                }
                if (item.getName() != null && "HR_MANAGER".equals(item.getName())) {
                    isRoleManager = true;
                }
                if (item.getName() != null && "HR_USER".equals(item.getName())) {
                    isRoleUser = true;
                }
            }
        }
        if (isRoleAdmin) {
            isRoleManager = false;
            isRoleUser = false;
        } else {
            if (isRoleManager) {
                isRoleUser = false;
            }
        }
        if (pageIndex > 0) {
            pageIndex--;
        } else {
            pageIndex = 0;
        }

        String whereClause = "";
        String orderBy = " ORDER BY entity.createDate ";

        String sqlCount = "select count(entity.id) from Staff as entity where (1=1) ";
        String sql = "select new  com.globits.hr.dto.StaffDto(entity,false) from Staff as entity where (1=1) ";

        List<UUID> listDepId = new ArrayList<>();
        if (dto.getDepartmentId() != null) {
            listDepId = this.getAllDepartmentIdByParentId(dto.getDepartmentId());

            if (listDepId != null && listDepId.size() > 0) {
                whereClause += " AND entity.department.id IN (:listDepId) ";
            } else {
                whereClause += " AND entity.department.id = :departmentId ";
            }
        }
        if (isRoleUser) {
            if (user.getUsername() != null) {
                whereClause += " and entity.staffCode = :staffCode ";
            }
        }
        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            whereClause += " AND ( entity.displayName LIKE :text OR entity.staffCode LIKE :text ) ";
        }
        if (dto.getAcademicTitleLevel() != null) {
            whereClause += " AND ( entity.academicRank.id IN (select a.id from AcademicTitle a where a.level >= :academicTitleLevel ) ) ";
        }
        if (dto.getEducationDegreeLevel() != null) {
            whereClause += " AND ( entity.degree.id IN (select a.id from EducationDegree a where a.level >= :educationDegreeLevel ) ) ";
        }
        if (dto.getEmployeeStatusId() != null && StringUtils.hasText(dto.getEmployeeStatusId().toString())) {
            whereClause += " AND ( entity.status.id  =: employeeStatusId ) ";
        }
        if (dto.getCivilServantTypeId() != null && StringUtils.hasText(dto.getCivilServantTypeId().toString())) {
            whereClause += " AND ( entity.civilServantType.id  =: civilServantTypeId ) ";
        }
        if (dto.getGender() != null && StringUtils.hasText(dto.getGender())) {
            whereClause += " AND ( entity.gender  =: gender ) ";
        }
        sql += whereClause + orderBy;
        sqlCount += whereClause;

        Query query = manager.createQuery(sql, StaffDto.class);
        Query qCount = manager.createQuery(sqlCount);

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            query.setParameter("text", '%' + dto.getKeyword() + '%');
            qCount.setParameter("text", '%' + dto.getKeyword() + '%');
        }
        if (isRoleUser && user != null && user.getUsername() != null) {
            query.setParameter("staffCode", user.getUsername());
            qCount.setParameter("staffCode", user.getUsername());
        }
        if (dto.getCivilServantTypeId() != null && StringUtils.hasText(dto.getCivilServantTypeId().toString())) {
            query.setParameter("civilServantTypeId", dto.getCivilServantTypeId());
            qCount.setParameter("civilServantTypeId", dto.getCivilServantTypeId());
        }
        if (dto.getAcademicTitleLevel() != null) {
            query.setParameter("academicTitleLevel", dto.getAcademicTitleLevel());
            qCount.setParameter("academicTitleLevel", dto.getAcademicTitleLevel());
        }
        if (dto.getEducationDegreeLevel() != null) {
            query.setParameter("educationDegreeLevel", dto.getEducationDegreeLevel());
            qCount.setParameter("educationDegreeLevel", dto.getEducationDegreeLevel());
        }
        if (dto.getEmployeeStatusId() != null && StringUtils.hasText(dto.getEmployeeStatusId().toString())) {
            query.setParameter("employeeStatusId", dto.getEmployeeStatusId());
            qCount.setParameter("employeeStatusId", dto.getEmployeeStatusId());
        }
        if (dto.getGender() != null && StringUtils.hasText(dto.getGender())) {
            query.setParameter("gender", dto.getGender());
            qCount.setParameter("gender", dto.getGender());
        }
        if (listDepId != null && listDepId.size() > 0) {
            query.setParameter("listDepId", listDepId);
            qCount.setParameter("listDepId", listDepId);
        } else {
            if (dto.getDepartmentId() != null) {
                query.setParameter("departmentId", dto.getDepartmentId());
                qCount.setParameter("departmentId", dto.getDepartmentId());
            }
        }
        List<StaffDto> entities;
        long count = (long) qCount.getSingleResult();
        Page<StaffDto> result;
        if (dto.getIsExportExcel() && dto.getIsExportExcel() != null) {
            entities = query.getResultList();
            result = new PageImpl<>(entities);
        } else {
            int startPosition = pageIndex * pageSize;
            query.setFirstResult(startPosition);
            query.setMaxResults(pageSize);
            Pageable pageable = PageRequest.of(pageIndex, pageSize);
            entities = query.getResultList();
            result = new PageImpl<>(entities, pageable, count);
        }
        return result;
    }

    private void addChildren(Department parent, List<Department> children) {
        if (null != parent.getSubDepartments()) {
            for (Department child : parent.getSubDepartments()) {
                children.add(child);
                addChildren(child, children);
            }
        }
    }

    @Override
    public List<UUID> getAllDepartmentIdByParentId(UUID parentId) {
        Department parent = departmentRepository.getOne(parentId);
        if (parent.getId() != null) {
            List<UUID> ret = new ArrayList<>();
            List<Department> children = new ArrayList<>();
            ret.add(parent.getId());
            this.addChildren(parent, children);
            if (children.size() > 0) {
                for (Department dp : children) {
                    ret.add(dp.getId());
                }
            }
            return ret;
        }
        return null;
    }

    @Override
    public StaffDto savePositionStaff(PositionTitleStaffDto dto) {
        if (dto != null) {
            Staff entity = null;
            PositionStaff positionStaff = new PositionStaff();
            Set<PositionStaff> listFamilyRelationship = new HashSet<>();
            PositionTitle positionTitle = null;
            if (dto.getStaffCode() != null) {
                List<Staff> listStaff = staffRepository.getByCode(dto.getStaffCode());
                if (listStaff != null && listStaff.size() > 0) {
                    entity = listStaff.get(0);
                    positionStaff.setStaff(entity);
                    listFamilyRelationship = entity.getPositions();
                }
            }
            if (entity == null) {
                return null;
            }
            if (dto.getPositionTitleCode() != null) {
                List<PositionTitle> listPositionTitle = positionTitleRepository.findByCode(dto.getPositionTitleCode());
                if (listPositionTitle != null && listPositionTitle.size() > 0) {
                    positionTitle = listPositionTitle.get(0);
                    positionStaff.setPosition(positionTitle);
                }
            }
            if (positionTitle == null) {
                return null;
            }
            if (dto.getFromDate() != null) {
                positionStaff.setFromDate(new DateTime(dto.getFromDate()));
            }
            if (dto.getToDate() != null) {
                positionStaff.setToDate(new DateTime(dto.getToDate()));
            }
            if (dto.getDepartmentCode() != null) {
                List<HRDepartment> depart = hRDepartmentRepository.findByName(dto.getDepartmentCode());
                if (depart != null && depart.size() > 0) {
                    positionStaff.setHrDepartment(depart.get(0));
                }
            }
            positionStaff = positionStaffRepository.save(positionStaff);
            listFamilyRelationship.add(positionStaff);
            entity.setPositions(listFamilyRelationship);
            entity = staffRepository.save(entity);
            return new StaffDto(entity);
        }
        return null;
    }

    @Override
    public Staff getByCode(String code) {
        List<Staff> lst = this.staffRepository.getByCode(code);
        if (lst != null && lst.size() > 0) {
            return lst.get(0);
        }
        return null;
    }

    @Override
    public Boolean checkIdNumber(StaffDto dto) {
        Long count = staffRepository.countByIdNumber(dto.getId(), dto.getIdNumber());
        return count != 0L;
    }

    @Override
    public StaffDto updateStaffImage(UUID id, String imagePath) {
        if (id != null) {
            Staff entity = null;
            Optional<Staff> optional = staffRepository.findById(id);
            if (optional.isPresent()) {
                entity = optional.get();
            }
            if (entity != null) {
                entity.setImagePath(imagePath);
                entity = staffRepository.save(entity);
                return new StaffDto(entity);
            }
        }
        return null;
    }
}
