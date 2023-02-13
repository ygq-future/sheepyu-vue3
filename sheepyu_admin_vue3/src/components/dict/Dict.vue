<template>
  <el-tag v-if="render === 'tag'" :type='tagItem.colorType'>{{ tagItem.label }}</el-tag>

  <el-switch
    v-model='state.value'
    v-if="render === 'switch'"
    :active-value='1'
    :inactive-value='0'
    @change='onValueChange'
  />

  <el-select v-if="render === 'select'" v-model='state.value' clearable filterable @change='onValueChange'>
    <el-option v-for='item in dictList' :key='item.id' :value='item.value' :label='item.label' />
  </el-select>
</template>

<script setup lang='ts'>
import { DictTypeEnum } from '@/stores/dict/dictTypeEnum'
import type { SystemDictDataRespVo } from '@/api/system/dict'
import { useDict } from '@/stores/dict/dict'

const dict = useDict()
const props = withDefaults(defineProps<{
  //字典默认支持三种渲染方式, 默认为tag
  render?: 'tag' | 'switch' | 'select'
  type: DictTypeEnum
  //用于switch和select组件的双向绑定
  modelValue?: string | number
  //用于tag组件的显示
  value?: string | number
}>(), {
  render: 'tag'
})

const emits = defineEmits<{
  (e: 'update:modelValue', modelValue: number): void
  (e: 'change', value: number): void
}>()

const state = reactive<{
  value: string | number | undefined
}>({
  value: props.modelValue
})

const dictList = computed<SystemDictDataRespVo[]>(() => dict.findDictDataByType(props.type))
const tagItem = computed<SystemDictDataRespVo>(() => {
  const dictData = dict.findDictData(props.type, props.value?.toString() || '')
  if (!dictData) {
    console.warn(`没有找到对应的字典数据, type: ${props.type}, value: ${props.value}`)
    return { id: 0, dictType: 'unknown', sort: 0, label: 'unknown', value: '', status: 0 }
  }
  return dictData
})

function onValueChange(value: number) {
  emits('update:modelValue', value)
  emits('change', value)
}
</script>

<style scoped lang='scss'>

</style>
