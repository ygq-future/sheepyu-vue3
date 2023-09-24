<template>
  <div class='default-main'>
    <TableHeader
      ref='tableHeaderRef'
      v-model='state.query.keyword'
      auth='system:job'
      :buttons="['add', 'delete', 'edit']"
      :rows='state.selection'
      @refresh='pageJob'
      @add='onAdd'
      @batch-delete='onBatchDelete'
      @batch-edit='onBatchEdit'
      @input-enter='pageJob'
      @input-clear='$nextTick(() => pageJob())'
    >
      <template #comSearch>
        <ComSearch
          :com-search-config='state.comSearchConfig'
          :form='state.query'
          @search='pageJob'
          @reset='pageJob'
        />
      </template>
    </TableHeader>

    <Table
      ref='tableRef'
      v-model:selection='state.selection'
      auth='system:job'
      :data='state.tableData'
      :table-config='state.tableConfig'
      :pagination='state.query'
      @edit='(row) => onBatchEdit([row.id])'
      @delete='(row) => onBatchDelete([row.id])'
      @current-change='pageJob'
      @size-change='pageJob'
    >
      <template #buttons='scope'>
        <el-tooltip content='执行一次' placement='top' :show-after='500'>
          <el-button v-auth="'system:job:update'" v-blur type='warning' @click='execute(scope.data.id)'>
            <template #icon>
              <MyIcon name='fa fa-flash' />
            </template>
          </el-button>
        </el-tooltip>

        <el-tooltip content='暂停' placement='top' :show-after='500'>
          <el-button
            v-show='scope.data.status === 1'
            v-auth="'system:job:update'"
            v-blur type='danger'
            @click='updateStatus(scope.data.id)'
          >
            <template #icon>
              <MyIcon name='fa fa-stop-circle' />
            </template>
          </el-button>
        </el-tooltip>
        <el-tooltip content='开始' placement='top' :show-after='500'>
          <el-button
            v-show='scope.data.status === 2'
            v-auth="'system:job:update'"
            v-blur type='success'
            @click='updateStatus(scope.data.id)'
          >
            <template #icon>
              <MyIcon name='fa fa-play-circle' />
            </template>
          </el-button>
        </el-tooltip>
      </template>
    </Table>

    <PopupForm
      ref='popupFormRef'
      :form='state.form'
      :config='state.popupFormConfig'
      @close='onClose'
      @next='findJob'
      @submit='onSubmit'
    />
  </div>
</template>
<script setup lang='ts'>
import TableHeader from '@/components/table/header/TableHeader.vue'
import Table from '@/components/table/Table.vue'
import PopupForm from '@/components/form/PopupForm.vue'
import type { TableConfig } from '@/components/table/interface'
import type { ComSearchConfig } from '@/components/search/interface'
import type { SystemJobCreateVo, SystemJobQueryVo, SystemJobRespVo, SystemJobUpdateVo } from '@/api/system/job'
import {
  createJobApi,
  deleteJobApi, executeJobApi,
  findJobApi,
  pageJobApi,
  updateJobApi, updateJobStatusApi
} from '@/api/system/job'
import { DictTypeEnum } from '@/enums/DictTypeEnum'
import type { PopupFormConfig } from '@/components/form/interface'
import ComSearch from '@/components/search/ComSearch.vue'

const tableRef = ref()
const tableHeaderRef = ref()
const popupFormRef = ref()

const state = reactive<{
  selection: any[]
  query: SystemJobQueryVo
  form: SystemJobCreateVo | SystemJobUpdateVo
  tableData: SystemJobRespVo[]
  comSearchConfig: ComSearchConfig
  tableConfig: TableConfig
  popupFormConfig: PopupFormConfig
}>({
  selection: [],
  query: {
    current: 1,
    size: 10,
    total: 0
  },
  form: {
    name: '',
    handlerName: '',
    cron: '',
    retryCount: 0,
    retryInterval: 0
  },
  tableData: [],
  comSearchConfig: [
    {
      label: '任务状态',
      prop: 'status',
      placeholder: '任务状态',
      render: 'dict',
      dictType: DictTypeEnum.SYSTEM_JOB_STATUS
    }
  ],
  tableConfig: {
    rowKey: 'id',
    selection: true,
    pagination: true,
    columns: [
      { label: '主键', prop: 'id', render: 'text' },
      { label: '任务名称', prop: 'name', render: 'text', width: 100 },
      { label: '任务状态', prop: 'status', dictRender: 'tag', dictType: DictTypeEnum.SYSTEM_JOB_STATUS, width: 100 },
      {
        label: '处理器名称',
        prop: 'handlerName',
        render: 'link',
        width: 120,
        path: '/system/job/log/{}',
        pathProps: ['id']
      },
      { label: '参数', prop: 'handlerParam', render: 'text' },
      { label: 'CRON表达式', prop: 'cron', render: 'text', width: 120 },
      { label: '重试次数', prop: 'retryCount', render: 'text', width: 100 },
      { label: '重试间隔', prop: 'retryInterval', render: 'text', width: 100 },
      { label: '创建时间', prop: 'createTime', render: 'text' }
    ],
    operate: {
      buttons: ['edit', 'delete'],
      width: 160
    }
  },
  popupFormConfig: {
    title: '新增定时任务',
    labelWidth: 110,
    formItemConfigs: [
      { label: '任务名称', prop: 'name', placeholder: '任务名称', render: 'text' },
      { label: '处理器名字', prop: 'handlerName', placeholder: '处理器名字', render: 'text' },
      { label: '参数', prop: 'handlerParam', required: false, placeholder: '参数', render: 'text' },
      { label: 'CRON 表达式', prop: 'cron', placeholder: 'CRON表达式', render: 'text' },
      { label: '重试次数', prop: 'retryCount', placeholder: '重试次数', render: 'number' },
      { label: '重试间隔', prop: 'retryInterval', placeholder: '重试间隔', render: 'number' }
    ]
  }
})

async function execute(id: number) {
  await executeJobApi(id)
}

async function updateStatus(id: number) {
  await updateJobStatusApi(id)
  await pageJob()
}

function onAdd() {
  state.popupFormConfig.title = '新增定时任务'
  state.popupFormConfig.isEdit = false
  popupFormRef.value.show()
}

async function onBatchDelete(ids: number[]) {
  await deleteJobApi(ids.join(','))
  await pageJob()
}

function onBatchEdit(ids: number[]) {
  state.popupFormConfig.title = '修改定时任务'
  state.popupFormConfig.isEdit = true
  state.popupFormConfig.ids = [...ids]
  popupFormRef.value.show()
}

async function onSubmit(cb: Function) {
  const data = toRaw(state.form)
  if (state.popupFormConfig.isEdit) {
    await updateJobApi(data as SystemJobUpdateVo)
  } else {
    await createJobApi(data as SystemJobCreateVo)
  }
  cb && cb()
  await pageJob()
}

async function findJob(id: number) {
  const { data } = await findJobApi(id)
  state.form = data
}

async function pageJob() {
  state.tableConfig.loading = true
  const { data } = await pageJobApi(toRaw(state.query))
  state.tableConfig.loading = false
  state.tableData = data.list
  state.query.total = data.total
}

function onClose() {
  state.form = {
    name: '',
    handlerName: '',
    cron: '',
    retryCount: 0,
    retryInterval: 0
  }
}

onMounted(() => {
  pageJob()
})
</script>

<script lang='ts'>
export default defineComponent({
  name: 'system-job'
})
</script>
