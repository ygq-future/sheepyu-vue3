<template>
  <el-date-picker
    v-model='datetime'
    :type='range ? "datetimerange" : "datetime"'
    unlink-panels
    range-separator=' - '
    start-placeholder='开始日期'
    end-placeholder='结束日期'
    value-format='YYYY-MM-DD HH:mm:ss'
    :shortcuts='range ? rangeShortcuts : shortcuts'
  />
</template>

<script setup lang='ts'>
import { rangeShortcuts, shortcuts } from '@/components/datetime/interface'
import type { WritableComputedRef } from '@vue/reactivity'

const props = withDefaults(defineProps<{
  modelValue: string | string[]
  //是否为range选择
  range?: boolean
}>(), {
  modelValue: ''
})

const emits = defineEmits<{
  (e: 'update:modelValue', modelValue: string | string[]): void
}>()

const datetime: WritableComputedRef<string | string[]> = computed({
  set(value) {
    if (!value) {
      value = props.range ? [] : ''
    }
    emits('update:modelValue', value)
  },
  get() {
    return props.modelValue
  }
})

onBeforeMount(() => {
  if (!props.modelValue) {
    emits('update:modelValue', props.range ? [] : '')
  }
})
</script>
