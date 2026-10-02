import { makeAutoObservable } from "mobx";
import { pagingCompanies, deleteCompany, getCompany, createCompany, editCompany } from "./CompanyService";

export default class CompanyStore {
  companyList = [];
  totalElements = 0;
  totalPages = 0;
  page = 1;
  pageSize = 10;
  keyword = "";

  shouldOpenDialog = false;
  shouldOpenConfirmationDialog = false;
  itemId = null;

  company = {
    name: "",
    code: "",
    address: "",
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

  handleEditItem = (item) => {
    this.itemId = item.id;
    this.shouldOpenDialog = true;
    this.fetchCompanyDetails(item.id);
  };

  handleAddItem = () => {
    this.itemId = null;
    this.company = { name: "", code: "", address: "" };
    this.shouldOpenDialog = true;
  };

  handleCloseDialog = () => {
    this.shouldOpenDialog = false;
    this.itemId = null;
    this.company = { name: "", code: "", address: "" };
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
      keyword: this.keyword,
    };
    try {
      const res = await pagingCompanies(searchObject);
      if (res && res.data) {
        this.companyList = res.data.content || [];
        this.totalElements = res.data.totalElements || 0;
        this.totalPages = res.data.totalPages || 0;
      }
    } catch (error) {
      console.error("error fetching companies", error);
    }
  };

  fetchCompanyDetails = async (id) => {
    try {
      const res = await getCompany(id);
      if (res && res.data) {
        this.company = res.data;
      }
    } catch (error) {
      console.error("error fetching company details", error);
    }
  };

  handleFormSubmit = async (values) => {
    try {
      if (this.itemId) {
        await editCompany(values);
      } else {
        await createCompany(values);
      }
      this.handleCloseDialog();
      this.updatePageData();
    } catch (error) {
      console.error("error saving company", error);
    }
  };

  handleConfirmDelete = async () => {
    try {
      await deleteCompany(this.itemId);
      this.updatePageData();
      this.handleCloseConfirmationDialog();
    } catch (error) {
      console.error("delete company failed", error);
    }
  };
}
