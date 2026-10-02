package com.globits.resourceserver.sample.service.impl;

import java.util.UUID;
import java.util.List;
import javax.transaction.Transactional;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.resourceserver.sample.dto.ColorDto;
import com.globits.resourceserver.sample.dto.SearchDto;
import com.globits.resourceserver.sample.service.ColorService;
import com.globits.resourceserver.sample.dao.ColorRepository;
import com.globits.resourceserver.sample.model.Color;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Transactional
@Service
public class ColorServiceImpl extends GenericServiceImpl<Color, UUID> implements ColorService {

	@Autowired
	private ColorRepository colorRepository;

	@Override
	public Page<ColorDto> pagingColors(SearchDto dto) {
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
		String orderBy = " ";

		String sqlCount = "select count(entity.id) from Color as entity where (1=1) ";
		String sql = "select new com.globits.resourceserver.sample.dto.ColorDto(entity) from Color as entity where (1=1) ";

		if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
			whereClause += " AND (entity.value LIKE :keyWord OR entity.code LIKE :keyWord ) ";
		}
		sql += whereClause + orderBy;
		sqlCount += whereClause;
		Query q = manager.createQuery(sql, ColorDto.class);
		Query qCount = manager.createQuery(sqlCount);

		if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
			q.setParameter("keyWord", '%' + dto.getKeyword().trim() + '%');
			qCount.setParameter("keyWord", '%' + dto.getKeyword().trim() + '%');
		}
		int startPosition = pageIndex * pageSize;
		q.setFirstResult(startPosition);
		q.setMaxResults(pageSize);

		List<ColorDto> entities = q.getResultList();
		long count = (long) qCount.getSingleResult();

		Pageable pageable = PageRequest.of(pageIndex, pageSize);
		Page<ColorDto> result = new PageImpl<ColorDto>(entities, pageable, count);

		return result;
	}

	@Override
	public ColorDto getColor(UUID id) {
		Color entity = colorRepository.getOne(id);
		if (entity != null) {
			return new ColorDto(entity);
		}
		return null;
	}

	@Override
	public ColorDto createOrEditColor(UUID id, ColorDto dto) {
		if (dto != null) {
			Color entity = null;
			if (id != null) {
				entity = colorRepository.getOne(id);
			}
			if (entity == null) {
				entity = new Color();
			}
			entity.setCode(dto.getCode());
			entity.setValue(dto.getValue());

			entity = colorRepository.save(entity);
			if (entity != null) {
				return new ColorDto(entity);
			}

		}
		return null;
	}

	@Override
	public Boolean deleteColor(UUID id) {
		if (id != null) {
			colorRepository.deleteById(id);
			return true;
		}
		return false;
	}

}
