<template>
  <div class='default-main'>
    <TableHeader
      ref='tableHeaderRef'
      v-model='state.query.keyword'
      auth='system:config'
      :buttons="['add', 'edit']"
      :rows='state.selection'
      :com-search='false'
      @refresh='pageConfig'
      @add='onAdd'
      @batch-edit='onBatchEdit'
      @input-enter='pageConfig'
      @input-clear='$nextTick(() => pageConfig())'
    >
    </TableHeader>

    <Table
      ref='tableRef'
      v-model:selection='state.selection'
      auth='system:config'
      :data='state.tableData'
      :table-config='state.tableConfig'
      :pagination='state.query'
      @edit='(row) => onBatchEdit([row.id])'
      @delete='(row) => onDelete(row.id)'
      @current-change='pageConfig'
      @size-change='pageConfig'
    >
    </Table>

    <PopupForm
      ref='popupFormRef'
      :form='state.form'
      :config='state.popupFormConfig'
      @close='onClose'
      @next='findConfig'
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
import type {
  SystemConfigCreateVo,
  SystemConfigQueryVo,
  SystemConfigRespVo,
  SystemConfigUpdateVo
} from '@/api/system/config'
import { createConfigApi, deleteConfigApi, findConfigApi, pageConfigApi, updateConfigApi } from '@/api/system/config'
import type { PopupFormConfig } from '@/components/form/interface'

const tableRef = ref()
const tableHeaderRef = ref()
const popupFormRef = ref()

const state = reactive<{
  selection: any[]
  query: SystemConfigQueryVo
  form: SystemConfigCreateVo | SystemConfigUpdateVo
  tableData: SystemConfigRespVo[]
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
    configKey: '',
    configValue: ''
  },
  tableData: [],
  comSearchConfig: [],
  tableConfig: {
    rowKey: 'id',
    selection: true,
    pagination: true,
    columns: [
      { label: '主键', prop: 'id', render: 'text' },
      { label: '配置名称', prop: 'name', render: 'text' },
      { label: '配置键名', prop: 'configKey', render: 'text' },
      { label: '配置键值', prop: 'configValue', render: 'text' },
      { label: '备注', prop: 'remark', render: 'text', showTip: true },
      { label: '创建人', prop: 'creator', render: 'text' },
      { label: '创建时间', prop: 'createTime', render: 'text' }
    ],
    operate: {
      buttons: ['edit', 'delete'],
      width: 120
    }
  },
  popupFormConfig: {
    title: '新增配置',
    formItemConfigs: [
      { label: '配置名称', prop: 'name', placeholder: '参数名称', render: 'text' },
      { label: '配置键名', prop: 'configKey', placeholder: '参数键名', render: 'text' },
      { label: '配置键值', prop: 'configValue', placeholder: '参数键值', render: 'text' },
      { label: '备注', prop: 'remark', required: false, placeholder: '备注', render: 'textarea' }
    ]
  }
})

function onAdd() {
  state.popupFormConfig.title = '新增配置'
  state.popupFormConfig.isEdit = false
  popupFormRef.value.show()
}

async function onDelete(id: number) {
  await deleteConfigApi(id)
  await pageConfig()
}

function onBatchEdit(ids: number[]) {
  state.popupFormConfig.title = '修改配置'
  state.popupFormConfig.isEdit = true
  state.popupFormConfig.ids = [...ids]
  popupFormRef.value.show()
}

async function onSubmit(cb: Function) {
  const data = toRaw(state.form)
  if (state.popupFormConfig.isEdit) {
    await updateConfigApi(data as SystemConfigUpdateVo)
  } else {
    await createConfigApi(data as SystemConfigCreateVo)
  }
  cb && cb()
  await pageConfig()
}

async function findConfig(id: number) {
  const { data } = await findConfigApi(id)
  state.form = data
}

async function pageConfig() {
  state.tableConfig.loading = true
  const { data } = await pageConfigApi(toRaw(state.query))
  state.tableConfig.loading = false
  state.tableData = data.list
  state.query.total = data.total
}

function onClose() {
  state.form = {
    name: '',
    configKey: '',
    configValue: ''
  }
}

onMounted(() => {
  pageConfig()
})
</script>

<script lang='ts'>
export default defineComponent({
  name: 'system-config'
})
</script>
