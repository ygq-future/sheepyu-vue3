<template>
  <el-form-item :prop='config.prop' :label='config.label'>
    <el-input
      v-if='(!config.render && !config.dictType && !config.dictRender)
      || config.render === "text"'
      v-model='form[config.prop]'
    />

    <el-input
      v-if='config.render === "number"'
      type='number'
      v-model.number='form[config.prop]'
      :disabled='disabled'
    />

    <Dict
      v-if='config.dictType && config.dictRender'
      v-model='form[config.prop]'
      :render='config.dictRender'
      :type='config.dictType'
      :disabled='disabled'
    />

    <IconSelector
      v-if='config.render === "icon"'
      v-model='form[config.prop]'
      width='100%'
      :disabled='disabled'
    />

    <el-tree-select
      v-if='config.render === "tree-select"'
      v-model='form[config.prop]'
      check-strictly
      :node-key='config.props.value'
      :data='config.data'
      :render-after-expand='false'
      :props='config.props'
      :disabled='disabled'
    />

    <span class='tip' v-if='config.tip'>{{ config.tip }}</span>
  </el-form-item>
</template>

<script setup lang='ts'>
import IconSelector from '@/components/icon/IconSelector.vue'
import type { FormItemConfig } from '@/components/form/interface'

const props = defineProps<{
  config: FormItemConfig
  form: any
  disabled?: boolean
}>()

onUnmounted(() => {
  props.form[props.config.prop] = undefined
})
</script>

<style scoped lang='scss'>
.tip {
  display: inline-block;
  width: 100%;
  color: gray;
  font-size: 12px;
  line-height: 18px;
}
</style>
