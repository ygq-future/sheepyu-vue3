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

    <el-dropdown trigger='click' style='height: 100%'>
      <div class='menu-item'>
        <Icon name='el-icon-Operation' :color='topTextColor'></Icon>
      </div>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item :disabled="config.layout.size === 'small'" @click="changeLayoutSize('small')">small
          </el-dropdown-item>
          <el-dropdown-item :disabled="config.layout.size === 'default'" @click="changeLayoutSize('default')">default
          </el-dropdown-item>
          <el-dropdown-item :disabled="config.layout.size === 'large'" @click="changeLayoutSize('large')">large
          </el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>

    <div class='menu-item'>
      <el-popover
        :offset='10'
        placement='bottom-start'
        :width='240'
        trigger='click'
      >
        <template #reference>
          <div class='user-icon'>
            <el-avatar :size='25' :src='admin.state.avatar' />
            <span class='user-name'>{{ admin.state.nickname }}</span>
          </div>
        </template>

        <div class='user-info'>
          <el-avatar :size='60' :src='admin.state.avatar' />
          <div class='user-other'>
            <span>{{ admin.state.nickname }}</span>
            <span>{{ admin.state.loginTime }}</span>
          </div>
          <div class='user-footer'>
            <el-button type='primary' plain v-blur>个人资料</el-button>
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
import { useAdmin } from '@/stores/user/user'

const router = useRouter()
const config = useConfig()
const admin = useAdmin()
const state = reactive<{
  screenFull: boolean
}>({
  screenFull: false
})

const topTextColor = computed(() => config.getColor('topTextColor'))

function logout() {
  admin.clear()
  router.push('/login')
}

function onShowConfig() {
  config.layout.showConfig = true
}

function changeLayoutSize(size: 'small' | 'default' | 'large') {
  config.layout.size = size
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
