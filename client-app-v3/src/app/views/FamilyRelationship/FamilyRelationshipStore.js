import { makeAutoObservable } from "mobx";
import { pagingFamilyRelationship, deleteFamilyRelationship, getFamilyRelationship, createFamilyRelationship, editFamilyRelationship } from "./FamilyRelationshipService";

export default class FamilyRelationshipStore {
  familyRelationshipList = [];
  totalElements = 0;
  totalPages = 0;
  page = 1;
  pageSize = 10;
  keyword = "";
  
  shouldOpenDialog = false;
  shouldOpenConfirmationDialog = false;
  itemId = null;
  
  familyRelationship = {
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
    this.fetchFamilyRelationshipDetails(item.id);
  };

  handleAddItem = () => {
    this.itemId = null;
    this.familyRelationship = { name: "", code: "", description: "" };
    this.shouldOpenDialog = true;
  };

  handleCloseDialog = () => {
    this.shouldOpenDialog = false;
    this.itemId = null;
    this.familyRelationship = { name: "", code: "", description: "" };
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
    this.familyRelationship[name] = value;
  };

  updatePageData = async () => {
    const searchObject = {
      pageIndex: this.page,
      pageSize: this.pageSize,
      keyword: this.keyword,
    };
    try {
      const res = await pagingFamilyRelationship(searchObject);
      if (res && res.data) {
        this.familyRelationshipList = res.data.content;
        this.totalElements = res.data.totalElements;
        this.totalPages = res.data.totalPages;
      }
    } catch (error) {
      console.error("error", error);
    }
  };

  fetchFamilyRelationshipDetails = async (id) => {
    try {
      const res = await getFamilyRelationship(id);
      if (res && res.data) {
        this.familyRelationship = res.data;
      }
    } catch (error) {
      console.error("error fetching details", error);
    }
  };

  handleFormSubmit = async (values) => {
    try {
      if (this.itemId) {
        await editFamilyRelationship(values);
      } else {
        await createFamilyRelationship(values);
      }
      this.handleCloseDialog();
      this.updatePageData();
    } catch (error) {
      console.error("error saving familyRelationship", error);
    }
  };

  handleConfirmDelete = async () => {
    try {
      await deleteFamilyRelationship(this.itemId);
      this.updatePageData();
      this.handleCloseConfirmationDialog();
    } catch (error) {
      console.error("Delete failed", error);
    }
  };
}
