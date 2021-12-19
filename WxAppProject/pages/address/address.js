// const WXAPI = require('apifm-wxapi')
// const AUTH = require('../../utils/auth')
const app = getApp()
// APP.configLoadOK = () => {
  import Notify from '../../dist/notify/notify';

// }
Page({
  data: {
    // 是否是从点餐页面传递过来的
    isOrder: false,
    addressList: [{name: "Bob", phone: "10080", address: "ww", id: 1}],
    addressEdit: false,
    update: false,
    addressData: {}
  },
  // 添加地址
  addAddress: function () {
    this.setData({
      addressEdit: true,
      cancelBtn: true,
      id: null,
      addressData: {}
    })
  },
  // 取消编辑
  editCancel: function(){
    this.setData({
      addressEdit: false,         
    })
  },
  // 编辑地址
  editAddress(e) {
    var id = e.currentTarget.dataset.id   
    console.log(this.data.addressList[id]); 
    this.setData({ 
      addressData: this.data.addressList[id],
      addressEdit: true,
      update: true,
      id:id,
    })
    this.data.addressList[id]
  }, 

  // // 删除地址按钮
  deleteAddress: function (e) {
    console.log(e);
    let index = e.currentTarget.dataset.id;
    let addressList = this.data.addressList;
    console.log(index);
    let addrId = addressList[index].id;
    app.dialogWithAction(this, "确定删除？", "删除后不可恢复哦", 
      (action) => new Promise((resolve) => {
        console.log("wwwww", addrId);
          if (action === 'confirm') {
            app.removeUserAddr(addrId, (res)=> {
              addressList.splice(index, 1);
              resolve(true);
              this.setData({addressList: addressList})
            }, (code)=> {
              resolve(true);
              app.dialog(this, "服务错误", "发生了意料之外的错误");
            })
            
          } else {
            console.log("wwwwwwwwwws");
            // 拦截取消操作
            resolve(true);
          }
      }) 
    )
  },
  
  nameChange(e) {
    const addressData = this.data.addressData
    addressData.name = e.detail
    this.setData({
      addressData
    })
  },
  phoneChange(e) {
    const addressData = this.data.addressData
    addressData.phone = e.detail
    this.setData({
      addressData
    })
  },
  addressChange(e) {
    const addressData = this.data.addressData
    addressData.address = e.detail
    this.setData({
      addressData
    })
  },
  // // 保存按钮
  bindSave() {    
    let userId = wx.getStorageSync('openid');
    let addr = this.data.addressData;
    if (addr.name == null || addr.address == null || ((String)(addr.phone)).length != 11) {
      app.dialog(this, "错误", "请正确填写您的信息");
      return;
    }
    addr.userId = userId;
    let data = JSON.stringify(addr);
    let update = this.data.update;
    app.dialogWithAction(this, "确定添加？", update ? "您正在执行更新操作" : "您正在执行添加操作", 
      (action) => new Promise((resolve) => {
        console.log("wwwww");
          if (action === 'confirm') {
            if (update) {
              app.updateUserAddr(data, (res)=> {
                let addrList = this.data.addressList;
                // addrList.push(addr);
                this.setData({
                  addressList: addrList,
                  addressData: {},
                  addressEdit: false,
                  update: false
                })
                resolve(true);
              }, (code)=> {
                resolve(true);
                app.dialog(this, "服务错误", "发生了意料之外的错误");
              })
            } else {
              app.addUserAddr(data, (res)=> {
                let addrList = this.data.addressList;
                addrList.push(addr);
                this.setData({
                  addressList: addrList,
                  addressData: {},
                  addressEdit: false,
                  update: false
                })
                resolve(true);
              }, (code)=> {
                resolve(true);
                app.dialog(this, "服务错误", "发生了意料之外的错误");
              })
            }
            
          } else {
            console.log("wwwwwwwwwws");
            // 拦截取消操作
            resolve(true);
          }
      }) 
    )

  },
  clickAddress(e) {
    let index = e.currentTarget.dataset.set;
    console.log("clickAddress", e, index, this.data.addressList[index]);
    if (this.data.isOrder) {
      let pages = getCurrentPages();
          let prevPage = pages[pages.length - 2];
          prevPage.setData({
               address: this.data.addressList[index]
          })
          wx.navigateBack({
            delta: 1,
       })
    }
    
  },
  onLoad: function (e) {    
    wx.showLoading({
      title: "正在加载中",
      mask: true,
    });
    console.log("eeeeeeee" , e);
    if (e.tt) {
      this.setData({isOrder: true})
    }
      
    app.getUserAddr((res)=> {
      console.log("res" == res);
      this.setData({addressList: res.data.arrays});
      wx.hideLoading();
    }, (code)=> {
      wx.hideLoading();
      app.dialog(this, "服务错误", "获取信息失败，请检查您的网络")
    })
  },

  onShow: function() {
  },  


  chooseLocation() {
    wx.chooseLocation({
      success: (res) => {
        const addressData = this.data.addressData
        addressData.address = res.address + res.name
        addressData.latitude = res.latitude
        addressData.longitude = res.longitude
        console.log(res);
        this.setData({
          addressData
        })
      },
      fail: (e) => {
        console.error(e)
      },
    })
  }
})