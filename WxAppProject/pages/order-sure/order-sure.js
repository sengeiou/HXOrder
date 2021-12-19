// index.js
// 获取应用实例
const app = getApp()

//Page Object
Page({
    data: {
        remark: null,
        waitTime: 0,
        actions5: [{
                name: '取消'
            },
            {
                name: '继续',
                color: '#ed3f14',
                loading: false
            }
        ],
        explanation: ["你好二万块钱评价去哦卫计委去哦海外华侨科维奇尔和趣味就回去卡卡和期望和巨额钱款尽快考核空气好" +
            "请问请问"
        ],
        couponDiscount: 0,
        map: ["扫码点餐", "到店消费", "到店自取", "外卖配送"],
        time: '',
        order: {},
        radioCurrent: '1',
        address: null,

    },

    // 下单
    buy(e) {
        let order = this.data.order;
        let type = order.consumeType;
        order.couponDiscount = this.data.couponDiscount;
        order.remark = this.data.remark;
        order.taste = parseInt(this.data.radioCurrent);
        if (order.consumeType == 3) {
            if (this.data.address == null) {
                app.dialog(this, "您暂未选择收货地址", "请选择收获地址");
                return;
            }
            order.addressId = this.data.address.id;
        }

        let date = new Date();
        let time = date.getFullYear().toString() + "-" + date.getMonth().toString() + "-" + date.getDate().toString() + " " + this.data.time;
        console.log(time);
        let eTime = new Date(time).getTime();
        console.log("time == ", time, eTime);
        order.expectedTime = eTime;
        let pay = (order.originalPrice - order.shopDiscount - order.couponDiscount);
        let content = "";
        if (type == 0) {
            content = "您当前在店内用餐，考虑到用餐途中您可能继续加餐或减餐，故请您用餐结束后到前台等待管理员确认金额后前往订单页面点击立即支付结账，或您也可以与管理员商议选择其他支付方式";
        } else if (type == 1) {
            content = "您当前选择到店消费，考虑到用餐途中您可能继续加餐或减餐，故请您用餐结束后到前台等待管理员确认金额后前往订单页面点击立即支付结账，或您也可以与管理员商议选择其他支付方式" +
                "同时为了保证店家权益，您需要提前支付一定金额，占比百分之20，金额为 " + pay * 0.2 + "元";
            pay *= 0.2;
        } else {
            content = "确定吗，您需要支付 " + pay + "元";
        }
        console.log("orderJson = " + JSON.stringify(order), "pay = " + pay);
        let orderJson = JSON.stringify(order);
        app.dialogWithAction(this, "确定下单吗？", content,
            (action) => new Promise((resolve) => {
                if (action === 'confirm') {
                    console.log("gg");
                    //如果不是扫码点餐，则需要支付
                    if (type != 0) {
                        app.addOrder(orderJson, (res) => {
                            console.log("订单完成", res);
                            order.payId = res.data.payId;
                            order.id = res.data.orderId;

                            resolve(true);
                            // 发起支付
                            this.payOrder(order, pay);
                        }, (err) => {
                            resolve(true);
                            if (err == 409) {
                                app.dialog(this, "服务错误", "添加订单失败，" +
                                    "在您下单途中，您购物车中的菜品已经被别人抢走了，下次手快点吧")
                            } else {
                                app.errDialog(this, "服务错误")
                            }

                        })

                    } else {
                        // 不需要支付，直接添加订单，最后到前台支付
                        app.addOrder(orderJson, (res) => {
                            console.log("订单完成", res);
                            app.dialog(this, "下单成功", "您已成功下单", () => {
                                resolve(true);
                                wx.reLaunch({
                                    url: '../order-ok/order-ok?order=' + JSON.stringify(order),
                                    success: (result) => {
                                        console.log(result);
                                    },
                                });
                            });

                        }, (err) => {
                            resolve(true);
                            if (err == 409) {
                                app.dialog(this, "服务错误", "添加订单失败，" +
                                    "在您下单途中，您购物车中的菜品已经被别人抢走了，下次手快点吧")
                            } else {
                                app.errDialog(this)
                            }
                        })
                    }
                } else {
                    console.log("取消下单");
                    resolve(true);
                }
            })
        )
    },

    // 支付订单
    payOrder(order, pay) {
        app.dialogWithAction(this, "订单已生成", "您需要支付 " + pay + "元", (action)=> {
            new Promise(resolve=> {
                // 点击支付
                if (action == 'confirm') {
                    app.pay(order.payId, (res) => {
                        app.dialog(this, "下单成功", "您已成功支付" + pay + "元", () => {
                            resolve(true);
                            wx.reLaunch({
                                url: '../order-ok/order-ok?order=' + JSON.stringify(order),
                                success: (result) => {
                                    console.log(result);
                                },
                            });
                        });
                    }, (err) => {
                        app.dialog(this, "服务错误", "支付失败，请您重试");
                    })
                } else {
                    app.cancelPay(order.id, (res=> {
                        wx.switchTab({
                            url: '../order/order',
                            success: (result) => {
                             console.log(result);   
                            }
                        });
                    }))
                }
            })
        })
        
    },
    onLoad: function (options) {
        let d = new Date();
        let t = d.getHours().toString() + ":" + d.getMinutes().toString();
        let order = JSON.parse(options.order);
        console.log(order.store);
        app.getWatingTime(order.store.id, (res) => {
            this.setData({
                order: order,
                time: t,
                waitTime: res.data.time
            })
        }, (cod) => {
            app.dialog(this, "服务错误", "请检查您的网络是否连接")
        })

    },
    onRemarkChange(e) {
        this.setData({
            remark: e.detail
        })
    },

    bindTimeChange: function (e) {
        console.log("changeTime", e);
        this.setData({
            time: e.detail.value
        })
    },
    radioChange(e) {
        console.log("radioChange", e);
        this.setData({
            radioCurrent: e.detail
        })
    },

    onReady: function () {

    },
    onShow: function () {
        console.log("address ==== ", this.data.address);
    },
    onHide: function () {

    },
    onUnload: function () {

    },
    onPullDownRefresh: function () {

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