import { makeAutoObservable } from "mobx";
import { pagingCountries, deleteCountry, getCountry, createCountry, editCountry } from "./CountryService";

export default class CountryStore {
  countryList = [];
  totalElements = 0;
  totalPages = 0;
  page = 1;
  pageSize = 10;
  keyword = "";
  
  shouldOpenDialog = false;
  shouldOpenConfirmationDialog = false;
  itemId = null;
  
  country = {
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
    this.fetchCountryDetails(item.id);
  };

  handleAddItem = () => {
    this.itemId = null;
    this.country = { name: "", code: "", description: "" };
    this.shouldOpenDialog = true;
  };

  handleCloseDialog = () => {
    this.shouldOpenDialog = false;
    this.itemId = null;
    this.country = { name: "", code: "", description: "" };
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
    this.country[name] = value;
  };

  updatePageData = async () => {
    const searchObject = {
      pageIndex: this.page,
      pageSize: this.pageSize,
      keyword: this.keyword,
    };
    try {
      const res = await pagingCountries(searchObject);
      if (res && res.data) {
        this.countryList = res.data.content;
        this.totalElements = res.data.totalElements;
        this.totalPages = res.data.totalPages;
      }
    } catch (error) {
      console.error("error", error);
    }
  };

  fetchCountryDetails = async (id) => {
    try {
      const res = await getCountry(id);
      if (res && res.data) {
        this.country = res.data;
      }
    } catch (error) {
      console.error("error fetching details", error);
    }
  };

  handleFormSubmit = async (values) => {
    try {
      if (this.itemId) {
        await editCountry(values);
      } else {
        await createCountry(values);
      }
      this.handleCloseDialog();
      this.updatePageData();
    } catch (error) {
      console.error("error saving country", error);
    }
  };

  handleConfirmDelete = async () => {
    try {
      await deleteCountry(this.itemId);
      this.updatePageData();
      this.handleCloseConfirmationDialog();
    } catch (error) {
      console.error("Delete failed", error);
    }
  };
}
