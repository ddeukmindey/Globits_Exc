package com.globits.hr.service.impl;

import com.globits.core.repository.DepartmentRepository;
import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.hr.domain.SalaryConfig;
import com.globits.hr.domain.SalaryConfigItem;
import com.globits.hr.domain.SalaryItem;
import com.globits.hr.dto.SalaryConfigDto;
import com.globits.hr.dto.SalaryConfigItemDto;
import com.globits.hr.dto.search.SearchDto;
import com.globits.hr.repository.SalaryConfigItemRepository;
import com.globits.hr.repository.SalaryConfigRepository;
import com.globits.hr.repository.SalaryItemRepository;
import com.globits.hr.service.SalaryConfigService;
import com.globits.security.domain.User;
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

import javax.persistence.Query;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class SalaryConfigServiceImpl extends GenericServiceImpl<SalaryConfig, UUID> implements SalaryConfigService {
    @Autowired
    SalaryConfigRepository salaryConfigRepository;

    @Autowired
    SalaryConfigItemRepository salaryConfigItemRepository;

    @Autowired
    SalaryItemRepository salaryItemRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Override
    public SalaryConfigDto saveSalaryConfig(SalaryConfigDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User modifiedUser;
        LocalDateTime currentDate = LocalDateTime.now();
        String currentUserName = "Unknown User";
        if (authentication != null) {
            modifiedUser = (User) authentication.getPrincipal();
            currentUserName = modifiedUser.getUsername();
        }
        SalaryConfig salaryConfig = null;
        if (dto != null) {
            if (dto.getId() != null)
                salaryConfig = this.findById(dto.getId());
            if (salaryConfig == null) {
                salaryConfig = new SalaryConfig();
                salaryConfig.setCreateDate(currentDate);
                salaryConfig.setCreatedBy(currentUserName);
            }
            if (dto.getCode() != null) {
                salaryConfig.setCode(dto.getCode());
            }
            if (dto.getDepartment() != null && dto.getDepartment().getId() != null) {
                salaryConfig.setDepartment(departmentRepository.getOne(dto.getDepartment().getId()));
            }

            salaryConfig.setName(dto.getName());
            salaryConfig.setCode(dto.getCode());

            salaryConfig.setModifyDate(currentDate);
            salaryConfig.setModifiedBy(currentUserName);
            if (dto.getSalaryConfigItems() != null && dto.getSalaryConfigItems().size() > 0) {
                HashSet<SalaryConfigItem> salaryConfigItems = new HashSet<>();
                for (SalaryConfigItemDto sDto : dto.getSalaryConfigItems()) {
                    SalaryConfigItem salaryConfigItem = null;
                    if (sDto.getId() != null) {
                        Optional<SalaryConfigItem> optional = salaryConfigItemRepository.findById(sDto.getId());
                        if (optional.isPresent()) {
                            salaryConfigItem = optional.get();
                        }
                    }
                    if (salaryConfigItem == null) {
                        salaryConfigItem = new SalaryConfigItem();
                        salaryConfigItem.setCreateDate(currentDate);
                        salaryConfigItem.setCreatedBy(currentUserName);
                    }
                    salaryConfigItem.setSalaryConfig(salaryConfig);
                    if (sDto.getSalaryItem() != null && sDto.getSalaryItem().getId() != null) {
                        SalaryItem salaryItem = salaryItemRepository.getOne(sDto.getSalaryItem().getId());
                        salaryConfigItem.setSalaryItem(salaryItem);
                    }
                    salaryConfigItem.setFormula(sDto.getFormula());
                    salaryConfigItem.setItemOrder(sDto.getItemOrder());
                    salaryConfigItems.add(salaryConfigItem);
                }
                if (salaryConfig.getSalaryConfigItems() != null) {
                    salaryConfig.getSalaryConfigItems().clear();
                    salaryConfig.getSalaryConfigItems().addAll(salaryConfigItems);
                } else {
                    salaryConfig.setSalaryConfigItems(salaryConfigItems);
                }
            }
            salaryConfig = salaryConfigRepository.save(salaryConfig);
            return new SalaryConfigDto(salaryConfig);
        }
        return null;
    }

    @Override
    public Boolean deleteSalaryConfig(UUID id) {
        SalaryConfig salaryConfig = this.findById(id);
        if (salaryConfig != null) {
            salaryConfigRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public SalaryConfigDto getSalaryConfig(UUID id) {
        SalaryConfig salaryConfig = this.findById(id);
        if (salaryConfig != null) {
            return new SalaryConfigDto(salaryConfig);
        }
        return null;
    }

    @Override
    public int deleteSalaryConfig(List<SalaryConfigDto> dtos) {
        if (dtos == null) {
            return 0;
        }
        int ret = 0;
        for (SalaryConfigDto dto : dtos) {
            if (dto.getId() != null) {
                salaryConfigRepository.deleteById(dto.getId());
            }
            ret++;
        }
        return ret;
    }

    @Override
    public Page<SalaryConfigDto> searchByPage(SearchDto dto) {
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

        String sqlCount = "select count(entity.id) from SalaryConfig as entity where (1=1) ";
        String sql = "select new  com.globits.hr.dto.SalaryConfigDto(entity) from SalaryConfig as entity where (1=1) ";

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            whereClause += " AND ( entity.name LIKE :text OR entity.code LIKE :text OR entity.department.name LIKE :text ) ";
        }


        sql += whereClause + orderBy;
        sqlCount += whereClause;

        Query q = manager.createQuery(sql, SalaryConfigDto.class);
        Query qCount = manager.createQuery(sqlCount);

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            q.setParameter("text", '%' + dto.getKeyword() + '%');
            qCount.setParameter("text", '%' + dto.getKeyword() + '%');
        }
        int startPosition = pageIndex * pageSize;
        q.setFirstResult(startPosition);
        q.setMaxResults(pageSize);
        List<SalaryConfigDto> entities = q.getResultList();
        long count = (long) qCount.getSingleResult();

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return new PageImpl<>(entities, pageable, count);
    }

    @Override
    public Boolean checkCode(UUID id, String code) {
        if (StringUtils.hasText(code)) {
            Long count = salaryConfigRepository.checkCode(code, id);
            return count != 0L;
        }
        return null;
    }


}
