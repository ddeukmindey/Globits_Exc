package com.globits.hr.service.impl;

import java.util.Optional;
import java.util.UUID;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.hr.domain.StaffInsuranceHistory;
import com.globits.hr.domain.Staff;
import com.globits.hr.dto.StaffInsuranceHistoryDto;
import com.globits.hr.repository.StaffInsuranceHistoryRepository;
import com.globits.hr.repository.StaffRepository;
import com.globits.hr.service.StaffInsuranceHistoryService;
import com.globits.hr.service.StaffService;
import com.globits.security.domain.User;

@Transactional
@Service
public class StaffInsuranceHistoryServiceImpl extends GenericServiceImpl<StaffInsuranceHistory, UUID> implements StaffInsuranceHistoryService {

    @Autowired
    private StaffInsuranceHistoryRepository staffInsuranceHistoryRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private StaffService staffService;

    @Override
    public StaffInsuranceHistoryDto saveStaffInsuranceHistory(StaffInsuranceHistoryDto dto, UUID id) {
        if (dto == null) {
            return null;
        }
        Staff staff = null;
        if (dto.getStaff() != null && dto.getStaff().getId() != null) {
            Optional<Staff> optional = staffRepository.findById(dto.getStaff().getId());
            if (optional.isPresent()) {
                staff = optional.get();
            }
        }
        if (staff == null) {
            return null;
        }
        return getStaffInsuranceHistoryDto(dto, id, staff);
    }

    @Override
    public StaffInsuranceHistoryDto saveOrUpdate(StaffInsuranceHistoryDto dto, UUID id) {
        Staff staff = null;
        if (dto != null && dto.getStaff() != null && dto.getStaff().getId() != null) {
            Optional<Staff> optional = staffRepository.findById(dto.getStaff().getId());
            if (optional.isPresent()) {
                staff = optional.get();
            }
        }
        if (staff == null && dto != null && dto.getStaff() != null && dto.getStaff().getStaffCode() != null && dto.getStaff().getStaffCode().length() > 0) {
            staff = staffService.getByCode(dto.getStaff().getStaffCode());
        }
        if (staff == null) {
            return null;
        }
        return getStaffInsuranceHistoryDto(dto, id, staff);
    }

    private StaffInsuranceHistoryDto getStaffInsuranceHistoryDto(StaffInsuranceHistoryDto dto, UUID id, Staff staff) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User modifiedUser;
        LocalDateTime currentDate = LocalDateTime.now();
        String currentUserName = "Unknown User";
        if (authentication != null) {
            modifiedUser = (User) authentication.getPrincipal();
            currentUserName = modifiedUser.getUsername();
        }

        StaffInsuranceHistory staffInsuranceHistory = null;
        if (id != null) {
            staffInsuranceHistory = this.findById(id);
        }
        if (staffInsuranceHistory == null && dto.getId() != null) {
            staffInsuranceHistory = this.findById(dto.getId());
        }

        if (staffInsuranceHistory == null) {
            staffInsuranceHistory = new StaffInsuranceHistory();
            staffInsuranceHistory.setCreateDate(currentDate);
            staffInsuranceHistory.setCreatedBy(currentUserName);
        }
        staffInsuranceHistory.setModifyDate(currentDate);
        staffInsuranceHistory.setModifiedBy(currentUserName);

        if (dto.getStartDate() != null) {
            staffInsuranceHistory.setStartDate(dto.getStartDate());
        }
        if (dto.getEndDate() != null) {
            staffInsuranceHistory.setEndDate(dto.getEndDate());
        }
        if (dto.getNote() != null) {
            staffInsuranceHistory.setNote(dto.getNote());
        }
        if (dto.getSalaryCofficient() != null) {
            staffInsuranceHistory.setSalaryCofficient(dto.getSalaryCofficient());
        }
        if (dto.getInsuranceSalary() != null) {
            staffInsuranceHistory.setInsuranceSalary(dto.getInsuranceSalary());
        }
        if (dto.getStaffPercentage() != null) {
            staffInsuranceHistory.setStaffPercentage(dto.getStaffPercentage());
        }
        if (dto.getOrgPercentage() != null) {
            staffInsuranceHistory.setOrgPercentage(dto.getOrgPercentage());
        }
        if (dto.getStaffInsuranceAmount() != null) {
            staffInsuranceHistory.setStaffInsuranceAmount(dto.getStaffInsuranceAmount());
        }
        if (dto.getOrgInsuranceAmount() != null) {
            staffInsuranceHistory.setOrgInsuranceAmount(dto.getOrgInsuranceAmount());
        }
        if (dto.getSocialInsuranceBookCode() != null) {
            staffInsuranceHistory.setSocialInsuranceBookCode(dto.getSocialInsuranceBookCode());
        }
        if (dto.getProfessionName() != null) {
            staffInsuranceHistory.setProfessionName(dto.getProfessionName());
        }
        if (dto.getDepartmentName() != null) {
            staffInsuranceHistory.setDepartmentName(dto.getDepartmentName());
        }
        if (dto.getAllowanceCoefficient() != null) {
            staffInsuranceHistory.setAllowanceCoefficient(dto.getAllowanceCoefficient());
        }
        staffInsuranceHistory.setStaff(staff);
        staffInsuranceHistory = this.staffInsuranceHistoryRepository.save(staffInsuranceHistory);
        return new StaffInsuranceHistoryDto(staffInsuranceHistory);
    }
}
