import axios from "axios";
import type { AxiosResponse } from "axios";
import { ElMessage } from "element-plus";

// 运行时配置与环境变量优先级：window.__APP_CONFIG__ > import.meta.env > 默认"/api"
const runtimeConfig: any = (window as any).__APP_CONFIG__ || {};
const apiBase = runtimeConfig.API_BASE_URL || (import.meta as any).env?.VITE_API_BASE_URL || "/api";

// 创建 axios 实例
const api = axios.create({
  baseURL: apiBase,
  timeout: 10000,
  headers: {
    "Content-Type": "application/json"
  }
});

// 请求拦截器
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem("token");
    if (token) {
      (config.headers as any).Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

// 响应拦截器
api.interceptors.response.use(
  response => {
    const res = response.data;
    if (res.code === 'SUCCESS') {
      return res.data;
    }
    // 处理错误
    ElMessage.error(res.message || '请求失败');
    return Promise.reject(new Error(res.message || '请求失败'));
  },
  error => {
    // 处理HTTP错误
    const res = error.response?.data;
    ElMessage.error(res?.message || '网络错误');
    return Promise.reject(error);
  }
);

export default api;
