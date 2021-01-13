import $http from '@/api/index'

const state = {
  dictionaries: undefined
}

const mutations = {
  SET_DICTIONARIONS: (state, dictionaries) => {
    state.dictionaries = dictionaries
  }
}

const actions = {
  sysDictionariesgetAll({ commit }) {
    return new Promise((resolve, reject) => {
      $http.sysDictionariesgetAll().then(res => {
        // console.log(res)
        if (res.result) {
          commit('SET_DICTIONARIONS', res.obj)
          resolve()
        } else {
          commit('SET_DICTIONARIONS', [])
          reject()
        }
      }).catch(() => {
        commit('SET_DICTIONARIONS', [])
        reject()
      })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
