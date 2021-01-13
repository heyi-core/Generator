import axios from 'axios'
import { Message } from 'heyi-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'
import { Decrypt, Encrypt } from '@/api/secretutil'
import { openAes } from '@/settings'

const OPEN_AES = openAes

// const pending = {}
// const CancelToken = axios.CancelToken
// const removePending = (key, isRequest = false) => {
//   if (pending[key] && isRequest) {
//     pending[key]('取消重复请求')
//   }
//   delete pending[key]
// }
// const getRequestIdentify = (config, isReuest = false) => {
//   let url = config.url
//   if (isReuest) {
//     url = config.baseURL + config.url.substring(1, config.url.length)
//   }
//   return config.method === 'get' ? encodeURIComponent(url + JSON.stringify(config.params)) : encodeURIComponent(config.url + JSON.stringify(config.data))
// }

// 创建axios实例
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  withCredentials: true, // send cookies when cross-domain requests
  timeout: 60000 // request timeout
})

// 添加一个请求拦截器
service.interceptors.request.use(
  config => {
    if (store.getters.token) {
      config.headers['Authorization'] = getToken()
    }
    config.headers['Content-Type'] = 'application/json;charset=UTF-8'
    config.responseType = 'json'

    // 拦截重复请求(即当前正在进行的相同请求)
    // const requestData = getRequestIdentify(config, true)
    // removePending(requestData, true)
    //
    // config.cancelToken = new CancelToken((c) => {
    //   pending[requestData] = c
    // })
    return config
  },
  error => {
    // do something with request error
    return Promise.reject(error)
  }
)

// 添加一个响应拦截器
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
  */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    // 把已经完成的请求从 pending 中移除
    // const requestData = getRequestIdentify(response.config)
    // removePending(requestData)
    let res = response.data
    if (OPEN_AES) {
      res = Decrypt(response.data)
    }
    // if the custom code is not 20000, it is judged as an error.
    if (res.errorCode) {
      Message({
        message: res.description || 'error',
        type: 'error',
        duration: 5 * 1000
      })

      if (res.errorCode === 4002) {
        // 检测到登入失效跳转登录页面
        // store.dispatch('user/resetToken').then(() => {
        //   location.reload()
        // })
      } else if (res.errorCode === 4001) {
        // 用户没有权限
      }
      return Promise.reject(res.description || 'error')
    } else {
      return res
    }
  },
  error => {
    console.log('err' + error) // for debug
    if (error.message !== '取消重复请求') {
      Message({
        message: error.message,
        type: 'error',
        duration: 5 * 1000,
        customClass: 'messageIndex'
      })
    }
    return Promise.reject(error)
  }
)

export default {
  fetchGet(url, params = {}) {
    if (OPEN_AES) {
      params = Encrypt(JSON.stringify(params))
    }
    return new Promise((resolve, reject) => {
      service.get(url, { params }).then(response => {
        resolve(response)
      }).catch(error => {
        reject(error)
      })
    })
  },
  fetchPost(url, params = {}) {
    if (OPEN_AES && url !== '/uploadWatermark' && url !== '/uploadoss' && url !== '/deleteoss') {
      params = Encrypt(JSON.stringify(params))
    }
    return new Promise((resolve, reject) => {
      service.post(url, params).then(res => {
        resolve(res)
      }).catch(error => {
        reject(error)
      })
    })
  }
}
