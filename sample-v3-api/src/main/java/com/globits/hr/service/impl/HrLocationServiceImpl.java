package com.globits.hr.service.impl;

import com.globits.core.domain.Location;
import com.globits.core.dto.LocationDto;
import com.globits.core.repository.LocationRepository;
import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.hr.dto.search.SearchDto;
import com.globits.hr.service.HrLocationService;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class HrLocationServiceImpl extends GenericServiceImpl<Location, UUID> implements HrLocationService {
    @Autowired
    private LocationRepository locationRepository;

    @Override
    public Page<LocationDto> searchByPage(SearchDto dto) {
        if (dto == null)
            return null;

        int pageIndex = dto.getPageIndex();
        int pageSize = dto.getPageSize();
        if (pageIndex > 0) {
            pageIndex--;
        } else {
            pageIndex = 0;
        }

        String whereClause = "";

        String orderBy = " ORDER BY entity.name DESC";
        if (dto.getOrderBy() != null && StringUtils.hasLength(dto.getOrderBy())) {
            orderBy = " ORDER BY entity." + dto.getOrderBy() + " ASC";
        }

        String sqlCount = "select count(entity.id) from Location as entity where (1=1)";
        String sql = "select new com.globits.core.dto.LocationDto(entity) from Location as entity where (1=1)";

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            whereClause += " AND ( UPPER(entity.name) LIKE UPPER(:text) OR UPPER(entity.code) LIKE UPPER(:text) )";
        }

        sql += whereClause + orderBy;
        sqlCount += whereClause;

        Query q = manager.createQuery(sql, LocationDto.class);
        Query qCount = manager.createQuery(sqlCount);

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            q.setParameter("text", '%' + dto.getKeyword() + '%');
            qCount.setParameter("text", '%' + dto.getKeyword() + '%');
        }

        int startPosition = pageIndex * pageSize;
        q.setFirstResult(startPosition);
        q.setMaxResults(pageSize);
        List<LocationDto> entities = q.getResultList();
        long count = (long) qCount.getSingleResult();

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return new PageImpl<>(entities, pageable, count);
    }

    @Override
    public LocationDto saveOne(LocationDto dto, UUID id) {
        if (dto != null) {
            String currentUserName = "Unknown User";
            Location entity = null;
            if (id != null) {
                if (dto.getId() != null && !dto.getId().equals(id)) {
                    return null;
                }
                Optional<Location> optional = locationRepository.findById(id);
                if (optional.isPresent()) {
                    entity = optional.get();
                }
                if (entity != null) {
                    entity.setModifyDate(LocalDateTime.now());
                    entity.setModifiedBy(currentUserName);
                }
            }
            if (entity == null) {
                entity = new Location();
                entity.setCreateDate(LocalDateTime.now());
                entity.setModifyDate(LocalDateTime.now());
                entity.setCreatedBy(currentUserName);
                entity.setModifiedBy(currentUserName);
            }
            entity.setCode(dto.getCode());
            entity.setName(dto.getName());
            entity.setLatitude(dto.getLatitude());
            entity.setLongitude(dto.getLongitude());
            entity = locationRepository.save(entity);
            return new LocationDto(entity);
        }
        return null;
    }

    @Override
    public void remove(UUID id) {
        Location entity = null;
        Optional<Location> optional = locationRepository.findById(id);
        if (optional.isPresent()) {
            entity = optional.get();
        }
        if (entity != null) {
            locationRepository.delete(entity);
        }
    }

    @Override
    public Boolean checkCode(UUID id, String code) {
        String strQuery = "select count(entity.id) from Location entity where entity.code='" + code
                + "' and (entity.id <> '" + id + "' or '" + id + "' is null)";

        Query qCount = manager.createQuery(strQuery);
        long count = (long) qCount.getSingleResult();
        return count != 0L;
    }

    @Override
    public LocationDto getLocation(UUID id) {
        Optional<Location> entity = locationRepository.findById(id);
        return entity.map(LocationDto::new).orElse(null);
    }
}
