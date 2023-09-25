<template>
  <div class='container'>
    <el-image class='image' :src='notFoundSvg' />
    <el-text type='warning'>页面走丢了...</el-text>
    <div>
      <el-text size='small' type='info'>
        可能是你访问了错误的页面, 或者此页面你没有权限访问, 请返回上一级或者返回控制台!
      </el-text>
    </div>
    <el-link style='font-size: 14px' type='primary' @click='onBack'>返回上一级</el-link>
  </div>
</template>

<script setup lang='ts'>
import { useRouter } from 'vue-router'
import { ElNotification } from 'element-plus'
import notFoundSvg from '@/assets/404.svg'

const router = useRouter()
const notification = ElNotification.warning('页面走丢了!')

function onBack() {
  router.replace('/')
}
</script>

<style lang='scss' scoped>
.container {
  width: 100vw;
  height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 15px;
  user-select: none;

  .image {
    width: 600px;
  }
}
</style>
