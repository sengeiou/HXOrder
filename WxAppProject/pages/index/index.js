// index.js
// 获取应用实例
// import Dialog from '../../dist/dialog/dialog';
const app = getApp()

Page({
  data: {
    // 窗口宽度
    windowWidth: wx.getSystemInfoSync().windowWidth,

    //顶部轮播图
    carousel: {
      'https://activity.vtuzx.com/doc/vtuUI/weapp/swiper/1.png': '',
      'https://activity.vtuzx.com/doc/vtuUI/weapp/swiper/2.png': '',
      'https://activity.vtuzx.com/doc/vtuUI/weapp/swiper/4.png': '',
      'https://activity.vtuzx.com/doc/vtuUI/weapp/swiper/3.png': ''
    },
    //通知
    notify: '本店自2021年起推动线上活动！！',
    //热销榜单链表
    hotDishList: [{
      'name': '黄豆炖猪蹄豆炖猪',
      'id': 0,
      'dishImg': 'https://tse1-mm.cn.bing.net/th/id/R-C.b0089c802efa0179ae6f1a365473e2ed?rik=lfm%2bJ%2fspw5LX0w&riu=http%3a%2f%2fseopic.699pic.com%2fphoto%2f50079%2f2036.jpg_wh1200.jpg&ehk=qNQXICUl52BVrI2OOpTnCrI4wsTOPGByIeXp5MWc8nw%3d&risl=&pid=ImgRaw&r=0',
      'name': '黄豆炖猪蹄豆炖猪',
      'sale': 999,
      'price': 23,
      'desc': '很好吃的e12e2e一个菜',
      'tags': ['下酒菜', '一家人吃', '开胃菜', '长个子', '猪猪', '变高'],
      'likeNum': 99,
      //用户是否喜欢
      'like': false,
      //用户是否收藏
      'collection': true,
    }, {
      'name': '黄豆炖猪蹄豆炖猪',
      'id': 0,
      'dishImg': 'https://tse1-mm.cn.bing.net/th/id/R-C.b0089c802efa0179ae6f1a365473e2ed?rik=lfm%2bJ%2fspw5LX0w&riu=http%3a%2f%2fseopic.699pic.com%2fphoto%2f50079%2f2036.jpg_wh1200.jpg&ehk=qNQXICUl52BVrI2OOpTnCrI4wsTOPGByIeXp5MWc8nw%3d&risl=&pid=ImgRaw&r=0',
      'name': '黄豆炖猪蹄豆炖猪',
      'sale': 999,
      'price': 23,
      'desc': '很好吃的e12e2e一个菜',
      'tags': ['下酒菜', '一家人吃', '开胃菜', '长个子', '猪猪', '变高'],
      'likeNum': 99,
      //用户是否喜欢
      'like': false,
      //用户是否收藏
      'collection': true,
    }],


    newDishList: [],

    //套餐
    combos: [{
      'imgs': ['https://images.unsplash.com/photo-1551334787-21e6bd3ab135?w=640',
        'http://pic.5tu.cn/uploads/allimg/2108/pic_5tu_big_6311780_c3306d1b7b94bc4d3e04cc3171ebb4c9.jpg',
        'http://pic.5tu.cn/uploads/allimg/2106/pic_5tu_big_202105201436114884.jpg',
        'https://tse1-mm.cn.bing.net/th/id/R-C.b0089c802efa0179ae6f1a365473e2ed?rik=lfm%2bJ%2fspw5LX0w&riu=http%3a%2f%2fseopic.699pic.com%2fphoto%2f50079%2f2036.jpg_wh1200.jpg&ehk=qNQXICUl52BVrI2OOpTnCrI4wsTOPGByIeXp5MWc8nw%3d&risl=&pid=ImgRaw&r=0',
      ],
      //套餐具体配置
      'comboDish': [{
        'name': '黄瓜',
        'num': 1,
        'price': 65,
      }, {
        'name': '黄瓜',
        'num': 1,
        'price': 65,
      }, ],
      'sale': 123,
      'name': '黄豆炖猪蹄黄豆炖猪蹄黄豆炖猪蹄黄豆炖猪黄豆炖猪蹄豆炖猪蹄豆炖猪蹄豆炖猪蹄豆炖猪蹄豆炖猪蹄豆炖猪蹄豆炖猪蹄豆炖猪蹄蹄黄豆炖猪蹄黄豆炖猪蹄黄豆炖猪蹄黄豆炖猪蹄黄豆炖猪蹄',
      'tags': ['下酒菜', '一家人吃', '开胃菜', '长个子', '猪猪', '变高'],
      'likeNum': 99,
      'like': false,
      'collection': true,
      'discount1': 8
    }],

    recommendDishList: [{
      'id': '0',
      'dishImg': 'https://tse1-mm.cn.bing.net/th/id/R-C.62651b2704fe30b1be0529442b022312?rik=bZyUQoaO%2b2ZgpQ&riu=http%3a%2f%2fpic28.nipic.com%2f20130408%2f5225985_211813455124_2.jpg&ehk=YrOEWZaxBTM6W3ofM0kSckWiOAOBcM2e1bN218hB%2bXY%3d&risl=&pid=ImgRaw&r=0',
      'name': '爆炒肉丝炒肉丝炒肉丝炒肉丝炒肉丝炒肉丝炒肉丝',
      'sale': 9999897,
      'price': 35,
      'reason': '这是一个很好吃的菜，\n我们通常凉拌!\n你说呢？这是一个很好吃的菜，\n我们通常凉拌!\n你说呢？这是一个很好吃的菜，\n我们通常凉拌!\n你说呢？',
      'dishTags': ['好吃', '下凡'],
      'likeNum': 299,
      'like': false,
      'collection': true,
      'stars': 4.8
    }, ]
  },

  checkId(arrays, id) {
    for (let i in arrays) {
      if (arrays[i].id == id)
        return i;
    }
    return false;
  },

  //加入我的待选
  add: function (obj) {
    let index = obj.detail.index,
      row = obj.detail.row;
    console.log(index, row);

    let hotDishList = this.data.hotDishList;
    let recommendDishList = this.data.recommendDishList;
    let newDishList = this.data.newDishList;
    let combos = this.data.combos;

    /**因为更改一个列表的菜品可能会影响其他列表，即一个菜品会存在于多个列表，因此必须整体查询
     * 一个优化是更改整体架构，使得多个菜品属于同一个对象，这样更改一个对象即可
     * 如果列表没有更改，小程序是不会重新渲染的，因此不必过于担心性能问题
     */
    app.addOrRemoveUserWillBuyDish(row.id, row.willBuy ? 0 : 1, (res) => {
      let i1 = this.checkId(hotDishList, row.id),
        i2 = this.checkId(recommendDishList, row.id),
        i3 = this.checkId(newDishList, row.id),
        i4 = this.checkId(combos, row.id);
      if (i1) hotDishList[i1].willBuy = !hotDishList[i1].willBuy;
      if (i2) recommendDishList[i2].willBuy = !recommendDishList[i2].willBuy;
      if (i3) newDishList[i3].willBuy = !newDishList[i3].willBuy;
      if (i4) combos[i4].willBuy = !combos[i4].willBuy;
      this.setData({
        hotDishList: hotDishList,
        recommendDishList: recommendDishList,
        newDishList: newDishList,
        combos: combos
      });
    })
  },
  //喜欢
  clickLike: function (obj) {
    let index = obj.detail.index,
      row = obj.detail.row;
    console.log(obj);

    let hotDishList = this.data.hotDishList;
    let recommendDishList = this.data.recommendDishList;
    let newDishList = this.data.newDishList;
    let combos = this.data.combos;

    app.addOrRemoveUserLikeDish(row.id, row.like ? 0 : 1, (res) => {
      let i1 = this.checkId(hotDishList, row.id),
        i2 = this.checkId(recommendDishList, row.id),
        i3 = this.checkId(newDishList, row.id),
        i4 = this.checkId(combos, row.id);
        console.log(i1, i2,i3,i4);
      if (i1) {
        hotDishList[i1].like = !hotDishList[i1].like;
        hotDishList[i1].likeNum = hotDishList[i1].like ? hotDishList[i1].likeNum + 1 : hotDishList[i1].likeNum - 1;
      }
      if (i2) {
        recommendDishList[i2].like = !recommendDishList[i2].like;
        recommendDishList[i2].likeNum = recommendDishList[i2].like ? recommendDishList[i2].likeNum + 1 : recommendDishList[i2].likeNum - 1;
      }
      if (i3) {
        newDishList[i3].like = !newDishList[i3].like;
        newDishList[i3].likeNum = newDishList[i3].like ? newDishList[i3].likeNum + 1 : newDishList[i3].likeNum - 1;
      }
      if (i4) {
        combos[i4].like = !combos[i4].like;
        combos[i4].likeNum = combos[i4].like ? combos[i4].likeNum + 1 : combos[i4].likeNum - 1;
      }
      this.setData({
        hotDishList: hotDishList,
        recommendDishList: recommendDishList,
        newDishList: newDishList,
        combos: combos
      });
    })

  },
  //收藏
  clickCollection: function (obj) {
    let index = obj.detail.index,
      row = obj.detail.row;
    console.log(obj);

    let hotDishList = this.data.hotDishList;
    let recommendDishList = this.data.recommendDishList;
    let newDishList = this.data.newDishList;
    let combos = this.data.combos;

    app.addOrRemoveUserCollectedDish(row.id, row.collection ? 0 : 1, (res) => {
      let i1 = this.checkId(hotDishList, row.id),
        i2 = this.checkId(recommendDishList, row.id),
        i3 = this.checkId(newDishList, row.id),
        i4 = this.checkId(combos, row.id);
      if (i1) hotDishList[i1].collection = !hotDishList[i1].collection;
      if (i2) recommendDishList[i2].collection = !recommendDishList[i2].collection;
      if (i3) newDishList[i3].collection = !newDishList[i3].collection;
      if (i4) combos[i4].collection = !combos[i4].collection;
      this.setData({
        hotDishList: hotDishList,
        recommendDishList: recommendDishList,
        newDishList: newDishList,
        combos: combos
      });
    })
  },

  onLoad: function (options) {
    wx.showLoading({
      title: "正在加载中",
      mask: true,
    });

    app.getIndexDishInfo(false, (res) => {
      console.log(res);
      this.setData({
        hotDishList: res.data.hotDishList,
        recommendDishList: res.data.recommendDishList,
        newDishList: res.data.newDishList,
        combos: res.data.combos
      });
      console.log("data", this.data.hotDishList);
      this.getUserLikeAndCollectionInfo();
    }, (err) => {
      app.dialog(this, "获取信息出错", "请检查您的网络是否正确连接");
      wx.hideLoading();
    })
  },

  /**获取用户对于该菜品的喜欢，收藏和待选状态，将菜品数据与用户数据分离，菜品数据可以利用缓存直接返回 */
  getUserLikeAndCollectionInfo() {
    app.getUserLikeAndCollectedDish((res) => {
      let hotDishList = this.data.hotDishList;
      let recommendDishList = this.data.recommendDishList;
      let newDishList = this.data.newDishList;
      let combos = this.data.combos;
      this.addLikeAndCollection(hotDishList, res.data.like, res.data.collection, res.data.willBuy);
      this.addLikeAndCollection(recommendDishList, res.data.like, res.data.collection, res.data.willBuy);
      this.addLikeAndCollection(newDishList, res.data.like, res.data.collection, res.data.willBuy);
      this.addLikeAndCollection(combos, res.data.like, res.data.collection, res.data.willBuy);
      console.log("hotDishList", hotDishList);
      this.setData({
        hotDishList: hotDishList,
        recommendDishList: recommendDishList,
        newDishList: newDishList,
        combos: combos
      });
      wx.hideLoading();
    }, (err) => {
      app.dialog(this, "获取信息出错", "请检查您的网络是否正确连接");
      wx.hideLoading();
    });
  },

  addLikeAndCollection: function (dishes, likeDishes, CollectedDishes, willBuyDishes) {
    console.log(willBuyDishes, "will");
    for (let key in dishes) {
      dishes[key].like = false;
      dishes[key].collection = false;
      dishes[key].willBuy = false;
      let id = dishes[key].id;
      if (likeDishes.indexOf(id) != -1) {
        dishes[key].like = true;
      }

      if (CollectedDishes.indexOf(id) != -1) {
        dishes[key].collection = true;
      }

      if (willBuyDishes.indexOf(id) != -1) {
        dishes[key].willBuy = true;
      }
    }
    return dishes;
  },

  // 扫码点餐
  scan() {
    wx.scanCode({
      onlyFromCamera: false,
      scanType: ['qrCode', 'barCode', 'datamatrix', 'pdf417'],
      success: (res) => {
        console.log("扫码的结果是:", res, res.result);
        let strs = res.result.split("&");
        console.log("str == " , JSON.stringify(strs));
        
        let storeId = strs[0].split("=")[1];
        let table = strs[1].split("=")[1];
        console.log("storeId === ", storeId);
        if (table && storeId) {
          app.getStoreById(storeId, (res)=> {
            wx.navigateTo({
              url: '../dish-order/dish-order?type=0' + '&store=' + JSON.stringify(res.data) + '&table=' + table 
            });
          })
          
        }
      },
      fail: () => {},
      complete: () => {}
    });

  },

  onShow: function () {
    // Do something when page show.
  },
  onReady: function () {
    // Do something when page ready.
  },
  onHide: function () {
    // Do something when page hide.
  },
  onUnload: function () {
    // Do something when page close.
  },
  onPullDownRefresh: function () {
    // Do something when pull down.
    //刷新页面，重新获取信息，并且不使用缓存
    app.getIndexDishInfo(false, (res) => {
      this.setData({
        hotDishList: res.data.hotDishList,
        recommendDishList: res.data.recommendDishList,
        newDishList: res.data.newDishList,
        combos: res.data.combos
      });
      this.getUserLikeAndCollectionInfo();
      wx.stopPullDownRefresh();
    }, (err) => {
      app.dialog(this, "获取信息出错", "请检查您的网络是否正确连接");
      wx.hideLoading();
      wx.stopPullDownRefresh();
    })

  },
  onReachBottom: function () {
    // Do something when page reach bottom.
  },
  onShareAppMessage: function () {
    // return custom share data when user share.
  },
  onPageScroll: function () {
    // Do something when page scroll
  },
  onResize: function () {
    // Do something when page resize
  },
  // Event handler.
  viewTap: function () {
    this.setData({
      text: 'Set some data for updating view.'
    }, function () {
      // this is setData callback
    })
  }
})