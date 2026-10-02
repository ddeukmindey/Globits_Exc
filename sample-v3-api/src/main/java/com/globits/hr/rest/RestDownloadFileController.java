package com.globits.hr.rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.globits.hr.dto.StaffDto;
import com.globits.hr.dto.search.SearchStaffDto;
import com.globits.hr.service.StaffService;
import com.globits.hr.utils.ImportExportExcelUtil;

@RestController
@RequestMapping("/api/fileDownload")
public class RestDownloadFileController {
    @Autowired
    StaffService staffService;

    @RequestMapping(value = "/exportExcel_staff", method = RequestMethod.POST)
    public void exportHealthOrgEQARoundToExcelTable(HttpSession session, HttpServletResponse response, @RequestBody SearchStaffDto dto) throws IOException {
        Page<StaffDto> data = staffService.searchByPage(dto);
        List<StaffDto> dataList = data.getContent();
        ByteArrayResource excelFile;
        if (dataList.size() > 0) {
            excelFile = ImportExportExcelUtil.exportStaffToExcelTable(dataList, true);
            InputStream ins = null;
            if (excelFile != null) {
                ins = new ByteArrayInputStream(excelFile.getByteArray());
            }
            if (ins != null) {
                org.apache.commons.io.IOUtils.copy(ins, response.getOutputStream());
            }
        }
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.addHeader("Content-Disposition", "attachment; filename=DanhSachDonViThamGia.xlsx");
    }
}
