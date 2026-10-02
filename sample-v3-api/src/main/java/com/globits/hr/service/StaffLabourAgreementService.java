package com.globits.hr.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.StaffLabourAgreement;
import com.globits.hr.dto.StaffLabourAgreementDto;

public interface StaffLabourAgreementService extends GenericService<StaffLabourAgreement, UUID> {

    List<StaffLabourAgreementDto> getAll(UUID id);

    StaffLabourAgreementDto getAgreementById(UUID id);

    StaffLabourAgreementDto saveAgreement(StaffLabourAgreementDto agreementDto, UUID id);

    StaffLabourAgreementDto removeAgreement(UUID id);

    boolean removeLists(List<UUID> ids);

    Page<StaffLabourAgreementDto> getPages(int pageIndex, int pageSize);
}
