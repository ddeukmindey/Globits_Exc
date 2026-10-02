import { makeAutoObservable, runInAction } from "mobx";
import {
    pagingProjects,
    getProject,
    createProject,
    editProject,
    deleteProject
} from './ProjectService';
import { pagingStaff } from "../Staff/StaffService";

export default class ProjectStore {
    itemList = [];
    totalElements = 0;
    totalPages = 0;
    page = 1;
    pageSize = 10;
    keyword = "";

    shouldOpenDialog = false;
    shouldOpenConfirmationDialog = false;
    itemId = null;

    staffList = [];

    item = this.getEmptyItem();

    constructor() {
        makeAutoObservable(this);
    }

    getEmptyItem() {
        return {
            name: "",
            code: "",
            description: "",
            projectStaff: [],
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

    updatePageData = async () => {
        const searchObject = {
            pageIndex: this.page,
            pageSize: this.pageSize,
            keyword: this.keyword,
        }
        try {
            const res = await pagingProjects(searchObject);
            if (res && res.data) {
                runInAction(() => {
                    this.itemList = res.data.content;
                    this.totalElements = res.data.totalElements;
                    this.totalPages = res.data.totalPages;
                })
            }

        } catch (error) {
            console.log(error);
        }

    }
    fetchLookupLists = async () => {

        if (this.staffList.length === 0) {
            try {
                const largeSearchObj = { pageIndex: 1, pageSize: 10000, keyword: "" };
                const sRes = await pagingStaff(largeSearchObj);

                runInAction(() => {
                    this.staffList = sRes?.data?.content || [];
                });
            } catch (error) {
                console.error("Failed to fetch dictionary data", error);
            }
        }
    };

    handleAddItem = async () => {
        this.itemId = null;
        this.item = this.getEmptyItem();
        await this.fetchLookupLists();
        runInAction(() => {
            this.shouldOpenDialog = true;
        })
    }
    handleEditItem = async (rowData) => {
        this.itemId = rowData.id;
        await this.fetchLookupLists();
        await this.fetchDetails(rowData.id);
        runInAction(() => {
            this.shouldOpenDialog = true;
        });
    };

    fetchDetails = async (id) => {
        try {
            const res = await getProject(id);
            if (res && res.data) {
                runInAction(() => {
                    this.item = res.data;

                    if (!this.item.projectStaff) {
                        this.item.projectStaff = [];
                    } 
                });
            }
        } catch (error) {
            console.error("error fetching project details", error);
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
            await deleteProject(this.itemId);
            this.updatePageData();
            this.handleCloseConfirmationDialog();
        } catch (error) {
            console.error("delete project failed", error);
        }
    };

    handleChange = (e) => {
        const { name, value } = e.target;
        this.item[name] = value;
    }

    handleAutocompleteChange = (field, value) => {
        this.item[field] = value;
    }

    handleFormSubmit = async (values) => {
        try {
            if (this.itemId) {
                await editProject(values)
            }
            else {
                await createProject(values)
            }
            this.handleCloseDialog();
            this.updatePageData();
        } catch (error) {
            console.error("error saving project", error);
        }
    }

}
