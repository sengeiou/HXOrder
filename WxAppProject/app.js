// app.js
import Dialog from '/dist/dialog/dialog';
App({
	onLaunch() {
		wx.getStorage({
			key: 'token',
			success(res) {
				console.log(res.data)
			},
			fail: (err) => {
				console.log("获取 oppenid 缓存失败，未登录");
				this.login();
			}
		})
	},

	//--------------菜品相关 API ------------------

	getUserLikeAndCollectedDish(success, fail) {
		this.getRequest("/get_user_marked_dish", {
			
		}, success, fail)
	},
	/**
	 * @param {number} option 1代表添加， 0代表移除
	 */
	addOrRemoveUserLikeDish(dishId, option, success) {
		this.checkLoginStatus();
		let path = option == 0 ? "/remove_user_like_dish" : "/add_user_like_dish";
		this.postRequest(path, {	
				dishId: dishId
			}, success,
			(err) => {
				//参数错误，很有可能是 
				this.dialog(this, "服务错误", "网络或服务器出错，无法添加或移除喜欢");
			})
	},

	addOrRemoveUserWillBuyDish(dishId, option, success) {
		this.checkLoginStatus();
		let path = option == 0 ? "/remove_user_will_buy_dish" : "/add_user_will_buy_dish";
		this.postRequest(path, {
				
				dishId: dishId
			}, success,
			(err) => {
				//参数错误，很有可能是 
				this.dialog(this, "服务错误", "网络或服务器出错，无法添加或移除喜欢");
			})
	},

	addOrRemoveUserCollectedDish(dishId, option, success) {
		this.checkLoginStatus();
		let path = option == 0 ? "/remove_user_collected_dish" : "/add_user_collected_dish";
		this.postRequest(path, {
				
				dishId: dishId
			}, success,
			(err) => {
				//参数错误，很有可能是 
				this.dialog(this, "服务错误", "网络或服务器出错，无法添加或移除喜欢");
			})
	},

	getUserUsedDiscountNum(success, fail) {
		this.getRequest("/get_used_discount_num", {
			userId: wx.getStorageSync('openid')
		}, success, fail)
	},

	getDishClassification(success, fail) {
		this.getRequest("/get_dish_classification", null, success, fail)
	},

	// 获取首页的菜品信息，返回值为 res.hotDishList、res.recommendDishList, res.newDishList, res.combos
	getIndexDishInfo(useCache, success, fail) {
		this.getRequest("/get_index_dish_info", {
			cache: useCache
		}, success, fail);
	},


	getOrderDishInfo(storeId, success, fail) {
		this.getRequest("/get_order_dish_info", {
			storeId: storeId,
			
		}, success, fail);
	},

	getWatingTime(storeId, success, fail) {
		this.getRequest("/get_waiting_time", {
			storeId: storeId
		}, success, fail);
	},

	//该API与上面API不同的是，该函数会返回具体的菜品与套餐数据，而上面的只会返回收藏ID列表
	getUserCollectedDishes(success, fail) {
		this.getRequest("/get_user_collected_dishes", {
			
		}, success, fail)
	},

	// ------------------------------------------------- store =-----------
	getStore(useCache, success, fail) {
		this.getRequest("/get_store_list", {
			cache: useCache
		}, success, fail);
	},

	getStoreById(id, success, fail) {
		this.getRequest("/get_store", {
			storeId: id
		}, success, fail);
	},

	getUserCollectedStore(success, fail) {
		this.getRequest("/get_collected_stores", {
			
		}, success, fail)
	},

	addOrRemoveUserCollectedStore(storeId, option, success) {
		this.checkLoginStatus();
		let path = option == 0 ? "/remove_user_collected_store" : "/add_user_collected_store";
		this.postRequest(path, {
				
				storeId: storeId
			}, success,
			(err) => {
				//参数错误，很有可能是 
				this.dialog(this, "服务错误", "网络或服务器出错，无法添加或移除喜欢");
			})
	},

	addOrder(order, success, fail) {
		console.log("addOrder");
		this.postRequest("/add_user_order", {
			order: order,
			
		}, success, fail)
	},

	cancelPay(oid, success, fail) {
		this.postRequest("/cancelpay", {
			orderId: oid,
		}, success, fail)
	},

	pay(pid, success, fail) {
		this.postRequest("/pay", {
			payId: pid,
		}, success, fail)
	},

	cancelOrder(data, success, fail) {
		this.postRequest("/cancel_order", {
			apply: data
		}, success, fail)
	},

	deleteOrder(orderId, success, fail) {
		this.postRequest("/delete_order", {
			orderId: orderId
		}, success, fail)
	},

	getUserOrders(success, fail) {
		this.getRequest("/get_user_orders", {
			
		}, success, fail)
	},

	getUserAddr(success, fail) {
		this.getRequest("/get_user_address", {
			
		}, success, fail)
	},

	addUserAddr(data, success, fail) {
		this.postRequest("/add_user_address", {
			address: data
		}, success, fail)
	},

	updateUserAddr(data, success, fail) {
		this.postRequest("/update_user_address", {
			address: data,
		}, success, fail)
	},

	removeUserAddr(addrId, success, fail) {
		this.postRequest("/remove_user_address", {
			addressId: addrId,
		}, success, fail)
	},

	getUserInfo(success, fail) {
		this.getRequest("/get_user_info", {
			
		}, success, fail);
	},

	getUserMessage(success, fail) {
		this.getRequest("/get_user_message", {
			
		}, success, fail);
	},

	getUserMessageCount(success, fail) {
		this.getRequest("/get_user_message_count", {
			
			timestamp: (new Date()).valueOf()
		}, success, fail);
	},



	dialog(obj, title, content, close) {
		Dialog.alert({
			conent: obj,
			title: title,
			message: content,
		}).then(() => {
			// on close
			close();
		});
	},

	errDialog(obj) {
		this.dialog(obj, "发送了意料之外的错误", "请刷新重试，如果此错误仍然存在，请点击 我的 客服 及时与我们反应")
	},

	dialogWithAction(obj, title, content, beforeClose) {
		Dialog.confirm({
			conent: obj,
			title: title,
			message: content,
			beforeClose
		})
	},

	//POST需要额外封装请求头 "application/x-www-form-urlencoded"
	postRequest(path, data, success, fail) {
		var reqTask = wx.request({
			url: this.globalData.url + path,
			data: data,
			header: {
				"Content-Type": "application/x-www-form-urlencoded",
				"Authorization": wx.getStorageSync('token')
			},
			method: 'POST',
			dataType: 'json',
			responseType: 'text',
			success: (result) => {
				console.log(result);
				if (result.statusCode == 401) {
					wx.hideLoading();
					this.dialogWithAction(this, "您暂未登录", "点击确定尝试重新登陆",
						(action) => new Promise((resolve) => {
							if (action === 'confirm') {
								this.login();
								resolve(true);
							} else {
								resolve(true);
							}
						})
					)
					return;
				}
				if (result.statusCode != 200) {
					fail(result.statusCode);
				} else {
					success(result);
				}
			},
			fail: (err) => {
				console.log(err);
				fail(err)
			},
			complete: () => {}
		});

	},

	getRequest(path, data, success, fail) {
		console.log(success);
		var reqTask = wx.request({
			url: this.globalData.url + path,
			data: data,
			header: {
				"Authorization": wx.getStorageSync('token')
			},
			method: 'GET',
			dataType: 'json',
			responseType: 'text',
			success: (result) => {
				if (result.statusCode == 401) {
					
					wx.hideLoading();
					this.dialogWithAction(this, "您暂未登录", "点击确定尝试重新登陆并刷新页面重试",
						(action) => new Promise((resolve) => {
							if (action === 'confirm') {
								this.login();
								resolve(true);
							} else {
								resolve(true);
							}
						})
					)
					return;
				}
				if (result.statusCode != 200) {
					fail(result.statusCode);
				} else {
					success(result);
				}
			},
			fail: (err) => {
				console.log("errrwqw" + err);
				fail(err)
			},
			complete: () => {}
		});

	},

	checkLoginStatus() {
		let openid = wx.getStorageSync('openid');
		if (!openid) {
			this.login();
		}
	},

	login() {
		wx.login({
			success: (res) => { //请求自己后台获取用户 openid
				let that = this;
				this.postRequest("/login", {
						jsCode: res.code
					},
					(response) => {
						console.log(response);
						var openid = response.data.openid;
						var token = response.data.token;
						console.log("userData", response.data); //可以把openid存到本地，方便以后调用
						wx.setStorageSync('openid', openid);
						wx.setStorageSync('token', token);
						this.dialog(this, "登陆成功", "您已成功登录")
					},
					(err) => {
						wx.removeStorageSync('openid')
						this.dialog(this, "未知错误", "登录未成功，请您退出小程序重试，如果此错误持续存在，请联系我们 邮箱：happysnaker@foxmail.com")
					},
					(c)=> {
						console.log("ccccccccc");
					}
				)

			}
		})
	},

	globalData: {
		userInfo: null,
		// url: "http://127.0.0.1:8088"
		// url: "http://f393281q73.zicp.vip:80"
		url: "https://happysnaker.xyz:8088"
	}
})