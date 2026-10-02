package com.globits.hr.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.hr.domain.Staff;
import com.globits.hr.dto.StaffDto;

@Repository
public interface StaffRepository extends JpaRepository<Staff, UUID> {
    @Query("select new com.globits.hr.dto.StaffDto(s.id, s.staffCode, s.displayName, s.gender) from Staff s")
    Page<StaffDto> findByPageBasicInfo(Pageable pageable);

    @Query("select new com.globits.hr.dto.StaffDto(u, true) from Staff u where u.staffCode = ?1")
    StaffDto getByUsername(String username);

    @Query("select s from Staff s where s.user.username = ?1")
    Staff findByUsername(String username);

    @Query("select u from Staff u where u.staffCode = ?1")
    List<Staff> findByCode(String staffCode);

    @Query("select new com.globits.hr.dto.StaffDto(s) from Staff s where s.staffCode like %?1% or s.displayName like %?1%")
    Page<StaffDto> findPageByCodeOrName(String staffCode, Pageable pageable);

    @Query("select u from Staff u where u.staffCode = ?1")
    List<Staff> getByCode(String staffCode);

    @Query("select u from Staff u where u.id = ?1")
    Staff findOneById(UUID id);

    @Query("select count(u.id) from Staff u where u.idNumber = ?2 and (u.id <> ?1 or ?1 is null )")
    Long countByIdNumber(UUID id, String idNumber);
}
