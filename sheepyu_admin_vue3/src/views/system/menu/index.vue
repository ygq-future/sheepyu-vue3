<template>
  <div class='default-main'>
    <TableHeader
      ref='tableHeaderRef'
      v-model='state.query.keyword'
      auth='system:menu'
      :buttons="['add', 'delete', 'edit', 'unfold']"
      :rows='state.selection'
      @unfold='onUnfold'
      @refresh='findMenuList'
      @add='onAdd'
      @batch-delete='onBatchDelete'
      @batch-edit='onBatchEdit'
      @input-enter='findMenuList'
      @input-clear='$nextTick(() => findMenuList())'
    >
      <template #comSearch>
        <ComSearch
          :com-search-config='state.comSearchConfig'
          :form='state.query'
          @search='findMenuList'
          @reset='findMenuList'
        />
      </template>
    </TableHeader>

    <Table
      ref='tableRef'
      v-model:selection='state.selection'
      auth='system:menu'
      :data='state.tableData'
      :table-config='state.tableConfig'
      @fieldChange='onFieldChange'
      @edit='(row) => onBatchEdit([row.id])'
      @delete='(row) => onBatchDelete([row.id])'
    >

      <template #buttons='{data}'>
        <el-tooltip
          v-if='data?.type !== MenuTypeEnum.BUTTON'
          content='新增'
          placement='top'
          :show-after='500'
        >
          <el-button v-auth="'system:menu:create'" v-blur type='success' @click='onAdd(data)'>
            <template #icon>
              <MyIcon name='el-icon-Plus' />
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
      @next='findMenuById'
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
import type { SystemMenuCreateVo, SystemMenuQueryVo, SystemMenuRespVo, SystemMenuUpdateVo } from '@/api/system/menu'
import { changeStatus, createMenu, deleteMenu, findMenu, menuList, updateMenu } from '@/api/system/menu'
import { DictTypeEnum } from '@/enums/DictTypeEnum'
import type { PopupFormConfig } from '@/components/form/interface'
import { useTabs } from '@/stores/tabs/tabs'
import { MenuTypeEnum } from '@/enums/MenuTypeEnum'
import { ElNotification } from 'element-plus'
import ComSearch from '@/components/search/ComSearch.vue'

const tabs = useTabs()
const tableRef = ref()
const tableHeaderRef = ref()
const popupFormRef = ref()

const state = reactive<{
  selection: any[]
  query: SystemMenuQueryVo
  form: SystemMenuCreateVo | SystemMenuUpdateVo
  tableData: SystemMenuRespVo[]
  comSearchConfig: ComSearchConfig
  tableConfig: TableConfig
  popupFormConfig: PopupFormConfig
}>({
  selection: [],
  query: {},
  form: {
    parentId: 0,
    name: '',
    type: 1,
    sort: 0,
    status: 1,
    keepAlive: 1,
    icon: '',
    permission: '',
    path: ''
  },
  tableData: [],
  comSearchConfig: [
    {
      label: '状态',
      prop: 'status',
      placeholder: '菜单状态',
      render: 'dict',
      dictType: DictTypeEnum.COMMON_STATUS
    }
  ],
  tableConfig: {
    rowKey: 'id',
    selection: true,
    columns: [
      { label: 'id', prop: 'id' },
      { label: '名称', prop: 'name' },
      { label: '图标', prop: 'icon', render: 'icon' },
      { label: '权限标识', prop: 'permission' },
      { label: '排序', prop: 'sort', sortable: true },
      { label: '类型', prop: 'type', dictRender: 'tag', dictType: DictTypeEnum.SYSTEM_MENU_TYPE },
      { label: '缓存', prop: 'keepAlive', dictRender: 'switch', dictType: DictTypeEnum.COMMON_STATUS },
      { label: '状态', prop: 'status', dictRender: 'switch', dictType: DictTypeEnum.COMMON_STATUS }
    ],
    operate: {
      buttons: ['edit', 'delete'],
      width: 120
    }
  },
  popupFormConfig: {
    title: '新增菜单',
    formItemConfigs: [
      {
        label: '父级菜单',
        prop: 'parentId',
        placeholder: '请选择父级菜单',
        render: 'tree-select',
        props: { label: 'name', value: 'id' }
      },
      { label: '名称', prop: 'name', placeholder: '请输入名称' },
      { label: '类型', prop: 'type', dictRender: 'radio', dictType: DictTypeEnum.SYSTEM_MENU_TYPE },
      { label: '路由', prop: 'path', tip: '所有路由路径都不用带 /' },
      { label: '组件路径', prop: 'component' },
      { label: '权限标识', prop: 'permission' },
      { label: '图标', prop: 'icon', render: 'icon', required: false },
      { label: '排序', prop: 'sort', placeholder: '菜单排序', render: 'number' },
      { label: '状态', prop: 'status', dictRender: 'radio', dictType: DictTypeEnum.COMMON_STATUS },
      { label: '缓存', prop: 'keepAlive', dictRender: 'radio', dictType: DictTypeEnum.COMMON_STATUS }
    ]
  }
})

watch(() => state.query.status, val => {
  console.log(val)
})

const forbidList: number[] = [1, 2, 12]

async function onFieldChange(row: SystemMenuUpdateVo) {
  const data = toRaw(row)
  if (forbidList.includes(data.id)) {
    ElNotification.warning('不能操作重要数据!')
  } else {
    await changeStatus(data.id)
  }
  await findMenuList()
  tabs.notifyNeedUpdate()
}

function onUnfold(value: boolean) {
  tableRef.value.expandAll(value)
}

function onAdd(data?: any) {
  state.popupFormConfig.disabledProps = []
  if (data && data.id) {
    state.form.parentId = data.id
    state.popupFormConfig.disabledProps.push('parentId')
    if (data.type === MenuTypeEnum.MENU) {
      state.form.type = MenuTypeEnum.BUTTON
      state.popupFormConfig.disabledProps.push('type')
    }
  }
  state.popupFormConfig.title = '新增菜单'
  state.popupFormConfig.isEdit = false
  popupFormRef.value.show()
}

async function onBatchDelete(ids: number[]) {
  await deleteMenu(ids.join(','))
  await findMenuList()
  tabs.clearRoute()
}

function onBatchEdit(ids: number[]) {
  state.popupFormConfig.title = '修改菜单'
  state.popupFormConfig.isEdit = true
  state.popupFormConfig.disabledProps = ['type']
  state.popupFormConfig.ids = [...ids]
  popupFormRef.value.show()
}

async function onSubmit(cb: Function) {
  if (state.popupFormConfig.isEdit) {
    await updateMenu(state.form as SystemMenuUpdateVo)
  } else {
    await createMenu(state.form)
  }
  cb && cb()
  tabs.clearRoute()
  await findMenuList()
}

function onClose() {
  state.form = {
    parentId: 0,
    name: '',
    type: 1,
    sort: 0,
    status: 1,
    keepAlive: 1,
    icon: '',
    permission: '',
    path: ''
  }
}

async function findMenuById(id: number) {
  const { data } = await findMenu(id)
  if (forbidList.includes(data.id)) {
    state.popupFormConfig.disabledProps = ['keepAlive', 'status', 'type']
  }
  state.form = data
}

async function findMenuList() {
  state.tableConfig.loading = true
  const { data } = await menuList(toRaw(state.query))
  state.tableConfig.loading = false
  state.tableData = data
  setFormItemData(data)
  await nextTick(() => {
    tableRef.value.expandAll(!tableHeaderRef.value.getUnfold())
  })
}

function setFormItemData(data: SystemMenuRespVo[]) {
  const temp: SystemMenuRespVo[] = JSON.parse(JSON.stringify(data))
  removeLastChildren(temp)
  state.popupFormConfig.formItemConfigs[0].data = [{
    id: 0,
    name: '顶级目录',
    type: 1,
    sort: 0,
    parentId: 0,
    status: 1,
    keepAlive: 1,
    children: temp
  }]
}

function removeLastChildren(data: SystemMenuRespVo[]) {
  for (let item of data) {
    if (item.type === MenuTypeEnum.BUTTON) {
      return true
    }
    const result = removeLastChildren(item.children || [])
    if (result) {
      item.children = []
    }
  }
  return false
}

const hidePropsMap: Map<number, Array<string>> = new Map([
  [1, ['component', 'permission', 'keepAlive']],
  [2, []],
  [3, ['path', 'component', 'icon', 'keepAlive']]
])

watch(() => state.form.type, value => {
  state.popupFormConfig.hideProps = hidePropsMap.get(value)
}, { immediate: true })

onMounted(() => {
  findMenuList()
})
</script>

<script lang='ts'>
export default defineComponent({
  name: 'system-menu'
})
</script>
