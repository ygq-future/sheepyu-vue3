<template>
  <div class='default-main'>
    <TableHeader
      :buttons="['add', 'delete']"
      v-model='state.query.keyword'
    >
      <template #comSearch>
        <ComSearch
          :com-search-config='state.comSearchConfig'
          v-model='state.query'
        />
      </template>
    </TableHeader>

    <el-table :data='state.tableData' row-key='id' border stripe>
      <el-table-column prop='id' label='id' align='center' />
      <el-table-column prop='name' label='名称' align='center' />
      <el-table-column prop='icon' label='图标' align='center' />
      <el-table-column prop='sort' label='排序' align='center' />
      <el-table-column prop='type' label='类型' align='center'>
        <template #default='scope'>
          <Dict :value='scope.row.type' :type='DictTypeEnum.SYSTEM_MENU_TYPE' />
        </template>
      </el-table-column>
      <el-table-column prop='keepAlive' label='是否缓存' align='center'>
        <template #default='scope'>
          <Dict v-model='scope.row.keepAlive' :type='DictTypeEnum.COMMON_STATUS' render='switch' @change='onChange' />
        </template>
      </el-table-column>
      <el-table-column prop='status' label='状态' align='center'>
        <template #default='scope'>
          <Dict v-model='scope.row.status' :type='DictTypeEnum.COMMON_STATUS' render='switch' @change='onChange' />
        </template>
      </el-table-column>
      <el-table-column label='操作' align='center' width='250' fixed='right'>
        <template #default='scope'>
          <el-button type='primary'>
            <template #icon>
              <Icon name='fa fa-pencil' />
            </template>
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang='ts'>
import TableHeader from '@/components/table/header/TableHeader.vue'
import type { ComSearchConfig } from '@/components/table/interface'
import type { SystemMenuQueryVo, SystemMenuRespVo } from '@/api/system/menu'
import { menuList } from '@/api/system/menu'
import { DictTypeEnum } from '@/stores/dict/dictTypeEnum'

const state = reactive<{
  query: SystemMenuQueryVo
  tableData: SystemMenuRespVo[]
  comSearchConfig: ComSearchConfig
}>({
  query: {},
  tableData: [],
  comSearchConfig: [
    { label: '状态', prop: 'status', placeholder: '菜单状态', render: 'dict', dictType: DictTypeEnum.COMMON_STATUS }
  ]
})

watch(state.query, value => {
  console.log(value)
})

function onChange(value: number) {
  console.log(value)
}

async function findMenuList() {
  const { data } = await menuList(toRaw(state.query))
  state.tableData = data
}

onMounted(() => {
  findMenuList()
})
</script>


<style scoped lang='scss'>

</style>
