<template>
  <div class='default-main'>
    <TableHeader
      ref='tableHeaderRef'
      v-model='state.query.keyword'
      auth='system:dept'
      :buttons="['add', 'edit', 'unfold']"
      :rows='state.selection'
      :com-search='false'
      @refresh='listDept'
      @add='onAdd'
      @unfold='onUnfold'
      @batch-edit='onBatchEdit'
      @input-enter='listDept'
      @input-clear='$nextTick(() => listDept())'
    >
      <template #comSearch>
        <ComSearch
          :com-search-config='state.comSearchConfig'
          :form='state.query'
          @search='listDept'
          @reset='listDept'
        />
      </template>
    </TableHeader>

    <Table
      ref='tableRef'
      v-model:selection='state.selection'
      auth='system:dept'
      :data='state.tableData'
      :table-config='state.tableConfig'
      @fieldChange='onFieldChange'
      @edit='(row) => onBatchEdit([row.id])'
      @delete='(row) => onDelete(row.id)'
    >

      <template #buttons='{data}'>
        <el-tooltip content='新增' placement='top' :show-after='500'>
          <el-button v-auth="'system:dept:create'" v-blur type='success' @click='onAdd(data.id)'>
            <template #icon>
              <Icon name='el-icon-Plus' />
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
      @next='findDept'
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
import type { SystemDeptCreateVo, SystemDeptQueryVo, SystemDeptRespVo, SystemDeptUpdateVo } from '@/api/system/dept'
import {
  createDeptApi,
  deleteDeptApi,
  findDeptApi,
  listDeptApi,
  updateDeptApi
} from '@/api/system/dept'
import type { PopupFormConfig } from '@/components/form/interface'

const tableRef = ref()
const tableHeaderRef = ref()
const popupFormRef = ref()

const state = reactive<{
  selection: any[]
  query: SystemDeptQueryVo
  form: SystemDeptCreateVo | SystemDeptUpdateVo
  tableData: SystemDeptRespVo[]
  comSearchConfig: ComSearchConfig
  tableConfig: TableConfig
  popupFormConfig: PopupFormConfig
}>({
  selection: [],
  query: {},
  form: {
    name: '',
    parentId: 0,
    sort: 0
  },
  tableData: [],
  comSearchConfig: [],
  tableConfig: {
    rowKey: 'id',
    selection: true,
    columns: [
      { label: '部门编号', prop: 'id', render: 'text' },
      { label: '部门名称', prop: 'name', render: 'text' },
      { label: '显示顺序', prop: 'sort', render: 'text' },
      { label: '负责人', prop: 'leaderUserId', render: 'text' },
      { label: '联系电话', prop: 'phone', render: 'text' },
      { label: '邮箱', prop: 'email', render: 'text' }
    ],
    operate: {
      buttons: ['edit', 'delete'],
      width: 120
    }
  },
  popupFormConfig: {
    title: '新增部门',
    formItemConfigs: [
      {
        label: '父部门',
        prop: 'parentId',
        placeholder: '父部门',
        render: 'tree-select',
        props: { label: 'name', value: 'id' }
      },
      { label: '部门名称', prop: 'name', placeholder: '部门名称', render: 'text' },
      { label: '显示顺序', prop: 'sort', placeholder: '显示顺序', render: 'number' },
      { label: '负责人', prop: 'leaderUserId', required: false, placeholder: '负责人', render: 'number' },
      { label: '联系电话', prop: 'phone', required: false, placeholder: '联系电话', render: 'text' },
      { label: '邮箱', prop: 'email', required: false, placeholder: '邮箱', render: 'text' }
    ]
  }
})

async function onFieldChange(row: SystemDeptUpdateVo) {
  const data = toRaw(row)
  await updateDeptApi(data)
}

function onUnfold(value: boolean) {
  tableRef.value.expandAll(value)
}

function onAdd(id?: number) {
  if (id) state.form.parentId = id
  state.popupFormConfig.title = '新增部门'
  state.popupFormConfig.isEdit = false
  popupFormRef.value.show()
}

async function onDelete(id: number) {
  await deleteDeptApi(id)
  await listDept()
}

function onBatchEdit(ids: number[]) {
  state.popupFormConfig.title = '修改部门'
  state.popupFormConfig.isEdit = true
  state.popupFormConfig.ids = [...ids]
  popupFormRef.value.show()
}

async function onSubmit(cb: Function) {
  const data = toRaw(state.form)
  if (state.popupFormConfig.isEdit) {
    await updateDeptApi(data as SystemDeptUpdateVo)
  } else {
    await createDeptApi(data as SystemDeptCreateVo)
  }
  cb && cb()
  await listDept()
}

async function findDept(id: number) {
  const { data } = await findDeptApi(id)
  state.form = data
}

async function listDept() {
  state.tableConfig.loading = true
  const { data } = await listDeptApi(toRaw(state.query))
  state.tableConfig.loading = false
  state.tableData = data
  state.popupFormConfig.formItemConfigs[0].data = [{
    id: 0,
    name: '顶级部门',
    children: [...data]
  }]
  await nextTick(() => {
    tableRef.value.expandAll(!tableHeaderRef.value.getUnfold())
  })
}


function onClose() {
  state.form = {
    name: '',
    parentId: 0,
    sort: 0
  }
}

onMounted(() => {
  listDept()
})
</script>

<script lang='ts'>
export default defineComponent({
  name: 'system-dept'
})
</script>
