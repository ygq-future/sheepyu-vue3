<template>
  <el-config-provider size='small'>
    <div class='box'>
      <IconSelector v-model='icon'></IconSelector>
    </div>
  </el-config-provider>
</template>

<script setup lang='ts'>
import IconSelector from '@/components/icon/IconSelector.vue'
import iconFontInit from '@/util/iconfont'

const icon = ref('iconfont icon-kehuquanyi')
watch(icon, (value) => {
  console.log(value)
}, {
  immediate: true
})

iconFontInit()
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
