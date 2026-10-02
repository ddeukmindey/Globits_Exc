package com.globits.hr.exercise.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.hr.exercise.domain.Company;
import com.globits.hr.exercise.dto.CompanyDto;
import com.globits.hr.exercise.repository.CompanyRepository;
import com.globits.hr.exercise.service.CompanyService;
import com.globits.hr.dto.search.SearchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
@Service
public class CompanyServiceImpl extends GenericServiceImpl<Company, UUID> implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public Page<CompanyDto> searchByPage(SearchDto dto) {
        if (dto == null) {
            return null;
        }
        int pageIndex = dto.getPageIndex() > 0 ? dto.getPageIndex() - 1 : 0;
        int pageSize = dto.getPageSize() > 0 ? dto.getPageSize() : 10;
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return companyRepository.searchByPage(dto.getKeyword(), pageable);
    }

    @Override
    public CompanyDto getById(UUID id) {
        if (id != null) {
            Company entity = companyRepository.findById(id).orElse(null);
            if (entity != null) {
                return new CompanyDto(entity);
            }
        }
        return null;
    }

    @Override
    public CompanyDto saveOrUpdate(CompanyDto dto) {
        if (dto == null) {
            return null;
        }

        Company entity = null;
        if (dto.getId() != null) {
            entity = companyRepository.findById(dto.getId()).orElse(null);
        }

        if (entity == null) {
            entity = new Company();
        }

        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setAddress(dto.getAddress());

        entity = companyRepository.save(entity);
        return new CompanyDto(entity);
    }

    @Override
    public Boolean deleteById(UUID id) {
        if (id != null && companyRepository.existsById(id)) {
            companyRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Boolean checkCode(UUID id, String code) {
        if (code != null) {
            Long count = companyRepository.countByCode(code, id);
            return count != null && count > 0;
        }
        return false;
    }
}
