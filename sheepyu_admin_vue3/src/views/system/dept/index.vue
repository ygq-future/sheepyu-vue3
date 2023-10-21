<template>
  <div class='default-main'>
    <TableHeader
      ref='tableHeaderRef'
      v-model='state.query.keyword'
      auth='system:dept'
      :buttons="['add', 'edit', 'unfold']"
      :rows='state.selection'
      :com-search='false'
      unfold
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
      @edit='row => onBatchEdit([row.id])'
      @delete='row => onDelete(row.id)'
    >
      <template #buttons='{ data }'>
        <el-tooltip v-if='data?.type === DeptTypeEnum.DEPT' content='新增' placement='top' :show-after='500'>
          <el-button v-auth="'system:dept:create'" v-blur type='success' @click='onAdd(data?.id)'>
            <template #icon>
              <MyIcon name='el-icon-Plus' />
            </template>
          </el-button>
        </el-tooltip>
        <el-tooltip content='分配角色' placement='top' :show-after='500'>
          <el-button v-auth="'system:role:assign'" v-blur type='warning' @click='onAssignRole(data)'>
            <template #icon>
              <MyIcon name='fa fa-odnoklassniki' />
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

    <PopupForm
      ref='roleAssignFormRef'
      :form='state.roleAssignForm'
      :config='state.roleAssignFormConfig'
      @close='onRoleAssignClose'
      @submit='onRoleAssignSubmit'
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
  assignRoleToDeptApi,
  createDeptApi,
  deleteDeptApi,
  deptRoleApi,
  findDeptApi,
  listDeptApi,
  roleByDeptApi,
  treeDeptApi,
  updateDeptApi
} from '@/api/system/dept'
import type { PopupFormConfig } from '@/components/form/interface'
import ComSearch from '@/components/search/ComSearch.vue'
import { DictTypeEnum } from '@/enums/DictTypeEnum'
import { listUserApi } from '@/api/system/user'
import { ElLoading } from 'element-plus'
import { DeptTypeEnum } from '@/enums/DeptTypeEnum'

const tableRef = shallowRef()
const tableHeaderRef = shallowRef()
const popupFormRef = shallowRef()
const roleAssignFormRef = shallowRef()

const state = reactive<{
  selection: any[]
  query: SystemDeptQueryVo
  form: SystemDeptCreateVo | SystemDeptUpdateVo
  roleAssignForm: {
    deptId: number,
    roleIds: Array<number>,
    name?: string,
    leaderNicknames?: string
  }
  tableData: SystemDeptRespVo[]
  comSearchConfig: ComSearchConfig
  tableConfig: TableConfig
  popupFormConfig: PopupFormConfig
  roleAssignFormConfig: PopupFormConfig
}>({
  selection: [],
  query: {},
  form: {
    name: '',
    sort: 0,
    type: 0
  },
  roleAssignForm: {
    deptId: 0,
    roleIds: []
  },
  tableData: [],
  comSearchConfig: [],
  tableConfig: {
    rowKey: 'id',
    selection: true,
    columns: [
      { label: '编号', prop: 'id', render: 'text' },
      { label: '名称', prop: 'name', render: 'text' },
      { label: '负责人', prop: 'leaderNicknames', render: 'text' },
      { label: '类型', prop: 'type', dictRender: 'tag', dictType: DictTypeEnum.SYSTEM_DEPT_TYPE },
      { label: '显示顺序', prop: 'sort', render: 'text' },
      { label: '联系电话', prop: 'phone', render: 'text' },
      { label: '邮箱', prop: 'email', render: 'text' }
    ],
    operate: {
      buttons: ['edit', 'delete'],
      width: 160
    }
  },
  popupFormConfig: {
    title: '新增部门/职位',
    labelWidth: 120,
    formItemConfigs: [
      {
        label: '上级',
        prop: 'parentId',
        placeholder: '上级',
        render: 'tree-select',
        showCheckbox: false,
        props: { label: 'name', value: 'id' }
      },
      { label: '名称', prop: 'name', placeholder: '名称', render: 'text' },
      { label: '类型', prop: 'type', dictRender: 'radio', dictType: DictTypeEnum.SYSTEM_DEPT_TYPE },
      {
        label: '负责人',
        prop: 'leaderUserIds',
        required: false,
        multiple: true,
        placeholder: '负责人',
        render: 'select',
        props: { label: 'nickname' }
      },
      {
        label: '可查询部门/职位',
        prop: 'targetDeptIds',
        required: false,
        multiple: true,
        placeholder: '可查询部门/职位',
        render: 'tree-select',
        tip: '这里表示哪些部门/职位可以查询此部门/职位及其下的所有用户(只能管理员分配)',
        props: { label: 'name' }
      },
      { label: '显示顺序', prop: 'sort', placeholder: '显示顺序', render: 'number' },
      { label: '联系电话', prop: 'phone', required: false, placeholder: '联系电话', render: 'text' },
      { label: '邮箱', prop: 'email', required: false, placeholder: '邮箱', render: 'text' }
    ]
  },
  roleAssignFormConfig: {
    title: '分配角色',
    labelWidth: 120,
    width: 500,
    formItemConfigs: [
      { label: '名称', prop: 'name', disabled: true },
      { label: '负责人', prop: 'leaderNicknames', disabled: true, required: false },
      {
        label: '角色',
        prop: 'roleIds',
        placeholder: '请选择角色',
        render: 'tree-select',
        multiple: true,
        required: false,
        leftOnly: true,
        checkStrictly: false
      }
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
  state.popupFormConfig.disabledProps = []
  if (id) state.form.parentId = id
  state.popupFormConfig.title = '新增部门/职位'
  state.popupFormConfig.isEdit = false
  popupFormRef.value.show()
}

async function onAssignRole(row: SystemDeptRespVo) {
  const service = ElLoading.service({ fullscreen: true })
  const { data } = await roleByDeptApi(row.id)
  service.close()
  state.roleAssignForm.roleIds = data
  state.roleAssignForm.deptId = row.id
  state.roleAssignForm.name = row.name
  state.roleAssignForm.leaderNicknames = row.leaderNicknames
  roleAssignFormRef.value.show()
}

async function onDelete(id: number) {
  await deleteDeptApi(id)
  await listDept()
}

function onBatchEdit(ids: number[]) {
  state.popupFormConfig.disabledProps = ['parentId', 'type']
  state.popupFormConfig.hideProps = ['parentId']
  state.popupFormConfig.title = '修改部门/职位'
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

async function onRoleAssignSubmit(cb: Function) {
  const data = toRaw(state.roleAssignForm)
  await assignRoleToDeptApi(data.deptId, data.roleIds)
  cb && cb()
}

async function findDept(id: number) {
  const { data } = await findDeptApi(id)
  state.form = data
}

async function listDept() {
  state.tableConfig.loading = true
  const { data } = await listDeptApi(toRaw(state.query))
  const { data: treeDept } = await treeDeptApi()
  const { data: userList } = await listUserApi({})
  const { data: deptRoleTree } = await deptRoleApi()
  state.tableConfig.loading = false
  state.tableData = data
  state.popupFormConfig.formItemConfigs[0].data = treeDept
  state.popupFormConfig.formItemConfigs[3].data = userList
  state.popupFormConfig.formItemConfigs[4].data = data
  state.roleAssignFormConfig.formItemConfigs[2].data = deptRoleTree
  await nextTick(() => {
    tableRef.value && tableRef.value.expandAll(tableHeaderRef.value.getUnfold())
  })
}

function onClose() {
  state.form = {
    name: '',
    sort: 0,
    type: 0
  }
}

function onRoleAssignClose() {
  state.roleAssignForm = { deptId: 0, roleIds: [] }
}

watch(
  () => state.form.type,
  value => {
    state.popupFormConfig.hideProps = value === 0 ? [] : ['leaderUserIds', 'email', 'phone']
  },
  { immediate: true }
)

onMounted(() => {
  listDept()
})

onActivated(() => {
  listDept()
})
</script>

<script lang='ts'>
export default defineComponent({
  name: 'system-dept'
})
</script>
