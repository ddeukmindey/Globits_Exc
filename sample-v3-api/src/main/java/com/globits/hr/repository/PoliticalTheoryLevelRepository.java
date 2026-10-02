package com.globits.hr.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.globits.hr.domain.PoliticalTheoryLevel;
import com.globits.hr.dto.PoliticalTheoryLevelDto;

public interface PoliticalTheoryLevelRepository extends JpaRepository<PoliticalTheoryLevel, UUID> {
    @Query("select new com.globits.hr.dto.PoliticalTheoryLevelDto(s) from PoliticalTheoryLevel s")
    Page<PoliticalTheoryLevelDto> getListPage(Pageable pageable);

    @Query("select p from PoliticalTheoryLevel p where p.code = ?1")
    List<PoliticalTheoryLevel> findByCode(String code);

    @Query("select count(entity.id) from PoliticalTheoryLevel entity where entity.code =?1 and (entity.id <> ?2 or ?2 is null) ")
    Long checkCode(String code, UUID id);
}
