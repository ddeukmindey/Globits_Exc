import axios from "axios";
import ConstantList from "../../appConfig";

const API_PATH = ConstantList.API_ENPOINT + "/api/task";

export const pagingTasks = (searchObject) => {
  var url = API_PATH + "/searchByPage";
  return axios.post(url, searchObject);
};

export const getTask = (id) => {
  let url = API_PATH + "/" + id;
  return axios.get(url);
};

export const createTask = (obj) => {
  let url = API_PATH;
  return axios.post(url, obj);
};

export const editTask = (obj) => {
  let url = API_PATH + "/" + obj.id;
  return axios.put(url, obj);
};

export const deleteTask = (id) => {
  let url = API_PATH + "/" + id;
  return axios.delete(url);
};

export const exportExcel = (searchObject) => {
  let url = API_PATH + "/exportExcel";
  return axios.post(url, searchObject, { responseType: "blob" });
};
