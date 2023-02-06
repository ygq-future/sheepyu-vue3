<template>
  <el-drawer
    title='全局配置'
    v-model='config.layout.showConfig'
    close-on-click-modal
    direction='rtl'
  >
    <el-scrollbar style='padding: 20px'>
      <div class='config'>
        <el-divider border-style='dashed'>全局配置</el-divider>
        <div class='config-list'>
          <div class='config-item'>
            <span>布局方式</span>
            <el-radio-group v-model='config.layout.layoutMode'>
              <el-radio-button label='default'>默认</el-radio-button>
              <el-radio-button label='classic'>经典</el-radio-button>
            </el-radio-group>
          </div>

          <div class='config-item'>
            <span>主题模式</span>
            <el-radio-group v-model='config.layout.colorModeIndex' @change='onColorModeChange'>
              <el-radio-button v-for='(item, index) in config.layout.colorMode' :label='index'>
                {{ item }}
              </el-radio-button>
            </el-radio-group>
          </div>

          <div class='config-item'>
            <span>页面切换动画</span>
            <el-select v-model='config.layout.pageAnimation' style='width: 150px'>
              <el-option label='el-fade-in-linear' value='el-fade-in-linear'></el-option>
              <el-option label='el-fade-in' value='el-fade-in'></el-option>
              <el-option label='el-zoom-in-center' value='el-zoom-in-center'></el-option>
              <el-option label='el-zoom-in-top' value='el-zoom-in-top'></el-option>
              <el-option label='el-zoom-in-bottom' value='el-zoom-in-bottom'></el-option>
            </el-select>
          </div>
        </div>

        <el-divider border-style='dashed'>侧边栏</el-divider>
        <div class='config-list'>
          <div class='config-item'>
            <span>侧边栏背景色</span>
            <el-color-picker v-model='config.layout.asideBackColor[config.layout.colorModeIndex]' />
          </div>

          <div class='config-item'>
            <span>侧边栏文字色</span>
            <el-color-picker v-model='config.layout.asideTextColor[config.layout.colorModeIndex]' />
          </div>

          <div class='config-item'>
            <span>侧边栏激活项背景色</span>
            <el-color-picker v-model='config.layout.asideActiveBackColor[config.layout.colorModeIndex]' />
          </div>

          <div class='config-item'>
            <span>侧边栏激活项文字色</span>
            <el-color-picker v-model='config.layout.asideActiveTextColor[config.layout.colorModeIndex]' />
          </div>

          <div class='config-item'>
            <span>侧边栏展开宽度</span>
            <el-input style='width: 120px' type='number' v-model.number='config.layout.asideWidth'>
              <template #append>
                <span style='margin: 0'>px</span>
              </template>
            </el-input>
          </div>

          <div class='config-item'>
            <span>菜单默认图标</span>
            <IconSelector
              v-model='config.layout.asideDefaultIcon'
              :show-icon-text='false'
              width='155px' />
          </div>

          <div class='config-item'>
            <span>菜单默认折叠</span>
            <el-switch v-model='config.layout.asideCollapse'></el-switch>
          </div>

          <div class='config-item'>
            <span>菜单手风琴模式</span>
            <el-switch v-model='config.layout.asideAccordion'></el-switch>
          </div>

          <div class='config-item'>
            <span>是否显示logo</span>
            <el-switch v-model='config.layout.logoShow'></el-switch>
          </div>

          <div class='config-item'>
            <span>logo栏背景颜色</span>
            <el-color-picker v-model='config.layout.logoBackColor[config.layout.colorModeIndex]' />
          </div>

          <div class='config-item'>
            <span>logo栏文字颜色</span>
            <el-color-picker v-model='config.layout.logoTextColor[config.layout.colorModeIndex]' />
          </div>
        </div>

        <el-divider border-style='dashed'>顶栏</el-divider>
        <div class='config-list'>
          <div class='config-item'>
            <span>顶栏背景色</span>
            <el-color-picker v-model='config.layout.topBackColor[config.layout.colorModeIndex]' />
          </div>

          <div class='config-item'>
            <span>顶栏文字色</span>
            <el-color-picker v-model='config.layout.topTextColor[config.layout.colorModeIndex]' />
          </div>

          <div class='config-item'>
            <span>顶栏鼠标悬浮背景色</span>
            <el-color-picker v-model='config.layout.topHoverBackColor[config.layout.colorModeIndex]' />
          </div>

          <div class='config-item'>
            <span>顶栏菜单激活背景色</span>
            <el-color-picker v-model='config.layout.topMenuActiveBackColor[config.layout.colorModeIndex]' />
          </div>

          <div class='config-item'>
            <span>顶栏菜单激活文字色</span>
            <el-color-picker v-model='config.layout.topMenuActiveTextColor[config.layout.colorModeIndex]' />
          </div>
        </div>

        <div class='config-operate'>
          <el-popconfirm title='你确定要恢复当前主题的默认设置吗?' @confirm="resetConfig('current')">
            <template #reference>
              <el-button type='info'>恢复当前默认</el-button>
            </template>
          </el-popconfirm>

          <el-popconfirm title='你确定要恢复全部主题的默认设置吗?' @confirm="resetConfig('all')">
            <template #reference>
              <el-button type='info'>恢复全部默认</el-button>
            </template>
          </el-popconfirm>
        </div>
      </div>
    </el-scrollbar>
  </el-drawer>
</template>

<script setup lang='ts'>
import { useConfig } from '@/stores/config/config'
import { useRouter } from 'vue-router'
import IconSelector from '@/components/icon/IconSelector.vue'
import { useEventListener } from '@vueuse/core'
import { StorePersistKey } from '@/stores/storePersistKey'
import { Local } from '@/util/storage'

const config = useConfig()
const router = useRouter()

function onColorModeChange(colorModeIndex: number) {
  config.changeColorMode(colorModeIndex)
}

function resetConfig(type: 'current' | 'all') {
  if (type === 'current') {
    config.resetCurrentTheme()
  } else {
    config.resetConfig()
  }
}

onBeforeMount(() => {
  useEventListener(window, 'resize', () => {
    let defaultBeforeResizeLayout = {
      layoutMode: config.layout.layoutMode,
      asideCollapse: config.layout.asideCollapse
    }

    let beforeResizeLayout = Local.get(StorePersistKey.RESIZE_BEFORE_KEY)
    if (!beforeResizeLayout) {
      Local.set(StorePersistKey.RESIZE_BEFORE_KEY, defaultBeforeResizeLayout)
    }

    if (document.body.clientWidth <= 1024) {
      config.layout.shrink = true
      config.layout.layoutMode = 'classic'
      config.layout.asideCollapse = true
    } else {
      const tempResizeLayout = beforeResizeLayout || defaultBeforeResizeLayout
      config.layout.shrink = false
      config.layout.layoutMode = tempResizeLayout.layoutMode
      config.layout.asideCollapse = tempResizeLayout.asideCollapse
    }
  })
})
</script>

<style scoped lang='scss'>
.config-list {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: flex-start;

  .config-item {
    display: flex;
    align-items: center;
    font-size: 14px;
    margin: 10px 0;

    span {
      margin-right: 10px;
    }
  }
}

.config {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

:deep(.el-input-group__append, .el-input-group__prepend) {
  padding: 0 8px;
}
</style>
