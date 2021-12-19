// pages/message/message.js
const app = getApp();
Page({

    /**
     * 页面的初始数据
     */
    data: {
        messages: [
            // {title: "标题1", concent: "已退款，请检查", id: 1, type: 1, storeName: "中国店"},
            // {title: "标题2", concent: "已退款，请检查", id: 1, type: 1, storeName: "中国店"},
            // {title: "标3", concent: "已退款，请检查", id: 1, type: 1, storeName: "中国店"},
            // {title: "标题4", concent: "已退款，请检查", id: 1, type: 1, storeName: "中国店"},
            // {title: "标题5", concent: "已退款，请检查", id: 1, type: 0, storeName: "中国店"}
         ],
        activeNames: ['1'],
    },

    onChange(event) {
        this.setData({
          activeNames: event.detail,
        });
      },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
      app.getUserMessage((res)=> {
        this.setData({messages: res.data.messages});
      }, (err) => {
        console.log(err);
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
      this.onLoad();
      console.log("yyds");
      wx.stopPullDownRefresh();
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