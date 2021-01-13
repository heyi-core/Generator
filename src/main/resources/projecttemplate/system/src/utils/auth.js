const TokenKey = 'vue_admin_template_token'
const UserKey = 'vue_admin_template_userinfo'

export function getToken() {
  return sessionStorage.getItem(TokenKey)
}

export function setToken(token) {
  return sessionStorage.setItem(TokenKey, token)
}

export function removeToken() {
  return sessionStorage.setItem(TokenKey, '')
}

export function getUserInfo() {
  return sessionStorage.getItem(UserKey)
}

export function setUserInfo(userInfo) {
  return sessionStorage.setItem(UserKey, userInfo)
}

export function removeUserInfo() {
  return sessionStorage.setItem(UserKey, '')
}
