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
import type {  TableConfig } from '@/components/table/interface'
import type { ComSearchConfig } from '@/components/search/interface'
import type { SystemDemoCreateVo, SystemDemoQueryVo, SystemDemoRespVo, SystemDemoUpdateVo } from '@/api/system/demo'
import {
  createDemoApi,
  deleteDemoApi,
  findDemoApi,
  pageDemoApi,
  updateDemoApi,
  exportDemoApi,
  importDemoApi
} from '@/api/system/demo'
import { DictTypeEnum } from '@/enums/DictTypeEnum'
import type { PopupFormConfig } from '@/components/form/interface'
import { ElLoading } from 'element-plus'

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
      label: '????????????',
      prop: 'status',
      placeholder: '????????????',
      render: 'dict',
      dictType: DictTypeEnum.COMMON_STATUS
    },
    { label: '????????????', prop: 'beginTime', placeholder: '????????????', render: 'datetime' },
    { label: '', prop: 'createTime', placeholder: '', render: 'datetime' },
    { label: '??????', prop: 'age', placeholder: '??????', render: 'number' },
    { label: '??????', prop: 'sex', placeholder: '??????', render: 'dict', dictType: DictTypeEnum.COMMON_BOOLEAN_STATUS }
  ],
  tableConfig: {
    rowKey: 'id',
    selection: true,
    pagination: true,
    columns: [
      { label: 'id', prop: 'id', render: 'text' },
      { label: '????????????', prop: 'name', render: 'text' },
      { label: '????????????', prop: 'content', render: 'text' },
      { label: '????????????', prop: 'status', dictRender: 'tag', dictType: DictTypeEnum.COMMON_STATUS },
      { label: '????????????', prop: 'beginTime', render: 'text' },
      { label: '', prop: 'creator', render: 'text' },
      { label: '', prop: 'createTime', render: 'text' },
      { label: '??????', prop: 'age', render: 'text' },
      { label: '??????', prop: 'sex', dictRender: 'tag', dictType: DictTypeEnum.SEX }
    ],
    operate: {
      buttons: ['edit', 'delete'],
      width: 120
    }
  },
  popupFormConfig: {
    title: '????????????',
    formItemConfigs: [
      { label: '????????????', prop: 'name', placeholder: '????????????', render: 'text' },
      { label: '????????????', prop: 'content', required: false, placeholder: '????????????', render: 'textarea' },
      {
        label: '????????????',
        prop: 'status',
        placeholder: '????????????',
        dictRender: 'select',
        dictType: DictTypeEnum.COMMON_STATUS
      },
      { label: '????????????', prop: 'beginTime', placeholder: '????????????', render: 'datetime' },
      { label: '??????', prop: 'age', placeholder: '??????', render: 'number' },
      {
        label: '??????',
        prop: 'sex',
        placeholder: '??????',
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
  state.popupFormConfig.title = '????????????'
  state.popupFormConfig.isEdit = false
  popupFormRef.value.show()
}

async function onBatchDelete(ids: number[]) {
  await deleteDemoApi(ids.join(','))
  await pageDemo()
}

function onBatchEdit(ids: number[]) {
  state.popupFormConfig.title = '????????????'
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
  //????????????, ??????????????????????????????, ?????????????????????????????????(?????????????????????, ??????????????????)
  //????????????axios???download???????????????????????????(????????????, ????????????????????????????????????????????????????)
  //??????????????????????????????????????????
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
