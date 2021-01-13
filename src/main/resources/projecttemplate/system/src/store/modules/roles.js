import { asyncRoutes } from '@/router'
import btnRoles from '@/api/btnRoules'
import * as apiRoules from '@/api/api'
import $http from '@/api/index'

const state = {
}

const mutations = {
}

const actions = {
  // 获取角色的默认路由配置
  getRoleNav({ commit }, activeRole) {
    const resultData = {}
    return new Promise(resolve => {
      resultData.asyncRoutes = asyncRoutes
      resultData.routerRolers = activeRole.router ? JSON.parse(activeRole.router) : []
      resolve(resultData)
    })
  },
  getBtnRoles({ commit }, { name, activeRole }) {
    const resultData = {}
    resultData.allBtnRoule = btnRoles[name] ? btnRoles[name] : []
    return new Promise(resolve => {
      resultData.btnRoules = activeRole.button ? JSON.parse(activeRole.button) : []
      resolve(resultData)
    })
  },
  getApiRoles({ commit }, { name, activeRole }) {
    const resultData = {}
    resultData.allApiRoule = apiRoules[name] ? apiRoules[name] : []
    return new Promise(resolve => {
      resultData.apiRoules = activeRole.api ? JSON.parse(activeRole.api) : []
      resolve(resultData)
    })
  },
  // 获取当前角色上级
  getParentUser({ commit }, pUserList) {
    $http.getUpRoleByCustomeruser().then((res) => {
      if (res.result && res.obj.length > 0) {
        res.obj.forEach(role => {
          if (role.parentSysRoleId === 0) {
            pUserList.isTop.push(true)
          }
          $http.getAllByRoleId({
            'sysRoleId': role.sysRoleId
          }).then(response => {
            if (response.result && response.obj.length > 0) {
              pUserList.pUserList.push(...response.obj)
            }
          })
        })
      }
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

