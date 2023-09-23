<template>
  <template v-for='menu in menus'>
    <template v-if='menu.children && menu.children.length > 0'>
      <el-sub-menu :index='menu.meta?.fullpath' :key='menu.meta?.fullpath'>
        <template #title>
          <Icon :name='menu.meta?.icon ? menu.meta.icon : config.layout.asideDefaultIcon' />
          <span>{{ menu.meta?.title }}</span>
        </template>
        <menu-tree :menus='menu.children'></menu-tree>
      </el-sub-menu>
    </template>
    <template v-else>
      <el-menu-item :index='menu.meta?.fullpath' :key='menu.meta?.fullpath' @click='collapseMenu'>
        <Icon :name='menu.meta?.icon ? menu.meta.icon : config.layout.asideDefaultIcon' />
        <span>{{ menu.meta?.title }}</span>
      </el-menu-item>
    </template>
  </template>
</template>

<script setup lang='ts'>
import type { RouteRecordRaw } from 'vue-router'
import { useConfig } from '@/stores/config/config'
import { closeShade } from '@/util/pageShade'

const config = useConfig()

const props = defineProps<{
  menus: RouteRecordRaw[]
}>()

const collapseMenu = () => {
  if (config.layout.shrink) {
    closeShade()
    config.collapseMenu()
  }
}
</script>
