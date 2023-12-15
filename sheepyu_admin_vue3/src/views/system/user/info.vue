<template>
  <div class='default-main'>
    <el-row :gutter='20'>
      <el-col :xs='24' :md='8' :lg='8'>
        <el-card>
          <template #header>
            <span class='info'>个人信息</span>
          </template>
          <div class='content-list'>
            <div class='content-item flex-center'>
              <ImageUpload v-model='state.userForm.avatar' width='150px' @complete='onAvatarComplete' />
            </div>
            <div class='content-item'>
              <span><MyIcon name='fa fa-user' />用户名:</span>
              <span>{{ state.user.username }}</span>
            </div>
            <div class='content-item'>
              <span><MyIcon name='el-icon-Coordinate' />昵称:</span>
              <span>{{ state.user.nickname }}</span>
            </div>
            <div class='content-item'>
              <span><MyIcon name='fa fa-phone' />电话:</span>
              <span>{{ state.user.mobile }}</span>
            </div>
            <div class='content-item'>
              <span><MyIcon name='fa fa-envelope-o' />邮箱:</span>
              <span>{{ state.user.email }}</span>
            </div>
            <div class='content-item'>
              <span><MyIcon name='fa fa-sitemap' />部门/职位:</span>
              <span>{{ state.user.deptNames }}</span>
            </div>
            <div class='content-item'>
              <span><MyIcon name='fa fa-calendar-plus-o' />注册日期:</span>
              <span>{{ state.user.createTime }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs='24' :md='16' :lg='16'>
        <el-card class='right'>
          <template #header>
            <span class='info'>基本资料</span>
          </template>
          <el-tabs>
            <el-tab-pane label='基本资料'>
              <el-form :rules='rules' :model='state.userForm' label-width='80px'>
                <el-form-item label='用户昵称' prop='nickname'>
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
              <el-form ref='passFormRef' :model='state.passForm' label-width='80px' :rules='passRule'>
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
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang='ts'>
import type { SystemUserRespVo, SystemUserBaseInfoVo } from '@/api/system/user'
import { updatePassApi, updateUserBaseApi, userInfoApi } from '@/api/system/user'
import type { FormRules } from 'element-plus'
import { ElForm, ElNotification } from 'element-plus'
import { useUser } from '@/stores/user/user'
import ImageUpload from '@/components/upload/ImageUpload.vue'

const instance = getCurrentInstance()
const passFormRef = shallowRef<InstanceType<typeof ElForm>>()
const user = useUser()

const state = reactive<{
  user: SystemUserRespVo
  userForm: SystemUserBaseInfoVo
  passForm: {
    password: string,
    newPass: string,
    confirmPass: string
  }
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
    nickname: ''
  },
  passForm: {
    password: '',
    newPass: '',
    confirmPass: ''
  }
})

const rules: FormRules = {
  nickname: [{ required: true, message: '昵称不能为空', trigger: 'blur' }]
}

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

function onAvatarComplete(url: string) {
  state.userForm.avatar = url
  updateUser()
}

async function updateUser() {
  await updateUserBaseApi(toRaw(state.userForm))
  await userInfo()
}

async function userInfo() {
  const { data } = await userInfoApi()
  state.user = data
  state.userForm.nickname = data.nickname
  state.userForm.mobile = data.mobile || ''
  state.userForm.email = data.email || ''
  state.userForm.avatar = data.avatar || ''
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
  width: 100%;
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
    gap: 15px;
    border-bottom: 1px solid lightgray;

    span:nth-child(1) {
      display: flex;
      align-items: center;
      gap: 5px;
    }
  }

  .flex-center {
    width: 100%;
    display: flex;
    justify-content: center !important;
  }
}

.el-card {
  width: 100%;
  font-size: 14px;
  margin-bottom: 20px;
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
</style>
