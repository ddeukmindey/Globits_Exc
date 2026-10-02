package com.globits.hr.service.impl;

import java.util.List;
import java.util.UUID;

import com.globits.hr.domain.FamilyRelationship;
import com.globits.hr.repository.FamilyRelationshipRepository;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.hr.domain.Staff;
import com.globits.hr.domain.StaffFamilyRelationship;
import com.globits.hr.dto.StaffFamilyRelationshipDto;
import com.globits.hr.dto.function.StaffFamilyRelationshipFunctionDto;
import com.globits.hr.repository.StaffFamilyRelationshipRepository;
import com.globits.hr.repository.StaffRepository;
import com.globits.hr.service.StaffFamilyRelationshipService;
import com.globits.security.domain.User;
import org.springframework.util.StringUtils;

@Transactional
@Service
public class StaffFamilyRelationshipServiceImpl extends GenericServiceImpl<StaffFamilyRelationship, UUID>
        implements StaffFamilyRelationshipService {
    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private StaffFamilyRelationshipRepository staffFamilyRelationshipRepository;

    @Autowired
    private FamilyRelationshipRepository familyRelationshipRepository;

    @Override
    public Page<StaffFamilyRelationshipDto> getPages(int pageIndex, int pageSize) {
        if (pageIndex > 1) {
            pageIndex--;
        } else {
            pageIndex = 0;
        }
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return this.staffFamilyRelationshipRepository.getPages(pageable);
    }

    @Override
    public List<StaffFamilyRelationshipDto> getAll(UUID id) {
        return this.staffFamilyRelationshipRepository.getAll(id);
    }

    @Override
    public StaffFamilyRelationshipDto getFamilyById(UUID id) {
        return this.staffFamilyRelationshipRepository.getFamilyById(id);
    }

    @Override
    public StaffFamilyRelationshipDto saveFamily(StaffFamilyRelationshipDto familyDto, UUID id) {
        if (familyDto == null) {
            return null;
        }
        Staff staff = null;
        if (familyDto.getStaff() != null && familyDto.getStaff().getId() != null) {
            staff = this.staffRepository.getOne(familyDto.getStaff().getId());
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User modifiedUser;
        LocalDateTime currentDate = LocalDateTime.now();
        String currentUserName = "Unknown User";
        if (authentication != null) {
            modifiedUser = (User) authentication.getPrincipal();
            currentUserName = modifiedUser.getUsername();
        }
        StaffFamilyRelationship familyRelationship = null;
        if (id != null) {
            familyRelationship = this.findById(id);
        }
        if (familyRelationship == null && familyDto.getId() != null) {
            familyRelationship = this.findById(familyDto.getId());
        }

        if (familyRelationship == null) {// trường hợp thêm mới
            familyRelationship = new StaffFamilyRelationship();
            familyRelationship.setCreateDate(currentDate);
            familyRelationship.setCreatedBy(currentUserName);
        }
        familyRelationship.setModifyDate(currentDate);
        familyRelationship.setModifiedBy(currentUserName);

        if (familyDto.getFullName() != null) {
            familyRelationship.setFullName(familyDto.getFullName());
        }
        if (familyDto.getProfession() != null) {
            familyRelationship.setProfession(familyDto.getProfession());
        }
        if (familyDto.getAddress() != null) {
            familyRelationship.setAddress(familyDto.getAddress());
        }
        if (familyDto.getBirthDate() != null) {
            familyRelationship.setBirthDate(familyDto.getBirthDate());
        }
        if (familyDto.getDescription() != null) {
            familyRelationship.setDescription(familyDto.getDescription());
        }
        familyRelationship.setStaff(staff);

        familyRelationship = this.staffFamilyRelationshipRepository.save(familyRelationship);
        familyDto.setId(familyRelationship.getId());
        return new StaffFamilyRelationshipDto(familyRelationship);
    }

    @Override
    public StaffFamilyRelationshipDto removeFamily(UUID id) {
        StaffFamilyRelationship familyRelationship = this.findById(id);
        if (staffFamilyRelationshipRepository != null && familyRelationship != null) {
            this.staffFamilyRelationshipRepository.deleteById(id);
        }
        return new StaffFamilyRelationshipDto(familyRelationship);
    }

    @Override
    public boolean removeLists(List<UUID> ids) {
        if (ids != null && ids.size() > 0) {
            for (UUID id : ids) {
                this.staffFamilyRelationshipRepository.deleteById(id);
            }
        }
        return false;
    }

    @Override
    public StaffFamilyRelationshipDto saveImportStaffFamily(StaffFamilyRelationshipFunctionDto dto) {
        if (dto != null) {
            StaffFamilyRelationship staffFamilyRelationship = new StaffFamilyRelationship();
            Staff entity = null;
            if (dto.getStaffCode() != null) {
                List<Staff> listStaff = staffRepository.getByCode(dto.getStaffCode());
                if (listStaff != null && listStaff.size() > 0) {
                    entity = listStaff.get(0);
                    staffFamilyRelationship.setStaff(entity);
                }
            }
            if (entity == null) {
                return null;
            }
            if (dto.getFamilyRelationship() != null && StringUtils.hasText(dto.getFamilyRelationship())) {
                List<FamilyRelationship> listData = familyRelationshipRepository.findByCode(dto.getFamilyRelationship());
                if (listData != null && listData.size() > 0) {
                    staffFamilyRelationship.setFamilyRelationship(listData.get(0));
                }
            }

            if (dto.getAddress() != null) {
                staffFamilyRelationship.setAddress(dto.getAddress());
            }
            if (dto.getBirthDate() != null) {
                staffFamilyRelationship.setBirthDate(dto.getBirthDate());
            }
            if (dto.getFullName() != null) {
                staffFamilyRelationship.setFullName(dto.getFullName());
            }
            staffFamilyRelationship = staffFamilyRelationshipRepository.save(staffFamilyRelationship);
            return new StaffFamilyRelationshipDto(staffFamilyRelationship);
        }
        return null;
    }

}
