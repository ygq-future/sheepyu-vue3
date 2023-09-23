<template>

</template>

<script setup lang='ts'>
import { useRouter } from 'vue-router'
import { ElNotification } from 'element-plus'

const router = useRouter()
ElNotification.warning('页面走丢了, 正在返回上一级...')
setTimeout(() => {
  router.back()
}, 1000)
</script>


