<template>
  <div class='selector'>
    <el-popover
      placement='bottom'
      trigger='focus'
      :width='state.popoverWidth'
      :visible='state.popoverVisible'
    >
      <div
        class='popover-content'
        @mouseover.stop='state.iconSelectorMouseover = true'
        @mouseout.stop='state.iconSelectorMouseover = false'
      >
        <div class='content-header'>
          <span>请选择图标</span>
          <div class='types'>
          <span @click='changeType(item)' v-for='item in state.types'
                :class="state.type === item ? 'active' : ''">{{ item }}</span>
          </div>
        </div>

        <div class='content-body'>
          <el-scrollbar height='180'>
            <div v-if='renderIconNames.length > 0'>
              <div class='icon-item' @click='onIcon(item)' :key='key' v-for='(item, key) in renderIconNames'>
                <Icon :name='item' />
              </div>
            </div>
          </el-scrollbar>
        </div>
      </div>

      <template #reference>
        <el-input v-model='state.inputValue'
                  :disabled='disabled'
                  @focus='onFocus'
                  @blur='onBlur'
                  class='select-input'
                  placeholder='搜索图标'
                  ref='selectorInput'>
          <template #prepend>
            <Icon :key='`icon-${state.prependIcon}`' :name='state.prependIcon'></Icon>
            <span v-show='showIconText' style='font-size: 12px;margin-left: 5px'>{{ state.prependIcon }}</span>
          </template>
          <template #append>
            <Icon style='cursor: pointer' :size='22' name='el-icon-RefreshRight' @click='resetIcon' />
          </template>
        </el-input>
      </template>
    </el-popover>
  </div>
</template>

<script setup lang='ts'>
import { getAwesomeIconfontNames, getElementPlusIconfontNames, getIconfontNames } from '@/util/iconfont'
import { useEventListener } from '@vueuse/core'

import { useConfig } from '@/stores/config/config'

const config = useConfig()

const props = withDefaults(defineProps<{
  modelValue: string
  showIconText?: boolean
  width?: string
  disabled?: boolean
}>(), {
  modelValue: '',
  showIconText: true,
  width: '400px',
  disabled: false
})

const emits = defineEmits<{
  (e: 'update:modelValue', value: string): void
}>()

type IconType = 'ele' | 'ali' | 'awe'
const selectorInput = ref()
const state = reactive<{
  type: IconType
  types: IconType[]
  iconNames: string[]
  inputValue: string
  prependIcon: string
  popoverWidth: number
  inputFocus: boolean
  iconSelectorMouseover: boolean
  popoverVisible: boolean
  firstValue: string
}>({
  type: 'ele',
  types: ['ele', 'awe', 'ali'],
  iconNames: [],
  inputValue: '',
  prependIcon: props.modelValue || config.layout.asideDefaultIcon,
  popoverWidth: 0,
  inputFocus: false,
  iconSelectorMouseover: false,
  popoverVisible: false,
  firstValue: props.modelValue || config.layout.asideDefaultIcon
})

function getIconNames() {
  state.iconNames = []
  if (state.type == 'ele') {
    getElementPlusIconfontNames().then(res => {
      state.iconNames = res
    })
  } else if (state.type == 'awe') {
    getAwesomeIconfontNames().then(res => {
      state.iconNames = res.map(name => `fa ${name}`)
    })
  } else if (state.type == 'ali') {
    getIconfontNames().then(res => {
      state.iconNames = res.map(name => `iconfont ${name}`)
    })
  }
}

function changeType(type: IconType) {
  if (state.type == type) return
  state.type = type
  getIconNames()
}

function getInputWidth() {
  state.popoverWidth = selectorInput.value.$el.offsetWidth < 260 ? 260 : selectorInput.value.$el.offsetWidth
}

function onIcon(item: string) {
  state.prependIcon = item
  state.inputValue = ''
  emits('update:modelValue', item)
  state.popoverVisible = state.iconSelectorMouseover = false
  nextTick(() => {
    selectorInput.value.blur()
  })
}

function resetIcon() {
  onIcon(state.firstValue)
}

function onFocus() {
  state.inputFocus = state.popoverVisible = true
}

function onBlur() {
  state.inputFocus = false
  state.popoverVisible = state.iconSelectorMouseover
}

const renderIconNames = computed(() => {
  if (!state.inputValue) return state.iconNames

  let inputValue = state.inputValue.trim().toLowerCase()
  return state.iconNames.filter((icon: string) => {
    if (icon.toLowerCase().indexOf(inputValue) > 0) {
      return icon
    }
  })
})

onMounted(() => {
  getIconNames()
  useEventListener(document, 'click', () => {
    state.popoverVisible = state.inputFocus || state.iconSelectorMouseover
  })
  nextTick(() => {
    getInputWidth()
  })
})
</script>

<style scoped lang='scss'>
.selector {
  width: v-bind(width);
}

.popover-content {
  display: flex;
  flex-direction: column;

  .content-body {
    padding-top: 10px;

    .icon-item {
      display: inline-block;
      padding: 10px;
      width: 18px;
      height: 18px;
      margin: 3px;
      border: 1px solid #ececec;
      border-radius: 10px;
      cursor: pointer;

      &:hover {
        border-color: #61afff;
      }
    }
  }

  .content-header {
    display: flex;
    justify-content: space-between;

    .types {
      flex: 1;
      display: flex;
      justify-content: flex-end;

      span {
        margin-right: 10px;
        cursor: pointer;
      }

      span:last-child {
        margin-right: 0;
      }
    }
  }
}

.active {
  color: #61afff;
  text-decoration: underline;
}

:deep(.el-input-group__prepend) {
  padding: 0 10px;
}

:deep(.el-input-group__append) {
  padding: 0 10px;
}
</style>
