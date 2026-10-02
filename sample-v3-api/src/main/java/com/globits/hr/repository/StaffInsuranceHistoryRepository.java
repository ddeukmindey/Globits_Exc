package com.globits.hr.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.globits.hr.domain.StaffInsuranceHistory;

public interface StaffInsuranceHistoryRepository extends JpaRepository<StaffInsuranceHistory, UUID>{
	
}
