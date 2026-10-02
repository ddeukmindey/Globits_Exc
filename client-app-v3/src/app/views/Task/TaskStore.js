import { makeAutoObservable } from "mobx";
import { pagingTasks, deleteTask, getTask, createTask, editTask, exportExcel } from "./TaskService";
import { pagingProjects } from "../Project/ProjectService";
import { getAllStaffs } from "../Staff/StaffService";
import { saveAs } from "file-saver";

export default class TaskStore {
  taskList = [];
  projectList = [];
  staffList = [];
  totalElements = 0;
  totalPages = 0;
  page = 1;
  pageSize = 10;
  keyword = "";
  projectId = null;
  staffId = null;
  priority = null;
  status = null;

  shouldOpenDialog = false;
  shouldOpenConfirmationDialog = false;
  itemId = null;

  task = {
    name: "",
    description: "",
    startTime: null,
    endTime: null,
    priority: 1,
    status: 1,
    projectId: null,
    staffId: null,
  };

  constructor() {
    makeAutoObservable(this);
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

  setPriorityFilter = (priority) => {
    this.priority = priority;
    this.page = 1;
    this.updatePageData();
  };

  setStatusFilter = (status) => {
    this.status = status;
    this.page = 1;
    this.updatePageData();
  };

  handleEditItem = (item) => {
    this.itemId = item.id;
    this.shouldOpenDialog = true;
    this.fetchTaskDetails(item.id);
  };

  handleAddItem = () => {
    this.itemId = null;
    this.task = {
      name: "",
      description: "",
      startTime: null,
      endTime: null,
      priority: 1,
      status: 1,
      projectId: null,
      staffId: null,
    };
    this.shouldOpenDialog = true;
  };

  handleCloseDialog = () => {
    this.shouldOpenDialog = false;
    this.itemId = null;
    this.task = {
      name: "",
      description: "",
      startTime: null,
      endTime: null,
      priority: 1,
      status: 1,
      projectId: null,
      staffId: null,
    };
  };

  handleDeleteItem = (id) => {
    this.itemId = id;
    this.shouldOpenConfirmationDialog = true;
  };

  handleCloseConfirmationDialog = () => {
    this.shouldOpenConfirmationDialog = false;
    this.itemId = null;
  };

  updatePageData = async () => {
    const searchObject = {
      pageIndex: this.page,
      pageSize: this.pageSize,
      keyword: this.keyword || null,
      projectId: this.projectId || null,
      staffId: this.staffId || null,
      priority: this.priority || null,
      status: this.status || null,
    };
    try {
      const res = await pagingTasks(searchObject);
      if (res && res.data) {
        this.taskList = res.data.content || [];
        this.totalElements = res.data.totalElements || 0;
        this.totalPages = res.data.totalPages || 0;
      }
    } catch (error) {
      console.error("error fetching tasks", error);
    }
  };

  loadDropdownData = async () => {
    try {
      const pRes = await pagingProjects({ pageIndex: 1, pageSize: 100 });
      if (pRes && pRes.data) {
        this.projectList = pRes.data.content || [];
      }
    } catch (e) {
      console.error("error loading projects", e);
    }

    try {
      const sRes = await getAllStaffs();
      if (sRes && sRes.data) {
        this.staffList = sRes.data || [];
      }
    } catch (e) {
      console.error("error loading staff", e);
    }
  };

  fetchTaskDetails = async (id) => {
    try {
      const res = await getTask(id);
      if (res && res.data) {
        this.task = res.data;
      }
    } catch (error) {
      console.error("error fetching task details", error);
    }
  };

  handleFormSubmit = async (values) => {
    try {
      if (this.itemId) {
        await editTask(values);
      } else {
        await createTask(values);
      }
      this.handleCloseDialog();
      this.updatePageData();
    } catch (error) {
      console.error("error saving task", error);
    }
  };

  handleConfirmDelete = async () => {
    try {
      await deleteTask(this.itemId);
      this.updatePageData();
      this.handleCloseConfirmationDialog();
    } catch (error) {
      console.error("delete task failed", error);
    }
  };

  handleExportExcel = async () => {
    const searchObject = {
      keyword: this.keyword || null,
      projectId: this.projectId || null,
      staffId: this.staffId || null,
      priority: this.priority || null,
      status: this.status || null,
    };
    try {
      const res = await exportExcel(searchObject);
      const blob = new Blob([res.data], {
        type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
      });
      saveAs(blob, "Danh_sach_cong_viec.xlsx");
    } catch (error) {
      console.error("export excel failed", error);
    }
  };
}
