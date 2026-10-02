package com.globits.hr.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.Query;

import com.globits.core.domain.Country;
import com.globits.core.domain.Ethnics;
import com.globits.core.domain.Religion;
import com.globits.core.dto.EthnicsDto;
import com.globits.core.dto.ReligionDto;
import com.globits.hr.domain.*;
import com.globits.hr.dto.StaffEducationHistoryDto;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.hr.dto.DepartmentsTreeDto;
import com.globits.hr.dto.PositionTitleDto;
import com.globits.hr.dto.search.SearchDto;
import com.globits.hr.repository.PositionTitleRepository;
import com.globits.hr.service.PositionTitleService;
import com.globits.security.domain.User;

@Transactional
@Service
public class PositionTitleServiceImpl extends GenericServiceImpl<PositionTitle, UUID> implements PositionTitleService {
    @Autowired
    private PositionTitleRepository positionTitleRepository;

    @Override
    public PositionTitleDto saveTitle(PositionTitleDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User modifiedUser;
        LocalDateTime currentDate = LocalDateTime.now();
        String currentUserName = "Unknown User";
        if (authentication != null) {
            modifiedUser = (User) authentication.getPrincipal();
            currentUserName = modifiedUser.getUsername();
        }

        PositionTitle title = null;
        if (dto.getId() != null) {
            title = this.findById(dto.getId());
        }
        if (title == null) {
            title = new PositionTitle();
            title.setCreateDate(currentDate);
            title.setCreatedBy(currentUserName);
        }
        title.setModifiedBy(currentUserName);
        title.setModifyDate(currentDate);
        if (dto.getCode() != null) {
            title.setCode(dto.getCode());
        }
        if (dto.getName() != null) {
            title.setName(dto.getName());
        }
        title.setDescription(dto.getDescription());
        title.setType(dto.getType());
        title.setPositionCoefficient(dto.getPositionCoefficient());

        title = positionTitleRepository.save(title);
        return new PositionTitleDto(title);
    }

    @Override
    public PositionTitleDto getTitle(UUID id) {
        PositionTitle entity = this.findById(id);
        if (entity == null) {
            return null;
        } else {
            return new PositionTitleDto(entity);
        }
    }

    @Override
    public Boolean removeTitle(UUID id) {
        PositionTitle title = this.findById(id);
        if (title != null) {
            positionTitleRepository.delete(title);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean deleteMultiple(PositionTitleDto[] dtos) {
        boolean ret = true;

        if (dtos == null || dtos.length <= 0) {
            return ret;
        }
        ArrayList<PositionTitle> titles = new ArrayList<PositionTitle>();
        for (PositionTitleDto dto : dtos) {

            PositionTitle entity = this.findById(dto.getId());

            if (entity == null) {
                throw new RuntimeException();
            }
            titles.add(entity);
        }
        positionTitleRepository.deleteInBatch(titles);
        return ret;
    }

    @Override
    public Page<PositionTitleDto> searchByPage(SearchDto dto) {
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
        String orderBy = " ORDER BY entity.createDate ";
        String sqlCount = "select count(entity.id) from PositionTitle as entity where (1=1) ";
        String sql = "select new  com.globits.hr.dto.PositionTitleDto(entity) from PositionTitle as entity where (1=1) ";
        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            whereClause += " AND ( entity.name LIKE :text OR entity.code LIKE :text OR entity.type LIKE :text OR entity.description LIKE :text) ";
        }
        sql += whereClause + orderBy;
        sqlCount += whereClause;
        Query q = manager.createQuery(sql, PositionTitleDto.class);
        Query qCount = manager.createQuery(sqlCount);

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            q.setParameter("text", '%' + dto.getKeyword() + '%');
            qCount.setParameter("text", '%' + dto.getKeyword() + '%');
        }

        int startPosition = pageIndex * pageSize;
        q.setFirstResult(startPosition);
        q.setMaxResults(pageSize);
        List<PositionTitleDto> entities = q.getResultList();
        long count = (long) qCount.getSingleResult();

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return new PageImpl<>(entities, pageable, count);
    }

    @Override
    public Boolean checkCode(UUID id, String code) {
        if (StringUtils.hasText(code)) {
            Long count = positionTitleRepository.checkCode(code, id);
            return count != 0L;
        }
        return null;
    }

    @Override
    public Page<DepartmentsTreeDto> getByRoot(int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        return positionTitleRepository.getByRoot(pageable);
    }

    @Override
    public PositionTitleDto saveOrUpdate(UUID id, PositionTitleDto dto) {
        if (dto != null) {
            PositionTitle entity = null;
            if (id != null) {
                if (dto.getId() != null && !dto.getId().equals(id)) {
                    return null;
                }
                Optional<PositionTitle> religion = positionTitleRepository.findById(id);
                if (religion.isPresent()) {
                    entity = religion.get();
                }
                if (entity != null) {
                    entity.setModifyDate(LocalDateTime.now());
                }
            }
            if (entity == null) {
                entity = new PositionTitle();
                entity.setCreateDate(LocalDateTime.now());
                entity.setModifyDate(LocalDateTime.now());
            }
            entity.setCode(dto.getCode());
            entity.setName(dto.getName());
            entity.setDescription(dto.getDescription());
            entity.setType(dto.getType());
            entity.setPositionCoefficient(dto.getPositionCoefficient());
            entity = positionTitleRepository.save(entity);
            return new PositionTitleDto(entity);
        }
        return null;
    }
}
