/**
 * 作者 liuwanxu 19/07/02.
 */

/**
 * @param {string} path
 * @returns {Boolean}
 */
export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * 作者 liuwanxu 19/07/02.
 */

/**
 * @param {string} path
 * @returns {Boolean}
 */
export function validUsername(str) {
  const IS_PHONE = /^[1]([3-9])[0-9]{9}$/
  return IS_PHONE.test(str)
}
// 验证用户密码
export const userPassword = [{
  required: true,
  validator: (rule, value, callback) => {
    const IS_PASSWORD = /^([a-zA-Z0-9]|[._]){6,16}$/
    if (value === '') {
      callback(new Error('请输入密码'))
    } else if (!IS_PASSWORD.test(value)) {
      callback(new Error('6-16位，可包含字母（区分大小写）、数字和符号'))
    } else {
      callback()
    }
  },
  trigger: 'blur'
}]
export const vstring = [
  { required: true, message: '请输入', trigger: 'blur' }
]
export const vchange = [
  { required: true, message: '请选择', trigger: 'change' }
]
export const vdate = [
  { type: 'date', required: true, message: '请选择时间', trigger: 'change' }
]
export const vphone = [
  // /^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$/
  { required: true, validator: (rule, value, callback) => {
    const IS_PHONE = /^[1]([3-9])[0-9]{9}$/
    if (value === '' || typeof (value) === 'undefined') {
      callback(new Error('请输入手机号'))
    } else if (!IS_PHONE.test(value)) {
      callback(new Error('请输入正确的手机号'))
    } else {
      callback()
    }
  }, trigger: 'blur' }
]

/**
 * 完整的域名校验
 */
export function webSite(s) {
  const web = /[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\.?/
  return web.test(s)
}
