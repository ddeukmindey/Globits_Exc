package com.globits.hr.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.hr.domain.ShiftWork;
import com.globits.hr.domain.ShiftWorkTimePeriod;
import com.globits.hr.dto.ShiftWorkDto;
import com.globits.hr.dto.ShiftWorkTimePeriodDto;
import com.globits.hr.dto.search.SearchDto;
import com.globits.hr.repository.ShiftWorkRepository;
import com.globits.hr.repository.ShiftWorkTimePeriodRepository;
import com.globits.hr.service.ShiftWorkService;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Query;
import java.util.*;

@Service
public class ShiftWorkServiceImpl extends GenericServiceImpl<ShiftWork, UUID> implements ShiftWorkService {
    @Autowired
    private ShiftWorkRepository shiftWorkRepository;
    @Autowired
    private ShiftWorkTimePeriodRepository shiftWorkTimePeriodRepository;

    @Override
    public Page<ShiftWorkDto> searchByPage(SearchDto dto) {
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

        String sqlCount = "select count(entity.id) from ShiftWork as entity where (1=1)";
        String sql = "select new com.globits.hr.dto.ShiftWorkDto(entity) from ShiftWork as entity where (1=1)";

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            whereClause += " AND ( UPPER(entity.name) LIKE UPPER(:text) OR UPPER(entity.code) LIKE UPPER(:text) )";
        }

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
        List<ShiftWorkDto> entities = q.getResultList();
        long count = (long) qCount.getSingleResult();

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return new PageImpl<>(entities, pageable, count);
    }

    @Override
    public Boolean checkCode(UUID id, String code) {
        if (StringUtils.hasText(code)) {
            Long count = shiftWorkRepository.checkCode(code, id);
            return count != 0L;
        }
        return null;
    }

    @Override
    public void remove(UUID id) {
        ShiftWork entity = null;
        Optional<ShiftWork> optional = shiftWorkRepository.findById(id);
        if (optional.isPresent()) {
            entity = optional.get();
        }
        if (entity != null) {
            shiftWorkRepository.delete(entity);
        }
    }

    @Override
    public ShiftWorkDto getById(UUID id) {
        if (id != null) {
            Optional<ShiftWork> religion = shiftWorkRepository.findById(id);
            if (religion.isPresent()) {
                ShiftWork entity = religion.get();
                return new ShiftWorkDto(entity);
            }
            return null;
        }
        return null;
    }

    @Override
    public List<ShiftWorkDto> getAll() {
        List<ShiftWork> allTimePeriod = shiftWorkRepository.findAll();
        List<ShiftWorkDto> models = new ArrayList<>();
        for (ShiftWork workTimePeriod : allTimePeriod) {
            models.add(new ShiftWorkDto(workTimePeriod));
        }
        return models;
    }

    @Override
    public ShiftWorkDto saveOrUpdate(UUID id, ShiftWorkDto dto) {
        if (dto != null && dto.getCode() != null && StringUtils.hasText(dto.getCode())) {
            LocalDateTime currentDate = LocalDateTime.now();
            String currentUserName = "Unknow User";
            ShiftWork entity = null;
            if (dto.getId() != null) {
                if (dto.getId() != null && !dto.getId().equals(id)) {
                    return null;
                }

                Optional<ShiftWork> shiftWorkOptional = shiftWorkRepository.findById(id);
                if (shiftWorkOptional.isPresent()) {
                    entity = shiftWorkOptional.get();
                }
                if (entity != null) {
                    entity.setModifyDate(LocalDateTime.now());
                }
            }
            if (entity == null) {
                entity = new ShiftWork();
                entity.setCreateDate(LocalDateTime.now());
                entity.setModifyDate(LocalDateTime.now());
            }
            entity.setCode(dto.getCode());
            entity.setName(dto.getName());
            entity.setTotalHours(dto.getTotalHours());

            Set<ShiftWorkTimePeriod> timePeriods = new HashSet<>();
            if (dto.getTimePeriods() != null && dto.getTimePeriods().size() > 0) {
                for (ShiftWorkTimePeriodDto timePeriodDto : dto.getTimePeriods()) {
                    ShiftWorkTimePeriod timePeriod = null;
                    if (timePeriodDto != null && timePeriodDto.getId() != null) {
                        Optional<ShiftWorkTimePeriod> timePeriodOptional = shiftWorkTimePeriodRepository.findById(timePeriodDto.getId());
                        if (timePeriodOptional.isPresent()){
                            timePeriod = timePeriodOptional.get();
                        }
                    }
                    if (timePeriod == null) {
                        timePeriod = new ShiftWorkTimePeriod();
                        timePeriod.setShiftWork(entity);
                        timePeriod.setCreateDate(currentDate);
                        timePeriod.setCreatedBy(currentUserName);

                    }
                    if (timePeriodDto != null) {
                        timePeriod = timePeriodDto.toEntity(timePeriodDto, timePeriod);
                    }
                    timePeriods.add(timePeriod);
                }
            }
            if (entity.getTimePeriods() != null) {
                entity.getTimePeriods().clear();
                entity.getTimePeriods().addAll(timePeriods);
            } else {
                entity.setTimePeriods(timePeriods);
            }
            entity = shiftWorkRepository.save(entity);
            return new ShiftWorkDto(entity);
        }
        return null;
    }
}
