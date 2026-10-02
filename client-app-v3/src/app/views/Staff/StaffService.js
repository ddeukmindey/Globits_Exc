import axios from "axios";
import ConstantList from "../../appConfig";

const API_PATH = ConstantList.API_ENPOINT + "/api/staff";

export const pagingStaff = (searchObject) => {
    var url = API_PATH + "/searchByPage";
    return axios.post(url, searchObject);
};

export const getStaff = (id) => {
    let url = API_PATH + "/" + id;
    return axios.get(url);
};

export const createStaff = (obj) => {
    let url = API_PATH;
    return axios.post(url, obj);
};

export const editStaff = (obj) => {
    let url = API_PATH + "/" + obj.id;
    return axios.put(url, obj);
};

export const deleteStaff = (id) => {
    let url = API_PATH + "/" + id;
    return axios.delete(url);
};

export const getAllStaffs = () => {
    var url = API_PATH + "/all";
    return axios.get(url);
};

export const checkIdNumber = (obj) => {
    var url = API_PATH + "/checkIdNumber";
    return axios.post(url, obj);
};