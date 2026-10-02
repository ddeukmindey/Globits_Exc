import { makeAutoObservable, runInAction } from "mobx";
import {
    pagingStaff,
    deleteStaff,
    createStaff,
    getAllStaffs,
    getStaff,
    editStaff
} from './StaffService'

import { pagingCountries } from "../Country/CountryService";
import { pagingEthnicities } from "../Ethnics/EthnicsService";
import { pagingReligions } from "../Religion/ReligionService";
import { pagingDepartments } from "../Department/DepartmentService";
import { pagingFamilyRelationship } from "../FamilyRelationship/FamilyRelationshipService";
import { pagingProjects } from "../Project/ProjectService";

export default class StaffStore {
    itemList = [];
    totalElements = 0;
    totalPages = 0;
    page = 1;
    pageSize = 10;
    keyword = "";

    shouldOpenDialog = false;
    shouldOpenConfirmationDialog = false;
    itemId = null;

    countryList = [];
    ethnicityList = [];
    religionList = [];
    departmentList = [];
    relationshipList = [];
    projectList = [];

    selectedDepartmentId = null;
    selectedEthnicsId = null;
    selectedReligionId = null;
    selectedCountryId = null;
    selectedProjectId = null;

    item = this.getEmptyItem();

    constructor() {
        makeAutoObservable(this);
    }

    getEmptyItem() {
        return {
            lastName: "",
            firstName: "",
            displayName: "",
            gender: "M", // Default
            birthDate: null,
            birthPlace: "",
            permanentResidence: "",
            currentResidence: "",
            email: "",
            phoneNumber: "",
            idNumber: "",
            nationality: null,
            ethnics: null,
            religion: null,
            department: null,
            familyRelationships: [],
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

    setFilter = (field, value) => {
        this[field] = value;
        this.page = 1;
        this.updatePageData();
    };

    updatePageData = async () => {
        const searchObject = {
            pageIndex: this.page,
            pageSize: this.pageSize,
            keyword: this.keyword,
            departmentId: this.selectedDepartmentId,
            ethnicsId: this.selectedEthnicsId,
            religionId: this.selectedReligionId,
            countryId: this.selectedCountryId,
            projectId: this.selectedProjectId,
        }
        try {
            const res = await pagingStaff(searchObject);
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

        if (this.countryList.length === 0) {
            try {
                const largeSearchObj = { pageIndex: 1, pageSize: 10000, keyword: "" };
                const [cRes, eRes, rRes, dRes, fRes, pRes] = await Promise.all([
                    pagingCountries(largeSearchObj),
                    pagingEthnicities(largeSearchObj),
                    pagingReligions(largeSearchObj),
                    pagingDepartments(largeSearchObj),
                    pagingFamilyRelationship(largeSearchObj),
                    pagingProjects(largeSearchObj)
                ]);

                runInAction(() => {
                    this.countryList = cRes?.data?.content || [];
                    this.ethnicsList = eRes?.data?.content || [];
                    this.religionList = rRes?.data?.content || [];

                    this.departmentList = dRes?.data?.content || [];
                    this.relationshipList = fRes?.data?.content || [];
                    this.projectList = pRes?.data?.content || [];
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
            const res = await getStaff(id);
            if (res && res.data) {
                runInAction(() => {
                    this.item = res.data;


                    if (this.item.birthDate && this.item.birthDate.length > 10) {
                        this.item.birthDate = this.item.birthDate.substring(0, 10);
                    }

                    if (!this.item.familyRelationships) {
                        this.item.familyRelationships = [];
                    } else {

                        this.item.familyRelationships.forEach(rel => {
                            if (rel.birthDate && rel.birthDate.length > 10) {
                                rel.birthDate = rel.birthDate.substring(0, 10);
                            }
                        });
                    }
                });
            }
        } catch (error) {
            console.error("error fetching staff details", error);
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
            await deleteStaff(this.itemId);
            this.updatePageData();
            this.handleCloseConfirmationDialog();
        } catch (error) {
            console.error("delete staff failed", error);
        }
    };

    handleChange = (e) => {
        const { name, value } = e.target;
        this.item[name] = value;
        if (name === "firstName" || name === "lastName") {
            this.item.displayName = `${this.item.firstName || ""} ${this.item.lastName || ""}`.trim();
        }

    }

    handleAutocompleteChange = (field, value) => {
        if (value && value.tableData) {
            const { tableData, ...pureItem } = value;
            this.item[field] = pureItem;
        } else {
            this.item[field] = value;
        }
    }
    handleAddFamilyRow = () => {
        this.item.familyRelationships.push({
            fullName: "",
            profession: "",
            birthDate: "",
            familyRelationship: null,
            address: "",
            description: ""
        });
    };

    handleRemoveFamilyRow = (index) => {
        this.item.familyRelationships.splice(index, 1);
    }

    handleChangeFamilyRow = (index, field, value) => {
        this.item.familyRelationships[index][field] = value;
    }

    handleFamilyRowAutocompleteChange = (row, value) => {
        if (value && value.tableData) {
            const { tableData, ...pureItem } = value;
            this.item.familyRelationships[row].familyRelationship = pureItem;
        } else {
            this.item.familyRelationships[row].familyRelationship = value;
        }
    }

    handleFormSubmit = async (values) => {
        try {
            if (this.itemId) {
                await editStaff(values)
            }
            else {
                await createStaff(values)
            }
            this.handleCloseDialog();
            this.updatePageData();
        } catch (error) {
            console.error("error saving staff", error);
        }
    }


}