<template>
  <div class='cell'>
    <div class='cell-text' v-show='!visible' @click='show'>{{ props.modelValue }}</div>
    <div v-show='visible'>
      <el-input ref='textRef' @blur='visible = false' v-if='render === "text"' v-model='value' />
      <el-input-number ref='numRef' @blur='visible = false' v-if='render === "number"' v-model='value' />
      <el-select
        ref='selectRef'
        v-if='render === "select"'
        v-model='value'
        @visible-change='visibleChange'
      >
        <el-option
          v-for='(item, idx) in props.data'
          :label='item[props.props.label]'
          :value='item[props.props.value]'
          :key='idx' />
      </el-select>
    </div>
  </div>
</template>

<script setup lang='ts'>
//为了解决表格大量表单卡顿
import type { WritableComputedRef } from '@vue/reactivity'
import { ElInput, ElInputNumber, ElSelect } from 'element-plus'

const props = withDefaults(defineProps<{
  modelValue: string | number
  render?: 'text' | 'select' | 'number'
  //select时渲染的数据
  data?: any[]
  props?: { label: string, value: string }
}>(), {
  render: 'text',
  props: () => ({
    label: 'label',
    value: 'value'
  }),
  modelValue: ''
})

const emits = defineEmits<{
  (e: 'update:modelValue', modelValue: string | number): void
}>()

const value: WritableComputedRef<string | number> = computed({
  set(value) {
    emits('update:modelValue', value)
  },
  get() {
    return props.modelValue
  }
})

const textRef = ref<InstanceType<typeof ElInput>>()
const numRef = ref<InstanceType<typeof ElInputNumber>>()
const selectRef = ref<InstanceType<typeof ElSelect>>()
const visible = ref<boolean>(false)
const selectVisible = ref<boolean>(false)

function show() {
  visible.value = true
  textRef.value && textRef.value.focus()
  numRef.value && numRef.value.focus()
  selectRef.value && selectRef.value?.toggleMenu()
}

function visibleChange(value: boolean) {
  selectVisible.value = value
  if (!selectVisible.value) {
    setTimeout(() => {
      visible.value = false
    }, 100)
  }
}
</script>

<style scoped lang='scss'>
.cell {
  width: 100%;

  .cell-text {
    height: 30px;
    line-height: 30px;
    border: 0.5px solid var(--el-color-info);
    border-radius: 5px;
  }
}
</style>