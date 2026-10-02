package com.globits.hr.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.PersonCertificate;
import com.globits.hr.dto.PersonCertificateDto;
@Service
public interface PersonCertificateService extends GenericService<PersonCertificate, UUID>{
	PersonCertificateDto saveImportStaffEducationHistory(PersonCertificateDto dto);
}
