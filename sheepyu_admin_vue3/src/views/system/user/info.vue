<template>
  <div class='default-main'>
    <div class='card-container'>
      <el-card>
        <template #header>
          <span class='info'>个人信息</span>
        </template>
        <div class='content-list'>
          <div class='content-item flex-center'>
            <ImageUpload v-model='state.user.avatar' width='150px' />
          </div>
          <div class='content-item'>
            <span><Icon name='fa fa-user' />用户名:</span>
            <span>{{ state.user.username }}</span>
          </div>
          <div class='content-item'>
            <span><Icon name='el-icon-Coordinate' />昵称:</span>
            <span>{{ state.user.nickname }}</span>
          </div>
          <div class='content-item'>
            <span><Icon name='fa fa-phone' />电话:</span>
            <span>{{ state.user.mobile }}</span>
          </div>
          <div class='content-item'>
            <span><Icon name='fa fa-envelope-o' />邮箱:</span>
            <span>{{ state.user.email }}</span>
          </div>
          <div class='content-item'>
            <span><Icon name='fa fa-sitemap' />部门:</span>
            <span>{{ state.user.deptName }}</span>
          </div>
          <div class='content-item'>
            <span><Icon name='fa fa-sitemap' />职位:</span>
            <span>{{ state.user.postNames }}</span>
          </div>
          <div class='content-item'>
            <span><Icon name='fa fa-calendar-plus-o' />注册日期:</span>
            <span>{{ state.user.createTime }}</span>
          </div>
        </div>
      </el-card>

      <el-card class='right'>
        <template #header>
          <span class='info'>基本资料</span>
        </template>
        <el-tabs>
          <el-tab-pane label='基本资料'>
            <el-form :model='state.userForm' label-width='120px'>
              <el-form-item label='用户昵称'>
                <el-input v-model='state.userForm.nickname' />
              </el-form-item>

              <el-form-item label='手机号码'>
                <el-input v-model='state.userForm.mobile' />
              </el-form-item>

              <el-form-item label='邮箱'>
                <el-input v-model='state.userForm.email' />
              </el-form-item>

              <el-form-item>
                <el-button type='primary' @click='updateUser'>保存</el-button>
                <el-button type='danger' @click='close'>关闭</el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>

          <el-tab-pane label='修改密码'>
            <el-form ref='passFormRef' :model='state.passForm' label-width='120px' :rules='passRule'>
              <el-form-item prop='password' label='旧密码'>
                <el-input type='password' show-password v-model='state.passForm.password' />
              </el-form-item>

              <el-form-item prop='newPass' label='新密码'>
                <el-input type='password' show-password v-model='state.passForm.newPass' />
              </el-form-item>

              <el-form-item prop='confirmPass' label='确认密码'>
                <el-input type='password' show-password v-model='state.passForm.confirmPass' />
              </el-form-item>

              <el-form-item>
                <el-button type='primary' @click='updatePass'>保存</el-button>
                <el-button type='danger' @click='close'>关闭</el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </div>
  </div>
</template>

<script setup lang='ts'>
import { info, updateUserApi, updatePassApi } from '@/api/system/user'
import type { SystemUserRespVo } from '@/api/system/user'
import type { FormRules } from 'element-plus'
import { ElForm, ElNotification } from 'element-plus'
import { useUser } from '@/stores/user/user'

const instance = getCurrentInstance()
const passFormRef = ref<InstanceType<typeof ElForm>>()
const user = useUser()

const state = reactive<{
  user: SystemUserRespVo
  userForm: { nickname: string, mobile: string, email: string }
  passForm: { password: string, newPass: string, confirmPass: string }
}>({
  user: {
    id: 0,
    type: 2,
    nickname: '',
    status: 1,
    username: '',
    createTime: ''
  },
  userForm: {
    nickname: '',
    mobile: '',
    email: ''
  },
  passForm: {
    password: '',
    newPass: '',
    confirmPass: ''
  }
})

const passRule: FormRules = {
  password: [{ required: true, message: '原密码不能为空', trigger: 'blur' }],
  newPass: [{ required: true, message: '新密码不能为空', trigger: 'blur' }],
  confirmPass: [{ required: true, message: '确认密码不能为空', trigger: 'blur' }]
}

function close() {
  instance?.proxy?.$bus.emit('closeCurrentToRoute', '/dashboard')
}

async function updatePass() {
  const valid = await passFormRef.value!.validate(valid => valid)
  if (!valid) {
    return ElNotification.warning('密码不能为空')
  }

  if (state.passForm.newPass !== state.passForm.confirmPass) {
    return ElNotification.warning('密码不一致')
  }

  const data = { oldPass: state.passForm.password, newPass: state.passForm.newPass }
  await updatePassApi(data)
  ElNotification.success('下次登录生效')
}

async function updateUser() {
  const data = toRaw(state.user)
  data.nickname = state.userForm.nickname
  data.mobile = state.userForm.mobile
  data.email = state.userForm.email
  await updateUserApi(data)
  await userInfo()
}

async function userInfo() {
  const { data } = await info()
  state.user = data
  state.userForm.nickname = data.nickname
  state.userForm.mobile = data.mobile || ''
  state.userForm.email = data.email || ''
  user.setUserInfo(data)
}

onMounted(() => {
  userInfo()
})
</script>

<script lang='ts'>
export default defineComponent({
  name: 'system-user-info'
})
</script>


<style scoped lang='scss'>
.right {
  flex: 1;
  margin-left: 20px;
  height: fit-content;
}

.content-list {
  display: flex;
  flex-direction: column;
  align-items: center;

  .content-item {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 10px 0;
    border-bottom: 1px solid lightgray;

    .icon {
      margin-right: 5px;
    }

    span:nth-child(1) {
      display: flex;
      align-items: center;
    }
  }

  .flex-center {
    width: 100%;
    display: flex;
    justify-content: center !important;
  }
}

.el-card {
  width: 280px;
  font-size: 14px;
}

.info {
  display: inline-block;
  width: 100%;
  text-align: center;
}

:deep(.el-card__header) {
  padding: 10px 0 !important;
}

:deep(.el-card__body) {
  padding: 0 20px 20px 20px !important;
}

.card-container {
  display: flex;
}

.default-main {
  box-shadow: none !important;
}
</style>