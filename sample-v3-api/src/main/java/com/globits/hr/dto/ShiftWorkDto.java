package com.globits.hr.dto;

import com.globits.hr.domain.ShiftWork;
import com.globits.hr.domain.ShiftWorkTimePeriod;

import java.util.List;

public class ShiftWorkDto extends BaseObjectDto {
    private String code;
    private String name;
    private Double totalHours;
    private List<ShiftWorkTimePeriodDto> timePeriods;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Double totalHours) {
        this.totalHours = totalHours;
    }

    public List<ShiftWorkTimePeriodDto> getTimePeriods() {
        return timePeriods;
    }

    public void setTimePeriods(List<ShiftWorkTimePeriodDto> timePeriods) {
        this.timePeriods = timePeriods;
    }

    public ShiftWorkDto() {
    }

    public ShiftWorkDto(ShiftWork shiftWork) {
        this.id = shiftWork.getId();
        this.code = shiftWork.getCode();
        this.name = shiftWork.getName();
        this.totalHours = shiftWork.getTotalHours();
        if (shiftWork.getTimePeriods() != null && shiftWork.getTimePeriods().size() > 0) {
            for (ShiftWorkTimePeriod timePeriodEntity : shiftWork.getTimePeriods()) {
                this.getTimePeriods().add(new ShiftWorkTimePeriodDto(timePeriodEntity));
            }
        }
    }
}
