<template>
  <div class='default-main'>
    <TableHeader
      ref='tableHeaderRef'
      v-model='state.query.keyword'
      auth='system:post'
      :buttons="['add', 'delete', 'edit']"
      :rows='state.selection'
      :com-search='false'
      @refresh='pagePost'
      @add='onAdd'
      @batch-delete='onBatchDelete'
      @batch-edit='onBatchEdit'
      @input-enter='pagePost'
      @input-clear='$nextTick(() => pagePost())'
    >
      <template #comSearch>
        <ComSearch
          :com-search-config='state.comSearchConfig'
          :form='state.query'
          @search='pagePost'
          @reset='pagePost'
        />
      </template>
    </TableHeader>

    <Table
      ref='tableRef'
      v-model:selection='state.selection'
      auth='system:post'
      :data='state.tableData'
      :table-config='state.tableConfig'
      :pagination='state.query'
      @fieldChange='onFieldChange'
      @edit='(row) => onBatchEdit([row.id])'
      @delete='(row) => onBatchDelete([row.id])'
      @current-change='pagePost'
      @size-change='pagePost'
    >
    </Table>

    <PopupForm
      ref='popupFormRef'
      :form='state.form'
      :config='state.popupFormConfig'
      @close='onClose'
      @next='findPost'
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
import type { SystemPostCreateVo, SystemPostQueryVo, SystemPostRespVo, SystemPostUpdateVo } from '@/api/system/post'
import {
  createPostApi,
  deletePostApi,
  findPostApi,
  pagePostApi,
  updatePostApi
} from '@/api/system/post'
import { DictTypeEnum } from '@/enums/DictTypeEnum'
import type { PopupFormConfig } from '@/components/form/interface'

const tableRef = ref()
const tableHeaderRef = ref()
const popupFormRef = ref()

const state = reactive<{
  selection: any[]
  query: SystemPostQueryVo
  form: SystemPostCreateVo | SystemPostUpdateVo
  tableData: SystemPostRespVo[]
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
    sort: 0
  },
  tableData: [],
  comSearchConfig: [],
  tableConfig: {
    rowKey: 'id',
    selection: true,
    pagination: true,
    columns: [
      { label: '岗位编号', prop: 'id', render: 'text' },
      { label: '岗位名称', prop: 'name', render: 'text' },
      { label: '显示顺序', prop: 'sort', render: 'text' },
      { label: '创建者', prop: 'creator', render: 'text' },
      { label: '创建时间', prop: 'createTime', render: 'text', width: 200 }
    ],
    operate: {
      buttons: ['edit', 'delete'],
      width: 120
    }
  },
  popupFormConfig: {
    title: '新增岗位',
    formItemConfigs: [
      { label: '岗位名称', prop: 'name', placeholder: '岗位名称', render: 'text' },
      { label: '显示顺序', prop: 'sort', placeholder: '显示顺序', render: 'number' }
    ]
  }
})

async function onFieldChange(row: SystemPostUpdateVo) {
  const data = toRaw(row)
  await updatePostApi(data)
}

function onAdd() {
  state.popupFormConfig.title = '新增岗位'
  state.popupFormConfig.isEdit = false
  popupFormRef.value.show()
}

async function onBatchDelete(ids: number[]) {
  await deletePostApi(ids.join(','))
  await pagePost()
}

function onBatchEdit(ids: number[]) {
  state.popupFormConfig.title = '修改岗位'
  state.popupFormConfig.isEdit = true
  state.popupFormConfig.ids = [...ids]
  popupFormRef.value.show()
}

async function onSubmit(cb: Function) {
  const data = toRaw(state.form)
  if (state.popupFormConfig.isEdit) {
    await updatePostApi(data as SystemPostUpdateVo)
  } else {
    await createPostApi(data as SystemPostCreateVo)
  }
  cb && cb()
  await pagePost()
}

async function findPost(id: number) {
  const { data } = await findPostApi(id)
  state.form = data
}

async function pagePost() {
  state.tableConfig.loading = true
  const { data } = await pagePostApi(toRaw(state.query))
  state.tableConfig.loading = false
  state.tableData = data.list
  state.query.total = data.total
}


function onClose() {
  state.form = {
    name: '',
    sort: 0
  }
}

onMounted(() => {
  pagePost()
})
</script>

<script lang='ts'>
export default defineComponent({
  name: 'system-post'
})
</script>
