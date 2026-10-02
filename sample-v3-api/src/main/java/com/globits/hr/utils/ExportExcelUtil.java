package com.globits.hr.utils;

import com.globits.hr.dto.StaffDto;
import com.globits.hr.timesheet.dto.TimeSheetDetailDto;
import com.globits.hr.timesheet.dto.TimeSheetDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.core.io.ByteArrayResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class ExportExcelUtil {
    public static ByteArrayResource exportTimeSheetToExcelTable(List<TimeSheetDto> dataList, Boolean isCheck)
            throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Sheet1");

        XSSFFont fontBold = workbook.createFont();
        fontBold.setBold(true);
        fontBold.setFontHeight(10);

        XSSFFont fontBoldTitle = workbook.createFont();
        fontBoldTitle.setBold(true);
        fontBoldTitle.setFontHeight(11);

        XSSFCellStyle titleCellStyle = workbook.createCellStyle();
        titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
        titleCellStyle.setFont(fontBoldTitle);

        XSSFCellStyle tableHeadCellStyle = workbook.createCellStyle();
        tableHeadCellStyle.setFont(fontBoldTitle);
        tableHeadCellStyle.setAlignment(HorizontalAlignment.CENTER);
        tableHeadCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        XSSFRow row = sheet.createRow(0);
        XSSFCell cell;

        cell = row.createCell(0);
        cell.setCellValue("STT");
        cell.setCellStyle(tableHeadCellStyle);

        cell = row.createCell(1);
        cell.setCellValue("Dự án");
        cell.setCellStyle(tableHeadCellStyle);

        cell = row.createCell(2);
        cell.setCellValue("Ngày làm việc");
        cell.setCellStyle(tableHeadCellStyle);

        cell = row.createCell(3);
        cell.setCellValue("Công việc");
        cell.setCellStyle(tableHeadCellStyle);

        cell = row.createCell(4);
        cell.setCellValue("Mô tả");
        cell.setCellStyle(tableHeadCellStyle);

        cell = row.createCell(5);
        cell.setCellValue("Thời gian bắt đầu");
        cell.setCellStyle(tableHeadCellStyle);

        cell = row.createCell(6);
        cell.setCellValue("Thời gian kết thúc");
        cell.setCellStyle(tableHeadCellStyle);

        cell = row.createCell(7);
        cell.setCellValue("Tổng thời gian(giờ)");
        cell.setCellStyle(tableHeadCellStyle);

        cell = row.createCell(8);
        cell.setCellValue("Mức độ ưu tiên");
        cell.setCellStyle(tableHeadCellStyle);

        cell = row.createCell(9);
        cell.setCellValue("Người thực hiện");
        cell.setCellStyle(tableHeadCellStyle);

        cell = row.createCell(10);
        cell.setCellValue("Trạng thái");
        cell.setCellStyle(tableHeadCellStyle);
        for (int col = 0; col <= 10; col++) {
            sheet.addMergedRegion(new CellRangeAddress(0, 1, col, col));
        }

        cell = row.createCell(11);
        cell.setCellValue("Đầu việc");
        cell.setCellStyle(tableHeadCellStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 11, 15));

        row = sheet.createRow(1);
        cell = row.createCell(11);
        cell.setCellValue("Tiêu đề");
        cell.setCellStyle(tableHeadCellStyle);

        cell = row.createCell(12);
        cell.setCellValue("giờ bắt đầu");
        cell.setCellStyle(tableHeadCellStyle);

        cell = row.createCell(13);
        cell.setCellValue("giờ kết thúc");
        cell.setCellStyle(tableHeadCellStyle);

        cell = row.createCell(14);
        cell.setCellValue("Tổng giờ");
        cell.setCellStyle(tableHeadCellStyle);

        cell = row.createCell(15);
        cell.setCellValue("Nhân viên thực hiện");
        cell.setCellStyle(tableHeadCellStyle);

        XSSFCellStyle tableDataRowStyle = workbook.createCellStyle();
        tableDataRowStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        tableDataRowStyle.setAlignment(HorizontalAlignment.CENTER);
        XSSFRow tableDataRow;

        if (dataList != null && !dataList.isEmpty()) {
            SimpleDateFormat formatDate = new SimpleDateFormat("hh:mm dd-MM-yyyy");
            SimpleDateFormat formatDateWork = new SimpleDateFormat("dd-MM-yyyy");
            int increaseRow = 2;
            for (int i = 0; i < dataList.size(); i++) {
                XSSFCell cellData;
                TimeSheetDto data = dataList.get(i);
                tableDataRow = sheet.createRow(increaseRow);

                cellData = tableDataRow.createCell(0);
                cellData.setCellValue(i + 1);
                cellData.setCellStyle(tableDataRowStyle);

                if (data.getProject() != null) {
                    cellData = tableDataRow.createCell(1);
                    cellData.setCellValue(data.getProject().getName());
                    cellData.setCellStyle(tableDataRowStyle);
                }

                if (data.getWorkingDate() != null) {
                    cellData = tableDataRow.createCell(2);
                    cellData.setCellValue(formatDateWork.format(data.getWorkingDate()));
                    cellData.setCellStyle(tableDataRowStyle);
                }

                // if (data.getActivity() != null) {
                //     cellData = tableDataRow.createCell(3);
                //     cellData.setCellValue(data.getActivity().getName());
                //     cellData.setCellStyle(tableDataRowStyle);
                // }

                if (data.getDescription() != null) {
                    cellData = tableDataRow.createCell(4);
                    cellData.setCellValue(data.getDescription());
                    cellData.setCellStyle(tableDataRowStyle);
                }

                if (data.getStartTime() != null) {
                    cellData = tableDataRow.createCell(5);
                    cellData.setCellValue(formatDate.format(data.getStartTime()));
                    cellData.setCellStyle(tableDataRowStyle);
                }

                if (data.getEndTime() != null) {
                    cellData = tableDataRow.createCell(6);
                    cellData.setCellValue(formatDate.format(data.getEndTime()));
                    cellData.setCellStyle(tableDataRowStyle);
                }

                cellData = tableDataRow.createCell(7);
                cellData.setCellValue(data.getTotalHours());
                cellData.setCellStyle(tableDataRowStyle);

                if (data.getPriority() != null) {
                    cellData = tableDataRow.createCell(8);
                    cellData.setCellValue(data.getPriority());
                    cellData.setCellStyle(tableDataRowStyle);
                }

                List<StaffDto> staffDtoList = data.getTimeSheetStaff();
                StringBuilder strStaffs = new StringBuilder();
                if (staffDtoList != null && !staffDtoList.isEmpty()) {
                    for (StaffDto staffDto : staffDtoList) {
                        strStaffs.append(staffDto.getDisplayName()).append("\n");
                    }
                    cellData = tableDataRow.createCell(9);
                    cellData.setCellValue(strStaffs.toString());
                    cellData.setCellStyle(tableDataRowStyle);
                }

                if (data.getWorkingStatus() != null) {
                    cellData = tableDataRow.createCell(10);
                    cellData.setCellValue(data.getWorkingStatus().getName());
                    cellData.setCellStyle(tableDataRowStyle);
                }

                int lastRow;
                if (data.getDetails() != null && data.getDetails().size() > 1) {
                    lastRow = data.getDetails().size();
                    for (int col = 0; col <= 10; col++) {
                        sheet.addMergedRegion(new CellRangeAddress(increaseRow, increaseRow + lastRow - 1, col, col));
                    }
                }

                List<TimeSheetDetailDto> detailDtoList = data.getDetails();
                if (detailDtoList != null && !detailDtoList.isEmpty()) {
                    for (int j = 0; j < detailDtoList.size(); j++) {
                        String nameStaff = "";
                        if (detailDtoList.get(j).getEmployee() != null && detailDtoList.get(j).getEmployee().getDisplayName() != null) {
                            nameStaff = detailDtoList.get(j).getEmployee().getDisplayName();
                        }
                        String start = "";
                        if (detailDtoList.get(j).getStartTime() != null) {
                            start = formatDate.format(detailDtoList.get(j).getStartTime());
                        }
                        String end = "";
                        if (detailDtoList.get(j).getStartTime() != null) {
                            end = formatDate.format(detailDtoList.get(j).getEndTime());
                        }
                        if (detailDtoList.get(j).getWorkingItemTitle() == null) {
                            detailDtoList.get(j).setWorkingItemTitle("");
                        }
                        cellData = tableDataRow.createCell(11);
                        cellData.setCellValue(detailDtoList.get(j).getWorkingItemTitle());
                        cellData.setCellStyle(tableDataRowStyle);

                        cellData = tableDataRow.createCell(12);
                        cellData.setCellValue(start);
                        cellData.setCellStyle(tableDataRowStyle);

                        cellData = tableDataRow.createCell(13);
                        cellData.setCellValue(end);
                        cellData.setCellStyle(tableDataRowStyle);

                        cellData = tableDataRow.createCell(14);
                        cellData.setCellValue(detailDtoList.get(j).getDuration());
                        cellData.setCellStyle(tableDataRowStyle);

                        cellData = tableDataRow.createCell(15);
                        cellData.setCellValue(nameStaff);
                        cellData.setCellStyle(tableDataRowStyle);

                        if (j == detailDtoList.size() - 1) {
                            break;
                        }
                        increaseRow++;
                        tableDataRow = sheet.createRow(increaseRow);
                    }
                }
                increaseRow++;
            }
            for (int i = 1; i <= 15; i++) {
//                sheet.setColumnWidth(i, 20);
                sheet.setColumnWidth(i  , 22 * 256);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            out.close();
            return new ByteArrayResource(out.toByteArray());
        }
        return null;
    }
}
