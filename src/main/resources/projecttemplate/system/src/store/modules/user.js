import { getToken, setToken, removeToken, getUserInfo, setUserInfo, removeUserInfo } from '@/utils/auth'
import router, { resetRouter } from '@/router'
import $http from '@/api/index'
import { Message } from 'heyi-ui'

const state = {
  token: getToken(),
  userInfo: getUserInfo() ? JSON.parse(getUserInfo()) : {},
  name: undefined,
  avatar: undefined,
  navRoles: undefined,
  btnRoles: undefined,
  isChairman: false
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_NAVROLES: (state, roles) => {
    state.navRoles = roles
  },
  SET_BTNROLES: (state, btnRoles) => {
    state.btnRoles = btnRoles
  },
  SET_USERINFO: (state, userInfo) => {
    state.userInfo = userInfo
  },
  SET_CHAIRMAN: (state, Chairman) => {
    state.isChairman = Chairman
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    console.log(userInfo)
    const { sysUserPhone, password } = userInfo
    return new Promise((resolve, reject) => {
      $http.sysUserLoginByPassword({ sysUserPhone: sysUserPhone.trim(), sysUserPassword: password }).then(response => {
        if (response.result) {
          console.log(response)
          const { obj } = response
          commit('SET_TOKEN', obj.token)
          commit('SET_USERINFO', obj)
          setToken(obj.token)
          setUserInfo(JSON.stringify(obj))
          resetRouter()
        } else {
          Message.error(response.description)
        }
        resolve(response)
      }).catch(error => {
        reject(error)
      })
    })
  },
  loginTmpCode({ commit }, userInfo) {
    const { tmpAuthCode } = userInfo
    return new Promise((resolve, reject) => {
      $http.loginTmpCode({ tmpAuthCode: tmpAuthCode }).then(response => {
        if (response.result) {
          const { obj } = response.obj
          commit('SET_TOKEN', obj.token)
          commit('SET_USERINFO', obj)
          setToken(obj.token)
          setUserInfo(JSON.stringify(obj))
          resetRouter()
        } else {
          Message.error(response.description)
          reject(response.description)
        }
        resolve(response)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      $http.getByCustomeruser({
        sysUserId: state.userInfo.sysUserId
      }).then(res => {
        if (res.result) {
          const result = {
            navRoles: [],
            name: '',
            btnRoles: [],
            sysRoleId: []
          }
          res.obj.forEach(item => {
            const roleItem = {
              parentSysRoleId: item.parentSysRoleId,
              router: item.router ? JSON.parse(item.router) : [],
              button: item.button ? JSON.parse(item.button) : [],
              sysRoleId: item.sysRoleId
            }
            console.log(roleItem)
            if (roleItem === 0) {
              commit('SET_CHAIRMAN', true)
            }
            result.sysRoleId.push(roleItem.sysRoleId)
            result.navRoles.push(...roleItem.router)
            result.btnRoles.push(...roleItem.button)
            result.name += item.sysRoleName + '，'
          })
          result.navRoles = Array.from(new Set(result.navRoles))
          result.btnRoles = Array.from(new Set(result.btnRoles))
          result.sysRoleId = Array.from(new Set(result.sysRoleId))
          result.name = result.name.slice(0, result.name.length - 1)
          const newUserInfo = { ...state.userInfo, sysRoleName: result.name, sysRoleId: result.sysRoleId }
          commit('SET_NAME', result.name)
          commit('SET_AVATAR', result.avatar)
          commit('SET_NAVROLES', result.navRoles)
          commit('SET_BTNROLES', result.btnRoles)
          commit('SET_USERINFO', newUserInfo)
          setUserInfo(JSON.stringify(newUserInfo))
          resolve(result)
        } else {
          reject(res.description)
        }
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      commit('SET_NAME', '')
      commit('SET_AVATAR', '')
      commit('SET_NAVROLES', '')

      removeToken()
      removeUserInfo()
      resetRouter()
      resolve()
    })
  },
  // 更改当前用户信息
  changeUserInfo({ commit, state }, userInfo) {
    return new Promise((resolve, reject) => {
      $http.sysUserUpdate(userInfo).then(res => {
        if (res.result) {
          const oldUserInfo = state.userInfo
          const { obj } = res
          for (const item in obj) {
            if (oldUserInfo[item] !== obj[item]) {
              oldUserInfo[item] = obj[item]
            }
          }
          commit('SET_USERINFO', oldUserInfo)
          setUserInfo(JSON.stringify(oldUserInfo))
        }
        resolve(res)
      })
    })
  },
  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_NAME', '')
      commit('SET_AVATAR', '')
      commit('SET_NAVROLES', [])
      commit('SET_BTNROLES', [])
      removeToken()
      resolve()
    })
  },
  // dynamically modify permissions
  changeRoles({ commit, dispatch }, role) {
    return new Promise(async resolve => {
      const token = role + '-token'

      commit('SET_TOKEN', token)
      setToken(token)

      const { navRoles } = await dispatch('getInfo')

      resetRouter()

      // generate accessible routes map based on roles
      const accessRoutes = await dispatch('permission/generateRoutes', navRoles, { root: true })
      // dynamically add accessible routes
      router.addRoutes(accessRoutes)

      resolve()
    })
  },
  resetRouter() {
    resetRouter()
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

