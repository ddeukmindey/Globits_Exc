package com.globits.hr.timesheet.rest;

import com.globits.core.Constants;
import com.globits.hr.HrConstants;
import com.globits.hr.dto.StaffDto;
import com.globits.hr.timesheet.dto.SearchTimeSheetDto;
import com.globits.hr.timesheet.dto.TimeSheetDetailDto;
import com.globits.hr.timesheet.dto.TimeSheetDto;
import com.globits.hr.timesheet.service.TimeSheetService;
import com.globits.hr.utils.ExportExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/timesheet")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RestTimeSheetController {
    @Autowired
    private TimeSheetService timeSheetService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<TimeSheetDto> findTimeSheetById(@PathVariable("id") UUID id) {
        TimeSheetDto result = timeSheetService.findTimeSheetById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/workingdate/{workingdate}/{pageSize}/{pageIndex}", method = RequestMethod.GET)
    public ResponseEntity<Page<TimeSheetDto>> getTimeSheetByWorkingDate(@PathVariable String workingdate,
                                                                        @PathVariable int pageIndex, @PathVariable int pageSize) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = format.parse(workingdate);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        Page<TimeSheetDto> result = timeSheetService.getAllByWorkingDate(date, pageSize, pageIndex);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteTimeSheet(@PathVariable("id") UUID id) {
        Boolean ret = timeSheetService.deleteTimeSheetById(id);
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteTimeSheet(@RequestBody List<TimeSheetDto> list) {
        Boolean ret = timeSheetService.deleteTimeSheets(list);
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }

    @RequestMapping(value = "/searchStaff/{name}/{pageIndex}/{pageSize}", method = RequestMethod.GET)
    public ResponseEntity<Page<StaffDto>> findPageByName(@PathVariable("name") String name, @PathVariable int pageIndex,
                                                         @PathVariable int pageSize) {
        Page<StaffDto> result = timeSheetService.findPageByName(name, pageIndex, pageSize);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/staff/{name}/{pageIndex}/{pageSize}", method = RequestMethod.GET)
    public ResponseEntity<Page<TimeSheetDto>> findPageByStaff(@PathVariable("name") String name,
                                                              @PathVariable int pageIndex, @PathVariable int pageSize) {
        Page<TimeSheetDto> result = timeSheetService.findPageByStaff(name, pageIndex, pageSize);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/searchByDto/{pageIndex}/{pageSize}", method = RequestMethod.POST)
    public ResponseEntity<Page<TimeSheetDto>> searchByDto(@RequestBody SearchTimeSheetDto searchTimeSheetDto,
                                                          @PathVariable int pageIndex, @PathVariable int pageSize) {
        Page<TimeSheetDto> result = timeSheetService.searchByDto(searchTimeSheetDto, pageIndex, pageSize);
        if (result == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/detail/{id}/{pageIndex}/{pageSize}", method = RequestMethod.GET)
    public ResponseEntity<Page<TimeSheetDetailDto>> getTimeSheetDetail(@PathVariable UUID id,
                                                                       @PathVariable int pageIndex, @PathVariable int pageSize) {
        Page<TimeSheetDetailDto> result = timeSheetService.getTimeSheetDetailByTimeSheetID(id, pageIndex, pageSize);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public ResponseEntity<Boolean> confirmTimeSheets(@RequestBody List<TimeSheetDto> listdto) {
        Boolean ret = timeSheetService.confirmTimeSheets(listdto);
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }

    @RequestMapping(value = "/search-by-page", method = RequestMethod.POST)
    public ResponseEntity<Page<TimeSheetDto>> searchByPage(@RequestBody SearchTimeSheetDto searchDto) {
        Page<TimeSheetDto> page = timeSheetService.searchByPage(searchDto);
        if (page == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<TimeSheetDto> save(@RequestBody TimeSheetDto dto) {
        TimeSheetDto result = timeSheetService.saveOrUpdate(null, dto);
        if (result == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<TimeSheetDto> save(@RequestBody TimeSheetDto dto, @PathVariable UUID id) {
        TimeSheetDto result = timeSheetService.saveOrUpdate(id, dto);
        if (result == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
    @RequestMapping(value = "/update-status/{id}/{workingStatusId}", method = RequestMethod.PUT)
    public ResponseEntity<String> updateStatus(@PathVariable UUID id, @PathVariable UUID workingStatusId) {
        String result = timeSheetService.updateStatus(id, workingStatusId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/exportExcel", method = RequestMethod.POST)
    public void exportTimeSheetsToExcel(HttpServletResponse response, @RequestBody SearchTimeSheetDto searchTimeSheetDto) throws IOException {
        Page<TimeSheetDto> data = timeSheetService.searchByPage(searchTimeSheetDto);
        List<TimeSheetDto> dataList = data.getContent();
        ByteArrayResource excelFile;
        if (!dataList.isEmpty()) {
            excelFile = ExportExcelUtil.exportTimeSheetToExcelTable(dataList, true);
            InputStream ins = null;
            if (excelFile != null) {
                ins = new ByteArrayInputStream(excelFile.getByteArray());
            }
            if (ins != null) {
                org.apache.commons.io.IOUtils.copy(ins, response.getOutputStream());
            }
        }
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.addHeader("Content-Disposition", "attachment; filename=TimeSheet.xlsx");
    }
}
