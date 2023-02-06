<template>
  <div class='default-main' style='height: 100vh'>
    <el-card>
      Home
      <el-form :model='form'>
        <el-input v-model='form.name'></el-input>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang='ts'>
const form = reactive({})
</script>

<style scoped lang='scss'>

</style>
