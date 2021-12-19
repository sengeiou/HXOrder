Component({
  data: {
    
    color: ['red', 'blue', 'purple', 'orange', 'green']
  },
  properties: {
    cart: {
      type: Object,
      value: {},
      observer: function (newVal, oldVal, change) {
        console.log("观察者 cart！！", change);
      }
    },

    dishes: {
      type: Array,
      value:[

      ],
      observer: function (newVal, oldVal, change) {
        console.log("观察者！！", change);
      }
    },
    dishShowType: {
      type: String,
      value: 'none'
    },

    countMap: {
      type: Object,
      value: {}
    },

  },


  methods: {
    readMore: function(e) {
      let dish = this.data.dishes[e.currentTarget.dataset.id];
      let json =JSON.stringify(dish);
      let d = JSON.parse(json);
      console.log("传递", dish, json, d)
      wx.navigateTo({
        url: '../dish-detail/dish-detail?dish='+JSON.stringify(this.data.dishes[e.currentTarget.dataset.id])
      })
    },
    add: function(e) {
      let id = e.currentTarget.dataset.id;
      let dish = this.data.dishes[id];
      this.triggerEvent('add', {index: id, row: dish});
    },
    clickLike: function(e) {
      console.log("comn",e);
      let id = e.currentTarget.dataset.id;
      let dish = this.data.dishes[id];
      console.log("com", id, dish);
      this.triggerEvent('clickLike', {index: id, row: dish});
    },
    clickCollection: function(e) {
      let id = e.currentTarget.dataset.id;
      let dish = this.data.dishes[id];
      console.log("com", id, dish);
      this.triggerEvent('clickCollection', {index: id, row: dish});
    },
    countChange: function(e) {
      let id = e.currentTarget.dataset.id;
      let cnt = e.detail;
      let dish = this.data.dishes[id];
      console.log(cnt, dish);
      this.triggerEvent('countChange', [dish.id, cnt]);
    },    
  },

  options: {
    multipleSlots: true
  }
})