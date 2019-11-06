// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import Router from 'vue-router'  //引入vue-router
import Axios from 'axios';  //引入Axios
// import api from "./api/api.js"; //引入Api.js
// import Materialize from 'materialize-css'
// import 'materialize-css/dist/css/materialize.min.css'
import ElementUI from 'element-ui';
// import 'element-ui/lib/theme-chalk/index.css';
import '../theme/index.css'
import '../src/assets/them.scss'

import router from './router'

Vue.use(Router);
Vue.use(ElementUI);
// Vue.prototype.api = api;
// 配置主题 -- 配置默认主题

/** ****************************************************************************************************************************************************************************************/
/**
 * 配置 Axios
 * @type {AxiosStatic}
 */
Vue.prototype.$axios=Axios;
// Axios.defaults.baseURL = 'http://localhost:10010/v1';
// 设置AJAX超时时间
Axios.defaults.timeout = 3000;
// 设置提交数据时的格式
Axios.defaults.headers['Content-Type'] = 'application/json';
// 设置前置拦截器->以后所有的AJAX都会自动添加上 Authorization:token 的协议头
Axios.interceptors.request.use(function (config) {
  // 判断如果用户登录了就把 token 配置上 axios 的协议头中
  let token = localStorage.getItem('token');
  if (token) {
    config.headers['Authorization'] = token
  }
  // 处理请求前代码
  return config;
}, function (error) {
  // Do something with request error
  return Promise.reject(error);
});


const pending = {};
const CancelToken = Axios.CancelToken;
const removePending = (key, isRequest = false) => {
  if (pending[key] && isRequest) {
    pending[key]('取消重复请求')
  }
  delete pending[key]
};
const getRequestIdentify = (config, isReuest = false) => {
  // console.log( config.url );
  let url = config.url;
  if (isReuest) {
    url = config.baseURL + config.url.substring(1, config.url.length)
  }
  return config.method === 'get' ? encodeURIComponent(url + JSON.stringify(config.params)) : encodeURIComponent(config.url + JSON.stringify(config.data))
};

// 请求拦截器
Axios.interceptors.request.use(config => {
  // 拦截重复请求(即当前正在进行的相同请求)
  let requestData = getRequestIdentify(config, true);
  removePending(requestData, true);
  config.cancelToken = new CancelToken((c) => {
    pending[requestData] = c
  });
  return config
}, error => {
  return Promise.reject(error);
});

/** ****************************************************************************************************************************************************************************************/

Vue.config.productionTip = false;

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
});
