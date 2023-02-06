<template>
  <div class='logo'>
    <el-image v-show='!config.layout.asideCollapse' :src='logoPng'></el-image>
    <span v-show='!config.layout.asideCollapse' class='title'>SYAdmin</span>
    <Icon :name="config.layout.asideCollapse ? 'el-icon-Expand' : 'el-icon-Fold'"
          :color='logoTextColor'
          :size='22'
          @click='menuCollapse' />
  </div>
</template>

<script setup lang='ts'>
import logoPng from '@/assets/logo.png'
import { useConfig } from '@/stores/config/config'
import { closeShade } from '@/util/pageShade'

const config = useConfig()
const logoTextColor = computed(() => config.getColor('logoTextColor'))

const menuCollapse = () => {
  if (config.layout.shrink) {
    closeShade()
  }
  config.collapseMenu()
}
</script>

<style scoped lang='scss'>
.logo {
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 10px;
  box-sizing: border-box;
  background-color: v-bind("config.getColor('logoBackColor')");

  .el-image {
    width: 30px;
  }

  .title {
    flex: 1;
    text-align: left;
    font-size: var(--el-font-size-extra-large);
    font-weight: 600;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    color: v-bind(logoTextColor);
    padding-left: 5px;
  }

  .icon {
    cursor: pointer;
  }
}

</style>
