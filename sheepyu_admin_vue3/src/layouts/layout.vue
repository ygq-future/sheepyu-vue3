<template>
  <el-container class='layout-container'>
    <Aside v-show='!tabs.state.tabFullScreen' />
    <el-container class='content-wrapper'>
      <NavBar v-if='!tabs.state.tabFullScreen' />
      <Main />
    </el-container>
    <CloseFullScreen v-if='tabs.state.tabFullScreen' />
  </el-container>
</template>

<script setup lang='ts'>
import Aside from '@/layouts/components/aside/aside.vue'
import NavBar from '@/layouts/components/navbar/navBar.vue'
import Main from '@/layouts/components/main/main.vue'
import CloseFullScreen from '@/layouts/components/main/closeFullScreen.vue'
import { useTabs } from '@/stores/tabs/tabs'

const tabs = useTabs()
</script>

<style scoped lang='scss'>
.layout-container {
  height: 100%;
  width: 100%;
}

.content-wrapper {
  flex-direction: column;
  width: 100%;
  height: 100%;
}
</style>
