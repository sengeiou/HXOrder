import { login, logout, getInfo } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'

const user = {
  state: {
    token: getToken(),
    name: '',
    avatar: '',
    roles: []
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    }
  },

  actions: {
    // 登录
    Login({ commit }, userInfo) {
      const username = userInfo.username.trim()
      return new Promise(
          (resolve, reject) => {
          login(username, userInfo.password).then(response => {
            console.log("登录返回TOKEN " + response.token);
            // const tokenStr = data.tokenHead+data.token
            const tokenStr = response.data.token;
            setToken(tokenStr)
            commit('SET_TOKEN', tokenStr)
            resolve()
          }).catch(error => {
            console.log(error);
            reject("验证不通过")
          })
        }
      )
    },

    // 获取用户信息
    GetInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getInfo().then(response => {
          const data = response.data
          console.log("user info = " + JSON.stringify(data));
          if (data.roles && data.roles.length > 0) { // 验证返回的roles是否是一个非空数组
            //保存角色名
            commit('SET_ROLES', data.roles)
          } else {
            reject('获取用户角色出错!')
          }
          console.log(data.nickname);
          commit('SET_ROLES', data.roles)
          commit('SET_NAME', data.nackname)
          commit('SET_AVATAR', data.avatar)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 登出
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          commit('SET_NAME', '')
          commit('SET_AVATAR', '')
          removeToken()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          commit('SET_NAME', '')
          commit('SET_AVATAR', '')
        removeToken()
        resolve()
      })
    }
  }
}

export default user
