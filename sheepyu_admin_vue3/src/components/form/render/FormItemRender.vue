<template>
  <el-form-item :prop='config.prop' :label='config.label'>
    <el-input
      v-if='(!config.render && !config.dictType && !config.dictRender)
      || config.render === "text"'
      v-model='form[config.prop]'
      :disabled='disabled'
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
      @change='(value) => config.change && config.change(value)'
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

    <!--  默认无法选中父节点  -->
    <el-tree-select
      v-if='config.render === "tree-select-checkbox"'
      v-model='form[config.prop]'
      collapse-tags
      multiple
      default-expand-all
      show-checkbox
      :node-key='config.props.value'
      :data='config.data'
      :render-after-expand='false'
      :props='config.props'
      :disabled='disabled'
    />

    <!--  可以选中父节点, 但是取消了关联选择  -->
    <el-tree
      ref='treeRef'
      v-if='config.render === "tree-checkbox"'
      show-checkbox
      default-expand-all
      check-strictly
      check-on-click-node
      :node-key='config.props.value'
      :props='config.props'
      :render-after-expand='false'
      :default-checked-keys='form[config.prop]'
      :data='config.data'
      @check-change='onCheckChange'
    />

    <DateTime v-if='config.render === "datetime"' v-model='form[config.prop]' />

    <el-input
      v-if='config.render === "textarea"'
      v-model='form[config.prop]'
      type='textarea'
      :rows='4'
      :disabled='disabled'
      :placeholder='config.placeholder'
    />

    <el-select
      v-if='config.render === "select"'
      v-model='form[config.prop]'
      clearable
      :disabled='disabled'
      :placeholder='config.placeholder'
      @change='(value) => config.change && config.change(value)'
    >
      <el-option v-for='item in config.data' :label='item[config.props.label]' :value='item[config.props.value]' />
    </el-select>

    <span class='tip' v-if='config.tip'>{{ config.tip }}</span>
  </el-form-item>
</template>

<script setup lang='ts'>
import IconSelector from '@/components/icon/IconSelector.vue'
import type { FormItemConfig } from '@/components/form/interface'
import { ElTree } from 'element-plus'

const props = defineProps<{
  config: FormItemConfig
  form: any
  disabled?: boolean
}>()

const treeRef = ref<InstanceType<typeof ElTree>>()

function onCheckChange() {
  if (treeRef.value) {
    props.form[props.config.prop] = treeRef.value.getCheckedKeys()
  }
}

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
