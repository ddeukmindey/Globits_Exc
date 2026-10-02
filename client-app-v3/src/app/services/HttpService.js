import axios from "axios";
import UserService from "./UserService";

const HttpMethods = {
  GET: 'GET',
  POST: 'POST',
  DELETE: 'DELETE',
};

const _axios = axios.create();

const configure = () => {
  _axios.interceptors.request.use((config) => {
    if (window.Configs && window.Configs.AUTH_MODE === "Keycloak") {
      const cb = () => {
        config.headers.Authorization = `Bearer ${UserService.getToken()}`;
        return Promise.resolve(config);
      };
      return UserService.updateToken(cb);
    } else {
      const token = window.localStorage.getItem("jwt_token");
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
      return Promise.resolve(config);
    }
  });
};

const getAxiosClient = () => _axios;

export default {
  HttpMethods,
  configure,
  getAxiosClient,
}
