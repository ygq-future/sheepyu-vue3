<template>
  <div class='default-main'>
    <Table
      ref='tableRef'
      :data='data'
      :config='config'
      @fieldChange='fieldChange'
      @select='select'
    />
  </div>
</template>

<script setup lang='ts'>
import Table from '@/components/table/table.vue'
import type { TableConfig } from '@/components/table/interface'

function select(selection: object[], row: object) {
  console.log(toRaw(selection), toRaw(row))
}

function fieldChange(value: object) {
  console.log(value)
}

const config: TableConfig = {
  border: true,
  columnConfigs: [
    { type: 'selection', width: 50 },
    { type: 'index', label: '索引', width: 80 },
    { prop: 'name', label: '名称', width: 100 },
    { prop: 'age', label: '年龄' },
    { prop: 'icon', label: '标识', render: 'icon' },
    { prop: 'status', label: '状态', render: 'switch' }
  ]
}

const data = reactive([
  {
    name: 'jack',
    age: 12,
    icon: 'el-icon-Plus',
    status: 0
  }, {
    name: 'xiaohei',
    age: 13,
    icon: 'el-icon-Minus',
    status: 1
  }
])
</script>

<style scoped lang='scss'>

</style>
