const getters = {
<#if project.login == true >
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  btnRoles: state => state.user.btnRoles,
  navRoles: state => state.user.navRoles,
  userInfo: state => state.user.userInfo,
</#if>
  loadRouter: state => state.permission.loadRouter,
  permission_routes: state => state.permission.routes,
<#if project.openDictionary == true >
  dictionaries: state => state.dictionaries.dictionaries,
  getDict: state => {
    const mapList = state.dictionaries.dictionaries
    return (dictType, dictCode) => {
      if (!mapList) {
        return ''
      }
      const dictypeids = mapList.filter(function(item) {
        item.dictCode = parseInt(item.dictCode)
        dictCode = parseInt(dictCode)
        return (item.dictType === dictType) && (item.dictCode === dictCode)
      })
      return mapList && mapList.length && dictypeids.length ? dictypeids[0].dictName : ''
    }
  },
  getDicts: state => {
    const mapList = state.dictionaries.dictionaries
    return (dictType, isClear) => {
      if (!mapList) {
        return []
      }
      const dictypeids = mapList.filter((item) => {
        item.dictCode = parseInt(item.dictCode)
        return item.dictType === dictType
      })
      if (!isClear) {
        dictypeids.unshift({ dictCode: '', dictName: '请选择', dictId: '' })
      }
      return mapList && mapList.length ? dictypeids : []
    }
  },
</#if>
  sidebar: state => state.app.sidebar
}
export default getters
