package com.globits.hr.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.hr.domain.ShiftWorkTimePeriod;
import com.globits.hr.dto.ShiftWorkDto;
import com.globits.hr.dto.ShiftWorkTimePeriodDto;
import com.globits.hr.dto.search.SearchDto;
import com.globits.hr.repository.ShiftWorkTimePeriodRepository;
import com.globits.hr.service.ShiftWorkTimePeriodService;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ShiftWorkTimePeriodServiceImpl extends GenericServiceImpl<ShiftWorkTimePeriod, UUID> implements ShiftWorkTimePeriodService {
    @Autowired
    private ShiftWorkTimePeriodRepository timePeriodRepository;


    @Override
    public Page<ShiftWorkTimePeriodDto> searchByPage(SearchDto dto) {
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

        String orderBy = " ORDER BY entity.name DESC";
        if (dto.getOrderBy() != null && StringUtils.hasLength(dto.getOrderBy().toString()))
            orderBy = " ORDER BY entity." + dto.getOrderBy() + " ASC";

        String sqlCount = "select count(entity.id) from ShiftWorkTimePeriod as entity where (1=1)";
        String sql = "select new com.globits.hr.dto.ShiftWorkTimePeriodDto(entity) from ShiftWorkTimePeriod as entity where (1=1)";

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword()))
            whereClause += " AND ( UPPER(entity.name) LIKE UPPER(:text) OR UPPER(entity.code) LIKE UPPER(:text) )";

        sql += whereClause + orderBy;
        sqlCount += whereClause;

        Query q = manager.createQuery(sql, ShiftWorkDto.class);
        Query qCount = manager.createQuery(sqlCount);

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            q.setParameter("text", '%' + dto.getKeyword() + '%');
            qCount.setParameter("text", '%' + dto.getKeyword() + '%');
        }
        int startPosition = pageIndex * pageSize;
        q.setFirstResult(startPosition);
        q.setMaxResults(pageSize);
        List<ShiftWorkTimePeriodDto> entities = q.getResultList();
        long count = (long) qCount.getSingleResult();

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return new PageImpl<>(entities, pageable, count);
    }

    @Override
    public ShiftWorkTimePeriodDto saveOne(ShiftWorkTimePeriodDto dto, UUID id) {
        if (dto != null) {
            ShiftWorkTimePeriod entity = null;
            if (id != null) {
                if (dto.getId() != null && !dto.getId().equals(id)) {
                    return null;
                }
                Optional<ShiftWorkTimePeriod> optional = timePeriodRepository.findById(id);
                if (optional.isPresent()) {
                    entity = optional.get();
                }
                if (entity != null) {
                    entity.setModifyDate(LocalDateTime.now());
                }
            }
            if (entity == null) {
                entity = new ShiftWorkTimePeriod();
                entity.setCreateDate(LocalDateTime.now());
                entity.setModifyDate(LocalDateTime.now());
            }
            entity.setStartTime(dto.getStartTime());
            entity.setEndTime(dto.getEndTime());
            entity = timePeriodRepository.save(entity);
            return new ShiftWorkTimePeriodDto(entity);
        }
        return null;
    }

    @Override
    public void remove(UUID id) {
        ShiftWorkTimePeriod entity = null;
        Optional<ShiftWorkTimePeriod> optional = timePeriodRepository.findById(id);
        if (optional.isPresent()) {
            entity = optional.get();
        }
        if (entity != null) {
            timePeriodRepository.delete(entity);
        }
    }

    @Override
    public ShiftWorkTimePeriodDto getById(UUID id) {
        if (id != null) {
            Optional<ShiftWorkTimePeriod> religion = timePeriodRepository.findById(id);
            if (religion.isPresent()) {
                ShiftWorkTimePeriod entity = religion.get();
                return new ShiftWorkTimePeriodDto(entity);
            }
            return null;
        }
        return null;
    }

    @Override
    public List<ShiftWorkTimePeriodDto> getAll() {
        List<ShiftWorkTimePeriod> allTimePeriod = timePeriodRepository.findAll();
        List<ShiftWorkTimePeriodDto> models = new ArrayList<>();
        for (ShiftWorkTimePeriod workTimePeriod : allTimePeriod) {
            models.add(new ShiftWorkTimePeriodDto(workTimePeriod));
        }
        return models;
    }
}
