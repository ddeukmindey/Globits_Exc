package com.globits.hr.timesheet.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.globits.hr.dto.search.SearchDto;
import com.globits.hr.timesheet.domain.Project;
import com.globits.hr.timesheet.domain.ProjectActivity;
import com.globits.hr.timesheet.dto.ProjectActivityDto;
import com.globits.hr.timesheet.repository.ProjectActivityRepository;
import com.globits.hr.timesheet.repository.ProjectRepository;
import com.globits.hr.timesheet.service.ProjectActivityService;

@Service
public class ProjectActivityServiceImpl implements ProjectActivityService {
    @PersistenceContext
    EntityManager manager;
    @Autowired
    ProjectActivityRepository repos;
    @Autowired
    ProjectRepository projectRepos;

    @Override
    public ProjectActivityDto saveOrUpdate(UUID id, ProjectActivityDto dto) {
        if (dto != null && dto.getCode() != null && StringUtils.hasText(dto.getCode())) {
            ProjectActivity entity = null;
            if (dto.getId() != null) {
                if (dto.getId() != null && !dto.getId().equals(id)) {
                    return null;
                }
                Optional<ProjectActivity> projectActivityOptional = repos.findById(id);
                if (projectActivityOptional.isPresent()) {
                    entity = projectActivityOptional.get();
                }
                if (entity != null) {
                    entity.setModifyDate(LocalDateTime.now());
                }
            }
            if (entity == null) {
                entity = new ProjectActivity();
                entity.setCreateDate(LocalDateTime.now());
                entity.setModifyDate(LocalDateTime.now());
            }
            entity.setCode(dto.getCode());
            entity.setName(dto.getName());
            if (dto.getProject() != null) {
                if (dto.getProject().getId() != null) {
                    Optional<Project> projectOptional = projectRepos.findById(dto.getProject().getId());
                    if (projectOptional.isPresent()) {
                        Project project = projectOptional.get();
                        entity.setProject(project);
                    }

                }
            }
            entity = repos.save(entity);
            return new ProjectActivityDto(entity);
        }
        return null;
    }

    @Override
    public Boolean delete(UUID id) {
        if (id != null) {
            repos.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public ProjectActivityDto getProjectActivity(UUID id) {
        ProjectActivity entity = null;
        Optional<ProjectActivity> projectOptional = repos.findById(id);
        if (projectOptional.isPresent()) {
            entity = projectOptional.get();
        }
        if (entity != null) {
            return new ProjectActivityDto(entity);
        }
        return null;
    }

    @Override
    public Page<ProjectActivityDto> searchByPage(SearchDto dto) {
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
        String sqlCount = "select count(entity.id) from ProjectActivity as entity where (1=1)   ";
        String sql = "select new com.globits.hr.timesheet.dto.ProjectActivityDto(entity) from ProjectActivity as entity where (1=1)  ";

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            whereClause += " AND ( entity.name LIKE :text OR entity.code LIKE :text )";
        }

        sql += whereClause + orderBy;
        sqlCount += whereClause;

        Query q = manager.createQuery(sql, ProjectActivityDto.class);
        Query qCount = manager.createQuery(sqlCount);

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            q.setParameter("text", '%' + dto.getKeyword() + '%');
            qCount.setParameter("text", '%' + dto.getKeyword() + '%');
        }
        int startPosition = pageIndex * pageSize;
        q.setFirstResult(startPosition);
        q.setMaxResults(pageSize);
        List<ProjectActivityDto> entities = q.getResultList();
        if (entities == null || entities.size() == 0) {
            return null;
        }

        long count = (long) qCount.getSingleResult();
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return new PageImpl<>(entities, pageable, count);
    }

    @Override
    public Boolean checkCode(UUID id, String code) {
        return null;
    }
}
