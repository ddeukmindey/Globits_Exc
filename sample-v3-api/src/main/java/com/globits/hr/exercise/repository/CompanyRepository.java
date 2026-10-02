package com.globits.hr.exercise.repository;

import com.globits.hr.exercise.domain.Company;
import com.globits.hr.exercise.dto.CompanyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {

    @Query("select new com.globits.hr.exercise.dto.CompanyDto(c) from Company c " +
            "where (:keyword is null or lower(c.name) like lower(concat('%', :keyword, '%')) " +
            "or lower(c.code) like lower(concat('%', :keyword, '%')))")
    Page<CompanyDto> searchByPage(@Param("keyword") String keyword, Pageable pageable);

    @Query("select count(c.id) from Company c where c.code = :code and (:id is null or c.id != :id)")
    Long countByCode(@Param("code") String code, @Param("id") UUID id);
}
