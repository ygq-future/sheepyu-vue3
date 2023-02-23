<template>
  <div class='tab-wrapper'>
    <el-scrollbar>
      <div :class='`tabs-${config.layout.layoutMode}`' ref='tabsRef'>
        <div
          :class="['tab', index === tabs.state.activeIndex ? 'active' : '']"
          v-for='(item, index) in tabs.state.tabsView'
          @click='onChange(item)'
          @contextmenu.prevent='onContextmenu(item, $event)'
          :key='item.path'
        >
          <span>{{ item.meta.title }}</span>
          <Icon v-show='tabs.state.tabsView.length > 1' name='el-icon-Close' :size='12' @click.stop='onClose(item)' />
        </div>

        <div :style='navTabBackStyle' class='nav-tab-back'></div>
      </div>
    </el-scrollbar>

    <ContextMenu ref='contextMenuRef' :items='contextMenuItems' @itemClick='onItemClick' />
  </div>
</template>

<script setup lang='ts'>
import { useConfig } from '@/stores/config/config'
import { useTabs } from '@/stores/tabs/tabs'
import type { RouteLocationNormalized } from 'vue-router'
import { onBeforeRouteUpdate, useRoute, useRouter } from 'vue-router'
import type { ContextMenuItem, ContextmenuItemClickEmitArg } from '@/layouts/components/navbar/interface'
import ContextMenu from '@/layouts/components/navbar/contextMenu.vue'

const instance = getCurrentInstance()
const router = useRouter()
const route = useRoute()
const config = useConfig()
const tabs = useTabs()
const tabsRef = ref<HTMLDivElement>()
const contextMenuRef = ref()
const topMenuActiveBackColor = computed(() => config.getColor('topMenuActiveBackColor'))
const closeHoverBackColor = computed(() => {
  return config.layout.isDark ? '#2e366e' : '#c8f2fc'
})
const navTabBackStyle = reactive<{
  width: string,
  transform: string
}>({
  width: '0px',
  transform: 'translateX(0px)'
})
const contextMenuItems = reactive<ContextMenuItem[]>([
  { name: 'refresh', label: '重新加载', icon: 'el-icon-Refresh' },
  { name: 'close', label: '关闭标签', icon: 'el-icon-Close' },
  { name: 'fullScreen', label: '当前标签全屏', icon: 'el-icon-FullScreen' },
  { name: 'closeOther', label: '关闭其他标签', icon: 'el-icon-FolderRemove' },
  { name: 'closeAll', label: '关闭全部标签', icon: 'el-icon-FolderDelete' }
])

function changeNavTab() {
  nextTick(() => {
    const div = findDiv(tabs.state.activeIndex)
    if (!div) {
      return false
    }

    navTabBackStyle.width = `${div.clientWidth}px`
    navTabBackStyle.transform = `translateX(${div.offsetLeft}px)`
  })
}

function onChange(menu: RouteLocationNormalized) {
  router.push(menu)
}

function onContextmenu(menu: RouteLocationNormalized, e: MouseEvent) {
  //如果当前不是激活的标签, 禁用刷新
  contextMenuItems[0].disabled = menu.path !== tabs.state.activeRoute?.path
  //如果只剩下一个标签, 则不能关闭
  contextMenuItems[1].disabled = contextMenuItems[3].disabled = tabs.state.tabsView.length === 1

  const { clientX, clientY } = e
  contextMenuRef.value.onShow(menu, {
    x: clientX,
    y: clientY
  })
}

function onItemClick(item: ContextmenuItemClickEmitArg) {
  let menu
  if (!(menu = item.menu)) return

  switch (item.name) {
    case 'refresh':
      instance?.proxy?.$bus.emit('onTabRefresh', menu)
      break
    case 'close':
      onClose(menu)
      instance?.proxy?.$bus.emit('onTabClose', menu)
      break
    case 'fullScreen':
      if (route.path !== menu.path) {
        router.push(menu)
      }
      tabs.state.tabFullScreen = true
      break
    case 'closeOther':
      closeOther(menu)
      break
    case 'closeAll':
      closeAll()
  }
}

function closeAll() {
  //默认打开的页面
  const homePath = '/dashboard'
  if (tabs.state.activeRoute!.path == homePath) {
    tabs.closeTabs(tabs.state.activeRoute!)
    changeNavTab()
  } else {
    tabs.closeTabs()
    router.push(homePath)
  }
}

function closeOther(menu: RouteLocationNormalized) {
  tabs.closeTabs(menu)

  if (route.path !== menu.path) {
    router.push(menu)
  } else {
    changeNavTab()
  }
}

function onClose(item: RouteLocationNormalized) {
  const isActive = tabs.state.activeRoute?.path === item.path
  tabs.closeTab(item)
  instance?.proxy?.$bus.emit('onTabClose', item)

  if (isActive) {
    router.push(tabs.state.activeRoute as RouteLocationNormalized)
  } else {
    changeNavTab()
  }
}

function updateTab(menu: RouteLocationNormalized) {
  if (!menu.meta.keepalive) return

  tabs.addTab(menu)
  tabs.setActiveTab(menu)

  changeNavTab()
}

function findDiv(index: number): HTMLDivElement {
  return tabsRef.value?.children![index] as HTMLDivElement
}

onBeforeRouteUpdate((to) => {
  updateTab(to)
})

onMounted(() => {
  updateTab(router.currentRoute.value)
})
</script>

<style scoped lang='scss'>
.tab-wrapper {
  height: 50px;
  margin-right: 10px;
  z-index: 999;
  box-sizing: border-box;
  overflow: hidden;
}

.tabs-classic {
  position: relative;
  display: flex;
  $tab-height: 50px;

  .tab {
    flex-shrink: 0;
    height: $tab-height;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 0 20px;
    cursor: pointer;
    transition: 0.2s ease-in;
    margin-right: 1px;
    z-index: 999;

    span {
      font-size: 15px;
      margin-right: 5px;
      user-select: none;
    }

    .icon {
      transition: 0.3 ease-in;
      padding: 2px;
      border-radius: 50%;
    }

    &:hover {
      background-color: v-bind("config.getColor('topHoverBackColor')");
    }

    & > .icon:hover {
      background-color: v-bind(closeHoverBackColor);
      animation: icon-ani 0.3s ease-in-out;
    }
  }

  .nav-tab-back {
    position: absolute;
    height: $tab-height;
    background-color: v-bind(topMenuActiveBackColor);
    transition: all 0.2s;
    -webkit-transition: all 0.2s;
  }
}

.tabs-default {
  position: relative;
  display: flex;
  margin-right: 16px;
  $tab-height: 40px;

  .tab {
    flex-shrink: 0;
    height: $tab-height;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 0 20px;
    border-radius: var(--el-border-radius-base);
    cursor: pointer;
    transition: 0.2s ease-in;
    z-index: 999;

    span {
      font-size: 15px;
      margin-right: 5px;
      user-select: none;
    }

    .icon {
      transition: 0.3 ease-in;
      padding: 2px;
      border-radius: 50%;
    }

    & > .icon:hover {
      background-color: v-bind(closeHoverBackColor);
      animation: icon-ani 0.3s ease-in-out;
    }
  }

  .nav-tab-back {
    position: absolute;
    height: $tab-height;
    border-radius: var(--el-border-radius-base);
    background-color: v-bind(topMenuActiveBackColor);
    box-shadow: var(--el-box-shadow-light);
    transition: all 0.2s;
    -webkit-transition: all 0.2s;
  }
}

.active {
  color: v-bind("config.getColor('topMenuActiveTextColor')");
}

:deep(.el-scrollbar__bar) {
  bottom: 0;
  z-index: 1000;
}

:deep(.el-scrollbar__view) {
  height: 50px;
}

@keyframes icon-ani {
  0% {
    transform: scale(0.5);
  }
  50% {
    transform: scale(1.2);
  }
  100% {
    transform: scale(1);
  }
}
</style>
