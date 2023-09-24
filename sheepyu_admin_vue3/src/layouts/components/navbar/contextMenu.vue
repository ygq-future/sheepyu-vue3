<template>
  <transition name='el-zoom-in-center'>
    <div
      class='menu'
      :style='`top: ${state.axis.y + 5}px;left: ${state.axis.x - 14}px;width:${width}px`'
      :key='Math.random()'
      v-show='state.show'
    >
      <div
        v-for='(item, idx) in items'
        class='menu-item'
        :key='idx'
        :class='item.disabled ? "disabled" : ""'
        @click='onItemClick(item)'
      >
        <MyIcon :size='14' :name="item.icon ? item.icon : 'el-icon-Plus'" />
        <span>{{ item.label }}</span>
      </div>
    </div>
  </transition>
</template>

<script setup lang='ts'>
import type { RouteLocationNormalized } from 'vue-router'
import type { Axis, ContextMenuItem, ContextmenuItemClickEmitArg } from '@/layouts/components/navbar/interface'
import { onMounted, reactive } from 'vue'
import { useEventListener } from '@vueuse/core'

const props = withDefaults(defineProps<{
  width?: number
  items: ContextMenuItem[]
}>(), {
  width: 150,
  items: () => []
})

const emits = defineEmits<{
  (e: 'itemClick', item: ContextmenuItemClickEmitArg): void
}>()

const state: {
  show: boolean
  axis: Axis
  menu: RouteLocationNormalized | undefined
  arrowAxis: number
} = reactive({
  show: false,
  axis: {
    x: 0,
    y: 0
  },
  menu: undefined,
  arrowAxis: 10
})

const onShow = (menu: RouteLocationNormalized, axis: Axis) => {
  state.menu = menu
  state.axis = axis
  state.show = true
}

const onItemClick = (item: ContextmenuItemClickEmitArg) => {
  if (item.disabled) return
  item.menu = toRaw(state.menu)
  emits('itemClick', item)
}

const onHide = () => {
  state.show = false
}

defineExpose({
  onShow,
  onHide
})

onMounted(() => {
  useEventListener(document, 'click', onHide)
})
</script>

<style scoped lang='scss'>
.menu {
  z-index: 9999;
  position: absolute;
  background-color: var(--el-bg-color);
  border-radius: var(--el-border-radius-base);
  box-shadow: var(--el-box-shadow-light);
  font-size: 13px;
  padding: 8px 0;

  &::before {
    position: absolute;
    top: -5px;
    left: 12px;
    content: "";
    display: inline-block;
    width: 10px;
    height: 10px;
    transform: rotate(45deg);
    background-color: var(--el-bg-color);
    border-radius: 2px;
  }
}

.menu-item {
  position: relative;
  display: flex;
  align-items: center;
  width: 100%;
  height: 38px;
  padding: 8px 20px;
  user-select: none;
  cursor: pointer;
  box-sizing: border-box;

  .icon {
    margin-right: 5px;
  }

  &:not(.disabled):hover {
    background-color: var(--el-color-primary-light-9);
    color: var(--el-color-primary);
  }
}

.disabled {
  cursor: not-allowed;
  color: gray;
}
</style>
