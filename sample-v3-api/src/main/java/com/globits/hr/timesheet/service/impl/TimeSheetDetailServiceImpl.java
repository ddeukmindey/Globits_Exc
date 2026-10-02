package com.globits.hr.timesheet.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.globits.hr.domain.Staff;
import com.globits.hr.repository.StaffRepository;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.hr.timesheet.domain.TimeSheet;
import com.globits.hr.timesheet.domain.TimeSheetDetail;
import com.globits.hr.timesheet.dto.TimeSheetDetailDto;
import com.globits.hr.timesheet.repository.TimeSheetDetailRepository;
import com.globits.hr.timesheet.repository.TimeSheetRepository;
import com.globits.hr.timesheet.service.TimeSheetDetailService;
import com.globits.security.domain.User;

@Transactional
@Service
public class TimeSheetDetailServiceImpl extends GenericServiceImpl<TimeSheetDetail, UUID> implements TimeSheetDetailService{
	
	@Autowired
	TimeSheetDetailRepository timeSheetDetailRepository;
	
	@Autowired
	TimeSheetRepository timeSheetRepository;
	
	@Autowired
	StaffRepository staffRepository;

	private void setTimeSheetDetailValue(TimeSheetDetailDto dto, TimeSheetDetail entity) {
		if(dto.getTimeSheet() != null) {
			UUID timeSheetId = dto.getTimeSheet().getId();
			Optional<TimeSheet> timeSheet = timeSheetRepository.findById(timeSheetId);
			timeSheet.ifPresent(entity::setTimeSheet);
		}
		if(dto.getEmployee() != null) {
			UUID staffId = dto.getEmployee().getId();
			Optional<Staff> staff = staffRepository.findById(staffId);
			staff.ifPresent(entity::setEmployee);
		}
		entity.setWorkingItemTitle(dto.getWorkingItemTitle());
		entity.setDuration(dto.getDuration());
		entity.setStartTime(dto.getStartTime());
		entity.setEndTime(dto.getEndTime());
	}
	
	@Override
	public TimeSheetDetailDto saveTimeSheetDetail(TimeSheetDetailDto dto) {
		if (dto.getEndTime().before(dto.getStartTime())) {
			return null;
		}
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}
		TimeSheetDetail entity = null;
		if (dto.getId() != null) {
			Optional<TimeSheetDetail> opEntity = timeSheetDetailRepository.findById(dto.getId());
			if (opEntity.isPresent())
				entity = opEntity.get();
		}

		if (entity == null) {
			entity = new TimeSheetDetail();
			entity.setCreateDate(currentDate);
			entity.setCreatedBy(currentUserName);
		} else {
			entity.setModifyDate(currentDate);
			entity.setModifiedBy(currentUserName);
		}
		setTimeSheetDetailValue(dto, entity);
		entity = timeSheetDetailRepository.save(entity);
		return new TimeSheetDetailDto(entity);
	}
	
	@Override
	public Page<TimeSheetDetailDto> getPage(int pageSize, int pageIndex){
		Pageable pageable = PageRequest.of(pageIndex-1, pageSize);
		return timeSheetDetailRepository.getListPage(pageable);
	}
	
	@Override
	public Boolean deleteTimeSheetDetails(List<TimeSheetDetailDto> list) {
		try {
		ArrayList<TimeSheetDetail> entities = new ArrayList<>();
		if(list!=null) {
			for (TimeSheetDetailDto timeSheetDetailDto : list) {
				if (timeSheetDetailDto != null && timeSheetDetailDto.getId() != null) {
					TimeSheetDetail ts = timeSheetDetailRepository.getOne(timeSheetDetailDto.getId());
					entities.add(ts);
				}

			}
		}
		timeSheetDetailRepository.deleteInBatch(entities);
		return true;
		}catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	@Override
	public TimeSheetDetailDto findTimeSheetDetailById(UUID id) {
		return timeSheetDetailRepository.findTimeSheetDetailById(id);
	}
}
