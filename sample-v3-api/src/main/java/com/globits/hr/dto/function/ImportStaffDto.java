package com.globits.hr.dto.function;

import java.util.List;

import com.globits.hr.dto.StaffDto;

public class ImportStaffDto {
    List<StaffDto> listStaff;
    List<ImportExcelMessageDto> listMessage;

    public List<StaffDto> getListStaff() {
        return listStaff;
    }

    public void setListStaff(List<StaffDto> listStaff) {
        this.listStaff = listStaff;
    }

    public List<ImportExcelMessageDto> getListMessage() {
        return listMessage;
    }

    public void setListMessage(List<ImportExcelMessageDto> listMessage) {
        this.listMessage = listMessage;
    }
}
