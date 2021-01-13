
import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index'),
        meta: { title: '控制台', icon: 'icon-kongzhitai' }
      }
      // {
      //   path: '/user',
      //   hidden: true,
      //   name: 'usercenter',
      //   component: () => import('@/views/usercenter/UserCenter'),
      //   meta: { title: '个人资料', icon: 'icon-icon_signal_fill' }
      // }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  }
]
export const asyncRoutes = [
<#list tables as table>
  {
    path: '/${table.tableNameLower}',
    name: '${table.tableNameUpperCamel}',
    component: Layout,
    redirect: '/${table.tableNameLower}/list',
    meta: {
      title: '${table.tableComment}',
      icon: 'icon-chanpinleimu'
    },
    children: [
      {
        path: '/${table.tableNameLower}/list',
        name: '${table.tableNameUpperCamel}List',
        component: () => import('@/views/${table.tableNameLowerCamel}/${table.tableNameUpperCamel}'),
        meta: { title: '${table.tableComment}', icon: 'icon-chanpinleimu' }
      }
    ]
  }<#if table_has_next || project.login == true >,</#if>
</#list>
<#if project.login == true >
  {
    path: '/system',
      name: 'system',
    component: Layout,
    meta: {
    title: '系统管理',
      icon: 'icon-xitongguanli'
  },
    children: [
      {
        path: '/system/sysDictionaries',
        name: 'sysDictionaries',
        component: () => import('@/views/system/dictionaries/sysDictionaries'),
        meta: { title: '字典管理', icon: 'icon-zidianguanli' }
      },
      {
        path: '/system/roles',
        name: 'roles',
        component: () => import('@/views/system/roles/sysrole'),
        meta: { title: '角色管理', icon: 'icon-jiaoseguanli' }
      },
      {
        path: '/system/sysuser',
        name: 'sysuser',
        component: () => import('@/views/system/user/userRole'),
        meta: { title: '人员管理', icon: 'icon-renyuanguanli' }
      }
    ]
  }
</#if>
]
const createRouter = () => new Router({
  // mode: 'history', // 开启需要服务器配置伪静态
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}
resetRouter()

export default router
