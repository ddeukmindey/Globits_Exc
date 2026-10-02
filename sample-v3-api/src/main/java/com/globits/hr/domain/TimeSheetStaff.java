package com.globits.hr.domain;

import com.globits.core.domain.BaseObject;
import com.globits.hr.timesheet.domain.TimeSheet;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Table(name = "tbl_timesheet_staff")
@Entity
public class TimeSheetStaff extends BaseObject {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "timesheet_id")
    private TimeSheet timesheet;

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public TimeSheet getTimesheet() {
        return timesheet;
    }

    public void setTimesheet(TimeSheet timesheet) {
        this.timesheet = timesheet;
    }
}
