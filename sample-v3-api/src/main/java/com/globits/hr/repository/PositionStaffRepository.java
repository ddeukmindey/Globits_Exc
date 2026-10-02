package com.globits.hr.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.hr.domain.PositionStaff;
import com.globits.hr.domain.Staff;
import com.globits.hr.dto.PositionStaffDto;

@Repository
public interface PositionStaffRepository extends JpaRepository<PositionStaff, UUID> {
    @Query("select new com.globits.hr.dto.PositionStaffDto(u) from PositionStaff u  where u.current=1 and u.department.id = ?1")
    Page<PositionStaffDto> findTeacherByDepartment(UUID departmentId, Pageable pageable);

    @Query("select u from PositionStaff u  where u.staff.id=?1 and u.position.id = ?2 and u.department.id = ?3")
    List<PositionStaff> findBy(UUID staffId, UUID positionId, UUID departmentId);

    @Query("select distinct(u.staff) from PositionStaff u where u.department.id in ?1")
    List<Staff> findDistinctStaffByDepartment(List<UUID> departmentIds);

    @Query("select u from PositionStaff u where u.staff.id=?1")
    List<PositionStaff> findPositionStaffByStaff(UUID staffId);
}
