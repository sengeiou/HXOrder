//Page Object
var app = getApp();

Page({
    data: {
        phone: null,
        reason: null,
        orderId: null,
        oldOrderType: null,
        userId: null,
    },
    //options(Object)

    cancelOps() {
        wx.navigateBack({
            delta: 1
        });
    },

    onSubmit() {
        let data = this.data;
        console.log(data);
        if (!data.reason || ((String)(data.phone)).length != 11) {
            app.dialog(this, "表单错误", "请正确填写您的信息")
            return;
        }
        this.data.userId = wx.getStorageSync('openid');
        app.dialogWithAction(this, "您确定吗", "您正在提交一条取消订单申请",
            (action) => new Promise((resolve) => {
                if (action === 'confirm') {
                    app.cancelOrder(JSON.stringify(this.data), (res)=> {
                        resolve(true);
                        app.dialog(this, "已提交", "您已提交申请，请等待管理员确认，若管理员长时间未确认，20分钟后将驳回您的申请", 
                        (res)=>{
                            wx.navigateBack({
                                delta: 1
                            });
                        });
                    }, (code)=> {
                        resolve(true);
                        app.dialog(this, "服务错误", "发生了意料之外的错误");
                    })  
                } else {
                    resolve(true);
                }
            })
        )
    },

    onLoad: function (options) {
        console.log("ooooo", options);
        this.setData({
            orderId: options.id,
            oldOrderType: options.orderType
        })
    },

    onChangePhone(e) {
        console.log(e);
        this.setData({
            phone: e.detail
        })
    },

    onChangeReason(e) {
        console.log(e);
        this.setData({
            reason: e.detail
        })
    },
    onReady: function () {

    },
    onShow: function () {

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