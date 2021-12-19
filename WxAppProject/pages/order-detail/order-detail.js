// pages/order-detail/order-detail.js
const app = getApp();
Page({

    /**
     * 页面的初始数据
     */
    data: {
        leftHeight: 50,
        order: {
            type: 3,
            nowStatus: 0, // 现阶段处于哪个流程

                img: '../../images/PUS.png',
                storeName: '南昌大学店',
            dishOrders: [{
                    name: '黄瓜',
                    num: 1,
                    price: 65,
                }, {
                    name: '黄瓜',
                    num: 12,
                    price: 65,
                },

            ],
            time: '21:213',
            num: 0,
            totPrice: 22, //总价
            discount1: 0, // 店铺优惠
            discount2: 2, //使用券
            act_price: 20, //实际价格
            userInfo: {
                userName: '猪猪',
                userTel: '10086',
                userAddr: '南昌市南昌大学软件学院'
            },
            table: -1, //如果是到店消费，table表示桌号
            formType: 3,
            id: "0IOHDK",
            firstTime: '2021-12-23', //下单时间
            payTime: '2021-213-3', //付款时间
            finalTime: '21-123-2' //完成时间
        },
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        console.log(options);
        let order = JSON.parse(options.order);
        console.log("order=", order)
        order.actuallyPrice = order.originalPrice - order.shopDiscount 
        - order.couponDiscount + order.otherFee - order.margin;
        this.setData({
            order: order
        })
        
        console.log(order);
    },
    onSubmit() {
        let pay = this.data.order.orderType == 0 ? this.data.order.actuallyPrice * 0.2 : this.data.order.actuallyPrice;
        app.dialogWithAction(this, "确定吗", "确定支付" + pay + "元？",
        (action)=> new Promise((resolve)=> {
            if (action=== 'confirm') {
                app.pay(this.data.order.payId, (res)=> {
                    app.dialog(this, "操作成功", "您已成功支付");
                    wx.navigateBack({
                        delta: 1
                    });
                    resolve(true);
                }, (err)=> {
                    app.dialog(this, "操作失败", "更新订单状态失败，请及时与管理员联系");
                    resolve(true);
                })
            } else {
                resolve(true);
            }
        }))
    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady: function () {

        setTimeout(() => {
            let query = wx.createSelectorQuery();
            query.select('.right').boundingClientRect(rect => {
                if (rect) {
                    let height = rect.height;
                    console.log(height + "rpx");
                    this.setData({
                        leftHeight: height
                    })
                }
            }).exec();

        }, 300)

    },
    copyText: function (e) {
        console.log(e)
        wx.setClipboardData({
            data: e.currentTarget.dataset.text,
            success: function (res) {
                wx.getClipboardData({
                    success: function (res) {
                        wx.showToast({
                            title: '复制成功'
                        })
                    },
                    fail: function () {
                        title: '复制失败'
                    }
                })
            },

        })
    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {

    },

    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide: function () {

    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload: function () {

    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh: function () {
        console.log("刷新！");
        app.dialog(this, "亲爱的使用者", "由于开发人员的傻逼，请你返回订单页面刷新，不要再这刷新")
    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom: function () {

    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function () {

    }
})