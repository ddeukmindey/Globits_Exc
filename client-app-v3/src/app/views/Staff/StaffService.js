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

export const uploadStaffAvatar = (file, staffId) => {
    let url = ConstantList.API_ENPOINT + "/api/person/upload-avatar";
    let formData = new FormData();
    formData.append("file", file);
    if (staffId) {
        formData.append("personId", staffId);
    }
    return axios.post(url, formData, {
        headers: {
            "Content-Type": "multipart/form-data"
        }
    });
};

export const getAvatarUrl = (avatar) => {
    if (!avatar) return "";
    if (avatar.startsWith("http://") || avatar.startsWith("https://") || avatar.startsWith("data:")) {
        return avatar;
    }
    return ConstantList.API_ENPOINT + "/api/person/avatar/" + avatar;
};