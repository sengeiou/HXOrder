// index.js
// 获取应用实例
const app = getApp()

//Page Object
Page({
    data: {
        msg: 0,
        grids: [
            {
                icon: 'coupon',
                text: '优惠券',
                url: ''
            },
            {
                icon: 'star',
                text: '我的收藏',
                url: '../collection/collection'
            },
            {
                icon: 'map-marked',
                text: '收货地址',
                url: '../address/address'
            },
            {
                icon: 'bell',
                text: '消息',
                url: '../message/message'
            },
            {
                icon: 'manager',
                text: '客服'
            },
            {
                icon: 'scan',
                text: '兑换码'
            },
            {
                icon: 'warning',
                text: '平台须知'
            },
            {
                icon: 'ellipsis',
                text: '尽请期待'
            },

        ],
        hasUserInfo: false,
        user: {
            id: null,
            rank: 3,
            integral: 10,
            wallet: 20
        },
        userInfo: {
            avatarUrl: 'https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132',
            nickName: '点击我授权'
        },
    },

    click(e) {
        console.log(e);
        let id = e.currentTarget.dataset.id;
        wx.navigateTo({
            url: this.data.grids[id].url
        })
    },
    //options(Object)
    getUserProfile(e) {
        // 推荐使用wx.getUserProfile获取用户信息，开发者每次通过该接口获取用户个人信息均需用户确认
        // 开发者妥善保管用户快速填写的头像昵称，避免重复弹窗
        wx.getUserProfile({
            desc: '用于小程序接口展示', // 声明获取用户个人信息后的用途，后续会展示在弹窗中，请谨慎填写
            success: (res) => {
                console.log(res);
                this.setData({
                    userInfo: res.userInfo,
                    hasUserInfo: true
                })
                wx.setStorageSync("userInfo",JSON.stringify(res.userInfo))
                console.log(wx.getStorageSync("userInfo"));
            },
            fail: function () {
                wx.showToast({
                    title: '获取信息失败',
                    duration: 1000
                })
            }
        })
    },
    onLoad: function () {
        if (wx.getStorageSync("userInfo")) {
            let userInfo = JSON.parse(wx.getStorageSync("userInfo"));
            this.setData({
                userInfo: userInfo,
                hasUserInfo: true
            })
            console.log("this == ", this);
        }
        app.getUserInfo((res)=>{
            console.log("user" ,res);
            this.setData({user: res.data.user})
        }, (code)=> {
            app.dialog(this, "服务错误", "请检查您的网络后重试");
        })

        app.getUserMessageCount((res)=> {
            this.setData({msg: res.data.msgCount});   
        }, (code)=> {
            app.dialog(this, "服务错误", "请检查您的网络后重试");
        })
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
        wx.stopPullDownRefresh();
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