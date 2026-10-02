package com.globits.hr.service.impl;

import java.util.List;
import java.util.UUID;

import javax.persistence.Query;

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

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.hr.domain.SalaryItem;
import com.globits.hr.dto.SalaryItemDto;
import com.globits.hr.dto.SearchSalaryItemDto;
import com.globits.hr.dto.search.SearchDto;
import com.globits.hr.repository.SalaryItemRepository;
import com.globits.hr.service.SalaryItemService;

@Transactional
@Service
public class SalaryItemServiceImpl extends GenericServiceImpl<SalaryItem, UUID> implements SalaryItemService {

    @Autowired
    SalaryItemRepository salaryItemRepository;

    @Override
    public SalaryItemDto saveSalaryItem(SalaryItemDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User modifiedUser;
        LocalDateTime currentDate = LocalDateTime.now();
        String currentUserName = "Unknown User";
        if (authentication != null) {
            modifiedUser = (User) authentication.getPrincipal();
            currentUserName = modifiedUser.getUsername();
        }

        SalaryItem salaryItem = null;
        if (dto != null) {
            if (dto.getId() != null)
                salaryItem = this.findById(dto.getId());
            if (salaryItem == null) {
                salaryItem = new SalaryItem();
                salaryItem.setCreateDate(currentDate);
                salaryItem.setCreatedBy(currentUserName);
            }
            if (dto.getCode() != null) {
                salaryItem.setCode(dto.getCode());
            }
            salaryItem.setName(dto.getName());
            salaryItem.setFormula(dto.getFormula());
            salaryItem.setIsDefault(dto.getIsDefault());
            salaryItem.setIsActive(dto.getIsActive());
            salaryItem.setType(dto.getType());
            salaryItem.setDefaultValue(dto.getDefaultValue());
            salaryItem.setModifyDate(currentDate);
            salaryItem.setModifiedBy(currentUserName);

            salaryItem = salaryItemRepository.save(salaryItem);
            return new SalaryItemDto(salaryItem);
        }
        return null;
    }

    @Override
    public Boolean deleteSalaryItem(UUID id) {
        SalaryItem salaryItem = this.findById(id);
        if (salaryItem != null) {
            salaryItemRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public SalaryItemDto getSalaryItem(UUID id) {
        SalaryItem salaryItem = this.findById(id);
        if (salaryItem != null) {
            return new SalaryItemDto(salaryItem);
        }
        return null;
    }

    @Override
    public Page<SalaryItemDto> searchSalaryItem(SearchSalaryItemDto dto, int pageIndex, int pageSize) {
        String name = '%' + dto.getName() + '%';
        String code = '%' + dto.getCode() + '%';
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        return salaryItemRepository.searchByPage(name, code, pageable);
    }

    @Override
    public Page<SalaryItemDto> searchByPage(SearchDto dto) {
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

        String sqlCount = "select count(entity.id) from SalaryItem as entity where (1=1) ";
        String sql = "select new  com.globits.hr.dto.SalaryItemDto(entity) from SalaryItem as entity where (1=1) ";

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            whereClause += " AND ( entity.name LIKE :text OR entity.code LIKE :text ) ";
        }
        sql += whereClause + orderBy;
        sqlCount += whereClause;

        Query q = manager.createQuery(sql, SalaryItemDto.class);
        Query qCount = manager.createQuery(sqlCount);

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            q.setParameter("text", '%' + dto.getKeyword() + '%');
            qCount.setParameter("text", '%' + dto.getKeyword() + '%');
        }
        int startPosition = pageIndex * pageSize;
        q.setFirstResult(startPosition);
        q.setMaxResults(pageSize);
        List<SalaryItemDto> entities = q.getResultList();
        long count = (long) qCount.getSingleResult();

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return new PageImpl<>(entities, pageable, count);
    }

    @Override
    public Boolean checkCode(UUID id, String code) {
        if (StringUtils.hasText(code)) {
            Long count = salaryItemRepository.checkCode(code, id);
            return count != 0L;
        }
        return null;
    }
}
