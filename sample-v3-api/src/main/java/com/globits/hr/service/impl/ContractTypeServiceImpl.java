package com.globits.hr.service.impl;

import com.globits.hr.domain.ContractType;
import com.globits.hr.dto.ContractTypeDto;
import com.globits.hr.dto.search.SearchDto;
import com.globits.hr.repository.ContractTypeRepository;
import com.globits.hr.service.ContractTypeService;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContractTypeServiceImpl implements ContractTypeService {
    @PersistenceContext
    EntityManager manager;
    @Autowired
    ContractTypeRepository contractTypeRepository;

    @Override
    public Page<ContractTypeDto> searchByPage(SearchDto dto) {
        if (dto == null) {
            return null;
        }

        int pageIndex = dto.getPageIndex();
        int pageSize = dto.getPageSize();

        if (pageIndex > 0) {
            pageIndex--;
        } else {
            pageIndex = 0;
        }

        String whereClause = "";

        String orderBy = " ORDER BY entity.id DESC";

        String sqlCount = "select count(entity.id) from ContractType as entity where (1=1)";
        String sql = "select new com.globits.hr.dto.ContractTypeDto(entity) from ContractType as entity where (1=1) ";

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            whereClause += " AND ( UPPER(entity.code) LIKE UPPER(:text) ) OR ( UPPER(entity.name) LIKE UPPER(:text) )";
        }

        sql += whereClause + orderBy;
        sqlCount += whereClause;

        Query q = manager.createQuery(sql, ContractTypeDto.class);
        Query qCount = manager.createQuery(sqlCount);

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            q.setParameter("text", '%' + dto.getKeyword() + '%');
            qCount.setParameter("text", '%' + dto.getKeyword() + '%');
        }

        int startPosition = pageIndex * pageSize;
        q.setFirstResult(startPosition);
        q.setMaxResults(pageSize);
        List<ContractTypeDto> entities = q.getResultList();
        long count = (long) qCount.getSingleResult();

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return new PageImpl<>(entities, pageable, count);
    }

    @Override
    public ContractTypeDto saveOrUpdate(ContractTypeDto dto, UUID id) {
        if (dto != null) {
            ContractType entity = null;
            if (id != null) {
                if (dto.getId() != null && !dto.getId().equals(id)) {
                    return null;
                }
                Optional<ContractType> civil = contractTypeRepository.findById(dto.getId());
                if (civil.isPresent()) {
                    entity = civil.get();
                }
                if (entity != null) {
                    entity.setModifyDate(LocalDateTime.now());
                }
            }
            if (entity == null) {
                entity = new ContractType();
                entity.setCreateDate(LocalDateTime.now());
                entity.setModifyDate(LocalDateTime.now());
            }
            entity.setCode(dto.getCode());
            entity.setName(dto.getName());
            entity.setLanguageKey(dto.getLanguageKey());
            entity = contractTypeRepository.save(entity);
            return new ContractTypeDto(entity);
        }
        return null;
    }

    @Override
    public ContractTypeDto getOne(UUID id) {
        ContractType entity = null;
        Optional<ContractType> civil = contractTypeRepository.findById(id);
        if (civil.isPresent()) {
            entity = civil.get();
        }
        if (entity != null) {
            return new ContractTypeDto(entity);
        }
        return null;
    }

    @Override
    public void deleteOne(UUID id) {
        if (id != null) {
            contractTypeRepository.deleteById(id);
        }

    }

    @Override
    public Boolean checkCode(UUID id, String code) {
        List<ContractType> entities = contractTypeRepository.findByCode(code);
        if (entities != null && entities.size() > 0 && entities.get(0) != null && entities.get(0).getId() != null) {
            if (id != null && StringUtils.hasText(id.toString())) {
                return !entities.get(0).getId().equals(id);
            }
            return true;
        }
        return false;
    }
}
