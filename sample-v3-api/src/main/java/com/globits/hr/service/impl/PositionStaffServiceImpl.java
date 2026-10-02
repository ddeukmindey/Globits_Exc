package com.globits.hr.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.hr.domain.HRDepartment;
import com.globits.hr.domain.PositionStaff;
import com.globits.hr.domain.PositionTitle;
import com.globits.hr.domain.Staff;
import com.globits.hr.dto.PositionStaffDto;
import com.globits.hr.dto.function.PositionTitleStaffDto;
import com.globits.hr.repository.HRDepartmentRepository;
import com.globits.hr.repository.PositionStaffRepository;
import com.globits.hr.repository.PositionTitleRepository;
import com.globits.hr.repository.StaffRepository;
import com.globits.hr.service.PositionStaffService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PositionStaffServiceImpl extends GenericServiceImpl<PositionStaff, UUID> implements PositionStaffService{
	@Autowired
	StaffRepository staffRepository;
	@Autowired
	PositionTitleRepository positionTitleRepository;
	@Autowired
	HRDepartmentRepository hRDepartmentRepository;
	@Autowired
	PositionStaffRepository positionStaffRepository;

	@Override
	public PositionStaffDto saveImportStaffEducationHistory(PositionTitleStaffDto dto) {
		if(dto != null) {
			Staff entity =null;
			PositionStaff positionStaff = new PositionStaff();
			PositionTitle positionTitle = null;
			if(dto.getStaffCode() != null) {
				List<Staff> listStaff = staffRepository.getByCode(dto.getStaffCode());
				if(listStaff != null && listStaff.size() >0) {
					entity = listStaff.get(0);
					positionStaff.setStaff(entity);
				}
			}
			if(entity == null) {
				return null;
			}
			if(dto.getPositionTitleCode() != null) {
				List<PositionTitle> listPotitionTitle = positionTitleRepository.findByCode(dto.getPositionTitleCode());
				if(listPotitionTitle != null && listPotitionTitle.size() >0) {
					positionTitle = listPotitionTitle.get(0);
					positionStaff.setPosition(positionTitle);
				}
			}
			if(positionTitle == null) {
				return null;
			}
			if(dto.getFromDate() != null) {
				positionStaff.setFromDate(new DateTime(dto.getFromDate()));
			}
			if(dto.getToDate() != null) {
				positionStaff.setToDate(new DateTime(dto.getToDate()));
			}
			if(dto.getDepartmentCode() != null) {
				List<HRDepartment> depart = hRDepartmentRepository.findByCode(dto.getDepartmentCode());
				if(depart != null && depart.size()>0) {
					positionStaff.setDepartment(depart.get(0));
				}
			}
			positionStaff.setAllowanceCoefficient(dto.getAllowanceCoefficient());
			positionStaff.setDecisionCode(dto.getDecisionCode());
			positionStaff.setCurrent(dto.getCurrent());
			positionStaff.setConnectedAllowanceProcess(dto.getConnectedAllowanceProcess());
			positionStaff.setDecisionDate(dto.getDecisionDate());
			positionStaff.setMainPosition(dto.getMainPosition());
			positionStaff.setDepartmentStr(dto.getDepartmentStr());
			positionStaff = positionStaffRepository.save(positionStaff);
			return new PositionStaffDto(positionStaff);
		}
		return null;
	}

}
