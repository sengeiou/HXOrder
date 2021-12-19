import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '../views/layout/Layout'

/**
 * hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
 *                                if not set alwaysShow, only more than one route under the children
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noredirect           if `redirect:noredirect` will no redirct in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
  }
 **/
export const constantRouterMap = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },
  {
    path: '',
    component: Layout,
    redirect: '/home',
    children: [{
      path: 'home',
      name: 'home',
      component: () => import('@/views/home/index'),
      meta: {
        title: '首页',
        icon: 'home'
      }
    }]
  },
]

export const asyncRouterMap = [
  
  {
    path: '/dish-ms',
    component: Layout,
    redirect: '/dish-ms/dish',
    name: 'dish-ms',
    meta: {
      title: '菜品',
      icon: 'product'
    },
    children: [
      {
        path: 'dish',
        name: 'dish',
        component: () => import('@/views/dish-ms/dish/index'),
        meta: {
          title: '菜品列表',
          icon: 'product-list'
        }
      },
      {
        path: 'addDish',
        name: 'addDish',
        component: () => import('@/views/dish-ms/dish/add'),
        meta: {
          title: '添加菜品',
          icon: 'product-add'
        }
      },
      {
        path: 'updateDish',
        name: 'updateDish',
        component: () => import('@/views/dish-ms/dish/update'),
        meta: {
          title: '修改菜品',
          icon: 'product-add'
        },
        hidden: true
      },
      {
        path: 'combo',
        name: 'combo',
        component: () => import('@/views/dish-ms/combo/index'),
        meta: {
          title: '套餐列表',
          icon: 'product-list'
        }
      },
      {
        path: 'addCombo',
        name: 'addCombo',
        component: () => import('@/views/dish-ms/combo/add'),
        meta: {
          title: '添加套餐',
          icon: 'product-add'
        }
      },
      {
        path: 'updateCombo',
        name: 'updateCombo',
        component: () => import('@/views/dish-ms/combo/update'),
        meta: {
          title: '修改套餐',
          icon: 'product-add'
        },
        hidden: true
      },
      // {
      //   path: 'dishCate',
      //   name: 'dishCate',
      //   component: () => import('@/views/dish-ms/dishCate/index'),
      //   meta: {
      //     title: '菜品分类',
      //     icon: 'product-cate'
      //   }
      // },
      // {
      //   path: 'addDishCate',
      //   name: 'addDishCate',
      //   component: () => import('@/views/dish-ms/dishCate/add'),
      //   meta: {
      //     title: '添加菜品分类'
      //   },
      //   hidden: true
      // },
      // {
      //   path: 'updateDishCate',
      //   name: 'updateDishCate',
      //   component: () => import('@/views/dish-ms/dishCate/update'),
      //   meta: {
      //     title: '修改菜品分类'
      //   },
      //   hidden: true
      // },
      // {
      //   path: 'dishAttr',
      //   name: 'dishAttr',
      //   component: () => import('@/views/dish-ms/dishAttr/index'),
      //   meta: {
      //     title: '菜品类型',
      //     icon: 'product-attr'
      //   }
      // },
      // {
      //   path: 'productAttrList',
      //   name: 'productAttrList',
      //   component: () => import('@/views/dish-ms/dishAttr/productAttrList'),
      //   meta: {
      //     title: '菜品属性列表'
      //   },
      //   hidden: true
      // },
      // {
      //   path: 'addDishAttr',
      //   name: 'addDishAttr',
      //   component: () => import('@/views/dish-ms/dishAttr/addProductAttr'),
      //   meta: {
      //     title: '添加菜品属性'
      //   },
      //   hidden: true
      // },
      // {
      //   path: 'updateDishAttr',
      //   name: 'updateDishAttr',
      //   component: () => import('@/views/dish-ms/dishAttr/updateProductAttr'),
      //   meta: {
      //     title: '修改菜品属性'
      //   },
      //   hidden: true
      // },
    ]
  },
  {
    path: '/order-ms',
    component: Layout,
    redirect: '/order-ms/order',
    name: 'order-ms',
    meta: {
      title: '订单',
      icon: 'order'
    },
    children: [
      {
        path: 'order',
        name: 'order',
        component: () => import('@/views/order-ms/order/index'),
        meta: {
          title: '订单列表',
          icon: 'product-list'
        }
      },
      {
        path: 'toBePaidOrderList',
        name: 'toBePaidOrderList',
        component: () => import('@/views/order-ms/order/index'),
        meta: {
          title: '待支付订单',
          icon: 'product-list'
        },
      },      
      {
        path: 'toBeConfirmedOrderList',
        name: 'toBeConfirmedOrderList',
        component: () => import('@/views/order-ms/order/index'),
        meta: {
          title: '待确认订单',
          icon: 'product-list'
        },
      },     
      {
        path: 'ealPreparationOrderList',
        name: 'ealPreparationOrderList',
        component: () => import('@/views/order-ms/order/index'),
        meta: {
          title: '备餐中订单',
          icon: 'product-list'
        },
        hidden: false
      }, 
      {
        path: 'inDistributionOrderList',
        name: 'inDistributionOrderList',
        component: () => import('@/views/order-ms/order/index'),
        meta: {
          title: '配送中订单',
          icon: 'product-list'
        },
        hidden: false
      }, 
      {
        path: 'mealWaitingOrderList',
        name: 'mealWaitingOrderList',
        component: () => import('@/views/order-ms/order/index'),
        meta: {
          title: '待取餐订单',
          icon: 'product-list'
        },
        hidden: false
      }, 
      {
        path: 'toBeDinedOrderList',
        name: 'toBeDinedOrderList',
        component: () => import('@/views/order-ms/order/index'),
        meta: {
          title: '待用餐订单',
          icon: 'product-list'
        },
        hidden: false
      }, 
      {
        path: 'cancelOrderList',
        name: 'cancelOrderList',
        component: () => import('@/views/order-ms/order/index'),
        meta: {
          title: '取消中订单',
          icon: 'order-return'
        }
      },
      {
        path: 'orderDetail',
        name: 'orderDetail',
        component: () => import('@/views/order-ms/order/orderDetail'),
        meta: {
          title: '订单详情'
        },
        hidden: true
      },
      {
        path: 'handleToBePaidOrder',
        name: 'handleToBePaidOrder',
        component: () => import('@/views/order-ms/order/handleToBePaidOrder'),
        meta: {
          title: '处理支付订单'
        },
        hidden: true
      },
      {
        path: 'handleCancelOrder',
        name: 'handleCancelOrder',
        component: () => import('@/views/order-ms/order/handleCancelOrder'),
        meta: {
          title: '处理取消订单'
        },
        hidden: true
      },
      
    ]
  },
  {
    path: '/store-ms',
    component: Layout,
    redirect: '/store-ms/dish',
    name: 'store-ms',
    meta: {
      title: '店铺',
      icon: 'product'
    },
    children: [
      {
        path: 'store',
        name: 'store',
        component: () => import('@/views/store-ms/store/index'),
        meta: {
          title: '店铺列表',
          icon: 'product-list'
        }
      },
      {
        path: 'addStore',
        name: 'addStore',
        component: () => import('@/views/store-ms/store/add'),
        meta: {
          title: '添加店铺',
          icon: 'product-add'
        }
      },
      {
        path: 'updateStore',
        name: 'updateStore',
        component: () => import('@/views/store-ms/store/update'),
        meta: {
          title: '修改店铺',
          icon: 'product-add'
        },
        hidden: true
      },
    ]
  },
  {
    path: '/marketing',
    component: Layout,
    redirect: '/marketing-ms/coupon',
    name: 'marketing-ms',
    meta: {
      title: '营销',
      icon: 'sms'
    },
    children: [
        {
          path: 'coupon',
          name: 'coupon',
          component: () => import('@/views/marketing-ms/coupon/index'),
          meta: {title: '优惠券列表', icon: 'sms-coupon'},
        },
        // {
        //   path: 'addCoupon',
        //   name: 'addCoupon',
        //   component: () => import('@/views/marketing-ms/coupon/add'),
        //   meta: {title: '添加优惠券'},
        //   hidden:true
        // },
        // {
        //   path: 'updateCoupon',
        //   name: 'updateCoupon',
        //   component: () => import('@/views/marketing-ms/coupon/update'),
        //   meta: {title: '修改优惠券'},
        //   hidden:true
        // },
        // {
        //   path: 'couponHistory',
        //   name: 'couponHistory',
        //   component: () => import('@/views/marketing-ms/coupon/history'),
        //   meta: {title: '优惠券领取详情'},
        //   hidden:true
        // },
        {
          path: 'discont',
          name: 'dishDiscont',
          component: () => import('@/views/marketing-ms/discount/index'),
          meta: {title: '菜品折扣', icon: 'sms-subject'}
        },
        {
          path: 'recommend',
          name: 'dishRecommend',
          component: () => import('@/views/marketing-ms/recommend/index'),
          meta: {title: '菜品推荐', icon: 'sms-hot'}
        },
        {
          path: 'new',
          name: 'dishNew',
          component: () => import('@/views/marketing-ms/new/index'),
          meta: {title: '新品推荐', icon: 'sms-new'}
        },
    ]
  },
  {
    path: '/authority-ms',
    component: Layout,
    redirect: '/authority-ms/admin',
    name: 'ums',
    meta: {
      title: '权限',
      icon: 'ums'
    },
    children: [{
        path: 'admin',
        name: 'admin',
        component: () => import('@/views/authority-ms/admin/index'),
        meta: {
          title: '用户列表',
          icon: 'ums-admin'
        }
      },
      {
        path: 'role',
        name: 'role',
        component: () => import('@/views/authority-ms/role/index'),
        meta: {
          title: '角色列表',
          icon: 'ums-role'
        }
      },
      {
        path: 'allocMenu',
        name: 'allocMenu',
        component: () => import('@/views/authority-ms/role/allocMenu'),
        meta: {
          title: '分配菜单'
        },
        hidden: true
      },
      // {
      //   path: 'allocResource',
      //   name: 'allocResource',
      //   component: () => import('@/views/authority-ms/role/allocResource'),
      //   meta: {
      //     title: '分配资源'
      //   },
      //   hidden: true
      // },
      // {
      //   path: 'menu',
      //   name: 'menu',
      //   component: () => import('@/views/authority-ms/menu/index'),
      //   meta: {
      //     title: '菜单列表',
      //     icon: 'ums-menu'
      //   }
      // },
      // {
      //   path: 'addMenu',
      //   name: 'addMenu',
      //   component: () => import('@/views/authority-ms/menu/add'),
      //   meta: {
      //     title: '添加菜单'
      //   },
      //   hidden: true
      // },
      // {
      //   path: 'updateMenu',
      //   name: 'updateMenu',
      //   component: () => import('@/views/authority-ms/menu/update'),
      //   meta: {
      //     title: '修改菜单'
      //   },
      //   hidden: true
      // },
      // {
      //   path: 'resource',
      //   name: 'resource',
      //   component: () => import('@/views/authority-ms/resource/index'),
      //   meta: {
      //     title: '资源列表',
      //     icon: 'ums-resource'
      //   }
      // },
      // {
      //   path: 'resourceCategory',
      //   name: 'resourceCategory',
      //   component: () => import('@/views/authority-ms/resource/categoryList'),
      //   meta: {
      //     title: '资源分类'
      //   },
      //   hidden: true
      // }
    ]
  },
  {
    path: '*',
    redirect: '/404',
    hidden: true
  }
]

export function resetRouter() {
  const newRouter = createRouter();
  router.matcher = newRouter.matcher;
}

const createRouter = () => new Router({
  // mode: "history",
  scrollBehavior: () => ({
        y: 0
      }),
  routes: constantRouterMap
});
const router = createRouter();

export default router;
// export default new Router({
//   // mode: 'history', //后端支持可开
//   scrollBehavior: () => ({
//     y: 0
//   }),
//   routes: constantRouterMap
//   // routes: asyncRouterMap
// })

