package com.globits.hr.timesheet.domain;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@XmlRootElement
@Table(name = "tbl_timesheet_label")
@Entity
public class TimeSheetLabel extends BaseObject{
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "timesheet_id")
    private TimeSheet timesheet;

    @ManyToOne
	@JoinColumn(name="label_id")
	private Label label;

    public TimeSheet getTimesheet() {
        return timesheet;
    }

    public void setTimesheet(TimeSheet timesheet) {
        this.timesheet = timesheet;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    
}
