import { makeAutoObservable, runInAction } from "mobx";
import {
    pagingTimeSheets,
    getTimeSheet,
    createTimeSheet,
    editTimeSheet,
    deleteTimeSheet
} from './TimeSheetService';
import { pagingProjects } from "../Project/ProjectService";
import { getAllStaffs } from "../Staff/StaffService";

export default class TimeSheetStore {
    itemList = [];
    totalElements = 0;
    totalPages = 0;
    page = 1;
    pageSize = 10;
    keyword = "";

    selectedProjectId = null;
    projectList = [];
    staffList = [];

    shouldOpenDialog = false;
    shouldOpenConfirmationDialog = false;
    itemId = null;

    item = this.getEmptyItem();

    constructor() {
        makeAutoObservable(this);
    }

    getEmptyItem() {
        return {
            id: null,
            project: null,
            timeSheetStaff: [],
            workingDate: null,
            startTime: null,
            endTime: null,
            priority: "",
            description: "",
            details: [],
        }
    }

    setPage = (page) => {
        this.page = page;
        this.updatePageData();
    };

    setPageSize = (pageSize) => {
        this.pageSize = pageSize;
        this.page = 1;
        this.updatePageData();
    };

    setKeyword = (keyword) => {
        this.keyword = keyword;
        this.page = 1;
        this.updatePageData();
    };

    setSelectedProjectId = (projectId) => {
        this.selectedProjectId = projectId;
        this.page = 1;
        this.updatePageData();
    };

    updatePageData = async () => {
        const searchObject = {
            pageIndex: this.page,
            pageSize: this.pageSize,
            keyword: this.keyword,
            projectId: this.selectedProjectId,
        }
        try {
            const res = await pagingTimeSheets(searchObject);
            if (res && res.data) {
                runInAction(() => {
                    this.itemList = res.data.content;
                    this.totalElements = res.data.totalElements;
                    this.totalPages = res.data.totalPages;
                })
            }
        } catch (error) {
            console.error("Failed to fetch timesheets", error);
        }
    }

    fetchProjectList = async () => {
        if (this.projectList.length === 0) {
            try {
                const searchObj = { pageIndex: 1, pageSize: 10000, keyword: "" };
                const pRes = await pagingProjects(searchObj);
                runInAction(() => {
                    this.projectList = pRes?.data?.content || [];
                });
            } catch (error) {
                console.error("Failed to fetch projects", error);
            }
        }
    };

    fetchStaffList = async () => {
        if (this.staffList.length === 0) {
            try {
                const sRes = await getAllStaffs();
                runInAction(() => {
                    this.staffList = sRes?.data || [];
                });
            } catch (error) {
                console.error("Failed to fetch staff list", error);
            }
        }
    };

    handleAddItem = async () => {
        this.itemId = null;
        this.item = this.getEmptyItem();
        await this.fetchProjectList();
        await this.fetchStaffList();
        
        runInAction(() => {
            this.shouldOpenDialog = true;
        })
    }
    
    handleEditItem = async (id) => {
        this.itemId = id;
        await this.fetchProjectList();
        await this.fetchStaffList();
        await this.fetchDetails(id);
        runInAction(() => {
            this.shouldOpenDialog = true;
        });
    };

    fetchDetails = async (id) => {
        try {
            const res = await getTimeSheet(id);
            if (res && res.data) {
                runInAction(() => {
                    this.item = res.data;
                    if (!this.item.timeSheetStaff) {
                        this.item.timeSheetStaff = [];
                    }
                    if (!this.item.details) {
                        this.item.details = [];
                    }
                });
            }
        } catch (error) {
            console.error("Error fetching timesheet details", error);
        }
    };
    
    handleCloseDialog = () => {
        this.shouldOpenDialog = false;
        this.itemId = null;
        this.item = this.getEmptyItem();
    };

    handleDeleteItem = (id) => {
        this.itemId = id;
        this.shouldOpenConfirmationDialog = true;
    };

    handleCloseConfirmationDialog = () => {
        this.shouldOpenConfirmationDialog = false;
        this.itemId = null;
    };

    handleConfirmDelete = async () => {
        try {
            await deleteTimeSheet(this.itemId);
            this.updatePageData();
            this.handleCloseConfirmationDialog();
        } catch (error) {
            console.error("delete timesheet failed", error);
        }
    };

    handleChange = (e) => {
        const { name, value } = e.target;
        this.item[name] = value;
    }

    handleAutocompleteChange = (field, value) => {
        this.item[field] = value;
        // If changing timeSheetStaff, make sure to remove employees from details that are no longer in timeSheetStaff
        if (field === 'timeSheetStaff') {
            if (this.item.details && this.item.details.length > 0) {
                const staffIds = (value || []).map(s => s.id);
                this.item.details = this.item.details.map(detail => {
                    if (detail.employee && !staffIds.includes(detail.employee.id)) {
                        return { ...detail, employee: null };
                    }
                    return detail;
                });
            }
        }
        // If changing project, clear timeSheetStaff
        if (field === 'project') {
             this.item.timeSheetStaff = [];
             this.item.details = [];
        }
    }

    handleDateChange = (field, date) => {
        this.item[field] = date;
    }

    handleAddDetail = () => {
        this.item.details.push({
            workingItemTitle: "",
            employee: null
        });
    }

    handleRemoveDetail = (index) => {
        this.item.details.splice(index, 1);
    }

    handleDetailChange = (index, field, value) => {
        this.item.details[index][field] = value;
    }

    handleFormSubmit = async (values) => {
        try {
            if (this.itemId) {
                await editTimeSheet(values)
            }
            else {
                await createTimeSheet(values)
            }
            this.handleCloseDialog();
            this.updatePageData();
        } catch (error) {
            console.error("error saving timesheet", error);
        }
    }

}
