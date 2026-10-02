package com.globits.hr.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.hr.domain.WorkingStatus;
import com.globits.hr.dto.WorkingStatusDto;
import com.globits.hr.dto.search.SearchDto;
import com.globits.hr.repository.WorkingStatusRepository;
import com.globits.hr.service.WorkingStatusService;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class WorkingStatusServiceImpl extends GenericServiceImpl<WorkingStatus, UUID> implements WorkingStatusService {
    @Autowired
    WorkingStatusRepository workingStatusRepository;

    @Override
    public Boolean deleteWorkingStatus(UUID id) {
        WorkingStatus workingStatus = this.findById(id);
        if (workingStatus != null) {
            workingStatusRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public WorkingStatusDto getWorkingStatus(UUID id) {
        WorkingStatus workingStatus = this.findById(id);
        if (workingStatus != null) {
            return new WorkingStatusDto(workingStatus);
        }
        return null;
    }

    @Override
    public WorkingStatusDto saveOrUpdate(UUID id, WorkingStatusDto dto) {
        if (dto != null && dto.getCode() != null && StringUtils.hasText(dto.getCode())) {
            WorkingStatus entity = null;
            if (dto.getId() != null) {
                if (dto.getId() != null && !dto.getId().equals(id)) {
                    return null;
                }

                Optional<WorkingStatus> workingStatusOptional = workingStatusRepository.findById(id);
                if (workingStatusOptional.isPresent()) {
                    entity = workingStatusOptional.get();
                }
                if (entity != null) {
                    entity.setModifyDate(LocalDateTime.now());
                }
            }
            if (entity == null) {
                entity = new WorkingStatus();
                entity.setCreateDate(LocalDateTime.now());
                entity.setModifyDate(LocalDateTime.now());
            }
            entity.setCode(dto.getCode());
            entity.setName(dto.getName());
            entity.setStatusValue(dto.getStatusValue());

            entity = workingStatusRepository.save(entity);
            return new WorkingStatusDto(entity);
        }
        return null;
    }

    @Override
    public Page<WorkingStatusDto> searchByPage(SearchDto dto) {
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
        String orderBy = " ORDER BY entity.createDate DESC";
        String sqlCount = "select count(entity.id) from WorkingStatus as entity where (1=1)   ";
        String sql = "select new com.globits.hr.dto.WorkingStatusDto(entity) from WorkingStatus as entity where (1=1)  ";
        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            whereClause += " AND ( entity.name LIKE :text OR entity.code LIKE :text )";
        }
        sql += whereClause + orderBy;
        sqlCount += whereClause;

        Query q = manager.createQuery(sql, WorkingStatusDto.class);
        Query qCount = manager.createQuery(sqlCount);

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            q.setParameter("text", '%' + dto.getKeyword() + '%');
            qCount.setParameter("text", '%' + dto.getKeyword() + '%');
        }
        int startPosition = pageIndex * pageSize;
        q.setFirstResult(startPosition);
        q.setMaxResults(pageSize);
        List<WorkingStatusDto> entities = q.getResultList();
        long count = (long) qCount.getSingleResult();

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return new PageImpl<>(entities, pageable, count);
    }

    @Override
    public Boolean checkCode(UUID id, String code) {
        if (StringUtils.hasText(code)) {
            Long count = workingStatusRepository.checkCode(code, id);
            return count != 0L;
        }
        return null;
    }


}
