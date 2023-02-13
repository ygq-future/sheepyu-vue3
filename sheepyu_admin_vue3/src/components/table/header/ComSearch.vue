<template>
  <div class='search-box'>
    <el-form ref='formRef' :model='formData' @input='emits("update:modelValue", formData)'>
      <el-row>
        <template v-for='item in comSearchConfig'>
          <el-col class='p-15' :sm='6' :xs='24' v-if='!item.render || item.render === "text"'>
            <el-form-item :label='item.label' :prop='item.prop'>
              <el-input
                v-model='formData[item.prop]'
                :placeholder='item.placeholder ? item.placeholder : "关键字模糊搜索"'
                clearable
              />
            </el-form-item>
          </el-col>

          <el-col class='p-15' :sm='6' :xs='24' v-if='item.render === "number"'>
            <el-form-item :label='item.label' :prop='item.prop'>
              <el-input
                type='number'
                v-model.number='formData[item.prop]'
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
              <el-select v-model='formData[item.prop]' clearable>
                <el-option v-for='option in selectFormat(item)' :label='option.label' :value='option.id' />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col class='p-15' :sm='6' :xs='24' v-if='item.render === "dict" && item.dictType'>
            <el-form-item :label='item.label' :prop='item.prop'>
              <Dict render='select' v-model='formData[item.prop]' :type='item.dictType' />
            </el-form-item>
          </el-col>

          <el-col class='p-15' :sm='12' :xs='24' v-if='item.render === "datetime"'>
            <el-form-item :label='item.label' :prop='item.prop'>
              <el-date-picker
                v-model='formData[item.prop]'
                type='datetimerange'
                unlink-panels
                range-separator=' - '
                start-placeholder='开始日期'
                end-placeholder='结束日期'
                :shortcuts='shortcuts'
              />
            </el-form-item>
          </el-col>
        </template>

        <el-col class='p-15' :sm='6' :xs='24'>
          <div class='operate-btn'>
            <el-button v-blur @click='emits("search", formData)'>搜索</el-button>
            <el-button v-blur @click='onReset'>重置</el-button>
          </div>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script setup lang='ts'>
import type { ComSearchConfig, ComSearchConfigItem, SelectOptionItem } from '@/components/table/header/interface'
import { ElForm } from 'element-plus'

const props = defineProps<{
  comSearchConfig: ComSearchConfig
  modelValue: object
}>()

const emits = defineEmits<{
  (e: 'update:modelValue', modelValue: object): void
  (e: 'search', modelValue: object): void
  (e: 'reset'): void
}>()

const formRef = ref<InstanceType<typeof ElForm>>()
const formData = computed(() => props.modelValue)

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

const minute = 1000 * 60
const hour = 1000 * 60 * 60
const day = 1000 * 60 * 60 * 24

function getDateTimeRange(offset: number): Date[] {
  const startTime = new Date()
  const endTime = new Date(startTime.getTime() + offset)
  return [startTime, endTime]
}

const shortcuts = [
  {
    text: '最近十分钟',
    value: () => getDateTimeRange(minute * 10)
  }, {
    text: '最近半小时',
    value: () => getDateTimeRange(minute * 30)
  }, {
    text: '最近一小时',
    value: () => getDateTimeRange(hour)
  }, {
    text: '最近十小时',
    value: () => getDateTimeRange(hour * 10)
  }, {
    text: '最近半天',
    value: () => getDateTimeRange(hour / 12)
  }, {
    text: '最近一天',
    value: () => getDateTimeRange(day)
  }, {
    text: '最近三天',
    value: () => getDateTimeRange(day * 3)
  }, {
    text: '最近一周',
    value: () => getDateTimeRange(day * 7)
  }, {
    text: '最近两周',
    value: () => getDateTimeRange(day * 14)
  }, {
    text: '最近一个月',
    value: () => getDateTimeRange(day * 30)
  }, {
    text: '最近半年',
    value: () => getDateTimeRange(day * 30 * 6)
  }, {
    text: '最近一年',
    value: () => getDateTimeRange(day * 30 * 12)
  }
]
</script>

<style scoped lang='scss'>
.operate-btn {
  display: flex;
  align-items: center;
}

.search-box {
  background-color: var(--el-bg-color);
  border: 0.5px solid var(--el-border-color);
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
