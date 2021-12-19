Component({
  data: {
    html: '',
  },
  properties: {
    otherFee: {
      type: Number,
      value: 0,
    },
    totalPrice: {
      type: Number,
      value: 0,

    },
    //优惠
    shopDiscount: {
      type: Number,
      value: 0,
    },
    //券
    couponDiscount: {
      type: Number,
      value: 0
    },
    //其他费用
    otherFee: {
      type: Number,
      value: 0
    },
    dishOrders: {
      type: Object,
      value: [{
        'dishName': '黄瓜',
        'dishNum': 1,
        'dishPrice': 65,
      }, {
        'dishName': '黄瓜',
        'dishNum': 1,
        'dishPrice': 65,
      }],
    }
  },

  methods: {}
})