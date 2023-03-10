<template>
  <el-table-column
    v-if='(!column.render && !column.dictRender) || column.render === "text"'
    :label='column.label'
    :prop='column.prop'
    :width='column.width'
    :align='column.align || "center"'
    :sortable='column.sortable'
    :show-overflow-tooltip='column.showTip === undefined || column.showTip'
  />

  <el-table-column
    v-if='column.dictRender && column.dictType'
    :label='column.label'
    :prop='column.prop'
    :width='column.width'
    :align='column.align || "center"'
    :sortable='column.sortable'
    :show-overflow-tooltip='column.showTip === undefined || column.showTip'
  >
    <template #default='scope'>
      <Dict v-if='column.dictRender === "tag"'
            :value='scope.row[column.prop]'
            :render='column.dictRender'
            :type='column.dictType'
      />
      <Dict v-if='column.dictRender === "switch"'
            v-model='scope.row[column.prop]'
            :render='column.dictRender'
            :type='column.dictType'
            @change='(val) => onFieldChange(scope.row, val)'
      />
    </template>
  </el-table-column>

  <el-table-column
    v-if='column.render === "icon"'
    :label='column.label'
    :prop='column.prop'
    :width='column.width'
    :align='column.align || "center"'
    :sortable='column.sortable'
    :show-overflow-tooltip='column.showTip === undefined || column.showTip'
  >
    <template #default='scope'>
      <Icon :name='scope.row[column.prop]' />
    </template>
  </el-table-column>

</template>

<script setup lang='ts'>
import type { ColumnConfig } from '@/components/table/interface'

defineProps<{
  column: ColumnConfig
}>()

const emits = defineEmits<{
  (e: 'field-change', row: any, val: number): void
}>()

function onFieldChange(row: any, val: number) {
  emits('field-change', row, val)
}

</script>

<style scoped lang='scss'>

</style>
