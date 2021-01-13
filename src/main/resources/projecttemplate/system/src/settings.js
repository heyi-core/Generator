module.exports = {

  title: '${project.artifactName}后台管理系统',

  /**
   * @type {boolean} true | false
   * @description Whether fix the header
   */
  fixedHeader: true,

  /**
   * @type {boolean} true | false
   * @description Whether show the logo in sidebar
   */
  sidebarLogo: true,

  /**
   * @type {boolean} true | false
   * @description 开启数据加密
   */
  openAes: process.env.VUE_APP_OSS_BUILD === 'production'
}
