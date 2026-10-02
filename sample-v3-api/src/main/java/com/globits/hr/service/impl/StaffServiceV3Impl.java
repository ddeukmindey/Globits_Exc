package com.globits.hr.service.impl;

import java.util.*;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.globits.core.domain.Country;
import com.globits.core.domain.Ethnics;
import com.globits.core.domain.PersonAddress;
import com.globits.core.domain.Profession;
import com.globits.core.domain.Religion;
import com.globits.core.dto.PersonAddressDto;
import com.globits.core.repository.AdministrativeUnitRepository;
import com.globits.core.repository.CountryRepository;
import com.globits.core.repository.DepartmentRepository;
import com.globits.core.repository.EthnicsRepository;
import com.globits.core.repository.PersonAddressRepository;
import com.globits.core.repository.ProfessionRepository;
import com.globits.core.repository.ReligionRepository;
import com.globits.core.service.CountryService;
import com.globits.core.service.EthnicsService;
import com.globits.core.service.ReligionService;
import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.core.utils.SecurityUtils;
import com.globits.hr.domain.CivilServantCategory;
import com.globits.hr.domain.CivilServantGrade;
import com.globits.hr.domain.CivilServantType;
import com.globits.hr.domain.EmployeeStatus;
import com.globits.hr.domain.HRDepartment;
import com.globits.hr.domain.LabourAgreementType;
import com.globits.hr.domain.Staff;
import com.globits.hr.repository.AcademicTitleRepository;
import com.globits.hr.repository.CertificateRepository;
import com.globits.hr.repository.CivilServantCategoryRepository;
import com.globits.hr.repository.CivilServantGradeRepository;
import com.globits.hr.repository.CivilServantTypeRepository;
import com.globits.hr.repository.ContractTypeRepository;
import com.globits.hr.repository.EducationDegreeRepository;
import com.globits.hr.repository.EmployeeStatusRepository;
import com.globits.hr.repository.FamilyRelationshipRepository;
import com.globits.hr.repository.HRDepartmentRepository;
import com.globits.hr.repository.HrEducationTypeRepository;
import com.globits.hr.repository.HrSpecialityRepository;
import com.globits.hr.repository.InformaticDegreeRepository;
import com.globits.hr.repository.LabourAgreementTypeRepository;
import com.globits.hr.repository.PersonCertificateRepository;
import com.globits.hr.repository.PoliticalTheoryLevelRepository;
import com.globits.hr.repository.PositionRepository;
import com.globits.hr.repository.ProfessionalDegreeRepository;
import com.globits.hr.repository.StaffInsuranceHistoryRepository;
import com.globits.hr.repository.StaffEducationHistoryRepository;
import com.globits.hr.repository.StaffFamilyRelationshipRepository;
import com.globits.hr.repository.StaffLabourAgreementRepository;
import com.globits.hr.repository.StaffRepository;
import com.globits.hr.repository.StaffSalaryHistoryRepository;
import com.globits.hr.service.LabourAgreementTypeService;
import com.globits.hr.service.StaffLabourAgreementService;
import com.globits.hr.service.StaffServiceV3;
import com.globits.hrv3.dto.view.GeneralInformationDto;
import com.globits.hrv3.dto.view.ProfileInformationDto;
import com.globits.security.domain.Role;
import com.globits.security.domain.User;
import com.globits.security.dto.RoleDto;
import com.globits.security.repository.RoleRepository;

@Service
public class StaffServiceV3Impl extends GenericServiceImpl<Staff, UUID> implements StaffServiceV3 {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    PersonAddressRepository personAddressRepository;

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
    DepartmentRepository departmentRepository;

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

    @Override
    public GeneralInformationDto saveOrUpdateGeneralInformation(UUID id, GeneralInformationDto staffDto) {
        if (staffDto == null) {
            return null;
        }
        Staff staff = null;
        if (id != null) {
            if (staffDto.getId() != null && !staffDto.getId().equals(id)) {
                return null;
            }
            Optional<Staff> optional = staffRepository.findById(id);
            if (optional.isPresent()) {
                staff = optional.get();
            }
        }
        if (staff == null) {
            staff = new Staff();
        }
        if (staffDto.getFirstName() != null) {
            staff.setFirstName(staffDto.getFirstName());
        }
        if (staffDto.getLastName() != null) {
            staff.setLastName(staffDto.getLastName());
        }
        if (staffDto.getBirthDate() != null) {
            staff.setBirthDate(staffDto.getBirthDate());
        }
        if (staffDto.getBirthPlace() != null) {
            staff.setBirthPlace(staffDto.getBirthPlace());
        }
        if (staffDto.getGender() != null) {
            staff.setGender(staffDto.getGender());
        }
        if (staffDto.getPhoto() != null) {
            staff.setPhoto(staffDto.getPhoto());
        }
        if (staffDto.getDisplayName() != null) {
            staff.setDisplayName(staffDto.getDisplayName());
        }
        if (staffDto.getPhoneNumber() != null) {
            staff.setPhoneNumber(staffDto.getPhoneNumber());
        }
        if (staffDto.getMaritalStatus() != null) {
            staff.setMaritalStatus(staffDto.getMaritalStatus());
        }
        staff.setImagePath(staffDto.getImagePath());
        staff.setPermanentResidence(staffDto.getPermanentResidence());
        staff.setCurrentResidence(staffDto.getCurrentResidence());
        if (staffDto.getEthnics() != null) {
            Ethnics ethnics = null;
            Optional<Ethnics> optional = ethnicsRepository.findById(staffDto.getEthnics().getId());
            if (optional.isPresent()) {
                ethnics = optional.get();
            }
            staff.setEthnics(ethnics);
        }
        if (staffDto.getNationality() != null) {
            Country nationality = null;
            Optional<Country> optional = countryRepository.findById(staffDto.getNationality().getId());
            if (optional.isPresent()) {
                nationality = optional.get();
            }
            staff.setNationality(nationality);
        }
        if (staffDto.getEmail() != null) {
            staff.setEmail(staffDto.getEmail());
        }
        if (staffDto.getReligion() != null && staffDto.getReligion().getId() != null) {
            Religion religion = null;
            Optional<Religion> optional = religionRepository.findById(staffDto.getReligion().getId());
            if (optional.isPresent()) {
                religion = optional.get();
            }
            staff.setReligion(religion);
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
        User user = new User();
        if (staffDto.getUser() != null) {
            user.setUsername(staffDto.getUser().getUsername());
            LocalDateTime currentDate = new LocalDateTime();
            String password = SecurityUtils.getHashPassword(staffDto.getUser().getPassword());
            user.setPassword(password);

            user.setPassword(password);
            user.setCreateDate(currentDate);
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
        }
        staff = staffRepository.save(staff);
        return new GeneralInformationDto(staff);
    }

    @Override
    public GeneralInformationDto getGeneralInformation(UUID id) {
        if (id != null) {
            Staff staff = null;
            Optional<Staff> optional = staffRepository.findById(id);
            if (optional.isPresent()) {
                staff = optional.get();
            }
            if (staff != null) {
                return new GeneralInformationDto(staff);
            }
        }
        return null;
    }

    @Override
    public ProfileInformationDto saveOrUpdateProfileInformation(UUID id, ProfileInformationDto staffDto) {
        if (staffDto == null) {
            return null;
        }
        Staff staff = null;
        if (id != null) {
            if (staffDto.getId() != null && !staffDto.getId().equals(id)) {
                return null;
            }

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
        if (staffDto.getStaffCode() != null) {
            staff.setStaffCode(staffDto.getStaffCode());
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
        staff.setPositionDecisionNumber(staffDto.getPositionDecisionNumber());
        if (staffDto.getCivilServantCategory() != null) {
            CivilServantCategory civilServantCategory = null;
            Optional<CivilServantCategory> optional = civilServantCategoryRepository.findById(staffDto.getCivilServantCategory().getId());
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
        if (staffDto.getDepartment() != null && staffDto.getDepartment().getId() != null) {
            HRDepartment department = null;
            Optional<HRDepartment> optional = hRDepartmentRepository.findById(staffDto.getDepartment().getId());
            if (optional.isPresent()) {
                department = optional.get();
            }
            staff.setDepartment(department);
        }
        if (staffDto.getLabourAgreementType() != null && staffDto.getLabourAgreementType().getId() != null) {
            LabourAgreementType labourAgreementType = null;
            Optional<LabourAgreementType> optional = labourAgreementTypeRepository.findById(staffDto.getLabourAgreementType().getId());
            if (optional.isPresent()) {
                labourAgreementType = optional.get();
            }
            staff.setLabourAgreementType(labourAgreementType);
        }
        if (staffDto.getContractDate() != null) {
            staff.setContractDate(staffDto.getContractDate());
        }
        if (staffDto.getJobTitle() != null) {
            staff.setJobTitle(staffDto.getJobTitle());
        }
        if (staffDto.getCivilServantType() != null) {
            CivilServantType type = null;
            Optional<CivilServantType> optional = civilServantTypeRepository.findById(staffDto.getCivilServantType().getId());
            if (optional.isPresent()) {
                type = optional.get();
            }
            staff.setCivilServantType(type);
        }
        staff.setStartDate(staffDto.getStartDate());
        staff.setRecruitmentDate(staffDto.getRecruitmentDate());
        staff.setSalaryStartDate(staffDto.getSalaryStartDate());
        staff = staffRepository.save(staff);
        return new ProfileInformationDto(staff);
    }

    @Override
    public ProfileInformationDto getProfileInformation(UUID id) {
        if (id != null) {
            Staff staff = null;
            Optional<Staff> optional = staffRepository.findById(id);
            if (optional.isPresent()) {
                staff = optional.get();
            }
            if (staff != null) {
                return new ProfileInformationDto(staff);
            }
        }
        return null;
    }

}
