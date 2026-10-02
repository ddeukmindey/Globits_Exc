package com.globits.hr.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.hr.domain.PositionTitle;
import com.globits.hr.dto.DepartmentsTreeDto;
import com.globits.hr.dto.PositionTitleDto;

@Repository
public interface PositionTitleRepository extends JpaRepository<PositionTitle, UUID> {
    @Query("select p from PositionTitle p where p.code = ?1")
    List<PositionTitle> findByCode(String code);

    @Query("select count(entity.id) from PositionTitle entity where entity.code =?1 and (entity.id <> ?2 or ?2 is null) ")
    Long checkCode(String code, UUID id);

    @Query("select new  com.globits.hr.dto.DepartmentsTreeDto(entity) from Department entity where entity.parent is null")
    Page<DepartmentsTreeDto> getByRoot(Pageable pageable);

    @Query("select new com.globits.hr.dto.PositionTitleDto(p) from PositionTitle p ")
    List<PositionTitleDto> getListPositionTitle();

    @Query("select new com.globits.hr.dto.PositionTitleDto(p) from PositionTitle p ")
    Page<PositionTitleDto> getPage(Pageable pageable);
}
