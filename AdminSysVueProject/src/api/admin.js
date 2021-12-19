import request from '@/utils/request'
export function fetchList(params) {
    return request({
      url: '/admin/list',
      method: 'get',
      params: params
    })
  }
  
  export function createAdmin(data) {
    return request({
      url: '/admin/add',
      method: 'post',
      data: data
    })
  }
  
  export function updateAdmin(data) {
    return request({
      url: '/admin/update',
      method: 'post',
      data: data
    })
  }
  
  export function updateStatus(params) {
    return request({
      url: '/admin/updateStatus',
      method: 'post',
      params: params
    })
  }
  
  export function deleteAdmin(id) {
    return request({
      url: '/admin/delete',
      method: 'post',
      params: {userId: id}
    })
  }
  
  export function getRoleByAdmin(id) {
    return request({
      url: '/admin/role',
      method: 'get',
      params: {userId: id}
    })
  }
  
  export function allocRole(data) {
    return request({
      url: '/admin/alloc_role',
      method: 'post',
      data: data
    })
  }