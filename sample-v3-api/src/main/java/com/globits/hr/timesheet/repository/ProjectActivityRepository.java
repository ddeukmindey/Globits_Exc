package com.globits.hr.timesheet.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.hr.timesheet.domain.ProjectActivity;
import com.globits.hr.timesheet.dto.ProjectActivityDto;
@Repository
public interface ProjectActivityRepository extends JpaRepository<ProjectActivity, UUID> {
	@Query("select count(entity.id) from ProjectActivity entity where entity.code =?1 and (entity.id <> ?2 or ?2 is null) ")
	Long checkCode(String code, UUID id);
	@Query("select new com.globits.hr.timesheet.dto.ProjectActivityDto(p) from ProjectActivity p")
	Page<ProjectActivityDto> getListPage( Pageable pageable);
	@Query("select new com.globits.hr.timesheet.dto.ProjectActivityDto(entity) from ProjectActivity entity where entity.name =?1")
	List<ProjectActivityDto> findByName(String name);
}
