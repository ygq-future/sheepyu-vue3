<template>
  <div class='default-main'>
    <TableHeader
      ref='tableHeaderRef'
      v-model='state.query.keyword'
      auth='system:dict'
      :buttons="['add', 'delete', 'edit']"
      :rows='state.selection'
      :search='false'
      @refresh='listDictData'
      @add='onAdd'
      @batch-delete='onBatchDelete'
      @batch-edit='onBatchEdit'
      @input-enter='listDictData'
      @input-clear='$nextTick(() => listDictData())'
    >
      <template #comSearch>
        <ComSearch
          :com-search-config='state.comSearchConfig'
          :form='state.query'
          @search='search'
          @reset='listDictData'
        />
      </template>
    </TableHeader>

    <Table
      ref='tableRef'
      v-model:selection='state.selection'
      auth='system:dict'
      :data='state.tableData'
      :table-config='state.tableConfig'
      @fieldChange='onFieldChange'
      @edit='(row) => onBatchEdit([row.id])'
      @delete='(row) => onBatchDelete([row.id])'
    >
    </Table>

    <PopupForm
      ref='popupFormRef'
      :form='state.form'
      :config='state.popupFormConfig'
      @close='onClose'
      @next='findDictData'
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
  SystemDictDataCreateVo,
  SystemDictDataQueryVo,
  SystemDictDataRespVo,
  SystemDictDataUpdateVo
} from '@/api/system/dict'
import {
  createDictDataApi,
  deleteDictDataApi,
  findDictDataApi,
  listDictDataApi,
  updateDictDataApi,
  dictTypeListApi
} from '@/api/system/dict'
import { DictTypeEnum } from '@/enums/DictTypeEnum'
import type { PopupFormConfig } from '@/components/form/interface'
import { loadDict } from '@/util/common'

const route = useRoute()
const router = useRouter()
const tableRef = ref()
const tableHeaderRef = ref()
const popupFormRef = ref()

const state = reactive<{
  selection: any[]
  query: SystemDictDataQueryVo
  form: SystemDictDataCreateVo | SystemDictDataUpdateVo
  tableData: SystemDictDataRespVo[]
  comSearchConfig: ComSearchConfig
  tableConfig: TableConfig
  popupFormConfig: PopupFormConfig
}>({
  selection: [],
  query: {
    dictType: route.params.type as string
  },
  form: {
    dictType: '',
    sort: 0,
    label: '',
    value: '',
    status: 1
  },
  tableData: [],
  comSearchConfig: [
    {
      label: '????????????',
      prop: 'dictType',
      placeholder: '????????????',
      render: 'select',
      selectOptions: [],
      selectIdKey: 'type',
      selectLabelKey: 'name',
      clearable: false
    }
  ],
  tableConfig: {
    rowKey: 'id',
    selection: true,
    columns: [
      { label: '??????', prop: 'id', render: 'text' },
      { label: '????????????', prop: 'label', render: 'text' },
      { label: '?????????', prop: 'value', render: 'text' },
      { label: '????????????', prop: 'sort', render: 'text' },
      { label: '????????????', prop: 'status', dictRender: 'switch', dictType: DictTypeEnum.COMMON_STATUS },
      { label: '????????????', prop: 'colorType', render: 'text' },
      { label: '??????', prop: 'remark', render: 'text' }
    ],
    operate: {
      buttons: ['edit', 'delete'],
      width: 120
    }
  },
  popupFormConfig: {
    title: '?????????????????????',
    formItemConfigs: [
      {
        label: '????????????',
        prop: 'dictType',
        placeholder: '????????????',
        render: 'select',
        props: { label: 'name', value: 'type' }
      },
      { label: '????????????', prop: 'sort', placeholder: '????????????', render: 'number' },
      { label: '????????????', prop: 'label', placeholder: '????????????', render: 'text' },
      { label: '?????????', prop: 'value', placeholder: '?????????', render: 'text' },
      {
        label: '????????????',
        prop: 'status',
        placeholder: '????????????',
        dictRender: 'radio',
        dictType: DictTypeEnum.COMMON_STATUS
      },
      {
        label: '????????????',
        prop: 'colorType',
        required: false,
        placeholder: '????????????: info, warning, success, danger',
        render: 'select',
        data: [{ value: 'info' }, { value: 'warning' }, { value: 'success' }, { value: 'danger' }],
        props: { label: 'value', value: 'value' },
        tip: '??????????????????????????????'
      },
      { label: '??????', prop: 'remark', required: false, placeholder: '??????', render: 'textarea' }
    ]
  }
})

async function onFieldChange(row: SystemDictDataUpdateVo) {
  const data = toRaw(row)
  await updateDictDataApi(data)
  loadDict()
}

function onAdd() {
  state.popupFormConfig.title = '?????????????????????'
  state.popupFormConfig.isEdit = false
  state.form.dictType = route.params.type as string
  popupFormRef.value.show()
}

async function onBatchDelete(ids: number[]) {
  await deleteDictDataApi(ids.join(','))
  await listDictData()
  loadDict()
}

function onBatchEdit(ids: number[]) {
  state.popupFormConfig.title = '?????????????????????'
  state.popupFormConfig.isEdit = true
  state.popupFormConfig.ids = [...ids]
  popupFormRef.value.show()
}

async function onSubmit(cb: Function) {
  const data = toRaw(state.form)
  if (state.popupFormConfig.isEdit) {
    await updateDictDataApi(data as SystemDictDataUpdateVo)
  } else {
    await createDictDataApi(data as SystemDictDataCreateVo)
  }
  cb && cb()
  await listDictData()
  loadDict()
}

function search() {
  const dictType = state.query.dictType
  if (dictType && dictType !== route.params.type) {
    router.push('/system/dict/' + dictType)
  }
}

async function findDictData(id: number) {
  const { data } = await findDictDataApi(id)
  state.form = data
}

async function listDictData() {
  state.tableConfig.loading = true
  const { data } = await listDictDataApi(toRaw(state.query))
  state.tableConfig.loading = false
  state.tableData = data
}

async function dictTypeList() {
  const { data } = await dictTypeListApi()
  state.comSearchConfig[0].selectOptions = data
  state.popupFormConfig.formItemConfigs[0].data = data
}

function onClose() {
  state.form = {
    dictType: '',
    sort: 0,
    label: '',
    value: '',
    status: 1
  }
}

onMounted(() => {
  listDictData()
  dictTypeList()
})
</script>

<script lang='ts'>
export default defineComponent({
  name: 'system-dict-data'
})
</script>
