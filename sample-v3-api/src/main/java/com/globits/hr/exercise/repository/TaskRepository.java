package com.globits.hr.exercise.repository;

import com.globits.hr.exercise.domain.Task;
import com.globits.hr.exercise.dto.TaskDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    @Query("select new com.globits.hr.exercise.dto.TaskDto(t) from Task t " +
            "where (:keyword is null or lower(t.name) like lower(concat('%', :keyword, '%')) or lower(t.description) like lower(concat('%', :keyword, '%'))) " +
            "and (:projectId is null or t.project.id = :projectId) " +
            "and (:staffId is null or t.staff.id = :staffId) " +
            "and (:status is null or t.status = :status) " +
            "and (:priority is null or t.priority = :priority) " +
            "order by t.createDate desc")
    Page<TaskDto> searchByPage(
            @Param("keyword") String keyword,
            @Param("projectId") UUID projectId,
            @Param("staffId") UUID staffId,
            @Param("status") Integer status,
            @Param("priority") Integer priority,
            Pageable pageable);

    @Query("select new com.globits.hr.exercise.dto.TaskDto(t) from Task t " +
            "where (:keyword is null or lower(t.name) like lower(concat('%', :keyword, '%')) or lower(t.description) like lower(concat('%', :keyword, '%'))) " +
            "and (:projectId is null or t.project.id = :projectId) " +
            "and (:staffId is null or t.staff.id = :staffId) " +
            "and (:status is null or t.status = :status) " +
            "and (:priority is null or t.priority = :priority) " +
            "order by t.createDate desc")
    List<TaskDto> searchAll(
            @Param("keyword") String keyword,
            @Param("projectId") UUID projectId,
            @Param("staffId") UUID staffId,
            @Param("status") Integer status,
            @Param("priority") Integer priority);
}
