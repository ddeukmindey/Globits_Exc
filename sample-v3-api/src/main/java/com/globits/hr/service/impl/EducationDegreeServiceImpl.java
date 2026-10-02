package com.globits.hr.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.hr.domain.EducationDegree;
import com.globits.hr.dto.EducationDegreeDto;
import com.globits.hr.dto.search.SearchDto;
import com.globits.hr.repository.EducationDegreeRepository;
import com.globits.hr.service.EducationDegreeService;
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
public class EducationDegreeServiceImpl extends GenericServiceImpl<EducationDegree, UUID>
        implements EducationDegreeService {

    @Autowired
    EducationDegreeRepository EducationDegreeRepository;

    @Override
    public EducationDegreeDto saveOrUpdate(UUID id, EducationDegreeDto dto) {
        LocalDateTime currentDate = LocalDateTime.now();
        String currentUserName = "Unknown User";
        if (dto != null) {
            EducationDegree EducationDegree = null;
            if (dto.getId() != null) {
                if (dto.getId() != null && !dto.getId().equals(id)) {
                    return null;
                }
                Optional<EducationDegree> optional = EducationDegreeRepository.findById(dto.getId());
                if (optional.isPresent()) {
                    EducationDegree = optional.get();
                }
            }
            if (EducationDegree == null) {
                EducationDegree = new EducationDegree();
                EducationDegree.setCreateDate(currentDate);
                EducationDegree.setCreatedBy(currentUserName);
            }
            if (dto.getCode() != null) {
                EducationDegree.setCode(dto.getCode());
            }
            EducationDegree.setName(dto.getName());

            EducationDegree.setModifyDate(currentDate);
            EducationDegree.setModifiedBy(currentUserName);

            EducationDegree = EducationDegreeRepository.save(EducationDegree);
            return new EducationDegreeDto(EducationDegree);
        }
        return null;
    }

    @Override
    public Boolean deleteEducationDegree(UUID id) {
        EducationDegree EducationDegree = null;
        Optional<EducationDegree> optional = EducationDegreeRepository.findById(id);
        if (optional.isPresent()) {
            EducationDegree = optional.get();
        }
        if (EducationDegree != null) {
            EducationDegreeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public EducationDegreeDto getEducationDegree(UUID id) {
        EducationDegree EducationDegree = null;
        Optional<EducationDegree> optional = EducationDegreeRepository.findById(id);
        if (optional.isPresent()) {
            EducationDegree = optional.get();
        }
        if (EducationDegree != null) {
            return new EducationDegreeDto(EducationDegree);
        }
        return null;
    }

    @Override
    public Page<EducationDegreeDto> searchByPage(SearchDto dto) {
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

        String sqlCount = "select count(entity.id) from EducationDegree as entity where (1=1) ";
        String sql = "select new  com.globits.hr.dto.EducationDegreeDto(entity) from EducationDegree as entity where (1=1) ";

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            whereClause += " AND ( entity.name LIKE :text OR entity.code LIKE :text ) ";
        }

        sql += whereClause + orderBy;
        sqlCount += whereClause;

        Query q = manager.createQuery(sql, EducationDegreeDto.class);
        Query qCount = manager.createQuery(sqlCount);

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            q.setParameter("text", '%' + dto.getKeyword() + '%');
            qCount.setParameter("text", '%' + dto.getKeyword() + '%');
        }

        int startPosition = pageIndex * pageSize;
        q.setFirstResult(startPosition);
        q.setMaxResults(pageSize);
        List<EducationDegreeDto> entities = q.getResultList();
        long count = (long) qCount.getSingleResult();

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return new PageImpl<>(entities, pageable, count);
    }

    @Override
    public Boolean checkCode(UUID id, String code) {
        if (StringUtils.hasText(code)) {
            Long count = EducationDegreeRepository.checkCode(id, code);
            return count != 0L;
        }
        return null;
    }

}
