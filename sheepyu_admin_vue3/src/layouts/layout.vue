<template>
  <el-container class='layout-container'>
    <Aside />
    <el-container class='content-wrapper'>
      <NavBar />
      <Main></Main>
    </el-container>
  </el-container>
</template>

<script setup lang='ts'>
import Aside from '@/layouts/components/aside/aside.vue'
import NavBar from '@/layouts/components/navbar/navBar.vue'
import Main from '@/layouts/components/main/main.vue'
</script>

<style scoped lang='scss'>
.layout-container {
  height: 100%;
  width: 100%;
}

.content-wrapper {
  flex-direction: column;
  width: 100%;
  height: 100%;
}
</style>
