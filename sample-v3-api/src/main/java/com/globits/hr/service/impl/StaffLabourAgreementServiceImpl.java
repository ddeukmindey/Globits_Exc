package com.globits.hr.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.hr.domain.LabourAgreementType;
import com.globits.hr.domain.Staff;
import com.globits.hr.domain.StaffLabourAgreement;
import com.globits.hr.dto.StaffLabourAgreementDto;
import com.globits.hr.repository.LabourAgreementTypeRepository;
import com.globits.hr.repository.StaffLabourAgreementRepository;
import com.globits.hr.repository.StaffRepository;
import com.globits.hr.service.StaffLabourAgreementService;
import com.globits.security.domain.User;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class StaffLabourAgreementServiceImpl extends GenericServiceImpl<StaffLabourAgreement, UUID> implements StaffLabourAgreementService {
    @Autowired
    private StaffLabourAgreementRepository agreementRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private LabourAgreementTypeRepository agreementTypeRepository;

    @Override
    public List<StaffLabourAgreementDto> getAll(UUID id) {
        return this.agreementRepository.getAll(id);
    }

    @Override
    public StaffLabourAgreementDto getAgreementById(UUID id) {
        return this.agreementRepository.getAgreementById(id);
    }

    @Override
    public StaffLabourAgreementDto saveAgreement(StaffLabourAgreementDto agreementDto, UUID id) {
        Staff staff = null;
        LabourAgreementType agreementType = null;
        if (agreementDto != null && agreementDto.getStaff() != null && agreementDto.getStaff().getId() != null) {
            staff = this.staffRepository.getOne(agreementDto.getStaff().getId());
        }
        if (agreementDto != null && agreementDto.getStaffCode() != null) {
            List<Staff> list = this.staffRepository.getByCode(agreementDto.getStaffCode());
            if (list != null && list.size() > 0) {
                staff = list.get(0);
            }
        }
        if (staff == null) {
            return null;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User modifiedUser;
        LocalDateTime currentDate = LocalDateTime.now();
        String currentUserName = "Unknown User";
        if (authentication != null) {
            modifiedUser = (User) authentication.getPrincipal();
            currentUserName = modifiedUser.getUsername();
        }
        StaffLabourAgreement agreement = null;
        if (id != null) {
            Optional<StaffLabourAgreement> optional = agreementRepository.findById(id);
            if (optional.isPresent()) {
                agreement = optional.get();
            }
        } else if (agreementDto.getId() != null) {
            Optional<StaffLabourAgreement> optional = agreementRepository.findById(agreementDto.getId());
            if (optional.isPresent()) {
                agreement = optional.get();
            }
        }
        if (agreement == null) {
            agreement = new StaffLabourAgreement();
            agreement.setCreateDate(currentDate);
            agreement.setCreatedBy(currentUserName);
        }
        agreement.setModifyDate(currentDate);
        agreement.setModifiedBy(currentUserName);

        if (agreementDto.getStartDate() != null)
            agreement.setStartDate(agreementDto.getStartDate());

        if (agreementDto.getEndDate() != null)
            agreement.setEndDate(agreementDto.getEndDate());

        if (agreementDto.getSignedDate() != null)
            agreement.setSignedDate(agreementDto.getSignedDate());
        if (agreementDto.getAgreementStatus() != null)
            agreement.setAgreementStatus(agreementDto.getAgreementStatus());

        agreement.setStaff(staff);

        if (agreementDto.getLabourAgreementType() != null && agreementDto.getLabourAgreementType().getId() != null) {
            agreementType = this.agreementTypeRepository.getOne(agreementDto.getLabourAgreementType().getId());
        }
        if (agreementDto.getContractTypeCode() != null) {
            List<LabourAgreementType> list = this.agreementTypeRepository.findByCode(agreementDto.getContractTypeCode());
            if (list != null && list.size() > 0) {
                agreementType = list.get(0);
            }

        }
        if (agreementDto.getContractDate() != null)
            agreement.setContractDate(agreementDto.getContractDate());
        if (agreementDto.getRecruitmentDate() != null)
            agreement.setRecruitmentDate(agreementDto.getRecruitmentDate());

        agreement.setLabourAgreementType(agreementType);
        agreement = this.agreementRepository.save(agreement);
        return new StaffLabourAgreementDto(agreement);

    }

    @Override
    public StaffLabourAgreementDto removeAgreement(UUID id) {
        StaffLabourAgreement agreement = null;
        Optional<StaffLabourAgreement> optional = agreementRepository.findById(id);
        if (optional.isPresent()) {
            agreement = optional.get();
        }
        StaffLabourAgreementDto agreementDto = new StaffLabourAgreementDto(agreement);

        if (agreementRepository != null) {
            this.agreementRepository.deleteById(id);
        }
        return agreementDto;
    }

    @Override
    public boolean removeLists(List<UUID> ids) {
        if (ids != null && ids.size() > 0) {
            for (UUID id : ids) {
                this.agreementRepository.deleteById(id);
            }
        }
        return false;

    }

    @Override
    public Page<StaffLabourAgreementDto> getPages(int pageIndex, int pageSize) {
        if (pageIndex > 1) {
            pageIndex--;
        } else {
            pageIndex = 0;
        }
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return agreementRepository.getPages(pageable);
    }
}
