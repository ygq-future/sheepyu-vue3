<template>
  <div :class="['menus', `menus-${config.layout.layoutMode}`]">
    <el-tooltip content='首页' effect='light' :offset='0' :show-arrow='false' :show-after='600' placement='bottom'>
      <div class='menu-item' @click='toHome'>
        <Icon name='el-icon-Monitor' :color='topTextColor'></Icon>
      </div>
    </el-tooltip>

    <div class='menu-item' @click='onScreenFull'>
      <Icon name='el-icon-FullScreen' :color='topTextColor'></Icon>
    </div>

    <div class='menu-item'>
      <Icon v-if='config.layout.colorModeIndex === 0'
            name='fa fa-moon-o'
            title='暗黑模式'
            @click='changeColorMode(1)'
      />
      <Icon v-else name='fa fa-sun-o' title='明亮模式' @click='changeColorMode(0)' />
    </div>

    <div class='menu-item'>
      <el-popover
        :offset='10'
        placement='bottom-start'
        :width='240'
        trigger='hover'
      >
        <template #reference>
          <div class='user-icon'>
            <el-avatar :size='25' :src='user.get().avatar' />
            <span class='user-name'>{{ user.get().nickname }}</span>
          </div>
        </template>

        <div class='user-info'>
          <el-avatar :size='60' :src='user.get().avatar' />
          <div class='user-other'>
            <span>{{ user.get().nickname }}</span>
            <span>{{ user.get().loginTime }}</span>
          </div>
          <div class='user-footer'>
            <el-button type='primary' plain v-blur @click='$router.push("/system/user/info")'>个人资料</el-button>
            <el-button type='danger' plain v-blur @click='logout'>注销</el-button>
          </div>
        </div>
      </el-popover>
    </div>

    <div class='menu-item' @click='onShowConfig'>
      <Icon name='el-icon-Setting' :color='topTextColor' />
    </div>

    <Config />
  </div>
</template>

<script setup lang='ts'>
import { useConfig } from '@/stores/config/config'
import screenfull from 'screenfull'
import { ElMessage } from 'element-plus'
import Config from '@/layouts/components/navbar/config.vue'
import { useRouter } from 'vue-router'
import { useUser } from '@/stores/user/user'
import { useTabs } from '@/stores/tabs/tabs'

const router = useRouter()
const config = useConfig()
const user = useUser()
const tabs = useTabs()
const state = reactive<{
  screenFull: boolean
}>({
  screenFull: false
})

const topTextColor = computed(() => config.getColor('topTextColor'))

function logout() {
  user.clear()
  tabs.clearRoute()
  router.push('/login?redirectUrl=' + tabs.state.activeRoute?.fullPath)
}

function changeColorMode(colorModeIndex: number) {
  config.changeColorMode(colorModeIndex)
}

function onShowConfig() {
  config.layout.showConfig = true
}

function toHome() {
  window.open('https://gitee.com/yu-gangqiang/sheepyu-vue3', '_blank')
}

function onScreenFull() {
  if (!screenfull.isEnabled) {
    ElMessage.warning('不支持全屏功能')
    return
  }

  screenfull.toggle()
}

screenfull.onchange(() => {
  state.screenFull = screenfull.isFullscreen
})
</script>

<style scoped lang='scss'>
:deep(.el-drawer) {
  width: 310px !important;
}

:deep(.el-drawer__body) {
  padding: 0 !important;
}

:deep(.el-drawer__header) {
  margin: 0;
}

.user-info {
  display: flex;
  flex-direction: column;
  align-items: center;

  .user-other {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin: 10px 0;
  }

  .user-footer {
    width: 100%;
    display: flex;
    justify-content: space-between;
  }
}

.user-icon {
  display: flex;
  height: 100%;
  align-items: center;

  .user-name {
    display: inline-block;
    max-width: 50px;
    margin-left: 5px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

.menus {
  height: 100%;
  display: flex;
  align-items: center;
  border-radius: var(--el-border-radius-base);
  background-color: v-bind("config.getColor('topBackColor')");

  &-default {
    box-shadow: var(--el-box-shadow-light);
  }

  .menu-item {
    display: flex;
    align-items: center;
    height: 100%;
    padding: 0 10px;
    cursor: pointer;
    box-sizing: border-box;

    &:hover {
      background-color: v-bind("config.getColor('topHoverBackColor')");
    }

    &:hover > .icon {
      animation: icon-ani 0.3s ease-in-out;
    }
  }
}

@keyframes icon-ani {
  0% {
    transform: scale(0);
  }
  80% {
    transform: scale(1.2);
  }
  100% {
    transform: scale(1);
  }
}
</style>
