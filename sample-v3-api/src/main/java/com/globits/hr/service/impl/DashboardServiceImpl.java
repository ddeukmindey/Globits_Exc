package com.globits.hr.service.impl;

import com.globits.hr.dto.function.DashboardDto;
import com.globits.hr.dto.search.DashboardSearchDto;
import com.globits.hr.service.DashboardService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Service
public class DashboardServiceImpl implements DashboardService {

    @PersistenceContext
    EntityManager manager;

    @Override
    public DashboardDto getDashboard(DashboardSearchDto search) {
        DashboardDto dashboardDto = new DashboardDto();
        dashboardDto.setStaffNumber(this.getTotalStaff());
        dashboardDto.setProjectNumber(this.getTotalProject());
        dashboardDto.setMonthTaskNumber(this.getMonthTasks());

        return dashboardDto;
    }

    public Long getTotalStaff() {
        String whereClause = " ";
        String sql = "select count(entity.id) from Staff as entity where (1=1) ";
        sql += whereClause;
        Query q = manager.createQuery(sql);
        return (Long) q.getSingleResult();
    }

    public Long getTotalProject() {
        String whereClause = "";
        String sql = "select count(entity.id) from Project as entity where (1=1) ";
        sql += whereClause;
        Query q = manager.createQuery(sql);
        return (Long) q.getSingleResult();
    }

    public Long getMonthTasks() {
        String sql = "select count(entity.id) from TimeSheet as entity where (1=1)  ";
        String whereClause = "AND MONTH(entity.createDate) = MONTH(CURRENT_DATE)";
        sql += whereClause;
        Query q = manager.createQuery(sql);
        return (Long) q.getSingleResult();
    }

}
