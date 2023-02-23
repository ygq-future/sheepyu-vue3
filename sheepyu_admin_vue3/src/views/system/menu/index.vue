<template>
  <div class='default-main'>
    <TableHeader
      v-model='state.query.keyword'
      :buttons="['add', 'delete', 'edit', 'unfold']"
      :rows='state.selection'
      @unfold='onUnfold'
      @batch-delete='onBatchDelete'
      @batch-edit='onBatchEdit'
    >
      <template #comSearch>
        <ComSearch
          :com-search-config='state.comSearchConfig'
          v-model='state.query'
        />
      </template>
    </TableHeader>

    <Table
      ref='tableRef'
      v-model:selection='state.selection'
      :data='state.tableData'
      :table-config='state.tableConfig'
      @fieldChange='onFieldChange'
    >
    </Table>
  </div>
</template>

<script setup lang='ts'>
import TableHeader from '@/components/table/header/TableHeader.vue'
import type { ComSearchConfig, TableConfig } from '@/components/table/interface'
import type { SystemMenuQueryVo, SystemMenuRespVo } from '@/api/system/menu'
import { menuList } from '@/api/system/menu'
import { DictTypeEnum } from '@/stores/dict/dictTypeEnum'
import Table from '@/components/table/Table.vue'

const state = reactive<{
  query: SystemMenuQueryVo
  tableData: SystemMenuRespVo[]
  comSearchConfig: ComSearchConfig
  tableConfig: TableConfig
  selection: any[]
}>({
  query: {},
  tableData: [],
  comSearchConfig: [
    { label: '状态', prop: 'status', placeholder: '菜单状态', render: 'dict', dictType: DictTypeEnum.COMMON_STATUS }
  ],
  tableConfig: {
    rowKey: 'id',
    selection: true,
    columns: [
      { label: 'id', prop: 'id' },
      { label: '名称', prop: 'name' },
      { label: '图标', prop: 'icon', render: 'icon' },
      { label: '排序', prop: 'sort', sortable: true },
      { label: '类型', prop: 'type', dictRender: 'tag', dictType: DictTypeEnum.SYSTEM_MENU_TYPE },
      { label: '缓存', prop: 'keepAlive', dictRender: 'switch', dictType: DictTypeEnum.COMMON_STATUS },
      { label: '状态', prop: 'status', dictRender: 'switch', dictType: DictTypeEnum.COMMON_STATUS }
    ],
    operate: {
      buttons: ['edit', 'delete'],
      width: 120
    }
  },
  selection: []
})

const tableRef = ref()

watch(state.query, value => {
  console.log(value)
})

function onFieldChange(row: any, val: any) {
  console.log(row, val)
}

function onUnfold(value: boolean) {
  tableRef.value.expandAll(value)
}

function onBatchDelete(ids: number[]) {
  console.log(ids)
}

function onBatchEdit(ids: number[]) {
  console.log(ids)
}

async function findMenuList() {
  const { data } = await menuList(toRaw(state.query))
  state.tableData = data
}

onMounted(() => {
  findMenuList()
})
</script>

<script lang='ts'>
export default defineComponent({
  name: 'system-menu'
})
</script>

<style scoped lang='scss'>

</style>
