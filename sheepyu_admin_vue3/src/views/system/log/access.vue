<template>
  <div class='default-main'>
    <TableHeader
      ref='tableHeaderRef'
      v-model='state.query.keyword'
      auth='system:log-access'
      @refresh='pageAccessLog'
      @input-enter='pageAccessLog'
      @input-clear='$nextTick(() => pageAccessLog())'
    >
      <template #comSearch>
        <ComSearch
          :com-search-config='state.comSearchConfig'
          :form='state.query'
          @search='pageAccessLog'
          @reset='pageAccessLog'
        />
      </template>
    </TableHeader>

    <Table
      ref='tableRef'
      auth='system:log-access'
      :data='state.tableData'
      :table-config='state.tableConfig'
      :pagination='state.query'
      @current-change='pageAccessLog'
      @size-change='pageAccessLog'
    >
    </Table>
  </div>
</template>
<script setup lang='ts'>
import TableHeader from '@/components/table/header/TableHeader.vue'
import Table from '@/components/table/Table.vue'
import type { TableConfig } from '@/components/table/interface'
import type { ComSearchConfig } from '@/components/search/interface'
import type { SystemAccessLogQueryVo, SystemAccessLogRespVo } from '@/api/system/log'
import { pageAccessLogApi } from '@/api/system/log'
import { DictTypeEnum } from '@/enums/DictTypeEnum'
import ComSearch from '@/components/search/ComSearch.vue'

const tableRef = ref()
const tableHeaderRef = ref()
const popupFormRef = ref()

const state = reactive<{
  query: SystemAccessLogQueryVo
  tableData: SystemAccessLogRespVo[]
  comSearchConfig: ComSearchConfig
  tableConfig: TableConfig
}>({
  query: {
    current: 1,
    size: 10,
    total: 0
  },
  tableData: [],
  comSearchConfig: [
    {
      label: '用户类型',
      prop: 'userType',
      placeholder: '用户类型',
      render: 'dict',
      dictType: DictTypeEnum.SYSTEM_USER_TYPE
    },
    {
      label: '登录类型',
      prop: 'loginType',
      placeholder: '登录类型',
      render: 'dict',
      dictType: DictTypeEnum.LOGIN_TYPE
    },
    {
      label: '登录结果',
      prop: 'loginResult',
      placeholder: '登录结果',
      render: 'dict',
      dictType: DictTypeEnum.LOGIN_RESULT
    },
    { label: '登录时间', prop: 'createTimes', placeholder: '登录时间', render: 'datetime' }
  ],
  tableConfig: {
    rowKey: 'id',
    pagination: true,
    columns: [
      { label: '用户编号', prop: 'userId', render: 'text', width: 100 },
      { label: '用户类型', prop: 'userType', dictRender: 'tag', dictType: DictTypeEnum.SYSTEM_USER_TYPE },
      { label: '登录类型', prop: 'loginType', dictRender: 'tag', dictType: DictTypeEnum.LOGIN_TYPE },
      { label: '登录结果', prop: 'loginResult', dictRender: 'tag', dictType: DictTypeEnum.LOGIN_RESULT, width: 150 },
      { label: '登录账号', prop: 'username', render: 'text' },
      { label: '昵称', prop: 'nickname', render: 'text' },
      { label: '登录IP', prop: 'userIp', render: 'text' },
      { label: '浏览器 UA', prop: 'userAgent', render: 'text', showTip: true },
      { label: '登录时间', prop: 'createTime', render: 'text', width: 170 }
    ]
  }
})

async function pageAccessLog() {
  state.tableConfig.loading = true
  const { data } = await pageAccessLogApi(toRaw(state.query))
  state.tableConfig.loading = false
  state.tableData = data.list
  state.query.total = data.total
}

onMounted(() => {
  pageAccessLog()
})
</script>

<script lang='ts'>
export default defineComponent({
  name: 'system-log-access'
})
</script>
