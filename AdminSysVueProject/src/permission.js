import router from './router'
import store from './store'
import NProgress from 'nprogress' // Progress 进度条
import 'nprogress/nprogress.css' // Progress 进度条样式
import {resetRouter} from '@/router'
import {
  asyncRouterMap,
  constantRouterMap
} from '@/router/index'
import {
  Message
} from 'element-ui'
import {
  getToken
} from '@/utils/auth' // 验权

const whiteList = ['/login'] // 不重定向白名单


router.beforeEach((to, from, next) => {
  console.log(getToken());
  NProgress.start()
  //如果存在 token，表示已经登录
  if (getToken()) {
  if (false) {
    //因为已经登录了，所以直接跳转 /home
    // if (to.path === '/login') {
      next({
        path: '/'
      })
      NProgress.done() // if current page is dashboard will not trigger	afterEach hook, so manually handle it
    } else {
      // next();
      //如果不存在用户信息
      if (store.getters.roles.length === 0) {
      // if (true) {
        console.log("123456");
        store.dispatch('GetInfo').then(res => { // 拉取用户信息
          console.log("成功获取用户信息 ==> " + res);
          let menus = res.data.menus; //菜单
          let username = res.data.username; //
          //获取用户信息成功后，调用 GenerateRoutes 方法
          console.log("获取用户信息");
          store.dispatch('GenerateRoutes', {
            menus,
            username
          }).then(() => { // 生成可访问的路由表
            // resetRouter()
            console.log("成功");
            console.log(asyncRouterMap, store.getters.addRouters);
            router.addRoutes(asyncRouterMap); // 动态添加可访问路由表
            next({
              ...to,
              replace: true
            })
          })
        }).catch((err) => {
          console.log("获取用户信息错误");
          store.dispatch('FedLogOut').then(() => {
            Message.error(err || 'Verification failed, please login again')
            next({
              path: '/'
            })
          })
        })
      } else {
        next()
      }
    }
  } else { //如果没登录，则跳转登录
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      next('/login')
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done() // 结束Progress
})
