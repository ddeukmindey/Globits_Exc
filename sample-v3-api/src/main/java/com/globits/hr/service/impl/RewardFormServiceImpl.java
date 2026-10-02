package com.globits.hr.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.hr.domain.RewardForm;
import com.globits.hr.dto.RewardFormDto;
import com.globits.hr.dto.search.SearchDto;
import com.globits.hr.repository.RewardFormRepository;
import com.globits.hr.service.RewardFormService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class RewardFormServiceImpl extends GenericServiceImpl<RewardForm, UUID> implements RewardFormService {

    @Autowired
    private RewardFormRepository rewardFormRepository;

    @Override
    public Page<RewardFormDto> searchByPage(SearchDto dto) {
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
        String sqlCount = "select count(entity.id) from RewardForm as entity where (1=1) ";
        String sql = "select new  com.globits.hr.dto.RewardFormDto(entity) from RewardForm as entity where (1=1) ";

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            whereClause += " AND ( entity.name LIKE :text OR entity.code LIKE :text ) ";
        }
        sql += whereClause + orderBy;
        sqlCount += whereClause;
        Query q = manager.createQuery(sql, RewardFormDto.class);
        Query qCount = manager.createQuery(sqlCount);
        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            q.setParameter("text", '%' + dto.getKeyword() + '%');
            qCount.setParameter("text", '%' + dto.getKeyword() + '%');
        }
        int startPosition = pageIndex * pageSize;
        q.setFirstResult(startPosition);
        q.setMaxResults(pageSize);
        List<RewardFormDto> entities = q.getResultList();
        long count = (long) qCount.getSingleResult();

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return new PageImpl<>(entities, pageable, count);
    }

    @Override
    public RewardFormDto saveOrUpdate(RewardFormDto dto, UUID id) {
        if (dto != null) {
            LocalDateTime currentDate = LocalDateTime.now();
            String currentUserName = "Unknown User";
            RewardForm reward = null;
            if (id != null) {
                if (dto.getId() != null && !dto.getId().equals(id)) {
                    return null;
                }
                Optional<RewardForm> optional = rewardFormRepository.findById(id);
                if (optional.isPresent()) {
                    reward = optional.get();
                }
            }
            if (reward == null) {
                reward = new RewardForm();
                reward.setModifiedBy(currentUserName);
                reward.setModifyDate(currentDate);
                reward.setCreateDate(currentDate);
                reward.setCreatedBy(currentUserName);
            } else {
                reward.setModifiedBy(currentUserName);
                reward.setModifyDate(currentDate);
            }
            reward.setCode(dto.getCode());
            reward.setLanguageKey(dto.getLanguageKey());
            reward.setName(dto.getName());
            reward.setRewardType(dto.getRewardType());
            reward.setFormal(dto.getFormal());
            reward.setDescription(dto.getDescription());
            reward.setEvaluateYear(dto.getEvaluateYear());
            reward.setEvaluateLevel(dto.getEvaluateLevel());
            reward = rewardFormRepository.save(reward);
            return new RewardFormDto(reward);
        }
        return null;
    }

    @Override
    public void deleteOne(UUID id) {
        RewardForm reward = this.findById(id);
        if (reward != null) {
            rewardFormRepository.delete(reward);
        }
    }

    @Override
    public void delete(List<UUID> ids) {
        if (ids != null) {
            for (UUID id : ids) {
                rewardFormRepository.deleteById(id);
            }
        }
    }

    @Override
    public Boolean checkCode(UUID id, String code) {
        List<RewardForm> entities = rewardFormRepository.findByCode(code);
        if (entities != null && entities.size() > 0 && entities.get(0) != null && entities.get(0).getId() != null) {
            if (id != null && StringUtils.hasText(id.toString())) {
                return !entities.get(0).getId().equals(id);
            }
            return true;
        }
        return false;
    }

    @Override
    public RewardFormDto getOne(UUID id) {
        RewardForm entity = null;
        Optional<RewardForm> optional = rewardFormRepository.findById(id);
        if (optional.isPresent()) {
            entity = optional.get();
        }
        if (entity != null) {
            return new RewardFormDto(entity);
        }
        return null;
    }

}
