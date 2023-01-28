<template>
  <el-config-provider size='small'>
    <div class='box'>
      <PartUpload v-model='url' :size='10'></PartUpload>
      <Upload v-model='url1'></Upload>
    </div>
  </el-config-provider>
</template>

<script setup lang='ts'>
import PartUpload from '@/components/upload/PartUpload.vue'
import Upload from '@/components/upload/Upload.vue'

const url = ref('')
const url1 = ref('')
watch([url, url1], (val) => {
  console.log(val)
})
</script>

<style lang='scss'>
html, body, #app {
  width: 100vw;
  height: 100vh;
  margin: 0;
  padding: 0;
}

.box {
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
</style>
