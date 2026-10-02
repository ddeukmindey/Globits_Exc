package com.globits.hr.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.hr.domain.HRDepartment;
import com.globits.hr.dto.HRDepartmentDto;

@Repository
public interface HRDepartmentRepository extends JpaRepository<HRDepartment, UUID> {
    @Query("select count(entity.id) from HRDepartment entity where entity.code =?1 and (entity.id <> ?2 or ?2 is null) ")
    Long checkCode(String code, UUID id);

    @Query("select new com.globits.hr.dto.HRDepartmentDto(ed) from HRDepartment ed")
    Page<HRDepartmentDto> getListPage(Pageable pageable);

    @Query("select c FROM HRDepartment c where c.code = ?1")
    List<HRDepartment> findByCode(String code);

    @Query("select count(c.id) FROM HRDepartment c where c.parent.id = ?1")
    Long countDepartment(UUID id);

    @Query("select count(c.id) FROM Staff c where c.department.id = ?1")
    Long countStaff(UUID id);

    @Query("select c FROM HRDepartment c where c.name = ?1")
    List<HRDepartment> findByName(String name);

}
