<template>
  <div class='default-main'>
    <TableHeader
      auth='system:job'
      :search='false'
      @refresh='pageJobLog'
      @input-enter='pageJobLog'
      @input-clear='$nextTick(() => pageJobLog())'
    >
      <template #comSearch>
        <ComSearch
          :com-search-config='state.comSearchConfig'
          :form='state.query'
          @search='pageJobLog'
          @reset='pageJobLog'
        />
      </template>
    </TableHeader>

    <Table
      auth='system:job'
      :data='state.tableData'
      :table-config='state.tableConfig'
      :pagination='state.query'
      @current-change='pageJobLog'
      @size-change='pageJobLog'
    >
    </Table>
  </div>
</template>
<script setup lang='ts'>
import TableHeader from '@/components/table/header/TableHeader.vue'
import Table from '@/components/table/Table.vue'
import type { TableConfig } from '@/components/table/interface'
import type { ComSearchConfig } from '@/components/search/interface'
import type { SystemJobLogQueryVo, SystemJobLogRespVo } from '@/api/system/job'
import {
  pageJobLogApi
} from '@/api/system/job'
import { DictTypeEnum } from '@/enums/DictTypeEnum'
import ComSearch from '@/components/search/ComSearch.vue'

const route = useRoute()

const state = reactive<{
  query: SystemJobLogQueryVo
  tableData: SystemJobLogRespVo[]
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
    { label: '执行时长', prop: 'durations', placeholder: '执行时长', render: 'number-range' },
    {
      label: '任务状态',
      prop: 'status',
      placeholder: '任务状态',
      render: 'dict',
      dictType: DictTypeEnum.SYSTEM_JOG_LOG_STATUS
    }
  ],
  tableConfig: {
    rowKey: 'id',
    pagination: true,
    columns: [
      { label: '日志编号', prop: 'id', render: 'text' },
      { label: '处理器名字', prop: 'handlerName', render: 'text' },
      { label: '参数', prop: 'handlerParam', render: 'text' },
      { label: '重试次数', prop: 'retryCount', render: 'text' },
      { label: '开始时间', prop: 'beginTime', render: 'text' },
      { label: '结束时间', prop: 'endTime', render: 'text' },
      { label: '执行时长', prop: 'duration', render: 'text' },
      { label: '任务状态', prop: 'status', dictRender: 'tag', dictType: DictTypeEnum.SYSTEM_JOG_LOG_STATUS },
      { label: '结果数据', prop: 'result', render: 'text' }
    ]
  }
})

async function pageJobLog() {
  state.tableConfig.loading = true
  const { data } = await pageJobLogApi(toRaw(state.query))
  state.tableConfig.loading = false
  state.tableData = data.list
  state.query.total = data.total
}

onMounted(() => {
  state.query.jobId = parseInt(route.params.jobId as string)
  pageJobLog()
})
</script>

<script lang='ts'>
export default defineComponent({
  name: 'system-job-log'
})
</script>
