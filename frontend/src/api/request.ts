import axios from "axios";
import {useUserStore} from "@/store/user";
import { ElMessage } from "element-plus";
import router from "@/router";

// 运行时配置与环境变量优先级：window.__APP_CONFIG__ > import.meta.env > 默认"/api"
const runtimeConfig: any = (window as any).__APP_CONFIG__ || {};
const apiBase = runtimeConfig.API_BASE_URL  || "/api";


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
    const token = useUserStore().token;
    if (token) {
      (config.headers as any).Authorization = `Bearer ${token}`;
    }
      config.timeout = 30000;
      if ('get' === config.method) {
          config.params = config.params || {};
          config.params._t = new Date().getTime(); //添加时间戳
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
    if(response.status == 200 && res == ""){
        return response;
    }
    // 处理错误
    ElMessage.error(res.message || '请求失败');
    return Promise.reject(new Error(res.message || '请求失败'));
  },
  err => {
    // 处理HTTP错误
      if (err && err.response) {
          switch (err.response.status) {
              case 400:
              // err.message = "错误请求";
              // break;
              case 401:
                  err.message = "未授权，请重新登录";
                  break;
              case 403:
                  err.message = "拒绝访问";
              // break;
              case 500:
                  // err.message = "服务器端出错";
                  // break;
                  err.message = err.response.data.msg;
                  break;
              case 404:
                  err.message = "请求错误,未找到该资源";
                  break;
              case 405:
                  err.message = "请求方法未允许";
                  break;
              case 408:
                  err.message = "请求超时";
                  break;
              case 501:
                  err.message = "网络未实现";
                  break;
              case 502:
                  err.message = "网络错误";
                  break;
              case 503:
                  err.message = "服务不可用";
                  break;
              case 504:
                  err.message = "网络超时";
                  break;
              case 505:
                  err.message = "http版本不支持该请求";
                  break;
              default:
                  err.message = `连接错误${err.response.status}`;
          }
      } else {
          err.message = "连接到服务器失败";
      }
      if (err?.response?.data?.code === 401 || err.response.status === 403) {
          router.push("/login");
      }
      return Promise.reject(err)
  }
);

export default api;
