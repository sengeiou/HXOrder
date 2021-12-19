// pages/collection/collection.js
const app = getApp();

Page({
    showCurrentIndex: 0,
    type: 0,
    /**
     * 页面的初始数据
     */
    data: {
        show: false,
        //待确认删除的索引
        willDelete: null,
        actions: [{
                name: '确定删除',
                subname: "删除后不可恢复，您确定吗？",
                color: '#ee0a24'
            },
            {name: '取消'}
        ],

        dishes: [{
            'dishImg': 'https://happysnaker-1306579962.cos.ap-nanjing.myqcloud.com/img/typora/image-20210912210836008.png',
            'dishName': '黄豆炖猪蹄黄黄豆炖猪蹄黄黄豆炖猪蹄黄黄豆炖猪蹄黄黄豆炖猪蹄黄黄豆炖猪蹄黄黄豆炖猪蹄黄黄豆炖猪蹄黄黄豆炖猪蹄黄黄豆炖猪蹄黄黄豆炖猪蹄黄黄豆炖猪蹄黄',
            'sellNum': 99,
            'price': 23,
            'desc': '很好吃的e12e2e一个菜',
            'dishTags': ['下酒菜', '一家人吃', '开胃菜', '长个子', '猪猪', '变高'],
            'praiseNum': 23,
            'likeNum': 99,
            'praise': false,
            'like': false,
            'collection': true,
            'stock': 3,
            'id': 'w1',

        }, {
            'dishImg': 'https://happysnaker-1306579962.cos.ap-nanjing.myqcloud.com/img/typora/image-20210912210836008.png',
            'dishName': '黄豆炖猪蹄黄黄豆炖猪蹄黄黄豆炖猪蹄黄黄豆炖猪蹄黄黄豆炖猪蹄黄黄豆炖猪蹄黄黄豆炖猪蹄黄黄豆炖猪蹄黄黄豆炖猪蹄黄黄豆炖猪蹄黄黄豆炖猪蹄黄黄豆炖猪蹄黄',
            'sellNum': 999,
            'price': 23,
            'desc': '很好吃的e12e2e一个菜',
            'dishTags': ['下酒菜', '一家人吃', '开胃菜', '长个子', '猪猪', '变高'],
            'praiseNum': 23,
            'likeNum': 99,
            'praise': false,
            'like': false,
            'collection': true,
            'stock': 3,
            'id': 'w1',
        }],
    },
    onClose() {
        this.setData({ show: false });
      },

    onSelect(event) {
        console.log(event.detail);
        let item = event.detail;
        if (item.name == "取消") {
            this.setData({show: false});
        } else {
            let index = this.data.willDelete;
            let dishes = Object.assign([], this.data.dishes);
            app.addOrRemoveUserCollectedDish(dishes[index].id, 0, (res)=> {
                dishes.splice(index, 1);
                this.setData({
                    show: false,
                    dishes: dishes
                })
            })
        }
    },

    delete(e) {
        let i = e.currentTarget.dataset.id;
        console.log("wwww", i, e, this.data);
        this.setData({
            willDelete: i,
            show: true,
        });

    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        wx.showLoading({
            title: "正在加载中",
            mask: true,
        });
          
        app.getUserCollectedDishes((res)=> {
            console.log(res.data.arrays);
            this.setData({dishes: res.data.arrays});
            wx.hideLoading();
        }, (code)=> {
            wx.hideLoading();
            app.dialog(this, "服务错误", "获取信息失败，请检查您的网络");
        })
    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady: function () {

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