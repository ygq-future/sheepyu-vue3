import { defineStore } from 'pinia'
import { IdEnum } from '@/stores/storeId'
import { StorePersistKey } from '@/stores/storePersistKey'
import type { RouteLocationNormalized, RouteRecordRaw } from 'vue-router'

interface NavTabs {
  //当前激活路由下标
  activeIndex: number
  //当前激活路由对象
  activeRoute: RouteLocationNormalized | null
  //topBar上面显示的标签
  tabsView: RouteLocationNormalized[]
  tabFullScreen: boolean
  //用于右侧菜单渲染
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

  function closeTab(route: RouteLocationNormalized): number {
    const index = findTab(route)
    if (index >= 0) {
      state.tabsView.splice(index, 1)
    }
    return index
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
      let len = state.tabsView.length
      state.activeRoute = state.tabsView[len - 1]
      state.activeIndex = len - 1
    }
  }

  function updateActiveTabIndex() {
    const index = findTab(state.activeRoute!)
    if (index >= 0) {
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

  const getMenuTree = (): RouteRecordRaw[] => state.tabsViewRoutes

  const getTabsView = (): RouteLocationNormalized[] => state.tabsView

  return {
    state,
    addTab,
    closeTab,
    setActiveTab,
    closeTabs,
    hasRoute,
    setRoutes,
    clearRoute,
    getMenuTree,
    updateActiveTabIndex,
    getTabsView
  }
}, {
  persist: {
    key: StorePersistKey.TABS_STORE_KEY,
    paths: ['state.tabFullScreen']
  }
})
