<template>
  <el-config-provider :locale='zhCn'>
    <router-view></router-view>
  </el-config-provider>
</template>

<script setup lang='ts'>
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import { useDict } from '@/stores/dict/dict'
import { dictTypeList } from '@/api/system/dict'

const dict = useDict()

//初始化加载系统字典数据
async function findDictTypeList() {
  const { data } = await dictTypeList()
  dict.setDictMap(data)
}

onMounted(() => {
  findDictTypeList()
})
</script>

<style lang='scss'>
html, body, #app {
  width: 100vw;
  height: 100vh;
  margin: 0;
  padding: 0;
}

html.light {
  background-color: #f5f5f5;
}

.default-main {
  margin: 16px;
  box-sizing: border-box;
}

.layout-shade {
  position: fixed;
  top: 0;
  left: 0;
  height: 100vh;
  width: 100vw;
  background-color: transparent;
  z-index: 9999;
}

@media screen and (max-width: 768px) {
  .xs-hide {
    display: none;
  }
}
</style>
