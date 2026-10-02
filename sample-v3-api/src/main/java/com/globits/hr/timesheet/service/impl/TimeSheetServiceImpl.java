package com.globits.hr.timesheet.service.impl;

import com.globits.core.dto.PersonDto;
import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.hr.domain.ShiftWork;
import com.globits.hr.domain.Staff;
import com.globits.hr.domain.TimeSheetStaff;
import com.globits.hr.domain.WorkingStatus;
import com.globits.hr.dto.SearchReportDto;
import com.globits.hr.dto.StaffDto;
import com.globits.hr.dto.SynthesisReportOfStaffDto;
import com.globits.hr.repository.ShiftWorkRepository;
import com.globits.hr.repository.StaffRepository;
import com.globits.hr.repository.TimeSheetStaffRepository;
import com.globits.hr.repository.WorkingStatusRepository;
import com.globits.hr.service.UserExtService;
import com.globits.hr.timesheet.domain.*;
import com.globits.hr.timesheet.dto.LabelDto;
import com.globits.hr.timesheet.dto.SearchTimeSheetDto;
import com.globits.hr.timesheet.dto.TimeSheetDetailDto;
import com.globits.hr.timesheet.dto.TimeSheetDto;
import com.globits.hr.timesheet.repository.*;
import com.globits.hr.timesheet.service.ProjectActivityService;
import com.globits.hr.timesheet.service.TimeSheetService;
import com.globits.security.dto.RoleDto;
import com.globits.security.dto.UserDto;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

@Transactional
@Service
public class TimeSheetServiceImpl extends GenericServiceImpl<TimeSheet, UUID> implements TimeSheetService {
    @PersistenceContext
    EntityManager manager;
    @Autowired
    TimeSheetRepository timeSheetRepository;
    @Autowired
    TimeSheetStaffRepository timeSheetStaffRepository;
    @Autowired
    ShiftWorkRepository shiftworkRepository;
    @Autowired
    WorkingStatusRepository workingStatusRepository;
    @Autowired
    TimeSheetDetailRepository timeDetailSheetRepository;
    @Autowired
    WorkingStatusRepository workingstatusRepository;
    @Autowired
    StaffRepository staffRepository;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ProjectActivityService projectActivityService;
    @Autowired
    ProjectActivityRepository projectActivityRepository;
    @Autowired
    private UserExtService userExtService;

    public TimeSheet setEntityValue(TimeSheetDto dto, TimeSheet entity, UserDto user, Boolean isRoleUser) {
        String currentUserName = "Unknow User";
        LocalDateTime currentDate = LocalDateTime.now();
        if (entity == null) {
            entity = new TimeSheet();
            entity.setCreateDate(currentDate);
        }
        entity.setEndTime(dto.getEndTime());
        entity.setStartTime(dto.getStartTime());
        if (dto.getEndTime() != null && dto.getStartTime() != null) {
            double totalTime = (double) dto.getEndTime().getTime() - (double) dto.getStartTime().getTime();
            double totalHours = totalTime / (60 * 60 * 1000);
            entity.setTotalHours(Math.floor(totalHours * 10) / 10);
        }

        entity.setTotalHours(dto.getTotalHours());
        entity.setWorkingDate(dto.getWorkingDate());
        entity.setPriority(dto.getPriority());
        entity.setDescription(dto.getDescription());
        if (dto.getApproveStatus() == null) {
            dto.setApproveStatus(0);
        } else {
            entity.setApproveStatus(dto.getApproveStatus());
        }

        if (dto.getWorkingStatus() != null) {
            Optional<WorkingStatus> workingStatusOptional = workingStatusRepository
                    .findById(dto.getWorkingStatus().getId());
            if (workingStatusOptional.isPresent()) {
                WorkingStatus workingStatus = workingStatusOptional.get();
                entity.setWorkingStatus(workingStatus);
            }

        }

        List<StaffDto> timeSheetStaffSet = dto.getTimeSheetStaff();
        if (timeSheetStaffSet != null && !timeSheetStaffSet.isEmpty()) {
            HashSet<TimeSheetStaff> timeSheetStaffHash = new HashSet<>();
            for (StaffDto item : timeSheetStaffSet) {
                TimeSheetStaff timeSheetStaff = new TimeSheetStaff();
                Staff staff = null;
                if (item != null && item.getId() != null) {
                    Optional<Staff> staffOptional = staffRepository.findById(item.getId());
                    if (staffOptional.isPresent()) {
                        staff = staffOptional.get();
                    }
                }
                timeSheetStaff.setTimesheet(entity);
                if (staff != null) {
                    timeSheetStaff.setStaff(staff);
                }
                timeSheetStaffHash.add(timeSheetStaff);
            }
            if (entity.getTimeSheetStaffSet() != null) {
                entity.getTimeSheetStaffSet().clear();
                entity.getTimeSheetStaffSet().addAll(timeSheetStaffHash);
            } else {
                entity.setTimeSheetStaffSet(timeSheetStaffHash);
            }
        } else if (timeSheetStaffSet != null) {
            if (entity.getTimeSheetStaffSet() != null) {
                entity.getTimeSheetStaffSet().clear();
            }
        } else {
            if (user != null && isRoleUser) {
                PersonDto person = user.getPerson();
                if (person != null) {
                    TimeSheetStaff timeSheetStaff = new TimeSheetStaff();
                    Staff staff = staffRepository.getOne(person.getId());
                    timeSheetStaff.setTimesheet(entity);
                    timeSheetStaff.setStaff(staff);
                    entity.getTimeSheetStaffSet().add(timeSheetStaff);
                }
            }
        }
        if (dto.getShiftWork() != null) {
            Optional<ShiftWork> shiftWorkOptional = shiftworkRepository.findById(dto.getShiftWork().getId());
            if (shiftWorkOptional.isPresent()) {
                ShiftWork shiftwork = shiftWorkOptional.get();
                entity.setShiftWork(shiftwork);
            }
        }
        Project project = null;
        if (dto.getProject() != null && dto.getProject().getId() != null) {
            Optional<Project> optional = projectRepository.findById(dto.getProject().getId());
            if (optional.isPresent()) {
                project = optional.get();
                entity.setProject(project);
            }
        }
        // ProjectActivity activity = null;
        // if (dto.getActivity() != null) {

        //     if (dto.getActivity().getId() != null) {
        //         Optional<ProjectActivity> optional = projectActivityRepository.findById(dto.getActivity().getId());
        //         if (optional.isPresent()) {
        //             activity = optional.get();
        //         }
        //     }
        //     if (activity == null) {
        //         activity = new ProjectActivity();
        //     }
        //     activity.setCode(dto.getActivity().getCode());
        //     activity.setName(dto.getActivity().getName());
        //     activity.setProject(project);
        //     activity = projectActivityRepository.save(activity);
        // }

        Set<TimeSheetDetail> details = new HashSet<>();
        if (dto.getDetails() != null && !dto.getDetails().isEmpty()) {
            for (TimeSheetDetailDto detailDto : dto.getDetails()) {
                TimeSheetDetail detail = null;
                if (detailDto != null && detailDto.getId() != null) {
                    detail = timeDetailSheetRepository.getOne(detailDto.getId());
                }
                if (detail == null) {
                    detail = new TimeSheetDetail();
                    detail.setTimeSheet(entity);
                    detail.setCreateDate(currentDate);
                    detail.setCreatedBy(currentUserName);
                }
                if (detailDto != null) {
                    detail = detailDto.toEntity(detailDto, detail);
                    if (detailDto.getEmployee() != null) {
                        Staff staff = null;
                        Optional<Staff> staffOptional = staffRepository.findById(detailDto.getEmployee().getId());
                        if (staffOptional.isPresent()) {
                            staff = staffOptional.get();
                        }
                        detail.setEmployee(staff);
                    }
                }
                details.add(detail);
            }
        }
        if (entity.getDetails() != null) {
            entity.getDetails().clear();
            entity.getDetails().addAll(details);
        } else {
            entity.setDetails(details);
        }

        List<LabelDto> labelSet = dto.getLabels();
        if (labelSet != null && !labelSet.isEmpty()) {
            HashSet<TimeSheetLabel> timeSheetLabels = new HashSet<>();
            for (LabelDto item : labelSet) {
                TimeSheetLabel timeSheetLabel = new TimeSheetLabel();
                Label label = new Label();
                if (item != null && item.getId() != null) {
                    Optional<Label> staffOptional = labelRepository.findById(item.getId());
                    if (staffOptional.isPresent()) {
                        label = staffOptional.get();
                    }
                }
                timeSheetLabel.setTimesheet(entity);
                if (label != null){
                    timeSheetLabel.setLabel(label);
                }
                timeSheetLabels.add(timeSheetLabel);
            }
            if (entity.getLabels() != null) {
                entity.getLabels().clear();
                entity.getLabels().addAll(timeSheetLabels);
            } else {
                entity.setLabels(timeSheetLabels);
            }
        } else if (labelSet != null) {
            if (entity.getLabels() != null) {
                entity.getLabels().clear();
            }
        }
        // entity.setActivity(activity);
        return entity;
    }

    @Override
    public TimeSheetDto findTimeSheetById(UUID id) {
        TimeSheet entity = null;
        Optional<TimeSheet> optional = timeSheetRepository.findById(id);
        if (optional.isPresent()) {
            entity = optional.get();
        }
        if (entity != null) {
            TimeSheetDto timeSheetDto = new TimeSheetDto(entity);
            return timeSheetDto;
        }
        return null;
    }

    @Override
    public Boolean deleteTimeSheetById(UUID id) {
        try {
            timeSheetRepository.deleteById(id);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean deleteTimeSheets(List<TimeSheetDto> list) {
        try {
            ArrayList<TimeSheet> entities = new ArrayList<>();
            if (list != null) {
                for (TimeSheetDto timeSheetDto : list) {
                    if (timeSheetDto != null && timeSheetDto.getId() != null) {
                        TimeSheet ts = timeSheetRepository.getOne(timeSheetDto.getId());
                        entities.add(ts);
                    }
                }
            }
            timeSheetRepository.deleteInBatch(entities);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Page<StaffDto> findPageByName(String textSearch, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        String keyword = '%' + textSearch + '%';
        String sql = "select new com.globits.hr.dto.StaffDto(s) from Staff s where s.displayName like :keyword";
        String sqlCount = "select count (s.id) from Staff s where s.displayName like :keyword";
        Query q = manager.createQuery(sql);
        Query qCount = manager.createQuery(sqlCount);
        q.setParameter("keyword", keyword);
        qCount.setParameter("keyword", keyword);
        List<StaffDto> content = new ArrayList<>();
        int startPosition = (pageIndex - 1) * pageSize;
        q.setFirstResult(startPosition);
        q.setMaxResults(pageSize);
        List<Staff> list = q.getResultList();

        for (Staff staff : list) {
            StaffDto dto = new StaffDto(staff);
            content.add(dto);
        }
        long total = (long) qCount.getSingleResult();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<TimeSheetDto> getAllByWorkingDate(Date workDate, int pageIndex, int pageSize) {
        String sqlCount = "select count(sw) FROM TimeSheet as sw WHERE sw.workingDate=:workDate";
        String sql = "select new com.globits.hr.timesheet.dto.TimeSheetDto(sw) FROM TimeSheet as sw WHERE sw.workingDate=:workDate";

        Query query = manager.createQuery(sql, TimeSheetDto.class);
        Query qCount = manager.createQuery(sqlCount);
        query.setParameter("workDate", workDate);
        qCount.setParameter("workDate", workDate);
        int startPosition = (pageIndex - 1) * pageSize;
        query.setFirstResult(startPosition);
        query.setMaxResults(pageSize);
        List<TimeSheetDto> entities = query.getResultList();
        long count = (long) qCount.getSingleResult();
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        return new PageImpl<>(entities, pageable, count);
    }

    @Override
    public Page<TimeSheetDetailDto> getTimeSheetDetailByTimeSheetID(UUID id, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        return timeSheetRepository.findTimeSheetDetailByTimeSheetId(id, pageable);
    }

    @Override
    public Boolean confirmTimeSheets(List<TimeSheetDto> list) {
        ArrayList<TimeSheet> entities = new ArrayList<>();
        try {
            for (TimeSheetDto timeSheetDto : list) {
                if (timeSheetDto != null && timeSheetDto.getId() != null) {
                    TimeSheet entity = timeSheetRepository.getOne(timeSheetDto.getId());
                    entity.setApproveStatus(1);
                    entities.add(entity);
                }
            }
            timeSheetRepository.saveAll(entities);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Page<TimeSheetDto> findPageByStaff(String textSearch, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        return timeSheetRepository.findPageByCodeOrName(textSearch, pageable);
    }

    @Override
    public Page<TimeSheetDto> searchByDto(SearchTimeSheetDto dto, int pageIndex, int pageSize) {
        if (dto.getFromDate() != null && dto.getToDate() != null && dto.getToDate().before(dto.getFromDate())) {
            return null;
        }
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        if (dto.getCodeAndName() != null) {
            if (dto.getWorkingDate() != null) {
                return timeSheetRepository.findPageByCodeAndNameAndDate(dto.getCodeAndName(),
                        dto.getWorkingDate(), pageable);
            } else {
                return timeSheetRepository.findPageByCodeOrName(dto.getCodeAndName(), pageable);
            }
        } else if (dto.getWorkingDate() != null) {
            return timeSheetRepository.findPageByDate(dto.getWorkingDate(), pageable);
        } else {
            return timeSheetRepository.getListPage(pageable);
        }
    }

    @Override
    public Page<SynthesisReportOfStaffDto> reportWorkingStatus(SearchReportDto searchReportDto, int pageIndex,
                                                               int pageSize) {

        return null;
    }

    //
    @Override
    public Page<TimeSheetDto> searchByPage(SearchTimeSheetDto dto) {
        if (dto == null) {
            return null;
        }
        if (dto.getFromDate() != null && dto.getToDate() != null && dto.getToDate().before(dto.getFromDate())) {
            return null;
        }

        boolean isRoleUser = false;
        boolean isRoleAdmin = false;
        boolean isRoleManager = false;

        UserDto user = userExtService.getCurrentUserName();
        if (user != null && user.getRoles() != null && user.getRoles().size() > 0) {
            for (RoleDto item : user.getRoles()) {
                if (item.getName() != null && "ROLE_ADMIN".equals(item.getName())) {
                    isRoleAdmin = true;
                }
                if (item.getName() != null && "HR_MANAGER".equals(item.getName())) {
                    isRoleManager = true;
                }
                if (item.getName() != null && "HR_USER".equals(item.getName())) {
                    isRoleUser = true;
                }
            }
        }

        if (isRoleAdmin) {
            isRoleManager = false;
            isRoleUser = false;
        } else {
            if (isRoleManager) {
                isRoleUser = false;
            }
        }

        int pageIndex = dto.getPageIndex();
        int pageSize = dto.getPageSize();
        if (pageIndex > 0) {
            pageIndex--;
        } else {
            pageIndex = 0;
        }

        String whereClause = "";
        String orderBy = " ORDER BY entity.startTime DESC";

        String sqlCount = "select count(entity.id) from TimeSheet as entity where (1=1)   ";
        String sql = "select new com.globits.hr.timesheet.dto.TimeSheetDto(entity) from TimeSheet entity where (1=1) ";

        if (dto.getProjectId() != null && StringUtils.hasText(dto.getProjectId().toString())) {
            whereClause += " AND ( entity.project.id  =: projectId ) ";
        }
        if (dto.getShiftWorkId() != null && StringUtils.hasText(dto.getShiftWorkId().toString())) {
            whereClause += " AND ( entity.shiftWork.id  =: shiftWorkId ) ";
        }
        if (isRoleUser && user.getUsername() != null) {
            sqlCount = "select count(entity.id) from TimeSheet entity, Staff s ,TimeSheetStaff ts where (s.id = ts.staff.id and entity.id = ts.timesheet.id)   ";
            sql = "select new com.globits.hr.timesheet.dto.TimeSheetDto(entity, false) from TimeSheet entity, Staff s , TimeSheetStaff ts where s.id = ts.staff.id and entity.id = ts.timesheet.id ";
            whereClause += " and ( s.staffCode = :staffCode ) ";
        }
        if (dto.getStaffId() != null && StringUtils.hasText(dto.getStaffId().toString())) {
            sqlCount = "select count(entity.id) from TimeSheet entity, Staff s ,TimeSheetStaff ts where (s.id = ts.staff.id and entity.id = ts.timesheet.id)   ";
            sql = "select new com.globits.hr.timesheet.dto.TimeSheetDto(entity, false) from TimeSheet entity, Staff s , TimeSheetStaff ts where s.id = ts.staff.id and entity.id = ts.timesheet.id ";
            whereClause += " AND ( s.id  =: staffId ) ";
        }
        if (dto.getWorkingStatusId() != null && StringUtils.hasText(dto.getWorkingStatusId().toString())) {
            whereClause += " AND ( entity.workingStatus.id  =: workingStatusId ) ";
        }
        if (dto.getFromDate() != null) {
            whereClause += " AND ( entity.startTime >= :fromDate ) ";
        }
        if (dto.getToDate() != null) {
            whereClause += " AND ( entity.endTime <= :toDate ) ";
        }
        if (dto.getPriority() != null) {
            whereClause += " AND ( entity.priority = :priority ) ";
        }
        sql += whereClause + orderBy;
        sqlCount += whereClause;

        Query query = manager.createQuery(sql, TimeSheetDto.class);
        Query qCount = manager.createQuery(sqlCount);
        if (dto.getFromDate() != null) {
            query.setParameter("fromDate", dto.getFromDate());
            qCount.setParameter("fromDate", dto.getFromDate());
        }
        if (dto.getToDate() != null) {
            query.setParameter("toDate", dto.getToDate());
            qCount.setParameter("toDate", dto.getToDate());
        }
        if (dto.getProjectId() != null && StringUtils.hasText(dto.getProjectId().toString())) {
            query.setParameter("projectId", dto.getProjectId());
            qCount.setParameter("projectId", dto.getProjectId());
        }
        if (dto.getShiftWorkId() != null && StringUtils.hasText(dto.getShiftWorkId().toString())) {
            query.setParameter("shiftWorkId", dto.getShiftWorkId());
            qCount.setParameter("shiftWorkId", dto.getShiftWorkId());
        }
        if (dto.getStaffId() != null && StringUtils.hasText(dto.getStaffId().toString())) {
            query.setParameter("staffId", dto.getStaffId());
            qCount.setParameter("staffId", dto.getStaffId());
        }
        if (dto.getWorkingStatusId() != null && StringUtils.hasText(dto.getWorkingStatusId().toString())) {
            query.setParameter("workingStatusId", dto.getWorkingStatusId());
            qCount.setParameter("workingStatusId", dto.getWorkingStatusId());
        }
        if (dto.getPriority() != null) {
            query.setParameter("priority", dto.getPriority());
            qCount.setParameter("priority", dto.getPriority());
        }
        if (isRoleUser && user != null && user.getUsername() != null) {
            query.setParameter("staffCode", user.getUsername());
            qCount.setParameter("staffCode", user.getUsername());
        }

        if (dto.getIsExportExcel()) {
            List<TimeSheetDto> listExportExcel = query.getResultList();
            return new PageImpl<>(listExportExcel);
        }

        int startPosition = pageIndex * pageSize;
        query.setFirstResult(startPosition);
        query.setMaxResults(pageSize);
        List<TimeSheetDto> entities = query.getResultList();
        long count = (long) qCount.getSingleResult();
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return new PageImpl<>(entities, pageable, count);
    }

    @Override
    public TimeSheetDto saveOrUpdate(UUID id, TimeSheetDto dto) {
        if (dto != null && dto.getStartTime().before(dto.getEndTime())) {
            // if (dto.getActivity().getName().length() < 1) {
            //     return null;
            // }
            if (dto.getDetails() != null && dto.getTimeSheetStaff().size() == 1) {
                for (TimeSheetDetailDto detailDto : dto.getDetails()) {
                    if (detailDto.getEmployee() == null) {
                        detailDto.setEmployee(dto.getTimeSheetStaff().get(0));
                    }
                }
            }
            TimeSheet entity = null;
            boolean isRoleUser = false;
            boolean isRoleAdmin = false;
            boolean isRoleManager = false;
            UserDto user = userExtService.getCurrentUserName();
            if (user != null && user.getRoles() != null && user.getRoles().size() > 0) {
                for (RoleDto item : user.getRoles()) {
                    if (item.getName() != null && "ROLE_ADMIN".equals(item.getName())) {
                        isRoleAdmin = true;
                    }
                    if (item.getName() != null && "HR_MANAGER".equals(item.getName())) {
                        isRoleManager = true;
                    }
                    if (item.getName() != null && "HR_USER".equals(item.getName())) {
                        isRoleUser = true;
                    }
                }
            }
            if (isRoleAdmin) {
                isRoleManager = false;
                isRoleUser = false;
            } else {
                if (isRoleManager) {
                    isRoleUser = false;
                }
            }
            boolean checkStaffIsUser = false;
            if (dto.getTimeSheetStaff() != null && !dto.getTimeSheetStaff().isEmpty()) {
                for (StaffDto staffDto : dto.getTimeSheetStaff()) {
                    if (user != null && user.getUsername().equalsIgnoreCase(staffDto.getStaffCode())) {
                        checkStaffIsUser = true;
                        break;
                    }
                }
            }

            if (dto.getId() != null) {
                if (!dto.getId().equals(id)) {
                    return null;
                }

                Optional<TimeSheet> projectOptional = timeSheetRepository.findById(id);
                if (projectOptional.isPresent()) {
                    entity = projectOptional.get();
                    if (isRoleUser && !checkStaffIsUser) {
                        return null;
                    }
                }
                if (entity != null) {
                    entity.setModifyDate(LocalDateTime.now());
                }
            }
            if (entity == null) {
                entity = new TimeSheet();
                entity.setCreateDate(LocalDateTime.now());
                entity.setModifyDate(LocalDateTime.now());
            }

            entity = setEntityValue(dto, entity, user, isRoleUser);
            entity = timeSheetRepository.save(entity);
            return new TimeSheetDto(entity);
        }
        return null;
    }

    @Override
    public String updateStatus(UUID id, UUID workingStatusId) {
        if (id != null) {
            Optional<TimeSheet> optional = timeSheetRepository.findById(id);
            TimeSheet entity = null;
            if (optional.isPresent()) {
                entity = optional.get();
            }
            if (entity == null) {
                return null;
            }
            if (workingStatusId != null) {
                WorkingStatus workingStatus = null;
                Optional<WorkingStatus> projectOptional = workingStatusRepository.findById(workingStatusId);
                if (projectOptional.isPresent()) {
                    workingStatus = projectOptional.get();
                }
                if (workingStatus == null) {
                    return null;
                }
                entity.setWorkingStatus(workingStatus);
                timeSheetRepository.save(entity);
                return "success";
            }
        }
        return null;
    }
}
