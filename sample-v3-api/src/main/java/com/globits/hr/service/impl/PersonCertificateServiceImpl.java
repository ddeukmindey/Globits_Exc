package com.globits.hr.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.globits.core.domain.Person;
import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.hr.domain.Certificate;
import com.globits.hr.domain.PersonCertificate;
import com.globits.hr.domain.Staff;
import com.globits.hr.dto.PersonCertificateDto;
import com.globits.hr.repository.CertificateRepository;
import com.globits.hr.repository.PersonCertificateRepository;
import com.globits.hr.repository.StaffRepository;
import com.globits.hr.service.PersonCertificateService;

@Service
public class PersonCertificateServiceImpl extends GenericServiceImpl<PersonCertificate, UUID> implements PersonCertificateService {
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private PersonCertificateRepository personCertificateRepository;
    @Autowired
    private CertificateRepository certificateRepository;

    @Override
    public PersonCertificateDto saveImportStaffEducationHistory(PersonCertificateDto dto) {
        if (dto != null) {
            PersonCertificate personCertificate = new PersonCertificate();
            Person entity = null;
            if (dto.getPersonCode() != null) {
                List<Staff> listStaff = staffRepository.getByCode(dto.getPersonCode());
                if (listStaff != null && listStaff.size() > 0) {
                    entity = listStaff.get(0);
                    personCertificate.setPerson(entity);

                }
            }
            if (entity == null) {
                return null;
            }
            if (dto.getCertificateType() != null && StringUtils.hasText(dto.getCertificateType())) {
                List<Certificate> listCertificate = certificateRepository.findByType(Integer.parseInt(dto.getCertificateType()));
                if (listCertificate != null && listCertificate.size() > 0) {
                    personCertificate.setCertificate(listCertificate.get(0));
                }
            }
            personCertificate.setLevel(dto.getLevel());
            personCertificate.setIssueDate(dto.getIssueDate());
            personCertificate.setName(dto.getName());
            personCertificate = personCertificateRepository.save(personCertificate);
            return new PersonCertificateDto(personCertificate);
        }
        return null;
    }

}
