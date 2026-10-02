package com.globits.hr.timesheet.service.impl;

import com.globits.hr.domain.Staff;
import com.globits.hr.dto.StaffDto;
import com.globits.hr.dto.search.SearchDto;
import com.globits.hr.repository.StaffRepository;
import com.globits.hr.service.UserExtService;
import com.globits.hr.timesheet.domain.Label;
import com.globits.hr.timesheet.domain.Project;
import com.globits.hr.timesheet.domain.ProjectStaff;
import com.globits.hr.timesheet.domain.TimeSheetDetail;
import com.globits.hr.timesheet.dto.LabelDto;
import com.globits.hr.timesheet.dto.ProjectDto;
import com.globits.hr.timesheet.dto.TimeSheetDetailDto;
import com.globits.hr.timesheet.repository.LabelRepository;
import com.globits.hr.timesheet.repository.ProjectRepository;
import com.globits.hr.timesheet.service.ProjectService;
import com.globits.security.dto.RoleDto;
import com.globits.security.dto.UserDto;
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
import java.util.*;

@Service
public class ProjectServiceImpl implements ProjectService {
    @PersistenceContext
    EntityManager manager;
    @Autowired
    ProjectRepository repos;
    @Autowired
    private UserExtService userExtService;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private LabelRepository labelRepository;

    @Override
    public ProjectDto saveOrUpdate(UUID id, ProjectDto dto) {
        if (dto != null && dto.getCode() != null && StringUtils.hasText(dto.getCode())) {
            Project entity = null;
            if (dto.getId() != null) {
                if (dto.getId() != null && !dto.getId().equals(id)) {
                    return null;
                }
                Optional<Project> projectOptional = repos.findById(id);
                if (projectOptional.isPresent()) {
                    entity = projectOptional.get();
                }
                if (entity != null) {
                    entity.setModifyDate(LocalDateTime.now());
                }
            }
            if (entity == null) {
                entity = new Project();
                entity.setCreateDate(LocalDateTime.now());
                entity.setModifyDate(LocalDateTime.now());
            }
            entity.setCode(dto.getCode());
            entity.setName(dto.getName());
            entity.setDescription(dto.getDescription());
            entity.setWorkload(dto.getWorkload());
            entity.setActualWorkload(dto.getActualWorkload());
            if (dto.getProjectStaff() != null && dto.getProjectStaff().size() > 0) {
                HashSet<ProjectStaff> projectStaffList = new HashSet<>();
                for (StaffDto item : dto.getProjectStaff()) {
                    ProjectStaff projectStaff = new ProjectStaff();
                    Staff staff = null;
                    if (item != null) {
                        if (item.getId() != null) {
                            Optional<Staff> optional = staffRepository.findById(item.getId());
                            if (optional.isPresent()) {
                                staff = optional.get();
                            }
                        }
                    }
                    projectStaff.setProject(entity);
                    if (staff != null) {
                        projectStaff.setStaff(staff);
                    }
                    projectStaffList.add(projectStaff);
                }
                if (entity.getProjectStaff() != null) {
                    entity.getProjectStaff().clear();
                    entity.getProjectStaff().addAll(projectStaffList);
                } else {
                    entity.setProjectStaff(projectStaffList);// Neu chua co thi set vao
                }
            } else if (dto.getProjectStaff() != null) {// Truong hop xoa het
                if (entity.getProjectStaff() != null) {
                    entity.getProjectStaff().clear();
                }
            }

            Set<Label> labels = new HashSet<>();
            if (dto.getLabels() != null && !dto.getLabels().isEmpty()) {
                for (LabelDto labelDto : dto.getLabels()) {
                    Label label = null;
                    if (labelDto != null && labelDto.getId() != null) {
                        label = labelRepository.getOne(labelDto.getId());
                    }
                    if (label == null) {
                        label = new Label();
                        label.setProject(entity);
                    }
                    if (labelDto != null) {
                        label = labelDto.toEntity(labelDto, label);
                        label.setProject(entity);
                    }
                    labels.add(label);
                }
            }
            if (entity.getLabels() != null) {
                entity.getLabels().clear();
                entity.getLabels().addAll(labels);
            } else {
                entity.setLabels(labels);
            }
            entity = repos.save(entity);
            return new ProjectDto(entity);
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
    public ProjectDto getProject(UUID id) {
        Project entity = null;
        Optional<Project> projectOptional = repos.findById(id);
        if (projectOptional.isPresent()) {
            entity = projectOptional.get();
        }
        if (entity != null) {
            return new ProjectDto(entity);
        }
        return null;
    }

    @Override
    public Page<ProjectDto> searchByPage(SearchDto dto) {
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
        String sqlCount = "select count(entity.id) from Project as entity where (1=1)   ";
        String sql = "select new com.globits.hr.timesheet.dto.ProjectDto(entity, true) from Project as entity where (1=1)  ";

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            whereClause += " AND ( entity.name LIKE :text OR entity.code LIKE :text )";
        }

        sql += whereClause + orderBy;
        sqlCount += whereClause;

        Query q = manager.createQuery(sql, ProjectDto.class);
        Query qCount = manager.createQuery(sqlCount);

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            q.setParameter("text", '%' + dto.getKeyword() + '%');
            qCount.setParameter("text", '%' + dto.getKeyword() + '%');
        }
        int startPosition = pageIndex * pageSize;
        q.setFirstResult(startPosition);
        q.setMaxResults(pageSize);
        List<ProjectDto> entities = q.getResultList();
        if (entities == null || entities.size() == 0) {
            return null;
        }
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
    public Page<ProjectDto> getPageProjectByUsername(SearchDto dto) {
        if (dto == null) {
            return null;
        }
        boolean isRoleUser = false;
        boolean isRoleAdmin = false;
        boolean isRoleManager = false;

        UserDto user = userExtService.getCurrentUserName();
        if (user != null && user.getRoles() != null && user.getRoles().size() > 0) {
            for (RoleDto item : user.getRoles()) {
                if (item.getName() != null && "ROLE_ADMIN".equals(item.getName())) {
                    isRoleAdmin = true;
                }
                if (item.getName() != null && "HR_MANAGER".equals(item.getName())) {
                    isRoleManager = true;
                }
                if (item.getName() != null && "HR_USER".equals(item.getName())) {
                    isRoleUser = true;
                }
            }
        }
        if (isRoleAdmin) {
            isRoleManager = false;
            isRoleUser = false;
        } else {
            if (isRoleManager) {
                isRoleUser = false;
            }
        }
        int pageIndex = dto.getPageIndex();
        int pageSize = dto.getPageSize();
        if (pageIndex > 0) {
            pageIndex--;
        } else {
            pageIndex = 0;
        }

        String whereClause = "";

        String groupBy = " GROUP BY entity.project.id ";
        String sql = "select new com.globits.hr.timesheet.dto.ProjectDto(entity.project) from ProjectStaff as entity where (1=1)  ";

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            whereClause += " AND ( entity.project.name LIKE :text OR entity.project.code LIKE :text )";
        }
        if (isRoleUser && user.getUsername() != null) {
            whereClause += " and entity.staff.staffCode = :staffCode";
        }
        sql += whereClause + groupBy;

        Query query = manager.createQuery(sql, ProjectDto.class);

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            query.setParameter("text", '%' + dto.getKeyword() + '%');
        }
        if (isRoleUser && user.getUsername() != null) {
            query.setParameter("staffCode", user.getUsername());
        }
        long count = query.getResultList().size();
        int startPosition = pageIndex * pageSize;
        query.setFirstResult(startPosition);
        query.setMaxResults(pageSize);
        List<ProjectDto> entities = query.getResultList();
        if (entities == null || entities.size() == 0) {
            return null;
        }

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return new PageImpl<>(entities, pageable, count);
    }
}
