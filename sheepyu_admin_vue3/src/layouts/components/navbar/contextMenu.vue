<template>
  <transition name='el-zoom-in-center'>
    <div
      class='el-popper is-pure is-light el-dropdown__popper ba-contextmenu'
      :style='`top: ${state.axis.y + 5}px;left: ${state.axis.x - 14}px;width:${props.width}px`'
      :key='Math.random()'
      v-show='state.show'
      aria-hidden='false'
      data-popper-placement='bottom'
    >
      <ul class='el-dropdown-menu'>
        <template v-for='(item, idx) in props.items' :key='idx'>
          <li class='el-dropdown-menu__item' :class="item.disabled ? 'is-disabled' : ''" tabindex='-1'
              @click='onItemClick(item)'>
            <Icon :size='14' :name='item.icon' />
            <span>{{ item.label }}</span>
          </li>
        </template>
      </ul>
      <span class='el-popper__arrow' :style='{ left: `${state.arrowAxis}px` }'></span>
    </div>
  </transition>
</template>

<script setup lang='ts'>
import type { RouteLocationNormalized } from 'vue-router'
import type { Axis, ContextMenuItem, ContextmenuItemClickEmitArg } from '@/layouts/components/navbar/interface'
import { onMounted, reactive, toRaw } from 'vue'
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
.ba-contextmenu {
  z-index: 9999;
}

.el-popper,
.el-popper.is-light .el-popper__arrow::before {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border: none;
}

.el-dropdown-menu__item {
  padding: 8px 20px;
  user-select: none;
}

.el-dropdown-menu__item .icon {
  margin-right: 5px;
}

.el-dropdown-menu__item:not(.is-disabled) {
  &:hover {
    background-color: var(--el-dropdown-menuItem-hover-fill);
    color: var(--el-dropdown-menuItem-hover-color);
  }
}
</style>
