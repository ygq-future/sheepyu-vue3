<template>
  <div class='default-main'>
    <TableHeader
      ref='tableHeaderRef'
      v-model='state.query.keyword'
      auth='system:demo'
      :buttons="['add', 'delete', 'edit', 'import', 'export']"
      :rows='state.selection'
      @refresh='pageDemo'
      @add='onAdd'
      @batch-delete='onBatchDelete'
      @batch-edit='onBatchEdit'
      @input-enter='pageDemo'
      @input-clear='$nextTick(() => pageDemo())'
      @export='exportDemo'
      @batch-import='batchImport'
      @download='downloadTemplate'
    >
      <template #comSearch>
        <ComSearch
          :com-search-config='state.comSearchConfig'
          :form='state.query'
          @search='pageDemo'
          @reset='pageDemo'
        />
      </template>
    </TableHeader>

    <Table
      ref='tableRef'
      v-model:selection='state.selection'
      auth='system:demo'
      :data='state.tableData'
      :table-config='state.tableConfig'
      :pagination='state.query'
      @fieldChange='onFieldChange'
      @edit='(row) => onBatchEdit([row.id])'
      @delete='(row) => onBatchDelete([row.id])'
      @current-change='pageDemo'
      @size-change='pageDemo'
    >
    </Table>

    <PopupForm
      ref='popupFormRef'
      :form='state.form'
      :config='state.popupFormConfig'
      @close='onClose'
      @next='findDemo'
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
import type { SystemDemoCreateVo, SystemDemoQueryVo, SystemDemoRespVo, SystemDemoUpdateVo } from '@/api/system/demo'
import {
  createDemoApi,
  deleteDemoApi,
  exportDemoApi,
  findDemoApi,
  importDemoApi,
  pageDemoApi,
  updateDemoApi
} from '@/api/system/demo'
import { DictTypeEnum } from '@/enums/DictTypeEnum'
import type { PopupFormConfig } from '@/components/form/interface'
import { ElLoading } from 'element-plus'
import ComSearch from '@/components/search/ComSearch.vue'

const tableRef = ref()
const tableHeaderRef = ref()
const popupFormRef = ref()

const state = reactive<{
  selection: any[]
  query: SystemDemoQueryVo
  form: SystemDemoCreateVo | SystemDemoUpdateVo
  tableData: SystemDemoRespVo[]
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
    status: 0,
    beginTime: '',
    age: 0,
    sex: 0
  },
  tableData: [],
  comSearchConfig: [
    {
      label: '启用状态',
      prop: 'status',
      placeholder: '启用状态',
      render: 'dict',
      dictType: DictTypeEnum.COMMON_STATUS
    },
    { label: '开始时间', prop: 'beginTime', placeholder: '开始时间', render: 'datetime' },
    { label: '', prop: 'createTime', placeholder: '', render: 'datetime' },
    { label: '年龄', prop: 'age', placeholder: '年龄', render: 'number' },
    { label: '性别', prop: 'sex', placeholder: '性别', render: 'dict', dictType: DictTypeEnum.COMMON_BOOLEAN_STATUS }
  ],
  tableConfig: {
    rowKey: 'id',
    selection: true,
    pagination: true,
    columns: [
      { label: 'id', prop: 'id', render: 'text' },
      { label: '测试名称', prop: 'name', render: 'text' },
      { label: '测试内容', prop: 'content', render: 'text' },
      { label: '启用状态', prop: 'status', dictRender: 'tag', dictType: DictTypeEnum.COMMON_STATUS },
      { label: '开始时间', prop: 'beginTime', render: 'text' },
      { label: '', prop: 'creator', render: 'text' },
      { label: '', prop: 'createTime', render: 'text' },
      { label: '年龄', prop: 'age', render: 'text' },
      { label: '性别', prop: 'sex', dictRender: 'tag', dictType: DictTypeEnum.SEX }
    ],
    operate: {
      buttons: ['edit', 'delete'],
      width: 120
    }
  },
  popupFormConfig: {
    title: '新增测试',
    formItemConfigs: [
      { label: '测试名称', prop: 'name', placeholder: '测试名称', render: 'text' },
      { label: '测试内容', prop: 'content', required: false, placeholder: '测试内容', render: 'textarea' },
      {
        label: '启用状态',
        prop: 'status',
        placeholder: '启用状态',
        dictRender: 'select',
        dictType: DictTypeEnum.COMMON_STATUS
      },
      { label: '开始时间', prop: 'beginTime', placeholder: '开始时间', render: 'datetime' },
      { label: '年龄', prop: 'age', placeholder: '年龄', render: 'number' },
      {
        label: '性别',
        prop: 'sex',
        placeholder: '性别',
        dictRender: 'select',
        dictType: DictTypeEnum.SEX
      }
    ]
  }
})

async function onFieldChange(row: SystemDemoUpdateVo) {
  const data = toRaw(row)
  await updateDemoApi(data)
}

function onAdd() {
  state.popupFormConfig.title = '新增测试'
  state.popupFormConfig.isEdit = false
  popupFormRef.value.show()
}

async function onBatchDelete(ids: number[]) {
  await deleteDemoApi(ids.join(','))
  await pageDemo()
}

function onBatchEdit(ids: number[]) {
  state.popupFormConfig.title = '修改测试'
  state.popupFormConfig.isEdit = true
  state.popupFormConfig.ids = [...ids]
  popupFormRef.value.show()
}

async function onSubmit(cb: Function) {
  const data = toRaw(state.form)
  if (state.popupFormConfig.isEdit) {
    await updateDemoApi(data as SystemDemoUpdateVo)
  } else {
    await createDemoApi(data)
  }
  cb && cb()
  await pageDemo()
}

async function findDemo(id: number) {
  const { data } = await findDemoApi(id)
  state.form = data
}

async function pageDemo() {
  state.tableConfig.loading = true
  const { data } = await pageDemoApi(toRaw(state.query))
  state.tableConfig.loading = false
  state.tableData = data.list
  state.query.total = data.total
}

function exportDemo() {
  const service = ElLoading.service({ fullscreen: true })
  exportDemoApi(toRaw(state.query)).then(() => {
    service.close()
  }).catch(() => {
    service.close()
  })
}

function batchImport(file: File) {
  const service = ElLoading.service({ fullscreen: true })
  importDemoApi(file).then(() => {
    pageDemo()
    service.close()
  }).catch(() => {
    service.close()
  })
}

function downloadTemplate() {
  //很多方式, 可以先上传文件到后端, 然后直接填一个文件链接(这种不是很安全, 没有权限校验)
  //或者调用axios的download方法从后端拿文件流(比较安全, 但是后端文件流从哪里来?写死也不太好)
  //目前没有做到这个业务先放一边
  console.log('download')
}

function onClose() {
  state.form = {
    name: '',
    status: 0,
    beginTime: '',
    age: 0,
    sex: 0
  }
}

onMounted(() => {
  pageDemo()
})
</script>

<script lang='ts'>
export default defineComponent({
  name: 'system-demo'
})
</script>
