const app = getApp()


Page({
    data: {

        /* 0:扫码点餐 1:到店消费 2:到店自取 3:外码配送 */
        type: 1,
        f_type: 1, //这是收藏页做的选择
        page: 0, //0是默认页，1是收藏页
        collectedStores: [],
        stores: null,
        //表示消费类型索引
        showCurrentIndex: 0,
        radioCurrent: '1', //展开的店铺索引
        favoriteShowCurrentIndex: 0, //收藏页面展开的店铺
        markers: [{
                id: 0,
                name: "no",
                iconPath: "../../images/location.png",
                latitude: 28.5499994,
                longitude: 115.324520,
                width: 50,
                height: 50,
            }, {
                id: 1,
                name: "no",
                iconPath: "../../images/location.png",
                latitude: 28.688494,
                longitude: 115.934841,
                width: 50,
                height: 50
            },
            {
                id: 2,
                name: "no",
                iconPath: "../../images/location.png",
                latitude: 24.099994,
                longitude: 115.324520,
                width: 50,
                height: 50
            }
        ],
        //用户的经纬度
        latitude: null,
        longitude: null,
        seartchValue: null
    },



    onLoad: function () {
        wx.showLoading({
            title: "正在加载中",
            mask: true,
        });
        // this.getMap();
        app.getStore(true, (res) => {
            let stores = res.data.arrays;
            app.getUserCollectedStore((res) => {
                let collectedStores = [];
                console.log("wwws", res.data.arrays);
                for (let i in stores) {
                    stores[i].collection = false;
                    if (res.data.arrays.indexOf(stores[i].id) != -1) {
                        stores[i].collection = true;
                        collectedStores.push(i);
                    }
                }
                this.setData({
                    collectedStores: collectedStores,
                    stores: stores
                })
                this.getMap();
                wx.hideLoading();
            }, (err) => {
                wx.hideLoading();
                app.dialog(this, "网络错误", "获取店铺信息出错，请重试")
            });
        }, (err) => {
            wx.hideLoading();
            app.dialog(this, "网络错误", "获取店铺信息出错，请重试")
        })


    },

    //获取用户位置信息
    getMap() {
        wx.getLocation({
            isHighAccuracy: true,
            highAccuracyExpireTime: 4000,
            type: 'wgs84',
            success: (res) => {
                var latitude = res.latitude
                var longitude = res.longitude
                var speed = res.speed
                var accuracy = res.accuracy
                console.log("get map successfully", latitude, longitude);
                let stores = this.data.stores;
                let markers = new Array(stores.length);
                for (let i in stores) {
                    markers[i] = {};
                    markers[i].iconPath = "";
                    markers[i].height = 50;
                    markers[i].width = 50;
                    markers[i].longitude = (Number)(stores[i].longitude);
                    markers[i].latitude = (Number)(stores[i].latitude);
                    markers[i].id = (Number)(i);
                    markers[i].name = stores[i].name;
                    stores[i].dis = this.getDistance(
                        stores[i].latitude, stores[i].longitude,
                        latitude, longitude
                    )
                }
                console.log(markers);
                this.setData({
                    latitude: latitude,
                    longitude: longitude,
                    stores: stores,
                    markers: markers
                })
            },
            fail: (err) => {
                app.dialog(this, "授权失败", "获取用户位置信息失败，无法为您提供位置服务，您可以下拉刷新重新授权")
                console.log("There is a erron where getting the map", err);
            }
        })
        this.mapCtx = wx.createMapContext('mapId')

        this.mapCtx.on('markerClusterClick', res => {
            console.log('markerClusterClick', res)
        })

        // 使用默认聚合效果时可注释下一句
        this.bindEvent()
    },

    //在收藏页面点击店铺信息展开
    favoriteClick0(e) {
        let id = e.currentTarget.dataset.id;
        console.log(e);
        if (this.data.favoriteShowCurrentIndex != id) {
            this.setData({
                favoriteShowCurrentIndex: id,
                f_type: 1
            });
        }
        console.log(this.data.favoriteShowCurrentIndex);
    },
    //点击店铺信息展开
    click0(e) {
        let id = e.currentTarget.dataset.id;
        console.log(e);
        if (this.data.showCurrentIndex != id) {
            this.setData({
                showCurrentIndex: id,
                type: 1
            });
        }
        console.log("type == " + this.data.type);
    },

    //点击联系我们
    click1(e) {
        let id = e.currentTarget.dataset.id;
        wx.makePhoneCall({
            phoneNumber: this.data.stores[id].contactPhone,
            success: function () {
                console.log("拨打电话成功！")
            },
            fail: function () {
                console.log("拨打电话失败！")
            }
        })
    },

    //点击收藏
    click2(e) {
        let id = e.currentTarget.dataset.id;
        let stores = this.data.stores;
        let collectedStores = this.data.collectedStores;
        // if (!stores[id].collection) {
        //     collectedStores.push(id)
        // } else {
        //     let index = collectedStores.indexOf(id);
        //     if (index != -1) {
        //         collectedStores.splice(index, 1);
        //     }
        // }


        let opt = !stores[id].collection ? 1 : 0;
        app.addOrRemoveUserCollectedStore(stores[id].id, opt, (res) => {
            stores[id].collection = !stores[id].collection;
            if (opt) {
                collectedStores.push(id);
            } else {
                let index = collectedStores.indexOf(id);
                if (index != -1) {
                    collectedStores.splice(index, 1);
                }
            }
            this.setData({
                stores: stores,
                collectedStores: collectedStores
            })
        });

    },

    //点击去这里
    click3(e) {
        let id = e.currentTarget.dataset.id;
        wx.openLocation({
            latitude: this.data.stores[id].latitude,
            longitude: this.data.stores[id].longitude,
            name: this.data.stores[id].name,
            address: this.data.stores[id].address,
            fail(res) {
                console.log(res);
            }
        })
    },

    //点击立即点餐
    click4(e) {
        let id = e.currentTarget.dataset.id;
        console.log("yyy", id, this.data.stores);
        let json = JSON.stringify(this.data.stores[id]);
        let type = this.data.page == 0 ? this.data.type : this.data.f_type;
        console.log("type === = = = = = ", type, json);
        wx.navigateTo({
            url: '../dish-order/dish-order?type=' + type + '&store=' + json
        });
    },

    search(e) {
        console.log(e);
    },

    changeTabs(e) {
        //切换页
        this.setData({
            page: (this.data.page ^ 1)
        })
        console.log("changeTab", this.data.page);
    },

    radioChange(e) {

        let key = e.detail;
        let type = 0;
        console.log(e.detail);
        if (key == '1' || key == 'f-1') {
            type = 1;
        } else if (key == '2' || key == 'f-2') {
            type = 2;
        } else if (key == '3' || key == 'f-3') {
            type = 3;
        }
        if (key.indexOf("f") == -1) {
            this.setData({
                type: type
            });
        } else {
            this.setData({
                f_type: type
            });
        }
        console.log("type = " + this.data.type, this.data.f_type);
    },


    bindEvent() {
        this.mapCtx.initMarkerCluster({
            enableDefaultStyle: false,
            zoomOnClick: true,
            gridSize: 60,
            complete(res) {
                console.log('initMarkerCluster', res)
            }
        })

        // enableDefaultStyle 为 true 时不会触发改事件
        this.mapCtx.on('markerClusterCreate', res => {
            console.log('clusterCreate', res)
            const clusters = res.clusters
            const markers = clusters.map(cluster => {
                const {
                    center,
                    clusterId,
                    markerIds
                } = cluster
                return {
                    ...center,
                    width: 0,
                    height: 0,
                    clusterId, // 必须
                    label: {
                        content: markerIds.length + '',
                        fontSize: 20,
                        width: 60,
                        height: 60,
                        bgColor: '#00ff00',
                        borderRadius: 30,
                        textAlign: 'center',
                        anchorX: 0,
                        anchorY: -30,
                    }
                }
            })
            this.mapCtx.addMarkers({
                markers,
                clear: false,
                complete(res) {
                    console.log('clusterCreate addMarkers', res)
                }
            })
        })

    },

    onMarkerTap(e) {
        console.log('@@ markertap', e.detail.markerId)
        let m = this.data.markers[e.detail.markerId];
        let s = this.data.stores[e.detail.markerId];
        wx.openLocation({ //​使用微信内置地图查看位置。
            latitude: m.latitude, //要去的纬度-地址
            longitude: m.longitude, //要去的经度-地址
            name: s.name,
            address: s.address
        })
    },


    getDistance(lat1, lng1, lat2, lng2) {
        var radLat1 = (lat1 * Math.PI) / 180.0;
        var radLat2 = (lat2 * Math.PI) / 180.0;
        var a = radLat1 - radLat2;
        var b = (lng1 * Math.PI) / 180.0 - (lng2 * Math.PI) / 180.0;
        var s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378.137; // EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        console.log(s);
        s = s.toFixed(3);
        console.log(s);
        return s;
    },
    onCalloutTap(e) {
        console.log('@@ onCalloutTap', e)
    },

    onLabelTap(e) {
        console.log('@@ labletap', e)
    },
    onPullDownRefresh: function () {
        console.log("刷新！");
        this.onLoad();
        wx.stopPullDownRefresh();
    },
})