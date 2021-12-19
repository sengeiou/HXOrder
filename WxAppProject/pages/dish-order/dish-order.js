// pages/dish-order/dist-order.js
const app = getApp()
Page({
	/*支持discount活动
	 * type: 0表示无活动; 1表示折扣；2表示立减；
	 * 主要通过key，val表示，例如val=8.5表示8.5折扣，立减8.5元;例如key=2,val=1表示满2元减1元 
	 */
	data: {
		active: 0,
		showCart: false,
		consumeTypeMap: ["扫码点餐", "到店消费", "到店自取", "外卖配送"],
		//dishId : num 用以记录哪个菜选择了几个，传入dish_show同步计数器
		// countMap: {},
		//购物车，里面存放菜品 id 和点餐数量 和 菜品已使用的折扣数量
		cart: {
			// id: {
			//num
			//discountUsedCount
			//}
		},
		id: "", //订单编号，表示这是否是一个已存在的订单加餐
		oldDishOrders: null, //如果这是一个已存在的加餐，该数据表示旧订单的已点菜列表
		comboType: 0,
		price: 0, // 总价
		num: 0, //购物车显示的数目
		discount: 0, //优惠价格
		page: '单点', //当前是点菜还是套餐
		show: true, //>????
		scrollTop: 0, //滑动部分距离顶部的距离，=》 相对距离
		//目前左侧导航激活的索引
		currentNav: 0,
		//点击左侧导航第几个位置
		tapNav: 0,
		/* 0:扫码点餐 1:到店消费 2:到店自取 3:外码配送 */
		consumeType: -1, // 订单类型
		table: -1, //桌号
		height: 100, // scrow-view 部分设置的高度
		store: {}, // 店铺
		//这个是菜品信息，单独分开来是因为可以通过 id 直接读取菜品信息
		//这一坨屎山已经不想改了
		dishInfo: {
			
		},
		//单点菜品，包括不同的展示分类
		singalOrder: [],

		combos: [{
				nav: "我的收藏",
				comboList: [{
					'id': '2',
					'imgs': ['https://images.unsplash.com/photo-1551334787-21e6bd3ab135?w=640',
						'http://pic.5tu.cn/uploads/allimg/2108/pic_5tu_big_6311780_c3306d1b7b94bc4d3e04cc3171ebb4c9.jpg',
						'http://pic.5tu.cn/uploads/allimg/2106/pic_5tu_big_202105201436114884.jpg',
						'https://tse1-mm.cn.bing.net/th/id/R-C.b0089c802efa0179ae6f1a365473e2ed?rik=lfm%2bJ%2fspw5LX0w&riu=http%3a%2f%2fseopic.699pic.com%2fphoto%2f50079%2f2036.jpg_wh1200.jpg&ehk=qNQXICUl52BVrI2OOpTnCrI4wsTOPGByIeXp5MWc8nw%3d&risl=&pid=ImgRaw&r=0',
					],
					'comboDish': [{
							'dishName': '黄瓜',
							'dishNum': 1,
							'dishPrice': 65,
						},

					],
					'stock': 3,
					'sale': 123,
					'name': '黄豆炖猪蹄o',
					'tags': ['下酒菜', '一家人吃', '开胃菜', '长个子', '猪猪', '变高'],
					'praiseNum': 23,
					'likeNum': 99,
					'praise': false,
					'like': false,
					'collection': true,
				}],
			},
			{
				nav: "我的待选",
				comboList: []
			}
		],
	},
	clickCart() {
		this.setData({
			showCart: true
		})
	},

	hideCartPop() {
		this.setData({
			showCart: false
		})
	},
	cartStepChange(e) {
		console.log(e);
		let id = (Number)(e.currentTarget.dataset.id);
		let cnt = e.detail;
		console.log([id, cnt]);
		if (id < 100000) {
			this.dishCountChange({
				detail: [id, cnt]
			});
		} else {
			this.comboCountChange({
				detail: [id, cnt]
			});
		}
	},
	clearCart() {
		this.hideCartPop();
		app.dialogWithAction(this, "确定清空购物车?", "确认后购物车将会被清空", () => {
			console.log("点击确定");
			this.setData({
				price: 0,
				discount: 0,
				num: 0,
				cart: {}
			})
		}, () => {})
	},
	//结算
	order(e) {
		if (this.data.num == 0) {
			app.dialog(this, "您还没有点菜", "点一些爱吃的菜再下单吧！")
			return;
		}
		console.log("点击结算");
		let dishOrders = [];
		let b = this.data.cart;
		let d = this.data.dishInfo;
		let oldDishOrders = this.data.oldDishOrders;
		//表示购物车
		for (let id in b) {
			console.log(b, id);
			let obj = {};
			obj.dishId = parseInt(id);
			obj.dishName = d[id].name;
			obj.dishPrice = d[id].price;
			obj.dishNum = b[id].num;
			obj.usedCount = b[id].discountUsedCount;

			// 如果是一个旧订单，我们必须将新加的菜和之前的菜分开，以便管理员确认
			if (oldDishOrders != null) {
				console.log("旧订单！！！！！！！！");
				let b = false;
				oldDishOrders.forEach(item=> {
					// 找到相同的菜
					if (item.dishId == obj.dishId) {
						// 饭店里减餐只能到前台去
						console.log("yyyyyds", obj.dishNum, item.dishNum);
						if (obj.dishNum < item.dishNum) {
							console.log("到这里额了");
							app.dialog(this, "错误", "我们不允许您减餐，这是由于您的菜品很可能已经下锅，如您确实有需要，请到前台与管理员商量")
							b = true;
							// 活久见， return 只能跳出 foreach，方法无法返回
							return -1;
						} else if (obj.dishNum > item.dishNum) {
							// 大于之前的菜，说明加餐了，将这次加的分离
							obj.dishNum -= item.dishNum;
							obj.isAdd = true;
							// 同时不要忘了加上之前的菜
							dishOrders.push(item);
						}
					}
				})
				if (b) 	return false;
			}
			dishOrders.push(obj);
		}
		console.log("奇不奇怪，我能到这里！");
		console.log(this.data.discount);
		let dish_json = JSON.stringify(dishOrders);
		let store_json = JSON.stringify(this.data.store);

		let order = {};
		if (this.data.consumeType == 0) {
			// 扫码点餐，设置为确认中
			order.orderType = 2;
		} else if (this.data.consumeType == 1) {
			// 扫码点餐，设置为支付保证金
			order.orderType = 0;
		} else {
			// 待支付
			order.orderType = 1;
		}
		order.storeId = this.data.store.id;
		order.userId = wx.getStorageSync('openid');
		order.consumeType = this.data.consumeType;
		order.table = this.data.table;
		order.originalPrice = this.data.price;
		order.shopDiscount = this.data.discount;
		order.dishOrders = dishOrders;
		order.store = this.data.store;
		console.log("this id == " + this.data.id);
		if (this.data.id) {
			order.id = this.data.id;
			order.isNew = false;
			console.log("order1Z!!!!!!!");
		}

		let orderJson = JSON.stringify(order);

		if (this.data.id) {
			app.dialogWithAction(this, "确定下单吗？", "您当前状态为继续加餐，是否提交新订单",
				(action) => new Promise((resolve) => {
					if (action === 'confirm') {
						console.log("确定");
						app.addOrder(orderJson, (res) => {
							console.log("成功11111", res);
							wx.reLaunch({
								url: '../order-ok/order-ok?order=' + JSON.stringify(order),
								success: (result) => {
									console.log(result);
								},
							});
						}, (err) => {
							console.log("五福出错");
							if (err == 409) {
								app.dialog(this, "服务错误", "添加订单失败，" +
									"在您下单途中，您购物车中的菜品已经被别人抢走了，下次手快点吧")
							} else {
								app.dialog(this, "服务错误", "请检查您的网络")
							}

						})
						resolve(true);
					} else {
						// 拦截取消操作
						resolve(true);
					}
				}))
			return;
		}

		console.log("order", order, orderJson);
		wx.navigateTo({
			url: '../order-sure/order-sure?order=' + orderJson,
			fail: function (err) {
				console.log(err);
			}
		})
	},


	comboCountChange(e) {
		let id = e.detail[0],
			nowNum = e.detail[1];

		let o = this.data.cart[id];
		let b = this.data.dishInfo[id];

		let prevNum = o ? o.num : 0;
		let dis = 0;
		console.log(1);

		if (b.discount.type == 1) {
			dis = parseFloat((((10 - b.discount.val) / 10) * b.price).toFixed(2)).toFixed(2);
		} else if (b.discount.type == 2) {
			dis = parseFloat(b.discount.val).toFixed(2);
		}
		console.log(2);
		console.log("b = ", b, (nowNum - prevNum), (b.price - dis), nowNum, prevNum);
		let nowPrice = (nowNum - prevNum) * (b.price - dis);
		console.log("dis = " + dis, "now = " + nowPrice);
		let item = {
			num: nowNum,
			discountUsedCount: -1,
		}
		var str1 = 'cart.' + id;

		let discount = parseFloat((this.data.discount + (nowNum - prevNum) * dis).toFixed(2));
		this.setData({
			num: this.data.num + (nowNum - prevNum),
			price: this.data.price + (nowNum - prevNum) * b.price,
			discount: discount,
			[str1]: item,
		})
	},


	//点菜 这个应该在后端做，前端没办法处理数据.....
	dishCountChange(e) {

		let id = e.detail[0],
			cnt = e.detail[1];


		//orderCart
		let o = this.data.cart[id];
		let b = this.data.dishInfo[id];
		console.log(o, b);
		//cnt 是当前用户选择的数量
		//要注意cnt 并不是增加的数量，而是一个选择的总数，例如原本下单了3，现在增加一件
		//则 prevNum = 3, cnt = 4
		let prevNum = o ? o.num : 0; // 之前的点餐数量
		let discountUsedCount = o ? o.discountUsedCount : 0; //使用的优惠次数
		let originalPrice = (cnt - prevNum) * b.price; //原始增量价格(原本需要增加的价格)
		let dis = 0; //要优惠价格
		//cnt 可能小于 prevNum，这意味 originalPrice 可能是负的
		//b.discount.type = 是优惠类型
		if (b.discount.type == 1) {
			dis = originalPrice * (1 - (b.discount.val / 10));
		} else if (b.discount.type == 2) {
			dis = (cnt - prevNum) * b.discount.val;
		}
		dis = parseFloat(dis.toFixed(2));
		console.log("dis = " + dis, b.discount.count);
		//用户减少数量(dis < 0--> cnt < prevNum)，设置用户使用的优惠次数减少
		if (dis < 0) {
			//如果确实有优惠的话
			if (b.discount.type == 1 || b.discount.type == 2) {
				if (prevNum <= b.discount.count && discountUsedCount > 0)
					discountUsedCount--;
				else
					dis = 0;
			}
		} else if (dis > 0) { //使用了优惠次数
			//优惠次数已满，不能使用优惠次数，将dis置为0表示不优惠
			if (discountUsedCount == b.discount.count) {
				dis = 0;
			}
			//优惠次数未满，可继续使用 
			else if (discountUsedCount < b.discount.count) {
				discountUsedCount++;
			}
		}

		let item = {
			num: cnt,
			discountUsedCount: discountUsedCount,
		}

		var str1 = 'cart.' + id;
		console.log(dis, this.data.discount, parseFloat(this.data.discount + dis));
		let discount = parseFloat((this.data.discount + dis).toFixed(2));
		this.setData({
			num: this.data.num + (cnt - prevNum),
			price: this.data.price + originalPrice,
			discount: discount,
			[str1]: item,
		})
		console.log(this.data.cart);
	},

	navClick(e) {
		console.log(e.currentTarget);
		this.setData({
			tapNav: e.currentTarget.dataset.index,
			currentNav: e.currentTarget.dataset.index
		})
	},

	changeTag(e) {
		console.log("走：", this.data.price);
		this.setData({
			comboType: e.currentTarget.dataset.id
		})
		console.log("走：", this.data.price);
	},

	getComboStock(combo, info) {
		console.log("获取套餐库存", combo, info);
		let arrays = combo.comboDish;
		let ans = 0x3f3f3f3f;
		for (let i in arrays) {
			let id = arrays[i].dishId;
			let num = info[id].stock;
			ans = ans > num ? num : ans;
		}
		return ans;
	},

	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad: function (options) {
		console.log("加载", options);
		wx.showLoading({
			title: "正在加载中",
			mask: true,
		});
		let bili = 750 / wx.getSystemInfoSync().windowWidth;
		let height = wx.getSystemInfoSync().windowHeight * bili;
		this.setData({
			height: height - 221.5,
		})

		//如果是旧订单，即点击继续加餐的话，则更新购物车
		if (options && options.old) {
			// this.getShowTagsAndIcons(false);
			let oldOrder = JSON.parse(options.old);
			console.log("旧订单", oldOrder);
			this.setData({
				consumeType: oldOrder.consumeType,
				store: oldOrder.store,
				discount: oldOrder.shopDiscount,
				price: oldOrder.originalPrice,
				table: oldOrder.table,
				id: oldOrder.id,
				oldDishOrders: oldOrder.dishOrders
			})
			this.request(() => {

				let cart = {};
				console.log("ooo", oldOrder.dishOrders);
				let num = 0;
				for (let key in oldOrder.dishOrders) {
					let obj = {};
					obj.num = oldOrder.dishOrders[key].dishNum;
					obj.discountUsedCount = 0;
					console.log("obj" + key, obj);
					cart[oldOrder.dishOrders[key].dishId] = obj;
					num += obj.num;
				}
				console.log(cart, oldOrder);
				console.log("OLD", oldOrder);
				this.setData({
					cart: cart,
					num: num
				})
				console.log("旧订单", this.data);
			});

			return;
		}
		let store = JSON.parse(options.store);
		let table = options.table ? options.table : -1;
		this.setData({
			consumeType: options.type,
			store: store,
			table: table
		})
		console.log(this.data.store);
		// 请求网络数据
		this.request(()=>{wx.hideLoading()});
		

	},


	request(complete) {
		app.getOrderDishInfo(this.data.store.id, (res) => {
			console.log("request", res, this.data.store.id);
			let dishes = res.data.dishes;
			let combos = res.data.combos;
			app.getUserLikeAndCollectedDish((res) => {
				this.addLikeAndCollection(dishes, res.data.like, res.data.collection, res.data.willBuy);
				this.addLikeAndCollection(combos, res.data.like, res.data.collection, res.data.willBuy);
				console.log("进入 fix", dishes, combos);
				this.fixDiscountNum(dishes, combos, complete);
			}, (err) => {
				app.dialog(this, "获取信息出错", "请检查您的网络是否正确连接");
				wx.hideLoading();
			});
		}, (err) => {
			wx.hideLoading();
			app.dialog(this, "网络出错", "请求服务器数据出错，请检查网络重试");
		})
	},

	//修正用户可享受的折扣数目
	fixDiscountNum(dishes, combos, complete) {
		app.getUserUsedDiscountNum((res) => {
			let obj = res.data;
			console.log("obj == ", obj);
			//用菜品最多允许享受的折扣次数 减去 用户已经使用的折扣次数，即为这次还可以使用的折扣次数
			for (let i in dishes) {
				let id = dishes[i].id;
				if (id in obj && dishes[i].discount) console.log("didididi", dishes[i]),
					dishes[i].discount.count -= obj[id].count;
			}

			let dishInfo = {};
			console.log(dishes, combos);
			dishes.forEach((item) => {
				if (!item.discount || item.discount == {}) {
					item.discount = {
						type: 0
					};
				}
				dishInfo[item.id] = item;
			});
			combos.forEach((item) => {
				if (!item.discount || item.discount == {}) {
					item.discount = {
						type: 0
					};
				}
				item.stock = this.getComboStock(item, dishInfo);
				dishInfo[item.id] = item;
			})
			//设置 dishInfo 信息
			this.setData({
				dishInfo: dishInfo
			});
			console.log("dishInfo = ", dishInfo);

			this.classify(dishes, combos, complete);
		}, (err) => {
			wx.hideLoading();
			app.dialog(this, "网络出错", "请求服务器数据出错，请检查网络重试");
			wx.hideLoading();
		})
	},

	classify(dishes, combos, complete) {
		app.getDishClassification((res) => {
			let arrays = res.data.arrays;
			console.log("分类信息", arrays);
			//前两个固定为我的收藏和我的待选，这是约定好的
			let singalOrder = [{
					nav: arrays[0].name,
					icon: arrays[0].icon,
					dishList: []
				}, {
					nav: arrays[1].name,
					icon: arrays[1].icon,
					dishList: []
				}, ];

			let dishCombos = [{
					nav: "我的收藏",
					comboList: []
				}, {
					nav: "我的待选",
					comboList: []
				}];
			//先将待选和收藏注入
			console.log(1);
			dishes.forEach((item) => {
				if (item.collection) {
					singalOrder[0].dishList.push(item);
				}
				if (item.willBuy) {
					singalOrder[1].dishList.push(item);
				}
			})
			//修正
			if (singalOrder[1].dishList.length == 0) {
				singalOrder.splice(1, 1);
			}
			if (singalOrder[0].dishList.length == 0) {
				singalOrder.splice(0, 1);
			}


			combos.forEach((item) => {
				if (item.collection) {
					dishCombos[0].comboList.push(item);
				}
				if (item.willBuy) {
					console.log(dishCombos, dishCombos[1], dishCombos[1].comboList);
					dishCombos[1].comboList.push(item);
				}
			})

			//修正
			if (dishCombos[1].comboList.length == 0) {
				dishCombos.splice(1, 1);
			}
			if (dishCombos[0].comboList.length == 0) {
				dishCombos.splice(0, 1);
			}
			arrays.forEach((item) => {
				//这是菜品的分类
				if (item.id < 100000) {
					console.log(12345688, combos);
					singalOrder.push({
						nav: item.name,
						icon: item.icon,
						dishList: []
					});
					dishes.forEach((dish) => {
						if (dish.classificationIds.indexOf(item.id) !== -1) {
							console.log("singalOrder[singalOrder.length - 1].dishList", singalOrder[singalOrder.length - 1].dishList);
							singalOrder[singalOrder.length - 1].dishList.push(dish);
						}
					})
					if (singalOrder[singalOrder.length - 1].dishList.length == 0) {
						singalOrder.pop();
					}
					console.log(12345);
				} else { //套餐的分类
					dishCombos.push({
						nav: item.name,
						comboList: []
					});
					combos.forEach((combo) => {
						console.log(23456);
						if (combo.classificationIds.indexOf(item.id) !== -1) {
							dishCombos[dishCombos.length - 1].comboList.push(combo);
						}
					})
					if (dishCombos[dishCombos.length - 1].comboList.length == 0) {
						dishCombos.pop();
					}
				}
			})
			this.setData({
				singalOrder: singalOrder,
				combos: dishCombos
			})
			complete();
			wx.hideLoading();
		}, (err) => {
			app.dialog(this, "获取信息出错", "请检查您的网络是否正确连接");
			wx.hideLoading();
		})
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

	clickLike: function (obj) {
		let index = obj.detail.index,
			row = obj.detail.row;
		let id = row.id;
		let dishes = this.data.singalOrder,
			combos = this.data.combos,
			dishInfo = this.data.dishInfo;
		app.addOrRemoveUserLikeDish(id, dishInfo[id].like ? 0 : 1, (res) => {
			dishInfo[id].like = !dishInfo[id].like;
			dishInfo[id].likeNum += dishInfo[id].like ? 1 : -1;
			this.setData({
				singalOrder: dishes,
				combos: combos,
				dishInfo: dishInfo
			})
		})

	},


	clickCollection: function (obj) {
		let index = obj.detail.index,
			row = obj.detail.row;
		let id = row.id;
		let dishes = this.data.singalOrder,
			combos = this.data.combos,
			dishInfo = this.data.dishInfo;
		app.addOrRemoveUserCollectedDish(id, dishInfo[id].collection ? 0 : 1, (res) => {
			dishInfo[id].collection = !dishInfo[id].collection;
			this.setData({
				singalOrder: dishes,
				combos: combos,
				dishInfo: dishInfo
			})
		})

	},

	onResize: function () {
		// Do something when page resize
		let bili = 750 / wx.getSystemInfoSync().windowWidth;
		let h = wx.getSystemInfoSync().windowHeight * bili;
		this.setData({
			panel_height: h - 78
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

	upper(e) {

	},

	lower(e) {
		console.log(e)
	},


	//右侧滑动的同时，左侧菜单xuan'zxuanz
	scroll(e) {
		let h = e.detail.scrollTop;
		let bili = 750 / wx.getSystemInfoSync().windowWidth;
		let arr = this.data.singalOrder;
		let sum = 0;
		let size = 10;
		if (this.data.page == '单点') {
			for (let i = 0; i < arr.length; i++) {
				let n = arr[i].dishList.length;
				sum += n;
				if (h * bili < sum * 300) {
					this.setData({
						currentNav: i
					})
					break;
				}
			}
		}
	},

	scrollToTop() {
		// this.setData({
		// 	show: true
		// })
		this.setAction({
			scrollTop: 0
		})
	},
	onPageScroll: function (e) {
		// Do something when page scroll


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
		wx.stopPullDownRefresh();
	},


	tagsChange(e) {
		let key = e.detail.title;
		console.log("tagsChange", key);
		this.setData({
			page: key
		})

	},

	change(e) {
		this.setData({
			value2: e.detail.name
		})
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