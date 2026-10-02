import { makeAutoObservable } from "mobx";
import { pagingReligions, deleteReligion, getReligion, createReligion, editReligion } from "./ReligionService";

export default class ReligionStore {
  religionList = [];
  totalElements = 0;
  totalPages = 0;
  page = 1;
  pageSize = 10;
  keyword = "";
  
  shouldOpenDialog = false;
  shouldOpenConfirmationDialog = false;
  itemId = null;
  
  religion = {
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
    this.fetchReligionDetails(item.id);
  };

  handleAddItem = () => {
    this.itemId = null;
    this.religion = { name: "", code: "", description: "" };
    this.shouldOpenDialog = true;
  };

  handleCloseDialog = () => {
    this.shouldOpenDialog = false;
    this.itemId = null;
    this.religion = { name: "", code: "", description: "" };
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
    this.religion[name] = value;
  };

  updatePageData = async () => {
    const searchObject = {
      pageIndex: this.page,
      pageSize: this.pageSize,
      keyword: this.keyword,
    };
    try {
      const res = await pagingReligions(searchObject);
      if (res && res.data) {
        this.religionList = res.data.content;
        this.totalElements = res.data.totalElements;
        this.totalPages = res.data.totalPages;
      }
    } catch (error) {
      console.error("error", error);
    }
  };

  fetchReligionDetails = async (id) => {
    try {
      const res = await getReligion(id);
      if (res && res.data) {
        this.religion = res.data;
      }
    } catch (error) {
      console.error("error fetching details", error);
    }
  };

  handleFormSubmit = async (values) => {
    try {
      if (this.itemId) {
        await editReligion(values);
      } else {
        await createReligion(values);
      }
      this.handleCloseDialog();
      this.updatePageData();
    } catch (error) {
      console.error("error saving religion", error);
    }
  };

  handleConfirmDelete = async () => {
    try {
      await deleteReligion(this.itemId);
      this.updatePageData();
      this.handleCloseConfirmationDialog();
    } catch (error) {
      console.error("Delete failed", error);
    }
  };
}