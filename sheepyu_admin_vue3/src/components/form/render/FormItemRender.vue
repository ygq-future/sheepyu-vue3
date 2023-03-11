<template>
  <el-form-item :prop='config.prop' :label='config.label'>
    <el-input
      v-if='(!config.render && !config.dictType && !config.dictRender)
      || config.render === "text"'
      v-model='form[config.prop]'
      :placeholder='config.placeholder'
    />

    <el-input-number
      v-if='config.render === "number"'
      v-model='form[config.prop]'
      :disabled='disabled'
      :placeholder='config.placeholder'
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

    <DateTime v-if='config.render === "datetime"' v-model='form[config.prop]' />

    <el-input
      v-if='config.render === "textarea"'
      v-model='form[config.prop]'
      type='textarea'
      :rows='4'
      :placeholder='config.placeholder'
    />

    <el-select
      v-if='config.render === "select"'
      v-model='form[config.prop]'
      clearable
      :placeholder='config.placeholder'
    >
      <el-option v-for='item in config.data' :label='item[config.props.label]' :value='item[config.props.value]' />
    </el-select>

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
