import Vue from 'vue'   //引入Vue
import Router from 'vue-router'  //引入vue-router
import login from '../components/login'
import page from '../components/page'
import homepage from '../components/homepage'

Vue.use(Router)  //Vue全局使用Router

const router = new Router({
  routes: [              //配置路由，这里是个数组
    {                    //每一个链接都是一个对象
      path: '/login',         //链接路径
      name: 'login',     //路由名称，
      component: login   //对应的组件模板
    }, {
      path:'/page',
        component: page,
        children:[        //子路由,嵌套路由 （此处偷个懒，免得单独再列一点）
          {
            path: '/', component: homepage
          }
      ]
    }, {
    path: '*',
      redirect: '/login'
    }
]
})
// 5.导出路由
export default router
