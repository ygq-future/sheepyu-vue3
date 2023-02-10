<template>
  <div class='default-main'>
    <el-card>
      DashBoard
      <icon-selector v-model='icon' />
      <el-tree
        ref='treeRef'
        :data='data'
        show-checkbox
        node-key='id'
        @check-change='checkChange'
      />
    </el-card>
  </div>
</template>

<script setup lang='ts'>
import IconSelector from '@/components/icon/IconSelector.vue'
import { ElTree } from 'element-plus'

const icon = ref('')
const treeRef = ref<InstanceType<typeof ElTree>>()

function checkChange() {
  console.log(treeRef.value?.getCheckedKeys())
}

const data = reactive<Array<any>>([
  {
    id: 0,
    label: 'aaa',
    children: [
      {
        id: 1,
        label: 'bbfdsfb'
      }, {
        id: 2,
        label: 'cfsdfsdcc'
      },
      {
        id: 3,
        label: 'bfdsfsdfbb'
      }, {
        id: 4,
        label: 'ccfdfsdc'
      }
    ]
  }
])
</script>

<style scoped lang='scss'>

</style>
