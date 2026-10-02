package com.globits.hr.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.hr.domain.CivilServantCategory;
import com.globits.hr.dto.CivilServantCategoryDto;

@Repository
public interface CivilServantCategoryRepository extends JpaRepository<CivilServantCategory, UUID> {
    @Query("select count(entity.id) from CivilServantCategory entity where entity.code =?1 and (entity.id <> ?2 or ?2 is null) ")
    Long checkCode(String code, UUID id);

    @Query("select c FROM CivilServantCategory c where c.code = ?1 ")
    List<CivilServantCategory> findByCode(String name);

    @Query("select new com.globits.hr.dto.CivilServantCategoryDto(c) FROM CivilServantCategory c ")
    List<CivilServantCategoryDto> getAllCivilServantCategory();
}

