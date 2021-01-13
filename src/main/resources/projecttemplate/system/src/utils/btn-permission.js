import store from '@/store'

const btnPermission = function(btnCode) {
  const btnRoles = store.getters.btnRoles
  return btnRoles.indexOf(btnCode) !== -1
}

export default btnPermission
