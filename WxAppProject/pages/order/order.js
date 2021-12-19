// index.js
// 获取应用实例
const app = getApp()

//Page Object

/**
 * 流程
 * consumeType 0: 待点餐 - 确认中 - 待支付 - 已完成   对应扫码点餐
 * consumeType 1: 待支付 - 确认中 - 备餐中 - 待用餐 - 已完成  对应到店消费
 * consumeType 2: 待支付 - 确认中 - 备餐中 - 待取餐 - 已完成  对应到店自取
 * consumeType 3: 待支付 - 确认中 - 备餐中 - 配送中 - 已完成  对应外卖
 * 
 * orderType: 0待确认, 1待支付, 2待取餐，3已完成  -- 已弃用
 * 0待点餐, 1待支付, 2确认中，3备餐中，4待用餐，5待取餐，6配送中，7已完成
 */
Page({
  data: {
    bug: true,
    value2: 'name',
    status: ["全部订单", "待支付保证金", "待支付", "确认中", "备餐中", "待用餐", "待取餐", "配送中", "已完成", "取消确认中", "已取消"],

    orders: [{
      enablePay: false,
      consumeType: 1,
      nowStatus: 4, // 现阶段处于哪个流程
      storeImg: '../../images/PUS.png',
      store: {
      },
      storeName: '南昌大学店',
      storeId: 1,
      dishOrders: [{
          id: '3',
          dishName: '黄瓜',
          dishNum: 1,
          dishPrice: 65,
        },
      ],
      num: 0,
      originalPrice: 22, //总价
      shopDiscount: 2, // 店铺优惠
      couponDiscount: 2, //使用券
      actuallyPrice: 20, //实际价格
      userInfo: {
        userName: '猪猪',
        userTel: '10086',
        userAddr: '南昌市南昌大学软件学院'
      },
      table: -1, //如果是到店消费，table表示桌号
      orderType: 1,
      id: "0IOHDKqwewqeewqeowihqiohi11",
      firstTime: '2021-12-23', //下单时间
      payTime: '2021-213-3', //付款时间
      finalTime: '21-123-2' //完成时间
    }, ],
  },

  //订单空，前往点餐
  order() {
    wx.switchTab({
      url: '../order-meal/order-meal',
    });
      
  },

  change(e) {
    this.setData({
      value2: e.detail.name
    })
  },

  cancelOrder(e) {
    let i = e.currentTarget.dataset.id;
    let order = this.data.orders[i];
    console.log("取消订单", i, this.data.orders);
    // 此时用户未支付，直接删除即可
    if (order.orderType == 0 || (order.orderType == 1 && order.consumeType >= 2)) {
      console.log("就是我！");
      app.dialogWithAction(this, "您确定吗，删除后不可恢复", "确定后，订单将永久删除", 
      (action) => new Promise((resolve) => {
        if (action === 'confirm') {
            app.deleteOrder(this.data.orders[i].id, (res)=> {
              this.onLoad();
              app.dialog(this, "成功", "删除成功")
            })
            resolve(true);
        } else {
            console.log("取消下单");
            resolve(true);
        }
    })
      )
      return;
    }

    //订单未完成，此时为用户取消订单
    if (this.data.orders[i].orderType != 7 && this.data.orders[i].orderType != 9) {
      wx.navigateTo({
        url: '../cancel-reason/cancel-reason?id=' + this.data.orders[i].id + "&orderType="+ this.data.orders[i].orderType,
        success: (result) => {
          
        },
        fail: () => {},
        complete: () => {}
      });
    } else {
      //删除订单
      app.dialogWithAction(this, "您确定吗，删除后不可恢复", "确定后，订单将永久删除", 
      (action) => new Promise((resolve) => {
        if (action === 'confirm') {
            app.deleteOrder(this.data.orders[i].id, (res)=> {
              this.onLoad();
              app.dialog(this, "成功", "删除成功")
            })
            resolve(true);
        } else {
            console.log("取消下单");
            resolve(true);
        }
    })
      )
    }
  },

  //继续点餐，将订单传递给点餐页面
  continue (e) {
    let id = e.currentTarget.dataset.id;
    let order = this.data.orders[id];
    let store = {
      name: order.storeName,
      img: order.storeImg,
      id: order.storeId
    }
    order.store = store;
    wx.redirectTo({
      url: '../dish-order/dish-order?old=' + JSON.stringify(order),
      fail: function (err) {
        console.log(err);
      }
    });
  },

  //查看详情
  showDetail(e) {
    let i = e.currentTarget.dataset.id;
    let order = this.data.orders[i];
    let json = JSON.stringify(order);
    console.log("json=", json);
    wx.navigateTo({
      url: '../order-detail/order-detail?order=' + json,
      fail: function (err) {
        console.log(err);
      }
    });
    console.log(e, i);
  },

  onLoad: function (options) {
    wx.showLoading({
      title: "正在加载中",
      mask: true,
    });
      
    app.getUserOrders((res)=> {
      console.log(res);
      let orders = res.data.orders;
      let statusMap =  [1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
      orders.forEach((item)=> {
        console.log(item.orderType + 1);
        statusMap[item.orderType + 1] = 1;
      })
      let status = ["全部订单", "待支付保证金", "待支付", "确认中", "备餐中", "待用餐", "待取餐", "配送中", "已完成", "取消确认中", "已取消"];
      for (let i in statusMap) {
        if (statusMap[i] == 0) {
          status[i] = "";
        } 
      }
      console.log("status == ", status);
      this.setData({
        status: status,
        orders: res.data.orders
      })
      wx.hideLoading();
    }, (err)=> {
      wx.hideLoading();
    })
    wx.stopPullDownRefresh();
  },
  onReady: function () {

  },
  onShow: function () {
    this.onLoad();
  },
  onHide: function () {

  },
  onUnload: function () {

  },
  onPullDownRefresh: function () {
    this.onLoad();
  },
  onReachBottom: function () {

  },
  onShareAppMessage: function () {

  },
  onPageScroll: function () {

  },
  //item(index,pagePath,text)
  onTabItemTap: function (item) {

  }
});