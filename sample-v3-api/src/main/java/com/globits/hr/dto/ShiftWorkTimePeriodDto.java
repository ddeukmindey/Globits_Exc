package com.globits.hr.dto;

import com.globits.hr.domain.ShiftWorkTimePeriod;

import java.util.Date;

public class ShiftWorkTimePeriodDto extends BaseObjectDto {
    private ShiftWorkDto shiftWorkDto;
    private Date endTime;
    private Date startTime;

    public ShiftWorkTimePeriodDto(ShiftWorkTimePeriod timePeriod) {
        this.endTime = timePeriod.getEndTime();
        this.startTime = timePeriod.getStartTime();
        if (timePeriod.getShiftWork() != null) {
            this.shiftWorkDto = new ShiftWorkDto(timePeriod.getShiftWork());
        }
    }

    public ShiftWorkTimePeriodDto() {
    }

    public ShiftWorkDto getShiftWorkDto() {
        return shiftWorkDto;
    }

    public void setShiftWorkDto(ShiftWorkDto shiftWorkDto) {
        this.shiftWorkDto = shiftWorkDto;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public ShiftWorkTimePeriod toEntity(ShiftWorkTimePeriodDto dto, ShiftWorkTimePeriod entity) {
        entity.setId(dto.getId());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        return entity;
    }
}

