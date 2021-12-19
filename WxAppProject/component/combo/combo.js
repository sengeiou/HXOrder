Component({
  data: {
    //套餐总价，需要计算
    totalPrice: [],
    //
    //set 用以动态调整图片展示
    set: [{
      previousMargin: '0rpx', //前边距，可用于露出前一项的一小部分，接受 px 和 rpx 值
      nextMargin: '300rpx',
      imgNum: 0,
      swaperHeight: 450,
    }],
    color: ['red', 'blue', 'purple', 'orange', 'green'],
  },
  properties: {
    index: 0,
    dishShowType: {
      type: String,
      value: ''
    },
    combos: {
      type: Array,
      value: [],
      observer: function (newVal, oldVal, change) {
        let set = new Array(newVal.length);
        let totalPrice = new Array(newVal.length);
        for (let i = 0; i < newVal.length; i++) {
          let sum = 0;
          
          for (let j in newVal[i].comboDish) {
            let val = newVal[i].comboDish[j];
            sum += (val.dishNum * val.dishPrice);
          }
          totalPrice[i] = sum;
          let n = newVal[i].imgs.length;
          let prev, next, h;
          if (n == 1) {
            h = 300;
            prev = '0rpx';
            next = '0rpx';
          } else if (n == 2) {
            h = 225;
            prev = '0rpx';
            next = '300rpx';
          } else {
            h = 225;
            prev = '0rpx';
            next = '350rpx';
          }
          let temp = {
            imgNum: n,
            previousMargin: prev,
            nextMargin: next,
            swaperHeight: h
          }
          set[i] = temp;
        }
        console.log("total", totalPrice);
        this.setData({
          set: set,
          totalPrice: totalPrice
        })
      }
    },
    countMap: {
      type: Object,
      value: {}
    },
  },

  lifetimes: {
    detached: function () {
      // 在组件实例被从页面节点树移除时执行
    },
  },
  methods: {
    add: function(e) {
      let id = e.currentTarget.dataset.id;
      let dish = this.data.combos[id];
      this.triggerEvent('add', {index: id, row: dish});
    },
    clickLike: function(e) {
      let id = e.currentTarget.dataset.id;
      let dish = this.data.combos[id];
      this.triggerEvent('clickLike', {index: id, row: dish});
    },
    clickCollection: function(e) {
      let id = e.currentTarget.dataset.id;
      let dish = this.data.combos[id];
      this.triggerEvent('clickCollection', {index: id, row: dish});
    },
    countChange: function (e) {
      console.log(e);
      let id = e.currentTarget.dataset.id;
      this.triggerEvent('countChange', [this.data.combos[id].id, e.detail]);
    },

    // 调整图片显示
    swiperBindchange(e) {
      let id = e.currentTarget.dataset.index;
      let n = this.data.set[id].imgNum;
      let i = e.detail.current;
      let set = this.data.set;
      let prev = set[id].previousMargin,
        next = set[id].nextMargin;

      // console.log(n);
      if (n == 2) {
        if (i == 0) {
          prev = '0rpx';
          next = '300rpx';

        } else {
          prev = '300rpx';
          next = '0rpx';
        }
      } else if (n > 2) {
        if (i == 0) {
          prev = '0rpx';
          next = '350rpx';
        } else if (i == n - 1) {
          prev = '350rpx';
          next = '0rpx';
        }
      }

      set[id].previousMargin = prev;
      set[id].nextMargin = next;
      this.setData({
        set: set
      })
    },
  }
})