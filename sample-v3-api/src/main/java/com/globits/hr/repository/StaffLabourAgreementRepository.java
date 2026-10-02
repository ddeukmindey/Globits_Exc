package com.globits.hr.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.hr.domain.StaffLabourAgreement;
import com.globits.hr.dto.StaffLabourAgreementDto;


@Repository
public interface StaffLabourAgreementRepository extends JpaRepository<StaffLabourAgreement, UUID> {

    @Query("select new com.globits.hr.dto.StaffLabourAgreementDto(agreement) from StaffLabourAgreement agreement where agreement.id = ?1")
    StaffLabourAgreementDto getAgreementById(UUID id);

    @Query("select new com.globits.hr.dto.StaffLabourAgreementDto(agreement) from StaffLabourAgreement agreement where agreement.staff.id = ?1")
    List<StaffLabourAgreementDto> getAll(UUID id);

    @Query("select new com.globits.hr.dto.StaffLabourAgreementDto(agreement)  from StaffLabourAgreement agreement")
    Page<StaffLabourAgreementDto> getPages(Pageable pageable);

}
