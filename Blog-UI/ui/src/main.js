// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import Router from 'vue-router'  //引入vue-router
// import Materialize from 'materialize-css'
// import 'materialize-css/dist/css/materialize.min.css'
import ElementUI from 'element-ui';
// import 'element-ui/lib/theme-chalk/index.css';
import '../theme/index.css'
import '../src/assets/them.scss'

import router from './router'

Vue.use(Router);
Vue.use(ElementUI);

// 配置主题 -- 配置默认主题



Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
