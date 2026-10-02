package com.globits.hr.timesheet.domain;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.hr.domain.TimeSheetStaff;

import com.globits.core.domain.BaseObject;
import com.globits.hr.domain.ShiftWork;
import com.globits.hr.domain.WorkingStatus;

@XmlRootElement
@Table(name = "tbl_timesheet")
@Entity
public class TimeSheet extends BaseObject {
	private static final long serialVersionUID = 1L;
	
	@Column(name="working_date")
	private Date workingDate;
	@Column(name ="total_hours")
	private double 	totalHours;//Tổng thời gian làm việc (không nhất thiết phải là giờ kết thúc - giờ bắt đầu vì còn vấn đề nghỉ giữa giờ, ra ngoài, ...)
	
	@Column(name="start_time")
	private Date startTime;//Thời điểm bắt đầu làm việc
	@Column(name="end_time")
	private Date endTime;//Thời điểm kết thúc công việc
	
	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@ManyToOne (cascade= CascadeType.PERSIST)
	@JoinColumn(name="project_id")
	private Project project;//Thuộc Project nào
	@ManyToOne (cascade= CascadeType.PERSIST)
	@JoinColumn(name="activity_id")
	private ProjectActivity activity;//Cho activity nào của project (có thể dùng sau)
	@ManyToOne (cascade= CascadeType.PERSIST)
	@JoinColumn(name="shift_work_id")
	private ShiftWork shiftWork;
	
	@OneToMany(mappedBy="timeSheet", cascade=CascadeType.ALL, orphanRemoval = true)
	private Set<TimeSheetDetail> details;
	
	@ManyToOne
	@JoinColumn(name="working_status_id")
	private WorkingStatus workingStatus;//Trạng thái thực hiện.
	
	@Column(name ="approve_status")
	private Integer approveStatus;//Trạng thái phê duyệt - 0 = chưa phê duyệt, 1 đã phê duyệt
	@Column(name ="priority")
	private Integer priority;//Sự ưu tiên - 3 = Cao, 2 = trung bình, 1 = thấp, 4 = Gấp

	@OneToMany(mappedBy = "timesheet", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<TimeSheetStaff> timeSheetStaffSet;

	@OneToMany(mappedBy = "timesheet", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<TimeSheetLabel> labels;

	public Date getWorkingDate() {
		return workingDate;
	}

	public void setWorkingDate(Date workingDate) {
		this.workingDate = workingDate;
	}

	public double getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(double totalHours) {
		this.totalHours = totalHours;
	}

	public ShiftWork getShiftWork() {
		return shiftWork;
	}

	public void setShiftWork(ShiftWork shiftWork) {
		this.shiftWork = shiftWork;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public WorkingStatus getWorkingStatus() {
		return workingStatus;
	}

	public void setWorkingStatus(WorkingStatus workingStatus) {
		this.workingStatus = workingStatus;
	}

	public Integer getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(Integer approveStatus) {
		this.approveStatus = approveStatus;
	}

	public Set<TimeSheetDetail> getDetails() {
		return details;
	}

	public void setDetails(Set<TimeSheetDetail> details) {
		this.details = details;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public ProjectActivity getActivity() {
		return activity;
	}

	public void setActivity(ProjectActivity activity) {
		this.activity = activity;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Set<TimeSheetStaff> getTimeSheetStaffSet() {
		return timeSheetStaffSet;
	}

	public void setTimeSheetStaffSet(Set<TimeSheetStaff> timeSheetStaffSet) {
		this.timeSheetStaffSet = timeSheetStaffSet;
	}

	public Set<TimeSheetLabel> getLabels() {
		return labels;
	}

	public void setLabels(Set<TimeSheetLabel> labels) {
		this.labels = labels;
	}

	
}
