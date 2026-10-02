package com.globits.hr.timesheet.repository;

import com.globits.hr.timesheet.domain.Label;
import com.globits.hr.timesheet.dto.LabelDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LabelRepository extends JpaRepository<Label, UUID> {
    @Query("SELECT new com.globits.hr.timesheet.dto.LabelDto(entity) from Label entity where entity.id = ?1")
    LabelDto getLabelDtoById(UUID id);

    @Query("select count(entity.id) from Label entity where entity.color =?1 and (entity.id <> ?2 or ?2 is null) ")
    Long checkCode(String code, UUID id);

    @Query("SELECT new com.globits.hr.timesheet.dto.LabelDto(entity) from Label entity")
    List<LabelDto> getAllLabel();

    @Query("SELECT new com.globits.hr.timesheet.dto.LabelDto(entity) from Label entity where entity.project.id = ?1")
    List<LabelDto> getAllLabelByProjectId(UUID id);
}
