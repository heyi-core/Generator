<#if project.login == true >
// 登录相关接口
export const login = {
  sysUserLoginByPassword: '/login/loginByPassword'
}
</#if>
<#if project.oss == true >
// 文件上传上传
export const upload = {
  uploadWatermark: '/uploadWatermark',
  uploadFile: '/uploadoss',
  deleteFile: '/deleteoss'
}
</#if>
<#list tables as table>
<#if table.tableNameLower == 'sysdictionary'>
// 字典管理
export const sysDictionaries = {
  sysDictionaryPages: '/sysdictionary/pages',
  sysDictionariesgetAll: '/sysdictionary/getAll',
  sysDictionariesadd: '/sysdictionary/add',
  sysDictionariesupdate: '/sysdictionary/update',
  sysDictionariesdetail: '/sysdictionary/detail',
  sysDictionarydelete: '/sysdictionary/delete'
}
<#elseif table.tableNameLower == 'sysuser'>
// 人员管理
export const sysuser = {
  sysUserallPages: '/sysuser/allPages',
  sysUserGetAllSysUserByPages: '/sysuser/getAllSysUserByPages',
  sysUserManyPeopleTransfer: '/sysuser/manyPeopleTransfer',
  sysUserpages: '/sysuser/pages',
  sysUseradd: '/sysuser/add',
  sysUserupdateDefault: '/sysuser/update',
  sysUserupdate: '/sysuser/update',
  sysUserdetail: '/sysuser/detail',
  sysUserdelete: '/sysuser/delete',
  sysUserupdatePassword: '/sysuser/updatePassword',
  sysUserleaveOffice: '/sysuser/leaveOffice',
  sysUserBusinessTransfer: '/sysuser/businessTransfer',
  getBySysUserPhone: '/sysuser/getBySysUserPhone',
  getByCustomeruser: '/sysrole/getBySysUser',
  findPassword: '/sysuser/foundPassword'
}
<#elseif table.tableNameLower == 'sysrole'>
// 角色管理
export const sysrole = {
  sysRolepages: '/sysrole/pages',
  sysRoleadd: '/sysrole/add',
  sysRoleupdate: '/sysrole/update',
  sysRoledetail: '/sysrole/detail',
  sysRoledelete: '/sysrole/delete',
  getchildsByRoleId: '/sysrole/getchildsByRoleId'
}
<#else>
// ${table.tableComment}相关接口
export const ${table.tableNameLowerCamel} = {
  ${table.tableNameLowerCamel}Pages: '/${table.tableNameLower}/pages',
  ${table.tableNameLowerCamel}GetAll: '/${table.tableNameLower}/getAll',
  ${table.tableNameLowerCamel}Add: '/${table.tableNameLower}/add',
  ${table.tableNameLowerCamel}Delete: '/${table.tableNameLower}/delete',
  ${table.tableNameLowerCamel}Update: '/${table.tableNameLower}/update'
}
</#if>
</#list>
