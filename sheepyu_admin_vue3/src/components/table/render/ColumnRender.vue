<template>
  <el-table-column
    :label='column.label'
    :prop='column.prop'
    :width="column.width || ''"
    :align='column.align || "center"'
    :sortable='column.sortable === true'
    :show-overflow-tooltip='column.showTip === undefined || column.showTip'
  >
    <template #default='{row}'>
      <span v-if='(!column.render && !column.dictRender) || column.render === "text"'>
        {{ row[column.prop] }}
      </span>

      <Dict v-if='column.dictRender === "tag"  && column.dictType'
            :value='row[column.prop]'
            :render='column.dictRender'
            :type='column.dictType'
      />

      <Dict v-if='column.dictRender === "switch" && column.dictType'
            v-model='row[column.prop]'
            :render='column.dictRender'
            :type='column.dictType'
            @change='(val: any) => onFieldChange(row, val)'
      />

      <MyIcon v-if='column.render === "icon"' :name='row[column.prop]' />

      <el-link v-if='column.render === "link"' @click='linkTo(row)'>{{ row[column.prop] }}</el-link>

      <el-image
        v-if='column.render === "img"'
        style='width: 100px; height: 100px'
        preview-teleported
        hide-on-click-modal
        fit='cover'
        :src='row[column.prop]'
        :preview-src-list='[row[column.prop]]'
      />
    </template>
  </el-table-column>
</template>

<script setup lang='ts'>
import type { ColumnConfig } from '@/components/table/interface'

const router = useRouter()

const props = defineProps<{
  column: ColumnConfig
}>()

const emits = defineEmits<{
  (e: 'field-change', row: any, val: any): void
}>()

function onFieldChange(row: any, val: any) {
  emits('field-change', row, val)
}

function linkTo(row: any) {
  let path = props.column.path
  const pathProps = props.column.pathProps
  //path为空, 不跳转
  if (!path) {
    return
  }
  //没有参数, 直接跳转
  let index = path.indexOf('{}')
  if (index === -1) {
    return router.push(path)
  }
  //有参数但是没传, 不会跳转
  if (!pathProps || pathProps.length === 0) {
    return
  }
  //替换参数
  for (let prop of pathProps) {
    path = path.replace('{}', row[prop])
    index = path.indexOf('{}')
    if (index === -1) {
      break
    }
  }
  router.push(path)
}

</script>

