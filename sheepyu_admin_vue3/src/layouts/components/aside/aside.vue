<template>
  <el-aside :class="[`layout-aside-${config.layout.layoutMode}`, config.layout.shrink ? 'shrink' : '']">
    <Logo />
    <el-scrollbar class='aside-scrollbar'>
      <el-menu router
               :collapse-transition='false'
               :unique-opened='config.layout.asideAccordion'
               :collapse='config.layout.asideCollapse'
               :default-active='route.path'
               :default-openeds='[route.path]'
      >
        <el-menu-item @click='collapseMenu' :index='homeRoute.meta.fullpath'>
          <Icon :name='homeRoute.meta.icon ? homeRoute.meta.icon : config.layout.asideDefaultIcon'></Icon>
          <span>{{ homeRoute.meta.title }}</span>
        </el-menu-item>

        <MenuTree :menus='tabs.state.tabsViewRoutes' />
      </el-menu>
    </el-scrollbar>
  </el-aside>
</template>

<script setup lang='ts'>
import Logo from '@/layouts/components/aside/logo.vue'
import MenuTree from '@/layouts/components/aside/menuTree.vue'
import { homeRoute } from '@/router/static'

import { useConfig } from '@/stores/config/config'
import { useRoute } from 'vue-router'
import { closeShade } from '@/util/pageShade'
import { useTabs } from '@/stores/tabs/tabs'

const route = useRoute()
const config = useConfig()
const tabs = useTabs()
const menuWidth = computed(() => config.menuWidth())
const asideBackColor = computed(() => config.getColor('asideBackColor'))
const asideTextColor = computed(() => config.getColor('asideTextColor'))
const asideActiveBackColor = computed(() => config.getColor('asideActiveBackColor'))
const asideActiveTextColor = computed(() => config.getColor('asideActiveTextColor'))
const collapseMenu = () => {
  if (config.layout.shrink) {
    closeShade()
    config.collapseMenu()
  }
}
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

.el-menu-item .icon {
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
