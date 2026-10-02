package com.globits.hr.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.StaffFamilyRelationship;
import com.globits.hr.dto.StaffFamilyRelationshipDto;
import com.globits.hr.dto.function.StaffFamilyRelationshipFunctionDto;

public interface StaffFamilyRelationshipService extends GenericService<StaffFamilyRelationship, UUID> {
    Page<StaffFamilyRelationshipDto> getPages(int pageIndex, int pageSize);

    List<StaffFamilyRelationshipDto> getAll(UUID id);

    StaffFamilyRelationshipDto getFamilyById(UUID id);

    StaffFamilyRelationshipDto saveFamily(StaffFamilyRelationshipDto familyDto, UUID id);

    StaffFamilyRelationshipDto removeFamily(UUID id);

    boolean removeLists(List<UUID> ids);

    StaffFamilyRelationshipDto saveImportStaffFamily(StaffFamilyRelationshipFunctionDto dto);

}
