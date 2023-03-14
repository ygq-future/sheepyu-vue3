import type { RouteLocationNormalized, RouteRecordRaw } from 'vue-router'
import { createRouter, createWebHistory } from 'vue-router'
import { staticRoutes } from '@/router/static'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import type { SystemMenuRespVo } from '@/api/system/menu'
import { userMenu } from '@/api/system/menu'
import { useAdmin } from '@/stores/user/user'
import { useTabs } from '@/stores/tabs/tabs'
import { MenuTypeEnum } from '@/enums/MenuTypeEnum'

const componentViews = import.meta.glob('@/views/**/*.vue')
const router = createRouter({
  history: createWebHistory(import.meta.env.VITE_APP_BASE_ROUTER),
  routes: staticRoutes
})

const whiteList: Array<string> = ['/login']

router.beforeEach(async (to, from, next) => {
  NProgress.configure({ showSpinner: false })
  NProgress.start()
  const admin = useAdmin()
  const tabs = useTabs()

  //没有token
  if (!admin.hasToken()) {
    if (whiteList.includes(to.path)) {
      return next()
    }
    return next('/login?redirectUrl=' + to.fullPath)
  }

  //菜单路由是否更新了
  if (tabs.hasRoute()) {
    handlerTitle(to)
    return next()
  }

  const { data } = await userMenu()
  //从菜单中抽取permissions并保存
  const permission: string[] = []
  getPermissionFromMenuTree(data, permission)
  admin.setPermissions(permission)

  //添加动态路由
  const routes: RouteRecordRaw[] = []
  generateRoutes(routes, data, null, null)
  routes.forEach(route => router.addRoute('layout', route))
  router.addRoute({ path: '/:path(.*)*', redirect: '/404' })
  tabs.setRoutes(routes)
  next({ ...to, replace: true })
})

router.afterEach((to, from, failure) => {
  NProgress.done()
})

function generateRoutes(routes: RouteRecordRaw[], menuTree: SystemMenuRespVo[], parentMenu: SystemMenuRespVo | null, parentRoute: RouteRecordRaw | null) {
  if (!menuTree || menuTree.length === 0) return

  for (let menu of menuTree) {
    //不是目录也不是菜单
    if (menu.type === MenuTypeEnum.BUTTON) continue

    let component
    let path = menu.path
    let fullpath = menu.parentId === 0 ? `/${path}` : path
    let name = path
    if (parentRoute) {
      fullpath = `${parentRoute.meta?.fullpath}/${fullpath}`
      name = `${parentRoute.name?.toString()}-${name}`
    }
    if (menu.type === MenuTypeEnum.MENU) {
      component = componentViews[`/src/views/${menu.component}.vue`]
    }
    if (!path) continue

    const route: RouteRecordRaw = {
      name,
      path,
      component,
      children: [],
      meta: {
        title: menu.name,
        icon: menu.icon,
        keepalive: menu.keepAlive === 1,
        fullpath: fullpath!
      }
    }

    if (!parentRoute) {
      routes.push(route)
      generateRoutes(routes, menu.children || [], menu, route)
      continue
    }

    if (parentRoute.children) {
      parentRoute.children.push(route)
    } else {
      parentRoute.children = [route]
    }
    generateRoutes(routes, menu.children || [], menu, route)
  }
}

function getPermissionFromMenuTree(menuTree: SystemMenuRespVo[], permissions: string[]) {
  menuTree.forEach(menu => {
    if (menu.permission) {
      permissions.push(menu.permission)
    }
    if (menu.children) {
      getPermissionFromMenuTree(menu.children, permissions)
    }
  })
}

function handlerTitle(to: RouteLocationNormalized) {
  let title = to.meta.title
  let param
  const reg = new RegExp('{(.*)}')
  if (title && (param = reg.exec(title))) {
    param = to.params[param[1]]
    if (param) {
      title = title.replace(reg, `${param}`)
      to.meta.title = title
    }
  }
  document.title = title || 'SYAdmin'
}

export default router
