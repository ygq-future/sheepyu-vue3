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
                clearable
              />
            </el-form-item>
          </el-col>

          <el-col class='p-15' :sm='6' :xs='24' v-if='item.render === "number"'>
            <el-form-item :label='item.label' :prop='item.prop'>
              <el-input
                type='number'
                v-model.number='form[item.prop]'
                :placeholder='item.placeholder ? item.placeholder : "精准搜索"'
                clearable
              />
            </el-form-item>
          </el-col>

          <el-col
            class='p-15' :sm='6' :xs='24'
            v-if='item.render === "select" && checkSelectOptions(item)'
          >
            <el-form-item :label='item.label' :prop='item.prop'>
              <el-select v-model='form[item.prop]' clearable>
                <el-option v-for='option in selectFormat(item)' :label='option.label' :value='option.id' />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col class='p-15' :sm='6' :xs='24' v-if='item.render === "dict" && item.dictType'>
            <el-form-item :label='item.label' :prop='item.prop'>
              <Dict render='select' v-model='form[item.prop]' :type='item.dictType' />
            </el-form-item>
          </el-col>

          <el-col class='p-15' :sm='12' :xs='24' v-if='item.render === "datetime"'>
            <el-form-item :label='item.label' :prop='item.prop'>
              <DateTime v-model='form[item.prop]' range />
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
import type { ComSearchConfig, ComSearchConfigItem, SelectOptionItem } from '@/components/table/interface'
import { ElForm } from 'element-plus'

const props = defineProps<{
  comSearchConfig: ComSearchConfig
  form: object
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

function checkSelectOptions(configItem: ComSearchConfigItem): boolean {
  const selectOptions = configItem.selectOptions
  if (!selectOptions || selectOptions.length === 0) {
    return false
  }

  for (let option of selectOptions) {
    let id: string | number | undefined = configItem.selectIdKey ? option[configItem.selectIdKey] : option.id
    let label: string | number | undefined = configItem.selectLabelKey ? option[configItem.selectLabelKey] : option.label
    if (id === undefined || label === undefined) return false
  }

  return true
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
