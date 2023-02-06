<template>
  <el-main class='layout-main'>
    <el-scrollbar class='layout-main-scrollbar'>
      <router-view v-slot='{Component}'>
        <transition :name='config.layout.pageAnimation' mode='out-in'>
          <keep-alive :include='state.keepAliveList'>
            <component :is='Component' />
          </keep-alive>
        </transition>
      </router-view>
    </el-scrollbar>
  </el-main>
</template>

<script setup lang='ts'>
import type { RouteLocationNormalized } from 'vue-router'
import { useRoute } from 'vue-router'
import { useConfig } from '@/stores/config/config'
import { useTabs } from '@/stores/tabs/tabs'
import { getCurrentInstance } from 'vue'

const instance = getCurrentInstance()
const route = useRoute()
const config = useConfig()
const tabs = useTabs()
const state = reactive<{
  keepAliveList: string[]
}>({
  keepAliveList: []
})

function addKeepAlive(route: RouteLocationNormalized) {
  if (!route) return
  if (!route.meta.keepalive) return
  const name: string | undefined = route.name?.toString()
  if (!name || state.keepAliveList.includes(name)) return
  state.keepAliveList.push(name)
}

function removeKeepAlive(route: RouteLocationNormalized) {
  if (!route.meta.keepalive) return
  const name: string | undefined = route.name?.toString()
  state.keepAliveList = state.keepAliveList.filter(item => item != name)
}

watch(() => route.path, () => {
  addKeepAlive(tabs.state.activeRoute as RouteLocationNormalized)
})

onBeforeMount(() => {
  instance?.proxy?.$bus.on('onTabRefresh', (route: RouteLocationNormalized) => {
    removeKeepAlive(route)
    nextTick(() => {
      addKeepAlive(route)
    })
  })

  instance?.proxy?.$bus.on('onTabClose', removeKeepAlive)
})

onMounted(() => {
  addKeepAlive(tabs.state.activeRoute as RouteLocationNormalized)
})

onUnmounted(() => {
  instance?.proxy?.$bus.off('onTabRefresh')
  instance?.proxy?.$bus.off('onTabClose')
})
</script>

<style scoped lang='scss'>
.layout-main {
  width: 100%;
  height: 100%;
  padding: 0 !important;
  overflow-x: hidden;
  border-radius: var(--el-border-radius-base);
}

.layout-main-scrollbar {
  width: 100%;
  overflow-x: hidden;
}
</style>
