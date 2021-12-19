import request from '@/utils/request'

export function login(username, password) {
  return request({
    url: '/login',
    method: 'post',
    // headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    data: {
      username,
      password
    }
  })
  // return {data: "123"};
}

export function getInfo() {
  return request({
    url: '/admin/info',
    method: 'get',
  })
}

export function logout() {
  return request({
    url: '/logout',
    method: 'post'
  })
}


