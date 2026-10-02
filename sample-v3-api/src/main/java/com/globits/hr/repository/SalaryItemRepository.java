package com.globits.hr.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.hr.domain.SalaryItem;
import com.globits.hr.dto.SalaryItemDto;

@Repository
public interface SalaryItemRepository extends JpaRepository<SalaryItem, UUID> {
    @Query("select new com.globits.hr.dto.SalaryItemDto(s) from SalaryItem s")
    Page<SalaryItemDto> getListPage(Pageable pageable);

    @Query("select new com.globits.hr.dto.SalaryItemDto(s) from SalaryItem s where s.name like ?1 or s.code like ?2")
    Page<SalaryItemDto> searchByPage(String name, String code, Pageable pageable);

    @Query("select count(entity.id) from SalaryItem entity where entity.code =?1 and (entity.id <> ?2 or ?2 is null) ")
    Long checkCode(String code, UUID id);
}
