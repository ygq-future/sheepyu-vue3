import { defineStore } from 'pinia'
import { IdEnum } from '@/stores/storeId'
import { StorePersistKey } from '@/stores/storePersistKey'
import type { RouteLocationNormalized, RouteRecordRaw } from 'vue-router'
import type { SystemMenuRespVo } from '@/api/system/menu'

interface NavTabs {
  activeIndex: number
  activeRoute: RouteLocationNormalized | null
  tabsView: RouteLocationNormalized[]
  tabFullScreen: boolean
  tabsViewRoutes: RouteRecordRaw[]
  //是否有路由更新, 关联菜单操作
  isRouteUpdated: boolean
}

export const useTabs = defineStore(IdEnum.TABS, () => {
  const state: NavTabs = reactive({
    activeIndex: 0,
    activeRoute: null,
    tabsView: [],
    tabFullScreen: false,
    tabsViewRoutes: [],
    isRouteUpdated: true
  })

  function hasRoute() {
    return !state.isRouteUpdated
  }

  function setRoutes(routes: RouteRecordRaw[]) {
    state.isRouteUpdated = false
    state.tabsViewRoutes = routes
  }

  function clearRoute() {
    state.isRouteUpdated = true
  }

  function closeTab(route: RouteLocationNormalized) {
    const index = findTab(route)
    if (index >= 0) {
      state.tabsView.splice(index, 1)
      setActiveTab(state.activeRoute as RouteLocationNormalized)
    }
  }

  function closeTabs(route: RouteLocationNormalized | undefined = undefined) {
    if (route) {
      state.tabsView = [route]
      setActiveTab(route)
    } else {
      state.tabsView = []
    }
  }

  function addTab(route: RouteLocationNormalized) {
    const index = findTab(route)
    if (index >= 0) {
      state.tabsView[index].query = route.query
      state.tabsView[index].params = route.params
      return
    }
    state.tabsView.push(route)
  }

  function setActiveTab(route: RouteLocationNormalized) {
    const index = findTab(route)
    if (index >= 0) {
      state.activeRoute = route
      state.activeIndex = index
    } else {
      state.activeRoute = state.tabsView[0]
      state.activeIndex = 0
    }
  }

  function findTab(route: RouteLocationNormalized): number {
    for (let i = 0; i < state.tabsView.length; i++) {
      if (state.tabsView[i].path === route.path) {
        return i
      }
    }
    return -1
  }

  return { state, addTab, closeTab, setActiveTab, closeTabs, hasRoute, setRoutes, clearRoute }
}, {
  persist: {
    key: StorePersistKey.TABS_STORE_KEY,
    paths: ['state.tabFullScreen']
  }
})
