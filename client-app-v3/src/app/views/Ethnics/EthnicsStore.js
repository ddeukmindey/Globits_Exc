import { makeAutoObservable } from "mobx";
import { pagingEthnicities, deleteEthnics, getEthnics, createEthnics, editEthnics } from "./EthnicsService";

export default class EthnicsStore {
  ethnicsList = [];
  totalElements = 0;
  totalPages = 0;
  page = 1;
  pageSize = 10;
  keyword = "";
  
  shouldOpenDialog = false;
  shouldOpenConfirmationDialog = false;
  itemId = null;
  
  ethnics = {
    name: "",
    code: "",
    description: "",
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
    this.fetchEthnicsDetails(item.id);
  };

  handleAddItem = () => {
    this.itemId = null;
    this.ethnics = { name: "", code: "", description: "" };
    this.shouldOpenDialog = true;
  };

  handleCloseDialog = () => {
    this.shouldOpenDialog = false;
    this.itemId = null;
    this.ethnics = { name: "", code: "", description: "" };
  };

  handleDeleteItem = (id) => {
    this.itemId = id;
    this.shouldOpenConfirmationDialog = true;
  };

  handleCloseConfirmationDialog = () => {
    this.shouldOpenConfirmationDialog = false;
    this.itemId = null;
  };

  handleChange = (e) => {
    const { name, value } = e.target;
    this.ethnics[name] = value;
  };

  updatePageData = async () => {
    const searchObject = {
      pageIndex: this.page,
      pageSize: this.pageSize,
      keyword: this.keyword,
    };
    try {
      const res = await pagingEthnicities(searchObject);
      if (res && res.data) {
        this.ethnicsList = res.data.content;
        this.totalElements = res.data.totalElements;
        this.totalPages = res.data.totalPages;
      }
    } catch (error) {
      console.error("error", error);
    }
  };

  fetchEthnicsDetails = async (id) => {
    try {
      const res = await getEthnics(id);
      if (res && res.data) {
        this.ethnics = res.data;
      }
    } catch (error) {
      console.error("error fetching details", error);
    }
  };

  handleFormSubmit = async (values) => {
    try {
      if (this.itemId) {
        await editEthnics(values);
      } else {
        await createEthnics(values);
      }
      this.handleCloseDialog();
      this.updatePageData();
    } catch (error) {
      console.error("error saving ethnics", error);
    }
  };

  handleConfirmDelete = async () => {
    try {
      await deleteEthnics(this.itemId);
      this.updatePageData();
      this.handleCloseConfirmationDialog();
    } catch (error) {
      console.error("Delete failed", error);
    }
  };
}
