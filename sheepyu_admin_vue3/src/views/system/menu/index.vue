<template>
  <div class='default-main'>
    <TableHeader
      :buttons="['add', 'edit', 'delete']"
      @input='onInput'
      @inputEnter='onInputEnter'
      @inputClear='onInputClear'
      v-model='state.form.keyword'
    >

      <template #btn>
        <el-tooltip content='新增' placement='top'>
          <el-button class='button-item' v-blur type='primary'>
            <Icon name='fa fa-plus' />
            <span class='button-text'>新增</span>
          </el-button>
        </el-tooltip>

        <el-tooltip content='编辑' placement='top'>
          <el-button v-blur type='primary'>
            <Icon name='fa fa-pencil' />
            <span class='button-text'>编辑</span>
          </el-button>
        </el-tooltip>
      </template>

      <template #comSearch>
        <ComSearch
          :com-search-config='comSearchConfig'
          v-model='state.form'
          @search='onSearch'
          @reset='onReset'
        />
      </template>
    </TableHeader>
  </div>
</template>

<script setup lang='ts'>
import TableHeader from '@/components/table/header/TableHeader.vue'
import type { ComSearchConfig } from '@/components/table/header/interface'

const state = reactive({
  form: {}
})

watch(state.form, value => {
  console.log(value)
})

const categories = [{
  id: 1,
  name: '手机'
}, {
  id: 2,
  name: '内衣'
}, {
  id: 3,
  name: '休闲'
}]

const comSearchConfig = reactive<ComSearchConfig>([
  { label: '名称', prop: 'name', placeholder: '名称模糊匹配' },
  { label: '年龄', prop: 'age', render: 'number', placeholder: '输入年龄' },
  { label: '职位', prop: 'position' },
  { label: '部门', prop: 'dept' },
  { label: '创建日期', prop: 'createDate', render: 'datetime' },
  { label: '创建精确时间', prop: 'createDateTime', render: 'datetime' },
  { label: '分类', prop: 'categoryId', render: 'select', selectOptions: categories, selectLabelKey: 'name' }
])

function onSearch(value: object) {
  console.log('search', value)
}

function onReset() {
  console.log('reset')
}

function onInput(value: string) {
  console.log(value)
}

function onInputEnter(value: string) {
  console.log(value)
}

function onInputClear() {
  console.log('clear')
}
</script>


<style scoped lang='scss'>

</style>
