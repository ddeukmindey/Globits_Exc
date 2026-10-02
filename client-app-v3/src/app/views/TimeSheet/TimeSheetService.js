import axios from "axios";
import ConstantList from "../../appConfig";

const API_PATH = ConstantList.API_ENPOINT + "/api/timesheet";

export const pagingTimeSheets = (searchObject) => {
    var url = API_PATH + "/search-by-page";
    return axios.post(url, searchObject);
};

export const getTimeSheet = (id) => {
    let url = API_PATH + "/" + id;
    return axios.get(url);
};

export const createTimeSheet = (obj) => {
    let url = API_PATH;
    return axios.post(url, obj);
};

export const editTimeSheet = (obj) => {
    let url = API_PATH + "/" + obj.id;
    return axios.put(url, obj);
};

export const deleteTimeSheet = (id) => {
    let url = API_PATH + "/" + id;
    return axios.delete(url);
};
