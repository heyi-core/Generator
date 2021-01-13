import Vue from 'vue'
import Vuex from 'vuex'
import getters from './getters'
import app from './modules/app'
import settings from './modules/settings'
<#if project.login == true >
import user from './modules/user'
</#if>
import permission from './modules/permission'
import roles from './modules/roles'
<#if project.openDictionary == true >
import dictionaries from './modules/dictionaries'
</#if>
import prod from './modules/prod'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    app,
    settings,
  <#if project.login == true >
    user,
  </#if>
    permission,
    roles,
  <#if project.openDictionary == true >
    dictionaries,
  </#if>
    prod
  },
  getters
})

export default store
