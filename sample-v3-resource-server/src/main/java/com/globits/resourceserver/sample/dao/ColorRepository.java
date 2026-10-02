package com.globits.resourceserver.sample.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.globits.resourceserver.sample.model.Color;
@Repository
public interface ColorRepository extends JpaRepository<Color, UUID>{
//	@Query("select new com.globits.resourceserver.sample.model.Color(c.value) from Color c")
//	Page<Color> getListPage(Pageable pageable);
}
