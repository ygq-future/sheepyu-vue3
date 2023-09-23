<template>
  <el-input-number
    style='margin-right: 10px'
    v-model='firstValue'
    :step='step'
    :placeholder='placeholder'
    :min='min'
    :max='max'
  />

  <el-input-number
    v-model='lastValue'
    :step='step'
    :placeholder='placeholder'
    :min='min'
    :max='max'
  />
</template>

<script setup lang='ts'>
import type { WritableComputedRef } from '@vue/reactivity'

const props = withDefaults(defineProps<{
  modelValue: number[]
  step?: number
  placeholder?: string
  min?: number
  max?: number
}>(), {
  modelValue: () => ([]),
  step: 10,
  min: 0
})

const emits = defineEmits<{
  (e: 'update:modelValue', value: number[]): void
}>()

const firstValue: WritableComputedRef<number> = computed({
  get() {
    return props.modelValue[0]
  },
  set(value) {
    emits('update:modelValue', [value, props.modelValue[1]])
  }
})
const lastValue: WritableComputedRef<number> = computed({
  get() {
    return props.modelValue[1]
  },
  set(value) {
    emits('update:modelValue', [props.modelValue[0], value])
  }
})
</script>
