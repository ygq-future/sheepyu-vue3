<template>
  <div class='default-main'>
    <el-card>
      DashBoard
      <icon-selector v-model='icon' default />
    </el-card>
  </div>
</template>

<script setup lang='ts'>
import IconSelector from '@/components/icon/IconSelector.vue'

const icon = ref('')
</script>

<script lang='ts'>
export default defineComponent({
  name: 'dashboard'
})
</script>

<style scoped lang='scss'>

</style>
