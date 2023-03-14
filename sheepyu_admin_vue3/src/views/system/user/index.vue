<template>
  <div class='default-main'>
    <TableHeader
      ref='tableHeaderRef'
      v-model='state.query.keyword'
      auth='system:user'
      :buttons="['add', 'edit', 'export']"
      :rows='state.selection'
      @refresh='pageUser'
      @add='onAdd'
      @batch-edit='onBatchEdit'
      @input-enter='pageUser'
      @input-clear='$nextTick(() => pageUser())'
      @export='exportUser'
    >
      <template #comSearch>
        <ComSearch
          :com-search-config='state.comSearchConfig'
          :form='state.query'
          @search='pageUser'
          @reset='pageUser'
        />
      </template>
    </TableHeader>

    <Table
      ref='tableRef'
      v-model:selection='state.selection'
      auth='system:user'
      :data='state.tableData'
      :table-config='state.tableConfig'
      :pagination='state.query'
      @fieldChange='onFieldChange'
      @current-change='pageUser'
      @size-change='pageUser'
    >
      <template #buttons='scope'>
        <el-tooltip v-if='scope.data.type === 2' content='编辑' placement='top' :show-after='500'>
          <el-button
            v-auth='`system:user:update`'
            v-blur
            type='primary'
            @click='onBatchEdit([scope.data.id])'
          >
            <template #icon>
              <Icon name='el-icon-Edit' />
            </template>
          </el-button>
        </el-tooltip>

        <el-popconfirm
          v-if='scope.data.type === 2'
          confirm-button-type='danger'
          title='确认删除这条记录吗?'
          @confirm='onDelete(scope.data.id)'
        >
          <template #reference>
            <div>
              <el-tooltip :show-after='500' content='删除' placement='top'>
                <el-button v-auth='`system:user:delete`' v-blur type='danger'>
                  <template #icon>
                    <Icon name='el-icon-Delete' />
                  </template>
                </el-button>
              </el-tooltip>
            </div>
          </template>
        </el-popconfirm>

        <el-popconfirm
          v-if='scope.data.type === 2'
          confirm-button-type='danger'
          title='确认重置此用户密码吗?'
          @confirm='onResetPassword(scope.data)'
        >
          <template #reference>
            <div>
              <el-tooltip content='重置密码' placement='top' :show-after='500'>
                <el-button v-auth="'system:user:reset-password'" v-blur type='warning'>
                  <template #icon>
                    <Icon name='el-icon-RefreshRight' />
                  </template>
                </el-button>
              </el-tooltip>
            </div>
          </template>
        </el-popconfirm>

        <el-tooltip v-if='scope.data.type === 2' content='分配角色' placement='top' :show-after='500'>
          <el-button v-auth="'system:role:assign'" v-blur type='success' @click='onAssignRole(scope.data)'>
            <template #icon>
              <Icon name='fa fa-odnoklassniki' />
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
      @next='findUser'
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
import type { SystemUserCreateVo, SystemUserQueryVo, SystemUserRespVo, SystemUserUpdateVo } from '@/api/system/user'
import {
  createUserApi,
  deleteUserApi,
  findUserApi,
  pageUserApi,
  updateUserApi,
  exportUserApi,
  resetPasswordApi,
  assignRoleApi,
  roleByUserApi
} from '@/api/system/user'
import { listDeptApi } from '@/api/system/dept'
import { listPostApi } from '@/api/system/post'
import { listRoleApi } from '@/api/system/role'
import { DictTypeEnum } from '@/enums/DictTypeEnum'
import type { PopupFormConfig } from '@/components/form/interface'
import { ElLoading } from 'element-plus'

const tableRef = ref()
const tableHeaderRef = ref()
const popupFormRef = ref()
const roleAssignFormRef = ref()

const state = reactive<{
  selection: any[]
  query: SystemUserQueryVo
  form: SystemUserCreateVo | SystemUserUpdateVo
  roleAssignForm: { userId: number, roleIds: Array<number>, username?: string, nickname?: string }
  tableData: SystemUserRespVo[]
  comSearchConfig: ComSearchConfig
  tableConfig: TableConfig
  popupFormConfig: PopupFormConfig
  roleAssignFormConfig: PopupFormConfig
}>({
  selection: [],
  query: {
    current: 1,
    size: 10,
    total: 0,
    type: 2
  },
  form: {
    username: '',
    password: '',
    nickname: '',
    status: 1
  },
  roleAssignForm: {
    userId: 0,
    roleIds: []
  },
  tableData: [],
  comSearchConfig: [
    {
      label: '部门',
      prop: 'deptId',
      placeholder: '部门',
      render: 'tree-select',
      selectIdKey: 'id',
      selectLabelKey: 'name'
    },
    {
      label: '状态',
      prop: 'status',
      placeholder: '帐号状态',
      render: 'dict',
      dictType: DictTypeEnum.COMMON_STATUS
    },
    {
      label: '身份类型',
      prop: 'type',
      placeholder: '身份类型',
      render: 'dict',
      dictType: DictTypeEnum.SYSTEM_USER_TYPE
    },
    { label: '最后登录时间', prop: 'loginTimes', placeholder: '最后登录时间', render: 'datetime' },
    { label: '创建时间', prop: 'createTimes', placeholder: '创建时间', render: 'datetime' }
  ],
  tableConfig: {
    rowKey: 'id',
    selection: true,
    pagination: true,
    columns: [
      { label: '编号', prop: 'id', render: 'text' },
      { label: '用户名', prop: 'username', render: 'text' },
      { label: '昵称', prop: 'nickname', render: 'text' },
      { label: '邮箱', prop: 'email', render: 'text' },
      { label: '手机号码', prop: 'mobile', render: 'text', width: 120 },
      { label: '头像', prop: 'avatar', render: 'img', width: 130 },
      { label: '部门', prop: 'deptName', render: 'text' },
      { label: '职位', prop: 'postNames', render: 'text' },
      { label: '状态', prop: 'status', dictRender: 'switch', dictType: DictTypeEnum.COMMON_STATUS },
      { label: '类型', prop: 'type', dictRender: 'tag', dictType: DictTypeEnum.SYSTEM_USER_TYPE, width: 90 },
      { label: '备注', prop: 'remark', render: 'text' },
      { label: '登录IP', prop: 'loginIp', render: 'text', width: 100 },
      { label: '登录时间', prop: 'loginTime', render: 'text', width: 100 },
      { label: '创建时间', prop: 'createTime', render: 'text', width: 100 }
    ],
    operate: {
      buttons: [],
      width: 160
    }
  },
  popupFormConfig: {
    title: '新增用户',
    formItemConfigs: [
      { label: '用户名', prop: 'username', placeholder: '用户账号', render: 'text' },
      { label: '密码', prop: 'password', placeholder: '密码', render: 'password' },
      { label: '昵称', prop: 'nickname', required: false, placeholder: '用户昵称', render: 'text' },
      { label: '邮箱', prop: 'email', required: false, placeholder: '用户邮箱', render: 'text' },
      { label: '手机号码', prop: 'mobile', required: false, placeholder: '手机号码', render: 'text' },
      { label: '头像', prop: 'avatar', required: false, placeholder: '头像地址', render: 'upload' },
      {
        label: '部门',
        prop: 'deptId',
        required: false,
        placeholder: '部门',
        render: 'tree-select',
        props: { label: 'name', value: 'id' }
      },
      {
        label: '职位',
        prop: 'postIds',
        required: false,
        placeholder: '职位',
        render: 'select',
        multiple: true,
        props: { label: 'name', value: 'id' }
      },
      {
        label: '状态',
        prop: 'status',
        placeholder: '状态',
        dictRender: 'radio',
        dictType: DictTypeEnum.COMMON_STATUS
      },
      { label: '备注', prop: 'remark', required: false, placeholder: '备注', render: 'textarea' }
    ]
  },
  roleAssignFormConfig: {
    title: '分配角色',
    formItemConfigs: [
      { label: '用户名', prop: 'username', disabled: true },
      { label: '昵称', prop: 'nickname', disabled: true },
      {
        label: '角色',
        prop: 'roleIds',
        placeholder: '请选择角色',
        render: 'select',
        multiple: true,
        props: { label: 'name', value: 'id' }
      }
    ]
  }
})

async function onFieldChange(row: SystemUserUpdateVo) {
  const data = toRaw(row)
  await updateUserApi(data)
  await pageUser()
}

async function onResetPassword(row: any) {
  state.tableConfig.loading = true
  await resetPasswordApi(row.id)
  state.tableConfig.loading = false
}

function onAdd() {
  state.popupFormConfig.title = '新增用户'
  state.popupFormConfig.isEdit = false
  state.popupFormConfig.hideProps = []
  popupFormRef.value.show()
}

async function onAssignRole(row: any) {
  const service = ElLoading.service({ fullscreen: true })
  const { data } = await roleByUserApi(row.id)
  service.close()
  state.roleAssignForm.roleIds = data
  state.roleAssignForm.userId = row.id
  state.roleAssignForm.username = row.username
  state.roleAssignForm.nickname = row.nickname
  roleAssignFormRef.value.show()
}

async function onDelete(id: number) {
  await deleteUserApi(id)
  await pageUser()
}

function onBatchEdit(ids: number[]) {
  state.popupFormConfig.title = '修改用户'
  state.popupFormConfig.isEdit = true
  state.popupFormConfig.ids = [...ids]
  state.popupFormConfig.hideProps = ['username', 'password']
  popupFormRef.value.show()
}

async function onSubmit(cb: Function) {
  const data = toRaw(state.form)
  if (state.popupFormConfig.isEdit) {
    await updateUserApi(data as SystemUserUpdateVo)
  } else {
    await createUserApi(data as SystemUserCreateVo)
  }
  cb && cb()
  await pageUser()
}

async function onRoleAssignSubmit(cb: Function) {
  const data = toRaw(state.roleAssignForm)
  await assignRoleApi(data.userId, data.roleIds)
  cb && cb()
}

async function findUser(id: number) {
  const { data } = await findUserApi(id)
  state.form = data
}

async function pageUser() {
  state.tableConfig.loading = true
  const { data } = await pageUserApi(toRaw(state.query))
  state.tableConfig.loading = false
  state.tableData = data.list
  state.query.total = data.total
}

async function listDept() {
  const { data } = await listDeptApi({})
  state.comSearchConfig[0].selectOptions = data
  state.popupFormConfig.formItemConfigs[6].data = data
}

async function listPost() {
  const { data } = await listPostApi()
  state.popupFormConfig.formItemConfigs[7].data = data
}

async function listRole() {
  const { data } = await listRoleApi()
  state.roleAssignFormConfig.formItemConfigs[2].data = data
}

function exportUser() {
  const service = ElLoading.service({ fullscreen: true })
  exportUserApi(toRaw(state.query)).then(() => {
    service.close()
  }).catch(() => {
    service.close()
  })
}

function onRoleAssignClose() {
  state.roleAssignForm = { userId: 0, roleIds: [] }
}

function onClose() {
  state.form = {
    username: '',
    password: '',
    nickname: '',
    status: 1
  }
}

onMounted(() => {
  pageUser()
  listDept()
  listPost()
  listRole()
})
</script>

<script lang='ts'>
export default defineComponent({
  name: 'system-user'
})
</script>
