<template>
  <div style='position: fixed;top: 0;left:0;bottom: 0;right: 0;'>
    <canvas id='canvas' />
  </div>
  <div class='theme-select'>
    <el-radio-group v-model='config.layout.colorModeIndex' @change='onColorModeChange' size='small'>
      <el-radio-button v-for='(item, index) in config.layout.colorMode' :label='index'>
        {{ item }}
      </el-radio-button>
    </el-radio-group>
  </div>
  <div class='box full'>
    <img class='img' src='@/assets/logo.png' alt=''>
    <span class='title'>SY管理后台登录</span>
    <el-form
      ref='formRef'
      :model='form'
      :rules='rules'
      label-width='0'
      size='default'
    >
      <el-form-item prop='login'>
        <el-input
          autofocus
          prefix-icon='el-icon-User'
          v-model='form.login'
          placeholder='用户名/邮箱/手机号'
        />
      </el-form-item>
      <el-form-item prop='password'>
        <el-input
          prefix-icon='el-icon-Lock'
          type='password'
          v-model='form.password'
          placeholder='密码'
        />
      </el-form-item>
      <el-form-item class='code' prop='code'>
        <el-input
          :disabled='!captchaEnable'
          prefix-icon='el-icon-More'
          v-model='form.code'
          placeholder='验证码'
          @keydown.enter='submit(formRef)'
        />
        <img v-if='captchaEnable'
             :src='captchaInfo.base64'
             alt='验证码'
             @click='getCaptcha'
             title='点击刷新'
        >
        <span v-else style='color: lightgray;user-select: none'>不用验证码</span>
      </el-form-item>
    </el-form>
    <el-button type='primary' @click='submit(formRef)'>登录</el-button>
  </div>
</template>

<script setup lang='ts'>
import { useConfig } from '@/stores/config/config'
import { useUser } from '@/stores/user/user'
import { useRouter } from 'vue-router'
import type { CaptchaRespVo, SystemUserLoginVo } from '@/api/system/user'
import { captcha, info, login } from '@/api/system/user'
import { ParticleLine } from '@/util/particleLine'
import type { ElForm, FormRules } from 'element-plus'
import { loadDict } from '@/util/common'
import { getConfig } from '@/api/system/config'
import { ConfigKeyEnum } from '@/enums/ConfigKeyEnum'

let particleLine: ParticleLine
const config = useConfig()
const user = useUser()
const router = useRouter()
const route = useRoute()
const formRef = ref<InstanceType<typeof ElForm>>()
const captchaEnable = ref<boolean>(true)
const form = reactive<SystemUserLoginVo>({
  login: '',
  password: ''
})
const captchaInfo = reactive<CaptchaRespVo>({
  base64: '',
  arithmetic: '',
  key: ''
})
const rules: FormRules = {
  login: [{ required: true, message: '用户名不能为空', trigger: 'blur' }],
  password: [{ required: true, message: '密码不能为空', trigger: 'blur' }]
}

function submit(formRef?: InstanceType<typeof ElForm>) {
  formRef?.validate(isValid => {
    if (!isValid) {
      return
    }

    let data = { ...toRaw(form) }
    form.code = ''
    data.key = captchaInfo.key
    data.code = captchaInfo.arithmetic + data.code
    login(data).then(res => {
      user.setAuthInfo(res.data)
      info().then(res => {
        user.setUserInfo(res.data)
        loadDict()
        const redirectUrl = route.query.redirectUrl as string
        router.push(redirectUrl ? redirectUrl : '/')
      })
    }).catch(() => {
      getCaptcha()
    })
  })
}

async function getCaptcha() {
  if (!captchaEnable.value) {
    return
  }

  const { data } = await captcha()
  if (!data) return
  captchaInfo.base64 = data.base64
  captchaInfo.arithmetic = data.arithmetic
  captchaInfo.key = data.key
}

function onColorModeChange(colorModeIndex: number) {
  config.changeColorMode(colorModeIndex)
  nextTick(() => {
    particleLine.isDark = config.layout.isDark
  })
}

onMounted(async () => {
  particleLine = new ParticleLine(config.layout.isDark)
  particleLine.init()
  const { data } = await getConfig(ConfigKeyEnum.CAPTCHA_ENABLE)
  captchaEnable.value = data
  await getCaptcha()
})
</script>

<style scoped lang='scss'>
.img {
  position: absolute;
  top: -40px;
  width: 80px;
  height: 80px;
  user-select: none;
  overflow: hidden;
  mask: no-repeat;
}

.theme-select {
  position: absolute;
  top: 5px;
  right: 5px;
}

.box {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  display: flex;
  flex-direction: column;
  align-items: center;
  z-index: 999;
  width: 400px;
  height: 300px;
  padding: 20px;
  box-shadow: var(--el-box-shadow-light);
  box-sizing: border-box;
  border-radius: var(--el-border-radius-base);

  .title {
    font-size: var(--el-font-size-extra-large);
    user-select: none;
    margin-top: 25px;
  }

  .el-form {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: center;
    width: 250px;
    box-sizing: border-box;
    margin-top: 20px;

    .code {
      display: flex;
      align-items: center;

      .el-input {
        width: 50%;
        margin-right: 20px;
      }

      img {
        flex: 1;
        height: 30px;
        user-select: none;
        cursor: pointer;
      }
    }
  }

  .el-button {
    width: 250px;
  }
}

@media screen and (max-width: 400px) {
  .full {
    width: 100% !important;
    box-shadow: none;
  }
}
</style>
