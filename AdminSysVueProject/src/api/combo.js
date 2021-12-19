import request from '@/utils/request'
export function fetchList(params) {
  return request({
    url:'/combo/list',
    method:'get',
    params:params
  })
}


export function fetchComboDishList(id) {
  return request({
    url:'/combo/combo_dish_list',
    method:'get',
    params:{comboId: id}
  })
}

export function getCombo(id) {
  return request({
    url:'/combo/get_combo',
    method:'get',
    params:{comboId: id}
  })
}

export function updateDeleteStatus(params) {
  return request({
    url:'/combo/delete',
    method:'post',
    params:params
  })
}


export function updatePublishStatus(params) {
  return request({
    url:'/combo/update_publish_status',
    method:'post',
    params:params
  })
}

export function createCombo(data) {
  return request({
    url:'/combo/add',
    method:'post',
    data:data
  })
}

export function updateCombo(data) {
  return request({
    url:'/combo/update',
    method:'post',
    data:data
  })
}

export function updateComboDishList(data) {
  return request({
    url:'/combo/update_combo_dish',
    method:'post',
    data: data
  })
}

export function deleteComboDish(data) {
  return request({
    url:'/combo/delete_combo_dish',
    method:'post',
    data: data
  })
}


