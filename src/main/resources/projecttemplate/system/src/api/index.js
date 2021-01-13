import * as types from './api.js'
import http from '@/api/request'
const ajax = {}

for (const key in types) {
  for (const api in types[key]) {
    ajax[api] = (params, method = 'POST') => {
      return method === 'POST' ? http.fetchPost(types[key][api], params) : http.fetchGet(types[key][api], params)
    }
  }
}

export default ajax
