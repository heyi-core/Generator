import router from './router'
import store from './store'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
<#if project.login == true >
import { Message } from 'heyi-ui'
import { getToken } from '@/utils/auth' // get token from cookie
<#else>

</#if>
import getPageTitle from '@/utils/get-page-title'
import '@/assets/styles/element-variables.scss'
NProgress.configure({ showSpinner: false }) // NProgress Configuration

<#if project.login == true >
const whiteList = ['/login'] // no redirect whitelist

router.beforeEach(async(to, from, next) => {
  // start progress bar
  NProgress.start()
  // set page title
  document.title = getPageTitle(to.meta.title)

  // determine whether the user has logged in
  const hasToken = getToken()
  if (hasToken) {
    if (to.path === '/login') {
      // if is logged in, redirect to the home page
      next({ path: '/' })
      NProgress.done()
    } else {
      // next()
      const hasRoles = store.getters.navRoles && store.getters.navRoles.length >= 0
      if (hasRoles) {
        next()
      } else {
        try {
        <#if project.openDictionary == true >
          await store.dispatch('dictionaries/sysDictionariesgetAll')
        </#if>
          const { navRoles } = await store.dispatch('user/getInfo')
          // return false
          const accessRoutes = await store.dispatch('permission/generateRoutes', navRoles)
          await router.addRoutes(accessRoutes)
          next({ ...to, replace: true })
        } catch (error) {
          await store.dispatch('user/resetToken')
          Message.error(error || 'Has Error')
          next(`/login?redirect=${r'${'}to.path}`)
          NProgress.done()
        }
      }
    }
  } else {
    /* has no token*/
    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next()
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      next(`/login?redirect=${r'${'}to.path}`)
      NProgress.done()
    }
  }
})
<#else>
router.beforeEach(async(to, from, next) => {
  // start progress bar
  NProgress.start()

  // set page title
  document.title = getPageTitle(to.meta.title)
  const loadRouter = store.getters.loadRouter
  if (loadRouter) {
    next()
  } else {
    try {
    <#if project.openDictionary == true >
      await store.dispatch('dictionaries/sysDictionariesgetAll')
    </#if>
      const accessRoutes = await store.dispatch('permission/generateRoutes', ['admin'])
      await router.addRoutes(accessRoutes)
      next({ ...to, replace: true })
    } catch (e) {
      next(`/404`)
      NProgress.done()
    }
  }
})
</#if>

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
