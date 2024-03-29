<template>
  <div class='default-main'>
    <TableHeader
      v-model='state.query.keyword'
      auth='system:log-api'
      @refresh='pageApiLog'
      @input-enter='pageApiLog'
      @input-clear='$nextTick(() => pageApiLog())'
    >
      <template #comSearch>
        <ComSearch
          :com-search-config='state.comSearchConfig'
          :form='state.query'
          @search='pageApiLog'
          @reset='pageApiLog'
        />
      </template>
    </TableHeader>

    <Table
      auth='system:log-api'
      :data='state.tableData'
      :table-config='state.tableConfig'
      :pagination='state.query'
      @fieldChange='onFieldChange'
      @current-change='pageApiLog'
      @size-change='pageApiLog'
    >
      <template #buttons='scope'>
        <el-tooltip content='查看详情' placement='top' :show-after='500'>
          <el-button v-auth="'system:log-api:query'" v-blur type='primary' @click='onDetail(scope.data)'>
            <template #icon>
              <MyIcon name='fa fa-info-circle' />
            </template>
          </el-button>
        </el-tooltip>
      </template>
    </Table>

    <PopupForm
      ref='popupFormRef'
      :config='state.popupFormConfig'
      :form='state.form'
    />
  </div>
</template>
<script setup lang='ts'>
import TableHeader from '@/components/table/header/TableHeader.vue'
import Table from '@/components/table/Table.vue'
import type { TableConfig } from '@/components/table/interface'
import type { ComSearchConfig } from '@/components/search/interface'
import type { SystemApiLogQueryVo, SystemApiLogRespVo } from '@/api/system/log'
import { pageApiLogApi, processApiLogApi } from '@/api/system/log'
import { DictTypeEnum } from '@/enums/DictTypeEnum'
import type { PopupFormConfig } from '@/components/form/interface'
import ComSearch from '@/components/search/ComSearch.vue'
import PopupForm from '@/components/form/PopupForm.vue'

const popupFormRef = shallowRef()

const state = reactive<{
  query: SystemApiLogQueryVo
  tableData: SystemApiLogRespVo[]
  comSearchConfig: ComSearchConfig
  tableConfig: TableConfig
  popupFormConfig: PopupFormConfig
  form: any
}>({
  form: {},
  query: {
    current: 1,
    size: 10,
    total: 0
  },
  tableData: [],
  comSearchConfig: [
    { label: '操作类型', prop: 'type', placeholder: '操作类型', render: 'dict', dictType: DictTypeEnum.OPERATE_TYPE },
    { label: '执行时长', prop: 'durations', placeholder: '执行时长', render: 'number-range' },
    {
      label: '结果状态',
      prop: 'status',
      placeholder: '结果状态',
      render: 'dict',
      dictType: DictTypeEnum.API_LOG_STATUS
    },
    {
      label: '处理状态',
      prop: 'processStatus',
      placeholder: '处理状态',
      render: 'dict',
      dictType: DictTypeEnum.API_LOG_PROCESS_STATUS
    },
    { label: '请求时间', prop: 'createTimes', placeholder: '请求时间', render: 'datetime' }
  ],
  tableConfig: {
    rowKey: 'id',
    pagination: true,
    columns: [
      { label: '用户编号', prop: 'userId', render: 'text', width: 100 },
      { label: '用户类型', prop: 'userType', dictRender: 'tag', dictType: DictTypeEnum.SYSTEM_USER_TYPE, width: 100 },
      { label: '操作名', prop: 'name', render: 'text', width: 150 },
      { label: '操作类型', prop: 'type', dictRender: 'tag', dictType: DictTypeEnum.OPERATE_TYPE, width: 100 },
      { label: '方法名', prop: 'requestMethod', render: 'text', width: 150 },
      { label: 'url', prop: 'requestUrl', render: 'text', width: 150 },
      { label: '请求时间', prop: 'createTime', render: 'text', width: 130 },
      { label: '访问IP', prop: 'userIp', render: 'text', width: 100 },
      { label: '执行时长(ms)', prop: 'duration', render: 'text', width: 130 },
      { label: '结果码', prop: 'resultCode', render: 'text' },
      { label: '结果状态', prop: 'status', dictRender: 'tag', dictType: DictTypeEnum.API_LOG_STATUS, width: 100 },
      { label: '异常发生时间', prop: 'exceptionTime', render: 'text', width: 150 },
      { label: '异常类', prop: 'exceptionName', render: 'text', showTip: true, width: 120 },
      {
        label: '处理状态',
        prop: 'processStatus',
        dictRender: 'switch',
        dictType: DictTypeEnum.API_LOG_PROCESS_STATUS,
        width: 100
      },
      { label: '处理时间', prop: 'processTime', render: 'text', width: 100 },
      { label: '处理用户', prop: 'processUserId', render: 'text', width: 100 }
    ],
    operate: {
      buttons: [],
      width: 80
    }
  },
  popupFormConfig: {
    title: '日志详情',
    labelWidth: 120,
    width: 700,
    looked: true,
    formItemConfigs: [
      { label: '用户编号', prop: 'userId', render: 'text' },
      { label: '用户类型', prop: 'userType', dictRender: 'tag', dictType: DictTypeEnum.SYSTEM_USER_TYPE },
      { label: '操作名', prop: 'name', render: 'text' },
      { label: '操作类型', prop: 'type', dictRender: 'tag', dictType: DictTypeEnum.OPERATE_TYPE },
      { label: '方法名', prop: 'requestMethod', render: 'text' },
      { label: 'url', prop: 'requestUrl', render: 'text' },
      { label: '请求参数', prop: 'requestParams', render: 'textarea' },
      { label: '请求时间', prop: 'createTime', render: 'text' },
      { label: '访问IP', prop: 'userIp', render: 'text' },
      { label: '执行时长(ms)', prop: 'duration', render: 'text' },
      { label: '结果码', prop: 'resultCode', render: 'text' },
      { label: '结果状态', prop: 'status', dictRender: 'tag', dictType: DictTypeEnum.API_LOG_STATUS },
      { label: '结果数据', prop: 'resultData', render: 'textarea' },
      { label: '异常发生时间', prop: 'exceptionTime', render: 'text' },
      { label: '异常根信息', prop: 'exceptionRootCauseMessage', render: 'textarea' },
      { label: '异常栈信息', prop: 'exceptionStackTraceFull', render: 'textarea' },
      { label: '异常关键信息', prop: 'exceptionStackTraceCrucial', render: 'textarea' },
      { label: '异常类', prop: 'exceptionName', render: 'text' },
      {
        label: '处理状态',
        prop: 'processStatus',
        dictRender: 'tag',
        dictType: DictTypeEnum.API_LOG_PROCESS_STATUS
      },
      { label: '处理时间', prop: 'processTime', render: 'text' },
      { label: '处理用户', prop: 'processUserId', render: 'text' }
    ]
  }
})

function onDetail(row: any) {
  state.form = { ...row }
  popupFormRef.value.show()
}

async function onFieldChange(row: any) {
  await processApiLogApi(row.id)
  await pageApiLog()
}

async function pageApiLog() {
  state.tableConfig.loading = true
  const { data } = await pageApiLogApi(toRaw(state.query))
  state.tableConfig.loading = false
  state.tableData = data.list
  state.query.total = data.total
}

onMounted(() => {
  pageApiLog()
})
</script>

<script lang='ts'>
export default defineComponent({
  name: 'system-log-api'
})
</script>
