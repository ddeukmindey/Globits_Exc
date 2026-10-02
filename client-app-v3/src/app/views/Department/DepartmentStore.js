import { makeAutoObservable } from "mobx";
import { pagingDepartments, getDepartment, createDepartment, editDepartment, deleteDepartment } from "./DepartmentService";

export default class DepartmentStore {
  departmentList = [];
  totalElements = 0;
  totalPages = 0;
  page = 1;
  pageSize = 10;
  keyword = "";

  shouldOpenDialog = false;
  shouldOpenConfirmationDialog = false;
  itemId = null;

  department = {
    name: "",
    code: "",
    description: "",
    func: "",
    industryBlock: "",
    foundedNumber: "",
    foundedDate: "",
    displayOrder: "",
    parent: null,
  };

  // Popup for Parent Department
  shouldOpenParentPopup = false;
  popupDepartmentList = [];
  popupTotalElements = 0;
  popupTotalPages = 0;
  popupPage = 1;
  popupPageSize = 5;
  popupKeyword = "";
  selectedParent = null; // Temporary selection in popup

  constructor() {
    makeAutoObservable(this);
  }

  // ---- Main Table Methods ----
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

  updatePageData = async () => {
    const searchObject = {
      pageIndex: this.page,
      pageSize: this.pageSize,
      keyword: this.keyword,
    };
    try {
      const res = await pagingDepartments(searchObject);
      if (res && res.data) {
        this.departmentList = res.data.content;
        this.totalElements = res.data.totalElements;
        this.totalPages = res.data.totalPages;
      }
    } catch (error) {
      console.error("error fetching departments", error);
    }
  };

  // ---- Dialog & Form Methods ----
  handleAddItem = () => {
    this.itemId = null;
    this.department = {
      name: "", code: "", description: "", func: "", industryBlock: "", 
      foundedNumber: "", foundedDate: "", displayOrder: "", parent: null
    };
    this.shouldOpenDialog = true;
  };

  handleEditItem = (item) => {
    this.itemId = item.id;
    this.shouldOpenDialog = true;
    this.fetchDepartmentDetails(item.id);
  };

  fetchDepartmentDetails = async (id) => {
    try {
      const res = await getDepartment(id);
      if (res && res.data) {
        this.department = res.data;
      }
    } catch (error) {
      console.error("error fetching details", error);
    }
  };

  handleCloseDialog = () => {
    this.shouldOpenDialog = false;
    this.itemId = null;
    this.department = {};
  };

  handleChange = (e) => {
    const { name, value } = e.target;
    this.department[name] = value;
  };

  handleFormSubmit = async (values) => {
    try {
      const payload = {
        ...values,
        foundedDate: values.foundedDate ? values.foundedDate : null,
      };
      if (this.itemId) {
        await editDepartment(payload);
      } else {
        await createDepartment(payload);
      }
      this.handleCloseDialog();
      this.updatePageData();
    } catch (error) {
      console.error("error saving department", error);
    }
  };

  // ---- Delete Methods ----
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
      await deleteDepartment(this.itemId);
      this.updatePageData();
      this.handleCloseConfirmationDialog();
    } catch (error) {
      console.error("Delete failed", error);
    }
  };

  // ---- Parent Selection Popup Methods ----
  handleOpenParentPopup = () => {
    this.selectedParent = this.department.parent; // Initialize with current parent
    this.shouldOpenParentPopup = true;
    this.updatePopupData();
  };

  handleCloseParentPopup = () => {
    this.shouldOpenParentPopup = false;
    this.selectedParent = null;
  };

  handleSelectRadioParent = (item) => {
    this.selectedParent = item;
  };

  handleConfirmParentOption = () => {
    this.department.parent = this.selectedParent;
    this.handleCloseParentPopup();
  };

  setPopupPage = (page) => {
    this.popupPage = page;
    this.updatePopupData();
  };

  setPopupPageSize = (pageSize) => {
    this.popupPageSize = pageSize;
    this.popupPage = 1;
    this.updatePopupData();
  };

  setPopupKeyword = (keyword) => {
    this.popupKeyword = keyword;
    this.popupPage = 1;
    this.updatePopupData();
  };

  updatePopupData = async () => {
    const searchObject = {
      pageIndex: this.popupPage + 1, // Material Table uses 0-based for its page, but API is 1-based? Actually GlobitsTable uses 0-based index or 1-based? Let's check GlobitsPagination later.
      pageSize: this.popupPageSize,
      keyword: this.popupKeyword,
    };
    try {
      // In Globits project, table page is usually 0-based if from TablePagination, 
      // but API expects 1-based. Let's send popupPage+1 if popupPage starts at 0, or just send popupPage if it starts at 1.
      // Usually MaterialUI TablePagination starts at 0. So popupPage+1.
      searchObject.pageIndex = this.popupPage + 1; 
      
      const res = await pagingDepartments(searchObject);
      if (res && res.data) {
        this.popupDepartmentList = res.data.content;
        this.popupTotalElements = res.data.totalElements;
        this.popupTotalPages = res.data.totalPages;
      }
    } catch (error) {
      console.error("error fetching popup departments", error);
    }
  };
}
