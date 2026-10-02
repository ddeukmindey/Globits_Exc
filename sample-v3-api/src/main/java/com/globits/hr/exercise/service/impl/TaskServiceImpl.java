package com.globits.hr.exercise.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.hr.domain.Staff;
import com.globits.hr.exercise.domain.Task;
import com.globits.hr.exercise.dto.TaskDto;
import com.globits.hr.exercise.dto.search.TaskSearchDto;
import com.globits.hr.exercise.repository.TaskRepository;
import com.globits.hr.exercise.service.TaskService;
import com.globits.hr.repository.StaffRepository;
import com.globits.hr.timesheet.domain.Project;
import com.globits.hr.timesheet.repository.ProjectRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@Transactional
@Service
public class TaskServiceImpl extends GenericServiceImpl<Task, UUID> implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Override
    public Page<TaskDto> searchByPage(TaskSearchDto dto) {
        if (dto == null) {
            return null;
        }
        int pageIndex = dto.getPageIndex() > 0 ? dto.getPageIndex() - 1 : 0;
        int pageSize = dto.getPageSize() > 0 ? dto.getPageSize() : 10;
        Pageable pageable = PageRequest.of(pageIndex, pageSize);

        return taskRepository.searchByPage(
                dto.getKeyword(),
                dto.getProjectId(),
                dto.getStaffId(),
                dto.getStatus(),
                dto.getPriority(),
                pageable
        );
    }

    @Override
    public TaskDto getById(UUID id) {
        if (id != null) {
            Task entity = taskRepository.findById(id).orElse(null);
            if (entity != null) {
                return new TaskDto(entity);
            }
        }
        return null;
    }

    @Override
    public TaskDto saveOrUpdate(TaskDto dto) {
        if (dto == null) {
            return null;
        }

        Task entity = null;
        if (dto.getId() != null) {
            entity = taskRepository.findById(dto.getId()).orElse(null);
        }

        if (entity == null) {
            entity = new Task();
        }

        if (dto.getStartTime() != null && dto.getEndTime() != null && dto.getEndTime().before(dto.getStartTime())) {
            throw new IllegalArgumentException("Thời gian kết thúc phải sau hoặc bằng thời gian bắt đầu");
        }

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        entity.setPriority(dto.getPriority());
        entity.setStatus(dto.getStatus());

        if (dto.getProjectId() != null) {
            Project project = projectRepository.findById(dto.getProjectId()).orElse(null);
            entity.setProject(project);
        } else {
            entity.setProject(null);
        }

        if (dto.getStaffId() != null) {
            Staff staff = staffRepository.findById(dto.getStaffId()).orElse(null);
            entity.setStaff(staff);
        } else {
            entity.setStaff(null);
        }

        entity = taskRepository.save(entity);
        return new TaskDto(entity);
    }

    @Override
    public Boolean deleteById(UUID id) {
        if (id != null && taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public void exportExcel(TaskSearchDto dto, HttpServletResponse response) throws IOException {
        if (dto == null) {
            dto = new TaskSearchDto();
        }

        List<TaskDto> list = taskRepository.searchAll(
                dto.getKeyword(),
                dto.getProjectId(),
                dto.getStaffId(),
                dto.getStatus(),
                dto.getPriority()
        );

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Tasks");

        // Header style
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

        String[] columns = {"STT", "Tên công việc", "Dự án", "Người thực hiện", "Thời gian bắt đầu", "Thời gian kết thúc", "Mức độ ưu tiên", "Trạng thái", "Mô tả"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        int rowIdx = 1;

        for (TaskDto task : list) {
            Row row = sheet.createRow(rowIdx);
            row.createCell(0).setCellValue(rowIdx);
            row.createCell(1).setCellValue(task.getName() != null ? task.getName() : "");
            row.createCell(2).setCellValue(task.getProjectName() != null ? task.getProjectName() : "");
            row.createCell(3).setCellValue(task.getStaffName() != null ? task.getStaffName() : "");
            row.createCell(4).setCellValue(task.getStartTime() != null ? dateFormat.format(task.getStartTime()) : "");
            row.createCell(5).setCellValue(task.getEndTime() != null ? dateFormat.format(task.getEndTime()) : "");

            String priorityStr = "";
            if (task.getPriority() != null) {
                if (task.getPriority() == 1) priorityStr = "Cao";
                else if (task.getPriority() == 2) priorityStr = "Trung bình";
                else if (task.getPriority() == 3) priorityStr = "Thấp";
            }
            row.createCell(6).setCellValue(priorityStr);

            String statusStr = "";
            if (task.getStatus() != null) {
                if (task.getStatus() == 1) statusStr = "Mới tạo";
                else if (task.getStatus() == 2) statusStr = "Đang làm";
                else if (task.getStatus() == 3) statusStr = "Hoàn thành";
                else if (task.getStatus() == 4) statusStr = "Tạm hoãn";
            }
            row.createCell(7).setCellValue(statusStr);
            row.createCell(8).setCellValue(task.getDescription() != null ? task.getDescription() : "");

            rowIdx++;
        }

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=tasks.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
