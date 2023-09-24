<template>
  <el-form-item :prop='config.prop' :label='config.label'>
    <el-input
      v-if='(!config.render && !config.dictType && !config.dictRender)
      || config.render === "text"'
      v-model='form[config.prop]'
      clearable
      :disabled='disabled'
      :placeholder='config.placeholder'
    />

    <el-input
      v-if='config.render === "password"'
      v-model='form[config.prop]'
      type='password'
      show-password
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
      :value='form[config.prop]'
      :type='config.dictType'
      :disabled='disabled'
      @change='(value: any) => config.change && config.change(value)'
    />

    <IconSelector
      v-if='config.render === "icon"'
      v-model='form[config.prop]'
      width='100%'
      :disabled='disabled'
    />

    <!--  多选情况下默认无法选中父节点  -->
    <el-tree-select
      v-if='config.render === "tree-select"'
      v-model='form[config.prop]'
      check-strictly
      default-expand-all
      clearable
      :collapse-tags='config.multiple'
      :multiple='config.multiple'
      :show-checkbox='config.multiple'
      :node-key="config.props?.value || 'id'"
      :data='config.data'
      :render-after-expand='false'
      :props='config.props || defaultProps'
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
      :node-key="config.props?.value || 'id'"
      :props='config.props || defaultProps'
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
      clearable
      :rows='4'
      :disabled='disabled'
      :placeholder='config.placeholder'
    />

    <el-select
      v-if='config.render === "select"'
      v-model='form[config.prop]'
      clearable
      :multiple='config.multiple'
      :disabled='disabled'
      :placeholder='config.placeholder'
      @change='(value: any) => config.change ? config.change(findSelectItem(value)) : defaultSelectChange(value)'
    >
      <el-option
        v-for='item in config.data'
        :label='item[config.props?.label || defaultProps.label]'
        :value='item[config.props?.value || defaultProps.value]'
      />
    </el-select>

    <ImageUpload v-if='config.render === "image-upload"' width='100px' v-model='form[config.prop]' />

    <Upload
      v-if='config.render === "upload"'
      v-model='form[config.prop]'
      :extend-types='config.uploadProps?.extendTypes'
      :size='config.uploadProps?.size'
    />

    <PartUpload
      v-if='config.render === "part-upload"'
      v-model='form[config.prop]'
      :extend-types='config.uploadProps?.extendTypes'
      :size='config.uploadProps?.size'
      :chunk-num='config.uploadProps?.chunkNum'
    />

    <Editor v-if='config.render === "editor"' v-model='form[config.prop]' />

    <span class='tip' v-if='config.tip'>{{ config.tip }}</span>
  </el-form-item>
</template>

<script setup lang='ts'>
import IconSelector from '@/components/icon/IconSelector.vue'
import type { FormItemConfig } from '@/components/form/interface'
import { ElTree } from 'element-plus'
import DateTime from '@/components/datetime/DateTime.vue'
import ImageUpload from '@/components/upload/ImageUpload.vue'
import Upload from '@/components/upload/Upload.vue'
import PartUpload from '@/components/upload/PartUpload.vue'

const props = defineProps<{
  config: FormItemConfig
  form: any
  disabled?: boolean
}>()

const treeRef = ref<InstanceType<typeof ElTree>>()
const defaultProps = {
  label: 'name',
  value: 'id'
}

function onCheckChange() {
  if (treeRef.value) {
    props.form[props.config.prop] = treeRef.value.getCheckedKeys()
  }
}

/**
 * select组件默认的change事件
 * @param value
 */
function defaultSelectChange(value: any) {
  let item = findSelectItem(value)
  if (props.config.props?.labelVModelKey) {
    props.form[props.config.props?.labelVModelKey] = item[props.config.props?.label || 'name']
  }
}

/**
 * select的change事件回调参数不是value值, 而是对象, 方便处理
 * @param value
 */
function findSelectItem(value: any) {
  return props.config.data.find((item: any) => item[props.config.props?.value || 'id'] === value)
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
