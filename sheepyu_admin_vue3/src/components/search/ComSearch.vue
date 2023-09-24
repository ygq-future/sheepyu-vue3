<template>
  <div class='search-box'>
    <el-form ref='formRef' :model='form'>
      <el-row>
        <template v-for='item in comSearchConfig'>
          <el-col class='p-15' :sm='6' :xs='24' v-if='!item.render || item.render === "text"'>
            <el-form-item :label='item.label' :prop='item.prop'>
              <el-input
                v-model='form[item.prop]'
                :placeholder='item.placeholder ? item.placeholder : "关键字模糊搜索"'
                :clearable='item.clearable === undefined || item.clearable'
              />
            </el-form-item>
          </el-col>

          <el-col class='p-15' :sm='6' :xs='24' v-if='item.render === "number"'>
            <el-form-item :label='item.label' :prop='item.prop'>
              <el-input-number
                v-model='form[item.prop]'
                :placeholder='item.placeholder ? item.placeholder : "精准搜索"'
              />
            </el-form-item>
          </el-col>

          <el-col class='p-15' :sm='6' :xs='24' v-if='item.render === "select"'>
            <el-form-item :label='item.label' :prop='item.prop'>
              <el-select v-model='form[item.prop]' :clearable='item.clearable === undefined || item.clearable'>
                <el-option v-for='option in selectFormat(item)' :label='option.label' :value='option.id' />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col class='p-15' :sm='6' :xs='24' v-if='item.render === "tree-select"'>
            <el-form-item :label='item.label' :prop='item.prop'>
              <el-tree-select
                v-model='form[item.prop]'
                default-expand-all
                check-strictly
                :node-key='item.selectIdKey'
                :data='item.selectOptions'
                :render-after-expand='false'
                :props='{label: item.selectLabelKey, value: item.selectIdKey}'
              />
            </el-form-item>
          </el-col>

          <el-col class='p-15' :sm='6' :xs='24' v-if='item.render === "dict" && item.dictType'>
            <el-form-item :label='item.label' :prop='item.prop'>
              <Dict :clearable='item.clearable' render='select' v-model='form[item.prop]' :type='item.dictType' />
            </el-form-item>
          </el-col>

          <el-col class='p-15' :sm='12' :xs='24' v-if='item.render === "datetime"'>
            <el-form-item :label='item.label' :prop='item.prop'>
              <DateTime v-model='form[item.prop]' range />
            </el-form-item>
          </el-col>

          <el-col class='p-15' :sm='12' :xs='24' v-if='item.render === "number-range"'>
            <el-form-item :label='item.label' :prop='item.prop'>
              <NumberRange v-model='form[item.prop]' :placeholder='item.placeholder' />
            </el-form-item>
          </el-col>
        </template>

        <el-col class='p-15' :sm='6' :xs='24'>
          <div class='operate-btn'>
            <el-button v-blur @click='emits("search", form)'>搜索</el-button>
            <el-button v-blur @click='onReset'>重置</el-button>
          </div>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script setup lang='ts'>
import type { ComSearchConfig, ComSearchConfigItem, SelectOptionItem } from '@/components/search/interface'
import { ElForm } from 'element-plus'
import NumberRange from '@/components/form/NumberRange.vue'
import DateTime from '@/components/datetime/DateTime.vue'

const props = defineProps<{
  comSearchConfig: ComSearchConfig
  form: Partial<any>
}>()

const emits = defineEmits<{
  (e: 'update:modelValue', modelValue: object): void
  (e: 'search', modelValue: object): void
  (e: 'reset'): void
}>()

const formRef = ref<InstanceType<typeof ElForm>>()

function onReset() {
  formRef.value?.resetFields()
  emits('reset')
}

function selectFormat(configItem: ComSearchConfigItem): SelectOptionItem[] {
  const selectOptions = configItem.selectOptions
  if (!selectOptions) {
    return []
  }
  return selectOptions.map(option => {
    let id: string | number = configItem.selectIdKey ? option[configItem.selectIdKey] : option.id
    let label: string | number = configItem.selectLabelKey ? option[configItem.selectLabelKey] : option.label
    return { id, label } as SelectOptionItem
  })
}
</script>

<style scoped lang='scss'>
.operate-btn {
  display: flex;
  align-items: center;
}

.search-box {
  background-color: var(--el-bg-color);
  border: 1px solid var(--el-border-color);
  border-bottom: none;
  padding: 13px 15px;
  box-sizing: border-box;
}

.p-15 {
  padding: 0 15px;
}

:deep(.el-range-separator) {
  font-size: 16px;
  font-weight: 700;
}
</style>
