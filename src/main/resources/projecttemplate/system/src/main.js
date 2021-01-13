import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets
import '@/assets/styles/index.scss' // global css
import '@/assets/iconfont/icon.scss' // global css

import ElementUI from 'heyi-ui'
import 'heyi-ui/lib/theme-chalk/index.css'
import locale from 'heyi-ui/lib/locale/lang/zh-CN' // lang i18n

import globalComponent from '@/common'
import myTips from '@/common/MyTips'

import { formatDate } from './utils/date-util.js'

import App from './App'
import store from './store'
import router from './router'
import ajax from '@/api/index'

import '@/permission' // permission control
<#if project.login == true >
import btnPermission from '@/utils/btn-permission'
</#if>
import uploader from 'vue-simple-uploader'
Vue.use(uploader)
/**
 * If you don't want to use mock-server
 * you want to use mockjs for request interception
 * you can execute:
 *
 * import { mockXHR } from '../mock'
 * mockXHR()
 */

// set ElementUI lang to EN
Vue.use(ElementUI, { locale })
Vue.use(globalComponent)
Vue.use(myTips)

Vue.config.productionTip = false
Vue.prototype.$http = ajax
<#if project.login == true >
Vue.prototype.btnPermission = btnPermission
</#if>
Vue.prototype.formatDate = formatDate
new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
