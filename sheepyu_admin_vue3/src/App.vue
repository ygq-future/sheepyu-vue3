<template>
  <el-config-provider :locale="zhCn">
    <router-view></router-view>
  </el-config-provider>
</template>

<script setup lang="ts">
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import { useConfig } from '@/stores/config/config'

//为了能在项目任何页面都有白天/暗夜适配
useConfig()
</script>

<style lang="scss">
html,
body,
#app {
  width: 100vw;
  height: 100vh;
  margin: 0;
  padding: 0;
}

html.light {
  background-color: #f5f5f5;
}

html.dark {
  --w-e-textarea-bg-color: #333;
  --w-e-textarea-color: #fff;
  --w-e-toolbar-color: #fff !important;
  --w-e-toolbar-bg-color: #333 !important;
}

.default-main {
  margin: 16px;
}

.layout-shade {
  position: fixed;
  top: 0;
  left: 0;
  height: 100vh;
  width: 100vw;
  background-color: transparent;
  z-index: 9999;
}

.el-dialog__body {
  padding-top: 0 !important;
  padding-bottom: 0 !important;
}

.el-dialog__footer {
  box-shadow: var(--el-box-shadow-light);
  padding-bottom: 15px !important;
  padding-top: 15px !important;
}

.el-dialog__header {
  padding-bottom: 15px !important;
  padding-top: 15px !important;
}
</style>
