import axios from 'axios'
import {
  Message,
  MessageBox
} from 'element-ui'
import store from '../store'
import {
  getToken
} from '@/utils/auth'

// 创建axios实例
const service = axios.create({
  baseURL: process.env.BASE_API, // api的base_url
  timeout: 15000 // 请求超时时间
})

// request拦截器
service.interceptors.request.use(config => {
  console.log("token == " + store.getters.token);
  if (store.getters.token) {
    config.headers['Authorization'] = getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
  }
  console.log("c == ", config);
  if (config.method == "post") {
    config.headers['Content-Type'] = "application/json;charset=UTF-8";
  }
  return config
}, error => {
  // Do something with request error
  console.log(error) // for debug
  Promise.reject(error)
})

// respone拦截器
service.interceptors.response.use(
  response => {
    /**
     * code为非200是抛错 可结合自己业务进行修改
     */
    const res = response;
    let code = res.data.code;
    console.log("回答", res);
    if (code && code != 200) {
      console.log("code err" + code);
     if (res.data.msg) {
      Message({
        message: res.data.msg,
        type: 'error',
        duration: 3 * 1000
      })
     }
      if (code === 401) {
        console.log(401);
        MessageBox.confirm('您的登录信息已过期，请您重新登录', '确定登出', {
          confirmButtonText: '重新登录',
          cancelButtonText: '不要，我要留在这里',
          type: 'warning'
        }).then(() => {
          store.dispatch('FedLogOut').then(() => {
            location.reload() // 重新实例化vue-router对象 避免bug
          })
        }).catch(() => {
          return null;
        })
      }
      return Promise.reject('error');
    }

    console.log("回答 ==> ", res);
    // 401:未登录;，这个code表示token过期
    
    //   return Promise.reject('error')
    // } else {
    //   return response.data
    // }
    console.log(typeof response.data);
    if (typeof response.data == `string`) {
      console.log("对象是 String", response.data);
      try {
        response.data = JSON.parse(response.data);
      } catch (e) {
        console.log("无法翻译，可能是一条普通字符串");
      }
    }
    return response;
  },
  error => {
    let err = error.message;
    Message({
      message: err,
      type: 'error',
      duration: 3 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
