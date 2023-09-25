<template>
  <el-tag v-if="render === 'tag'" :type="tagItem.colorType || ''">{{ tagItem.label }}</el-tag>

  <el-switch
    v-model='state.value'
    v-if="render === 'switch'"
    :active-value='1'
    :inactive-value='0'
    :loading='state.switchLoading'
    :disabled='disabled'
    @change='onValueChange'
  />

  <el-select
    v-if="render === 'select'"
    v-model='state.value'
    :clearable='clearable'
    filterable
    :disabled='disabled'
    @change='onValueChange'
  >
    <el-option v-for='item in state.dictList' :key='item.id' :value='valueOf(item.value)' :label='item.label' />
  </el-select>

  <el-radio-group
    v-if='render === "radio"'
    v-model='state.value'
    :disabled='disabled'
    @change='onValueChange'
  >
    <el-radio border v-for='item in state.dictList' :label='valueOf(item.value)' :key='item.id'>
      {{ item.label }}
    </el-radio>
  </el-radio-group>

  <el-checkbox-group
    v-if='render === "checkbox"'
    v-model='state.value'
    :disabled='disabled'
    @change='onValueChange'
  >
    <el-checkbox v-for='item in state.dictList' :key='item.id' :label='valueOf(item.value)'>
      {{ item.label }}
    </el-checkbox>
  </el-checkbox-group>
</template>

<script setup lang='ts'>
import type { DictTypeEnum } from '@/enums/DictTypeEnum'
import type { SystemDictDataRespVo } from '@/api/system/dict'
import { useDict } from '@/stores/dict/dict'
import type { DictRender } from '@/components/dict/interface'

const dict = useDict()
const props = withDefaults(defineProps<{
  //渲染方式, 默认为tag
  render?: DictRender
  type: DictTypeEnum
  //用于表单组件的双向绑定
  modelValue?: string | number | boolean
  //用于tag组件的显示
  value?: string | number | boolean
  disabled?: boolean
  clearable?: boolean
}>(), {
  render: 'tag',
  disabled: false,
  clearable: true
})

const emits = defineEmits<{
  (e: 'update:modelValue', modelValue: number): void
  (e: 'change', value: any): void
}>()

const state = reactive<{
  value: string | number | boolean | undefined
  dictList: SystemDictDataRespVo[]
  switchLoading: boolean
}>({
  value: props.modelValue,
  dictList: dict.findDictDataByType(props.type),
  switchLoading: false
})

const tagItem = computed<SystemDictDataRespVo>(() => {
  const dictData = dict.findDictData(props.type, props.value?.toString() || '')
  if (!dictData) {
    console.warn(`没有找到对应的字典数据, type: ${props.type}, value: ${props.value}`)
    return { id: 0, dictType: 'unknown', sort: 0, label: 'unknown', value: '', status: 0 }
  }
  return dictData
})

function valueOf(value: string): number | string | boolean {
  switch (typeof props.modelValue) {
    case 'number':
      return parseInt(value)
    case 'boolean':
      return value === 'true'
    default:
      return value
  }
}

function onValueChange(value: number) {
  emits('update:modelValue', value)
  emits('change', value)
  state.switchLoading = true
  setTimeout(() => {
    state.switchLoading = false
  }, 1000)
}

watch(() => props.modelValue, value => {
  //需要改变组件内的值
  state.value = value
})
</script>

