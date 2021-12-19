import request from '@/utils/request'

export function fetchList(params) {
  return request({
    url: '/role/list',
    method: 'get',
    params: params
  })
}

export function createRole(data) {
  return request({
    url: '/role/add',
    method: 'post',
    data: data
  })
}

export function updateRole(data) {
  return request({
    url: '/role/update',
    method: 'post',
    data: data
  })
}

export function deleteRole(params) {
  return request({
    url:'/role/delete',
    method:'post',
    params: params
  })
}


export function listMenuByRole(roleId) {
  return request({
    url: '/role/listMenu',
    method: 'get',
    params: {roleId: roleId}
  })
}

export function listStoreByRole(roleId) {
  return request({
    url: '/role/listStore',
    method: 'get',
    params: {roleId: roleId}
  })
}


export function allocMenu(data) {
  return request({
    url: '/role/allocMenu',
    method: 'post',
    data:data
  })
}

export function allocResource(data) {
  return request({
    url: '/role/allocResource',
    method: 'post',
    data:data
  })
}
