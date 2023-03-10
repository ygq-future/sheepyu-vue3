<template>
  <el-table
    ref='tableRef'
    border
    :data='data'
    :row-key='tableConfig.rowKey'
    :stripe='tableConfig.stripe === undefined || tableConfig.stripe'
    :tree-props='tableConfig.treeProps'
    v-loading='tableConfig.loading'
    @select='onSelect'
    @select-all='onSelectAll'
    @selection-change='onSelectionChange'
    @row-dblclick='onRowDblClick'
  >
    <el-table-column v-if='tableConfig.selection' type='selection' align='center' fixed='left' />

    <template v-for='item in tableConfig.columns'>
      <ColumnRender :column='item' @field-change='onFieldChange' />
    </template>

    <el-table-column
      v-if='tableConfig.operate'
      label='操作'
      :width='tableConfig.operate.width'
      :fixed='tableConfig.operate.fixed || "right"'
      :align='tableConfig.operate.align || "center"'
    >
      <template #default='scope'>
        <div class='btn-grp'>
          <el-tooltip
            v-if="tableConfig.operate.buttons.includes('edit')"
            content='编辑'
            placement='top'
            :show-after='500'
          >
            <el-button v-auth='`${props.auth}:update`' v-blur type='primary' @click='onEdit(scope.row)'>
              <template #icon>
                <Icon name='el-icon-Edit' />
              </template>
            </el-button>
          </el-tooltip>

          <el-popconfirm
            v-if="tableConfig.operate.buttons.includes('delete')"
            confirm-button-type='danger'
            title='确认删除这条记录吗?'
            @confirm='onDelete(scope.row)'
          >
            <template #reference>
              <div>
                <el-tooltip :show-after='500' content='删除' placement='top'>
                  <el-button v-auth='`${props.auth}:delete`' v-blur type='danger'>
                    <template #icon>
                      <Icon name='el-icon-Delete' />
                    </template>
                  </el-button>
                </el-tooltip>
              </div>
            </template>
          </el-popconfirm>

          <slot name='buttons' :data='scope.row'></slot>
        </div>
      </template>
    </el-table-column>
  </el-table>

  <div class='pagination' v-if='tableConfig.pagination'>
    <el-scrollbar>
      <el-pagination
        background
        :layout="config.layout.shrink ? 'prev, pager, next' : 'sizes, total, prev, pager, next, jumper'"
        v-model:current-page='paginationObj.current'
        v-model:page-size='paginationObj.size'
        :page-sizes='[10, 20, 30, 50, 100]'
        :total='paginationObj.total'
        @size-change='onSizeChange'
        @current-change='onCurrentChange'
      />
    </el-scrollbar>
  </div>
</template>

<script setup lang='ts'>
import ColumnRender from './render/ColumnRender.vue'
import type { TableConfig } from '@/components/table/interface'
import { ElTable } from 'element-plus'
import { useConfig } from '@/stores/config/config'

const config = useConfig()

const props = withDefaults(defineProps<{
  data: any
  tableConfig: TableConfig
  //如果开启了selection, 也可以使用双向绑定, 更加方便
  selection?: any[]
  pagination?: object
  auth?: string
}>(), {
  auth: 'none'
})

const emits = defineEmits<{
  (e: 'field-change', row: any, val: number): void
  (e: 'select', selection: Array<object>, row: object): void
  (e: 'select-all', selection: Array<object>): void
  (e: 'selection-change', selection: Array<object>): void
  (e: 'update:selection', selection: Array<object>): void
  (e: 'update:pagination', obj: object): void
  (e: 'edit', row: any): void
  (e: 'delete', row: any): void
  (e: 'size-change', size: number): void
  (e: 'current-change', current: number): void
}>()

const tableRef = ref<InstanceType<typeof ElTable>>()
const paginationObj = computed(() => props.pagination)

function onFieldChange(row: any, val: number) {
  emits('field-change', row, val)
}

function onSelect(selection: Array<object>, row: object) {
  emits('select', selection, row)
}

function onSelectAll(selection: Array<object>) {
  emits('select-all', selection)
}

function onSelectionChange(selection: Array<object>) {
  emits('selection-change', selection)
  emits('update:selection', selection)
}

function onRowDblClick(row: any) {
  if (row.children && row.children.length > 0) {
    tableRef.value?.toggleRowExpansion(row)
  }
}

function onEdit(row: any) {
  emits('edit', row)
}

function onDelete(row: any) {
  emits('delete', row)
}

function onSizeChange(size: number) {
  emits('update:pagination', paginationObj)
  emits('size-change', size)
}

function onCurrentChange(current: number) {
  emits('update:pagination', paginationObj)
  emits('current-change', current)
}

//递归展开
function expandAllRecursive(data: any, value: boolean) {
  if (!data) return

  data.forEach((item: any) => {
    if (item) {
      tableRef.value?.toggleRowExpansion(item, value)
      if (item.children && item.children.length > 0) {
        expandAllRecursive(item.children, value)
      }
    }
  })
}

defineExpose({
  clearSelection: () => {
    props.tableConfig.selection && tableRef.value?.clearSelection()
  },
  getSelectionRows: () => {
    return props.tableConfig.selection ? tableRef.value?.getSelectionRows() : []
  },
  toggleRowSelection: (row: object, selected: boolean) => {
    props.tableConfig.selection && tableRef.value?.toggleRowSelection(row, selected)
  },
  toggleAllSelection: () => {
    props.tableConfig.selection && tableRef.value?.toggleAllSelection()
  },
  expandAll: (value: boolean) => {
    expandAllRecursive(props.data, value)
  }
})
</script>

<style scoped lang='scss'>
.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: var(--el-bg-color);
  padding: 13px 15px;
  border: 1px solid var(--el-border-color);
  border-top: none;
}

.btn-grp {
  height: 100%;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;

  :deep(.el-button) {
    margin: 0 3px;
    height: auto;
    padding: 6px 7px;

    .icon {
      font-size: 16px !important;
    }
  }
}
</style>
