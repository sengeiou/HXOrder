import request from '@/utils/request'

export function fetchList(params) {
  return request({
    url:'/store/list',
    method:'get',
    params: params
  })
}
export function getStore(id) {
  return request({
    url:'/store/get',
    method:'get',
    params:{storeId: id}
  })
}

export function updateDeleteStatus(params) {
  return request({
    url:'/store/delete',
    method:'post',
    params:params
  })
}

export function updateWorkingStatus(params) {
  return request({
    url:'/store/update_working_status',
    method:'post',
    params:params
  })
}

export function updateTakeoutStatus(params) {
  return request({
    url:'/store/update_takeout_status',
    method:'post',
    params:params
  })
}

export function createStore(data) {
  return request({
    url:'/store/add',
    method:'post',
    data:data
  })
}

export function updateStore(data) {
  return request({
    url:'/store/update',
    method:'post',
    data:data
  })
}
