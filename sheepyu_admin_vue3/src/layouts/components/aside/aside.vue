<template>
  <el-aside :class="[`layout-aside-${config.layout.layoutMode}`, config.layout.shrink ? 'shrink' : '']">
    <Logo />
    <el-scrollbar class='aside-scrollbar' :max-height='scrollHeight'>
      <el-menu router
               :collapse-transition='false'
               :unique-opened='config.layout.asideAccordion'
               :collapse='config.layout.asideCollapse'
               :default-active='route.path'
               :default-openeds='[route.path]'
      >
        <MenuTree :menus='tabs.state.tabsViewRoutes' />
      </el-menu>
    </el-scrollbar>
  </el-aside>
</template>

<script setup lang='ts'>
import Logo from '@/layouts/components/aside/logo.vue'
import MenuTree from '@/layouts/components/aside/menuTree.vue'

import { useConfig } from '@/stores/config/config'
import { useRoute } from 'vue-router'
import { useTabs } from '@/stores/tabs/tabs'

const route = useRoute()
const config = useConfig()
const tabs = useTabs()
const menuWidth = computed(() => config.menuWidth())
const asideBackColor = computed(() => config.getColor('asideBackColor'))
const asideTextColor = computed(() => config.getColor('asideTextColor'))
const asideActiveBackColor = computed(() => config.getColor('asideActiveBackColor'))
const asideActiveTextColor = computed(() => config.getColor('asideActiveTextColor'))

const scrollHeight = computed(() => {
  //视图高度-logo高度-布局占用高度
  return window.innerHeight - (config.layout.logoShow ? 50 : 0) - (config.layout.layoutMode === 'default' ? 32 : 0)
})
</script>

<style scoped lang='scss'>
.shrink {
  position: fixed;
  top: 0;
  left: 0;
  z-index: 99999;
}

.aside-scrollbar {
  background: v-bind(asideBackColor);
  user-select: none;
}

.layout-aside-classic {
  width: v-bind(menuWidth);
  height: 100vh;
  margin: 0;
  background-color: v-bind(asideBackColor);
  box-shadow: var(--el-box-shadow-light);
  border-radius: var(--el-border-radius-base);
  overflow: hidden;
  transition: width 0.3s ease;
}

.layout-aside-default {
  height: calc(100vh - 32px);
  width: v-bind(menuWidth);
  margin: 16px 0 16px 16px;
  background-color: v-bind(asideBackColor);
  box-shadow: var(--el-box-shadow-light);
  border-radius: var(--el-border-radius-base);
  overflow: hidden;
  transition: width 0.3s ease;
}

:deep(.el-menu-item .icon) {
  margin-right: 5px;
}

:deep(.el-sub-menu__title .icon) {
  margin-right: 5px;
}

:deep(.el-menu) {
  border-right: none;
  --el-menu-bg-color: v-bind(asideBackColor);
  --el-menu-text-color: v-bind(asideTextColor);
  --el-menu-active-color: v-bind(asideActiveTextColor);

  .el-menu-item.is-active {
    background-color: v-bind(asideActiveBackColor);
  }
}
</style>
