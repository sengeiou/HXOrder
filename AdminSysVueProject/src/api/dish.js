import request from '@/utils/request'
export function getDish(id) {
  return request({
    url:'/dish/get',
    method:'get',
    params:{dishId: id}
  })
}





export function fetchList(params) {
  return request({
    url:'/dish/list',
    method:'get',
    params:params
  })
}

export function fetchClassificationList() {
  return request({
    url:'/dish/classification_list',
    method:'get'
  })
}

export function updateDeleteStatus(params) {
  return request({
    url:'/dish/delete',
    method:'post',
    params:params
  })
}

export function updateNewStatus(params) {
  return request({
    url:'/dish/update_new_status',
    method:'post',
    params:params
  })
}

export function updateRecommendStatus(params) {
  return request({
    url:'/dish/update_recommend_status',
    method:'post',
    params:params
  })
}

export function updatePublishStatus(params) {
  return request({
    url:'/dish/update_publish_status',
    method:'post',
    params:params
  })
}

export function createDish(data) {
  return request({
    url:'/dish/add',
    method:'post',
    data:data
  })
}

export function updateDish(data) {
  return request({
    url:'/dish/update',
    method:'post',
    data:data
  })
}

export function updateDiscount(data) {
  return request({
    url:'/marketing/update_discount',
    method:'post',
    data:data
  })
}

export function getDishStock(id) {
  return request({
    url:'/dish/dish_stock_list',
    method:'get',
    params: {dishId: id}
  })
}

export function updateDishStock(params) {
  return request({
    url:'/dish/update_dish_stock',
    method:'post',
    params: params
  })
}

