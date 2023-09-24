<template>
  <el-header :class='`nav-bar-${config.layout.layoutMode}`'>
    <MyIcon
      v-if='config.layout.shrink'
      :name="config.layout.asideCollapse ? 'el-icon-Expand' : 'el-icon-Fold'"
      :size='22'
      :color='logoTextColor'
      @click='menuCollapse' />
    <NavTabs class='nav-tabs hidden-xs-only' />
    <NavMenus class='nav-menus' />
  </el-header>
</template>

<script setup lang='ts'>
import NavMenus from '@/layouts/components/navbar/navMenus.vue'
import NavTabs from '@/layouts/components/navbar/navTabs.vue'

import { useConfig } from '@/stores/config/config'
import { showShade } from '@/util/pageShade'
import MyIcon from '@/components/icon/Icon.vue'

const config = useConfig()
const logoTextColor = computed(() => config.getColor('logoTextColor'))

const menuCollapse = () => {
  if (config.layout.shrink) {
    showShade(() => {
      config.collapseMenu()
    })
  }
  config.collapseMenu()
}
</script>

<style scoped lang='scss'>
.nav-bar-classic {
  display: flex;
  justify-content: space-between;
  width: 100%;
  height: 50px;
  padding: 0;
  color: v-bind("config.getColor('topTextColor')");
  background-color: v-bind("config.getColor('topBackColor')");
  overflow: hidden;

  .nav-tabs {
    flex: 1;
  }

  .icon {
    display: flex;
    align-items: center;
    height: 100%;
    padding: 0 10px;
    user-select: none;
    cursor: pointer;
  }
}

.nav-bar-default {
  display: flex;
  width: calc(100% - 32px);
  height: 50px;
  margin: 16px 16px 0 16px;
  padding: 0;
  border-radius: var(--el-border-radius-base);
  color: v-bind("config.getColor('topTextColor')");
  overflow: hidden;

  .nav-tabs {
    flex: 1;
  }
}
</style>
