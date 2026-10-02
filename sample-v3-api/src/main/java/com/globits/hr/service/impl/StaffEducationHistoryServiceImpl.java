package com.globits.hr.service.impl;

import java.util.*;

import com.globits.hr.domain.*;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.globits.core.domain.Country;
import com.globits.core.repository.CountryRepository;
import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.hr.dto.StaffEducationHistoryDto;
import com.globits.hr.repository.EducationDegreeRepository;
import com.globits.hr.repository.HrEducationTypeRepository;
import com.globits.hr.repository.HrSpecialityRepository;
import com.globits.hr.repository.StaffEducationHistoryRepository;
import com.globits.hr.repository.StaffRepository;
import com.globits.hr.service.StaffEducationHistoryService;
import com.globits.security.domain.User;

@Transactional
@Service
public class StaffEducationHistoryServiceImpl extends GenericServiceImpl<StaffEducationHistory, UUID>
        implements StaffEducationHistoryService {
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private StaffEducationHistoryRepository educationHistoryRepository;
    @Autowired
    CountryRepository countryRepository;
    @Autowired
    HrEducationTypeRepository hrEducationTypeRepository;
    @Autowired
    HrSpecialityRepository hrSpecialityRepository;
    @Autowired
    EducationDegreeRepository educationDegreeRepository;

    @Override
    public Page<StaffEducationHistoryDto> getPages(int pageIndex, int pageSize) {
        if (pageIndex > 1) {
            pageIndex--;
        } else {
            pageIndex = 0;
        }
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return this.educationHistoryRepository.getPages(pageable);
    }

    @Override
    public List<StaffEducationHistoryDto> getAll(UUID id) {
        // TODO Auto-generated method stub
        return this.educationHistoryRepository.getAll(id);
    }

    @Override
    public StaffEducationHistoryDto getEducationById(UUID id) {
        return this.educationHistoryRepository.getEducationById(id);
    }

    @Override
    public StaffEducationHistoryDto saveEducation(StaffEducationHistoryDto educationDto, UUID id) {

        Staff staff = null;
        if (educationDto != null && educationDto.getStaff() != null && educationDto.getStaff().getId() != null) {
            staff = this.staffRepository.getOne(educationDto.getStaff().getId());
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User modifiedUser;
        LocalDateTime currentDate = LocalDateTime.now();
        String currentUserName = "Unknown User";
        if (authentication != null) {
            modifiedUser = (User) authentication.getPrincipal();
            currentUserName = modifiedUser.getUsername();
        }
        StaffEducationHistory educationHistory = new StaffEducationHistory();
        if (id != null) {
            Optional<StaffEducationHistory> optional = educationHistoryRepository.findById(id);
            if (optional.isPresent()) {
                educationHistory = optional.get();
            }
        } else {
            if (educationDto != null && educationDto.getId() != null) {
                Optional<StaffEducationHistory> optional = educationHistoryRepository.findById(educationDto.getId());
                if (optional.isPresent()) {
                    educationHistory = optional.get();
                }
            }
        }

        educationHistory.setModifyDate(currentDate);
        educationHistory.setModifiedBy(currentUserName);
        if (educationDto != null) {
            if (educationDto.getSchoolName() != null) {
                educationHistory.setSchoolName(educationDto.getSchoolName());
            }
            if (educationDto.getStartDate() != null) {
                educationHistory.setStartDate(educationDto.getStartDate());
            }
            if (educationDto.getEndDate() != null) {
                educationHistory.setEndDate(educationDto.getEndDate());
            }
            if (educationDto.getDescription() != null) {
                educationHistory.setDescription(educationDto.getDescription());
            }
            if (educationDto.getStatus() != null) {
                educationHistory.setStatus(educationDto.getStatus());
            }
        }
        educationHistory.setStaff(staff);

        educationHistory = this.educationHistoryRepository.save(educationHistory);
        if (educationDto != null) {
            educationDto.setId(educationHistory.getId());
        }
        return educationDto;
    }

    @Override
    public boolean removeLists(List<UUID> ids) {
        if (ids != null && ids.size() > 0) {
            for (UUID id : ids) {
                this.educationHistoryRepository.deleteById(id);
            }
        }
        return false;

    }

    @Override
    public StaffEducationHistoryDto removeEducation(UUID id) {
        StaffEducationHistoryDto educationDto = new StaffEducationHistoryDto(this.educationHistoryRepository.getOne(id));
        if (educationHistoryRepository != null) {
            this.educationHistoryRepository.deleteById(id);
        }
        return educationDto;
    }

    @Override
    public StaffEducationHistoryDto saveImportStaffEducationHistory(StaffEducationHistoryDto dto) {
        if (dto != null) {
            StaffEducationHistory staffEducationHistory = new StaffEducationHistory();
            Staff entity = null;
            if (dto.getStaffCode() != null) {
                List<Staff> listStaff = staffRepository.getByCode(dto.getStaffCode());
                if (listStaff != null && listStaff.size() > 0) {
                    entity = listStaff.get(0);
                    staffEducationHistory.setStaff(entity);
                }
            }
            if (entity == null) {
                return null;
            }
            if (dto.getCountryCode() != null && StringUtils.hasText(dto.getCountryCode())) {
                Country country = countryRepository.findByCode(dto.getCountryCode());
                if (country != null) {
                    staffEducationHistory.setCountry(country);
                }
            }
            if (dto.getSpecialityCode() != null && StringUtils.hasText(dto.getSpecialityCode())) {
                List<HrSpeciality> listHrSpeciality = hrSpecialityRepository.findByCode(dto.getSpecialityCode());
                if (listHrSpeciality != null && listHrSpeciality.size() > 0) {
                    staffEducationHistory.setSpeciality(listHrSpeciality.get(0));
                }
            }
            if (dto.getEducationTypeCode() != null && StringUtils.hasText(dto.getEducationTypeCode())) {
                List<HrEducationType> listData = hrEducationTypeRepository.findByCode(dto.getEducationTypeCode());
                if (listData != null && listData.size() > 0) {
                    staffEducationHistory.setEducationType(listData.get(0));
                }
            }
            if (dto.getEducationDegreeCode() != null && StringUtils.hasText(dto.getEducationDegreeCode())) {
                List<EducationDegree> listData = educationDegreeRepository.findByCode(dto.getEducationDegreeCode());
                if (listData != null && listData.size() > 0) {
                    staffEducationHistory.setEducationDegree(listData.get(0));
                }
            }
            if (dto.getMajorCode() != null && StringUtils.hasText(dto.getMajorCode())) {
                List<HrSpeciality> listData = hrSpecialityRepository.findByCode(dto.getMajorCode());
                if (listData != null && listData.size() > 0) {
                    staffEducationHistory.setMajor(listData.get(0));
                }
            }
            staffEducationHistory.setStartDate(dto.getStartDate());
            staffEducationHistory.setEndDate(dto.getEndDate());
            staffEducationHistory.setSchoolName(dto.getSchoolName());
            staffEducationHistory.setPlace(dto.getPlace());
            staffEducationHistory.setIsCurrent(dto.getIsCurrent());
            staffEducationHistory.setFundingSource(dto.getFundingSource());
            staffEducationHistory.setDecisionCode(dto.getDecisionCode());
            staffEducationHistory.setNote(dto.getNote());
            staffEducationHistory.setIsConfirmation(dto.getConfirmation());
            staffEducationHistory.setIsCountedForSeniority(dto.getCountedForSeniority());
            staffEducationHistory.setBasis(dto.getBasis());
            staffEducationHistory.setDecisionDate(dto.getDecisionDate());
            staffEducationHistory.setReturnDate(dto.getReturnDate());
            staffEducationHistory.setNotFinish(dto.getNotFinish());
            staffEducationHistory.setFinishDateByDecision(dto.getFinishDateByDecision());
            staffEducationHistory.setExtendDateByDecision(dto.getExtendDateByDecision());
            staffEducationHistory.setExtendDecisionDate(dto.getExtendDecisionDate());
            staffEducationHistory.setIsExtended(dto.getIsExtended());
            staffEducationHistory.setExtendDecisionCode(dto.getExtendDecisionCode());
            staffEducationHistory = educationHistoryRepository.save(staffEducationHistory);
            return new StaffEducationHistoryDto(staffEducationHistory);
        }
        return null;
    }
}
