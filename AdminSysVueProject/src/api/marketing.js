import request from '@/utils/request'
export function fetchDiscountList(params) {
    return request({
      url:'/marketing/discount_list',
      method:'get',
      params:params
    })
}

export function addDiscount(data) {
    return request({
      url:'/marketing/add_discount',
      method:'post',
      data: data
    })
}

export function updateDiscount(data) {
    return request({
      url:'/marketing/update_discount',
      method:'post',
      data: data
    })
}

export function deleteDiscount(params) {
    return request({
      url:'/marketing/delete_discount',
      method:'post',
      params: params
    })
}