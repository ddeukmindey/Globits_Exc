package com.globits.hr.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.Staff;
import com.globits.hrv3.dto.view.GeneralInformationDto;
import com.globits.hrv3.dto.view.ProfileInformationDto;

@Service
public interface StaffServiceV3 extends GenericService<Staff, UUID> {
    GeneralInformationDto saveOrUpdateGeneralInformation(UUID id, GeneralInformationDto staffDto);

    GeneralInformationDto getGeneralInformation(UUID id);

    ProfileInformationDto saveOrUpdateProfileInformation(UUID id, ProfileInformationDto staffDto);

    ProfileInformationDto getProfileInformation(UUID id);
}
