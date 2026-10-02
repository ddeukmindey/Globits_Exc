package com.globits.hr.repository;

import com.globits.hr.domain.ShiftWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShiftWorkRepository extends JpaRepository<ShiftWork, UUID> {
    @Query("select count(entity.id) from ShiftWork entity where entity.code =?1 and (entity.id <> ?2 or ?2 is null) ")
    Long checkCode(String code, UUID id);
}
