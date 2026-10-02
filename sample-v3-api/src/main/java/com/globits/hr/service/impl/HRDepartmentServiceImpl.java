package com.globits.hr.service.impl;

import com.globits.core.domain.Department;
import com.globits.core.repository.DepartmentRepository;
import com.globits.hr.domain.HRDepartment;
import com.globits.hr.dto.HRDepartmentDto;
import com.globits.hr.dto.search.SearchDto;
import com.globits.hr.repository.HRDepartmentRepository;
import com.globits.hr.service.HRDepartmentService;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class HRDepartmentServiceImpl implements HRDepartmentService {
    @PersistenceContext
    EntityManager manager;

    @Autowired
    HRDepartmentRepository repos;
    @Autowired
    DepartmentRepository departmentRepository;

    @Override
    public HRDepartmentDto saveOrUpdate(UUID id, HRDepartmentDto dto) {
        if (dto != null && dto.getCode() != null && StringUtils.hasText(dto.getCode())) {
            HRDepartment hRDepartment = null;
            if (dto.getId() != null) {
                if (dto.getId() != null && !dto.getId().equals(id)) {
                    return null;
                }
                if (dto.getCode() != null) {
                    List<HRDepartment> depart = repos.findByCode(dto.getCode());
                    if (depart != null && depart.size() > 0) {
                        hRDepartment = depart.get(0);
                    }
                }
                Optional<HRDepartment> optional = repos.findById(id);
                if (optional.isPresent()) {
                    hRDepartment = optional.get();
                }
                if (hRDepartment != null)
                    hRDepartment.setModifyDate(LocalDateTime.now());
            }
            if (hRDepartment == null) {
                hRDepartment = new HRDepartment();
                hRDepartment.setCreateDate(LocalDateTime.now());
                hRDepartment.setModifyDate(LocalDateTime.now());
            }
            hRDepartment.setCode(dto.getCode());
            hRDepartment.setName(dto.getName());
            hRDepartment.setDescription(dto.getDescription());
            hRDepartment.setFunc(dto.getFunc());
            hRDepartment.setIndustryBlock(dto.getIndustryBlock());
            hRDepartment.setFoundedDate(dto.getFoundedDate());
            hRDepartment.setFoundedNumber(dto.getFoundedNumber());
            hRDepartment.setDisplayOrder(dto.getDisplayOrder());
            if (dto.getParent() != null && dto.getParent().getId() != null) {
                Department department = null;
                Optional<Department> optional = departmentRepository.findById(dto.getParent().getId());
                if (optional.isPresent()) {
                    department = optional.get();
                }
                hRDepartment.setParent(department);
            }

            if (dto.getParentCode() != null) {
                List<HRDepartment> depart = repos.findByCode(dto.getParentCode());
                if (depart != null && depart.size() > 0) {
                    Department department = depart.get(0);
                    hRDepartment.setParent(department);
                }
            }
            hRDepartment = repos.save(hRDepartment);
            return new HRDepartmentDto(hRDepartment);
        }
        return null;
    }

    @Override
    public Boolean deleteHRDepartment(UUID id) {
        if (id != null) {
            repos.deleteById(id);
            return true;
        }
        return false;
    }


    @Override
    public HRDepartmentDto getHRDepartment(UUID id) {
        HRDepartment hRDepartment = null;
        Optional<HRDepartment> optional = repos.findById(id);
        if (optional.isPresent()) {
            hRDepartment = optional.get();
        }
        if (hRDepartment != null) {
            return new HRDepartmentDto(hRDepartment);
        }
        return null;
    }

    @Override
    public Page<HRDepartmentDto> searchByPage(SearchDto dto) {
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
        String orderBy = " ORDER BY entity.displayOrder DESC";
        String sqlCount = "select count(entity.id) from HRDepartment as entity where entity.parent is null  ";
        String sql = "select new com.globits.hr.dto.HRDepartmentDto(entity) from HRDepartment as entity where (1=1)  ";

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            whereClause += " AND ( entity.name LIKE :text OR entity.code LIKE :text )";
        }

        if (dto.getDepartmentId() != null) {
            whereClause += " AND ( entity.id = :departmentId  )";
        } else {
            whereClause += " AND ( entity.parent is null ) ";
        }
        sql += whereClause + orderBy;
        sqlCount += whereClause;

        Query q = manager.createQuery(sql, HRDepartmentDto.class);
        Query qCount = manager.createQuery(sqlCount);

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            q.setParameter("text", '%' + dto.getKeyword() + '%');
            qCount.setParameter("text", '%' + dto.getKeyword() + '%');
        }
        if (dto.getDepartmentId() != null) {
            q.setParameter("departmentId", dto.getDepartmentId());
            qCount.setParameter("departmentId", dto.getDepartmentId());
        }
        int startPosition = pageIndex * pageSize;
        q.setFirstResult(startPosition);
        q.setMaxResults(pageSize);
        List<HRDepartmentDto> entities = q.getResultList();
        long count = (long) qCount.getSingleResult();

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return new PageImpl<>(entities, pageable, count);
    }

    @Override
    public Boolean checkCode(UUID id, String code) {
        if (StringUtils.hasText(code)) {
            Long count = repos.checkCode(code, id);
            return count != 0L;
        }
        return null;
    }

    @Override
    public Page<HRDepartmentDto> pagingDepartments(SearchDto dto) {
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
        String orderBy = " ORDER BY entity.displayOrder DESC";
        String sqlCount = "select count(entity.id) from HRDepartment as entity where  (1=1)  ";
        String sql = "select new com.globits.hr.dto.HRDepartmentDto(entity) from HRDepartment as entity where (1=1)  ";

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            whereClause += " AND ( entity.name LIKE :text OR entity.code LIKE :text )";
        }
        sql += whereClause + orderBy;
        sqlCount += whereClause;

        Query q = manager.createQuery(sql, HRDepartmentDto.class);
        Query qCount = manager.createQuery(sqlCount);

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            q.setParameter("text", '%' + dto.getKeyword() + '%');
            qCount.setParameter("text", '%' + dto.getKeyword() + '%');
        }
        int startPosition = pageIndex * pageSize;
        q.setFirstResult(startPosition);
        q.setMaxResults(pageSize);
        List<HRDepartmentDto> entities = q.getResultList();
        long count = (long) qCount.getSingleResult();

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return new PageImpl<>(entities, pageable, count);
    }
}
