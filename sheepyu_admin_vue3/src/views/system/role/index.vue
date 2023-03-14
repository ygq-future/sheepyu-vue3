<template>
  <div class='default-main'>
    <TableHeader
      ref='tableHeaderRef'
      v-model='state.query.keyword'
      auth='system:role'
      :buttons="['add', 'delete', 'edit']"
      :rows='state.selection'
      @refresh='pageRole'
      @add='onAdd'
      @batch-delete='onBatchDelete'
      @batch-edit='onBatchEdit'
      @input-enter='pageRole'
      @input-clear='$nextTick(() => pageRole())'
    >
      <template #comSearch>
        <ComSearch
          :com-search-config='state.comSearchConfig'
          :form='state.query'
          @search='pageRole'
          @reset='pageRole'
        />
      </template>
    </TableHeader>

    <Table
      ref='tableRef'
      v-model:selection='state.selection'
      auth='system:role'
      :data='state.tableData'
      :table-config='state.tableConfig'
      :pagination='state.query'
      @fieldChange='onFieldChange'
      @edit='(row) => onBatchEdit([row.id])'
      @delete='(row) => onBatchDelete([row.id])'
      @current-change='pageRole'
      @size-change='pageRole'
    >

      <template #buttons='scope'>
        <el-tooltip content='分配菜单' placement='top' :show-after='500'>
          <el-button v-auth="'system:menu:assign'" v-blur type='warning' @click='onAssignMenu(scope.data)'>
            <template #icon>
              <Icon name='fa fa-sitemap' />
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
      @next='findRole'
      @submit='onSubmit'
    />

    <PopupForm
      ref='menuAssignFormRef'
      :form='state.menuAssignForm'
      :config='state.menuAssignFormConfig'
      @close='onMenuAssignClose'
      @submit='onMenuAssignSubmit'
    />
  </div>
</template>
<script setup lang='ts'>
import TableHeader from '@/components/table/header/TableHeader.vue'
import Table from '@/components/table/Table.vue'
import PopupForm from '@/components/form/PopupForm.vue'
import type { TableConfig } from '@/components/table/interface'
import type { ComSearchConfig } from '@/components/search/interface'
import type { SystemRoleCreateVo, SystemRoleQueryVo, SystemRoleRespVo, SystemRoleUpdateVo } from '@/api/system/role'
import {
  createRoleApi,
  deleteRoleApi,
  findRoleApi,
  pageRoleApi,
  updateRoleApi,
  assignMenuApi,
  menuByRoleApi
} from '@/api/system/role'
import { menuList } from '@/api/system/menu'
import { DictTypeEnum } from '@/enums/DictTypeEnum'
import type { PopupFormConfig } from '@/components/form/interface'
import { ElLoading } from 'element-plus'

const tableRef = ref()
const tableHeaderRef = ref()
const popupFormRef = ref()
const menuAssignFormRef = ref()

const state = reactive<{
  selection: any[]
  query: SystemRoleQueryVo
  form: SystemRoleCreateVo | SystemRoleUpdateVo
  menuAssignForm: { roleId: number, menuIds: Array<number>, name?: string, code?: string }
  tableData: SystemRoleRespVo[]
  comSearchConfig: ComSearchConfig
  tableConfig: TableConfig
  popupFormConfig: PopupFormConfig
  menuAssignFormConfig: PopupFormConfig
}>({
  selection: [],
  query: {
    current: 1,
    size: 10,
    total: 0
  },
  form: {
    name: '',
    code: '',
    sort: 0,
    status: 1
  },
  menuAssignForm: {
    roleId: 0,
    menuIds: []
  },
  tableData: [],
  comSearchConfig: [
    {
      label: '状态',
      prop: 'status',
      placeholder: '角色状态',
      render: 'dict',
      dictType: DictTypeEnum.COMMON_STATUS
    }
  ],
  tableConfig: {
    rowKey: 'id',
    selection: true,
    pagination: true,
    columns: [
      { label: '角色编号', prop: 'id', render: 'text' },
      { label: '角色名称', prop: 'name', render: 'text' },
      { label: '角色编码', prop: 'code', render: 'text' },
      { label: '显示顺序', prop: 'sort', render: 'text' },
      { label: '角色状态', prop: 'status', dictRender: 'switch', dictType: DictTypeEnum.COMMON_STATUS },
      { label: '备注', prop: 'remark', render: 'text' }
    ],
    operate: {
      buttons: ['edit', 'delete'],
      width: 150
    }
  },
  popupFormConfig: {
    title: '新增角色',
    formItemConfigs: [
      { label: '角色名称', prop: 'name', placeholder: '角色名称', render: 'text' },
      {
        label: '角色编码',
        prop: 'code',
        placeholder: '角色权限字符串',
        render: 'text'
      },
      { label: '显示顺序', prop: 'sort', placeholder: '显示顺序', render: 'number' },
      {
        label: '角色状态',
        prop: 'status',
        placeholder: '角色状态',
        dictRender: 'radio',
        dictType: DictTypeEnum.COMMON_STATUS
      },
      { label: '备注', prop: 'remark', required: false, placeholder: '备注', render: 'textarea' }
    ]
  },
  menuAssignFormConfig: {
    title: '分配菜单权限',
    formItemConfigs: [
      { label: '角色名称', prop: 'name', disabled: true },
      { label: '角色编码', prop: 'code', disabled: true },
      {
        label: '菜单',
        prop: 'menuIds',
        placeholder: '请选择菜单',
        render: 'tree-checkbox',
        props: { label: 'name', value: 'id' }
      }
    ]
  }
})

async function onFieldChange(row: SystemRoleUpdateVo) {
  const data = toRaw(row)
  await updateRoleApi(data)
  await pageRole()
}

function onAdd() {
  state.popupFormConfig.title = '新增角色'
  state.popupFormConfig.isEdit = false
  popupFormRef.value.show()
}

async function onAssignMenu(row: any) {
  const service = ElLoading.service({ fullscreen: true })
  const { data } = await menuByRoleApi(row.id)
  service.close()
  state.menuAssignForm.menuIds = data
  state.menuAssignForm.roleId = row.id
  state.menuAssignForm.name = row.name
  state.menuAssignForm.code = row.code
  menuAssignFormRef.value.show()
}

async function onBatchDelete(ids: number[]) {
  await deleteRoleApi(ids.join(','))
  await pageRole()
}

function onBatchEdit(ids: number[]) {
  state.popupFormConfig.title = '修改角色'
  state.popupFormConfig.isEdit = true
  state.popupFormConfig.ids = [...ids]
  popupFormRef.value.show()
}

async function onSubmit(cb: Function) {
  const data = toRaw(state.form)
  if (state.popupFormConfig.isEdit) {
    await updateRoleApi(data as SystemRoleUpdateVo)
  } else {
    await createRoleApi(data as SystemRoleCreateVo)
  }
  cb && cb()
  await pageRole()
}

async function onMenuAssignSubmit(cb: Function) {
  const data = toRaw(state.menuAssignForm)
  await assignMenuApi(data.roleId, data.menuIds)
  cb && cb()
}

async function findRole(id: number) {
  const { data } = await findRoleApi(id)
  state.form = data
}

async function pageRole() {
  state.tableConfig.loading = true
  const { data } = await pageRoleApi(toRaw(state.query))
  state.tableConfig.loading = false
  state.tableData = data.list
  state.query.total = data.total
}

async function findMenuList() {
  const { data } = await menuList({ status: 1 })
  state.menuAssignFormConfig.formItemConfigs[2].data = data
}

function onMenuAssignClose() {
  state.menuAssignForm = { roleId: 0, menuIds: [] }
}

function onClose() {
  state.form = {
    name: '',
    code: '',
    sort: 0,
    status: 1
  }
}

onMounted(() => {
  pageRole()
  findMenuList()
})
</script>

<script lang='ts'>
export default defineComponent({
  name: 'system-role'
})
</script>
