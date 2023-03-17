<template>
  <div class='default-main'>
    <TableHeader
      ref='tableHeaderRef'
      v-model='state.query.keyword'
      auth='system:dict'
      :buttons="['add', 'edit']"
      :rows='state.selection'
      @refresh='pageDictType'
      @add='onAdd'
      @batch-edit='onBatchEdit'
      @input-enter='pageDictType'
      @input-clear='$nextTick(() => pageDictType())'
    >
      <template #comSearch>
        <ComSearch
          :com-search-config='state.comSearchConfig'
          :form='state.query'
          @search='pageDictType'
          @reset='pageDictType'
        />
      </template>

      <template #buttons>
        <el-tooltip :show-after='500' content='重载全局字典' placement='top'>
          <el-button v-blur type='success' @click='onLoadDict' :loading='state.buttonLoading'>
            <Icon name='el-icon-Cpu' />
            <span class='button-text'>重载全局字典</span>
          </el-button>
        </el-tooltip>
      </template>
    </TableHeader>

    <Table
      ref='tableRef'
      v-model:selection='state.selection'
      auth='system:dict'
      :data='state.tableData'
      :table-config='state.tableConfig'
      :pagination='state.query'
      @fieldChange='onFieldChange'
      @edit='(row) => onBatchEdit([row.id])'
      @delete='(row) => onDelete(row.id)'
      @current-change='pageDictType'
      @size-change='pageDictType'
    >
    </Table>

    <PopupForm
      ref='popupFormRef'
      :form='state.form'
      :config='state.popupFormConfig'
      @close='onClose'
      @next='findDictType'
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
  SystemDictTypeCreateVo,
  SystemDictTypeQueryVo,
  SystemDictTypeRespVo,
  SystemDictTypeUpdateVo
} from '@/api/system/dict'
import {
  createDictTypeApi,
  deleteDictTypeApi,
  findDictTypeApi,
  pageDictTypeApi,
  updateDictTypeApi
} from '@/api/system/dict'
import { DictTypeEnum } from '@/enums/DictTypeEnum'
import type { PopupFormConfig } from '@/components/form/interface'
import { loadDict } from '@/util/common'

const tableRef = ref()
const tableHeaderRef = ref()
const popupFormRef = ref()

const state = reactive<{
  buttonLoading?: boolean
  selection: any[]
  query: SystemDictTypeQueryVo
  form: SystemDictTypeCreateVo | SystemDictTypeUpdateVo
  tableData: SystemDictTypeRespVo[]
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
    type: '',
    name: '',
    status: 1
  },
  tableData: [],
  comSearchConfig: [
    {
      label: '开启状态',
      prop: 'status',
      placeholder: '开启状态',
      render: 'dict',
      dictType: DictTypeEnum.COMMON_STATUS
    }
  ],
  tableConfig: {
    rowKey: 'id',
    selection: true,
    pagination: true,
    columns: [
      { label: '主键', prop: 'id', render: 'text', width: 70 },
      { label: '类型', prop: 'type', render: 'link', width: 200, path: '/system/dict/{}', pathProps: ['type'] },
      { label: '字典名称', prop: 'name', render: 'text' },
      { label: '开启状态', prop: 'status', dictRender: 'switch', dictType: DictTypeEnum.COMMON_STATUS },
      { label: '备注', prop: 'remark', render: 'text' },
      { label: '创建者', prop: 'creator', render: 'text' }
    ],
    operate: {
      buttons: ['edit', 'delete'],
      width: 120
    }
  },
  popupFormConfig: {
    title: '新增字典类型',
    formItemConfigs: [
      { label: '类型', prop: 'type', placeholder: '类型', render: 'text' },
      { label: '字典名称', prop: 'name', placeholder: '字典名称', render: 'text' },
      {
        label: '开启状态',
        prop: 'status',
        placeholder: '开启状态',
        dictRender: 'radio',
        dictType: DictTypeEnum.COMMON_STATUS
      },
      { label: '备注', prop: 'remark', required: false, placeholder: '备注', render: 'textarea' }
    ]
  }
})

async function onFieldChange(row: SystemDictTypeUpdateVo) {
  const data = toRaw(row)
  await updateDictTypeApi(data)
  await pageDictType()
  loadDict()
}

function onAdd() {
  state.popupFormConfig.title = '新增字典类型'
  state.popupFormConfig.isEdit = false
  state.popupFormConfig.hideProps = []
  popupFormRef.value.show()
}

async function onDelete(id: number) {
  await deleteDictTypeApi(id)
  await pageDictType()
  loadDict()
}

function onBatchEdit(ids: number[]) {
  state.popupFormConfig.title = '修改字典类型'
  state.popupFormConfig.isEdit = true
  state.popupFormConfig.ids = [...ids]
  state.popupFormConfig.hideProps = ['type']
  popupFormRef.value.show()
}

async function onSubmit(cb: Function) {
  const data = toRaw(state.form)
  if (state.popupFormConfig.isEdit) {
    await updateDictTypeApi(data as SystemDictTypeUpdateVo)
  } else {
    await createDictTypeApi(data as SystemDictTypeCreateVo)
  }
  cb && cb()
  await pageDictType()
  loadDict()
}

async function findDictType(id: number) {
  const { data } = await findDictTypeApi(id)
  state.form = data
}

async function pageDictType() {
  state.tableConfig.loading = true
  const { data } = await pageDictTypeApi(toRaw(state.query))
  state.tableConfig.loading = false
  state.tableData = data.list
  state.query.total = data.total
}

function onLoadDict() {
  state.buttonLoading = true
  loadDict()
  setTimeout(() => {
    state.buttonLoading = false
  }, 1000)
}

function onClose() {
  state.form = {
    type: '',
    name: '',
    status: 1
  }
}

onMounted(() => {
  pageDictType()
})
</script>

<script lang='ts'>
export default defineComponent({
  name: 'system-dict'
})
</script>
