<template>
  <el-table
    :data='props.data'
    :border='config.border'
    @select='onSelect'
  >
    <el-table-column
      v-for='colum in props.config.columnConfigs'
      :label='colum.label'
      :header-align='colum.align ? colum.align : "center"'
      :align='colum.align ? colum.align : "center"'
      :width='colum.width'
      :type='colum.type'
    >
      <template #default='scope' v-if='!colum.type'>
        <span v-if='!colum.render'>
          {{ scope.row[colum.prop] }}
        </span>

        <Icon v-if="colum.render === 'icon'" :name='scope.row[colum.prop]' />

        <el-switch v-if='colum.render === "switch"'
                   v-model='scope.row[colum.prop]'
                   :active-value='1'
                   :inactive-value='0'
                   @change='onFieldChange(scope.row)'
        />
      </template>
    </el-table-column>
  </el-table>
</template>

<script setup lang='ts'>
import type { ElTable } from 'element-plus'
import type { TableConfig } from '@/components/table/interface'

interface Props extends Partial<InstanceType<typeof ElTable>> {
  config: TableConfig
}

const props = defineProps<Props>()
const emits = defineEmits<{
  (e: 'fieldChange', value: object): void
  (e: 'select', selection: object[], row: object): void
}>()
const state = reactive<{}>({})

function onFieldChange(value: object) {
  emits('fieldChange', value)
}

function onSelect(selection: object[], row: object) {
  emits('select', selection, row)
}
</script>

<style scoped lang='scss'>

</style>
