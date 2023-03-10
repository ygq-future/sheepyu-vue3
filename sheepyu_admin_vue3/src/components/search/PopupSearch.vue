<template>
  <el-dialog
    append-to-body
    destroy-on-close
    v-model='state.dialogVisible'
    :style='{maxWidth: `${props.config.maxWidth ?? 700}px`, minWidth: "370px"}'
    :title='props.config.title'
    :width='props.config.width'
    :close-on-click-modal='false'
    @close='hide'
  >
    <el-scrollbar max-height='400px'>
      <TableHeader
        ref='tableHeaderRef'
        v-model='state.keyword'
        :com-search='false'
        @refresh='emits("search", state.keyword)'
        @input-enter='emits("search", state.keyword)'
        @input-clear='$nextTick(() => emits("search", state.keyword))'
      />

      <Table
        ref='tableRef'
        v-model:selection='state.selection'
        :data='props.config.data || []'
        :table-config='props.config.tableConfig'
        :pagination='state.query'
      />
    </el-scrollbar>

    <template #footer>
      <span class='dialog-footer'>
        <el-button @click='hide'>取消</el-button>
        <el-button type='primary' @click='onConfirm'>提交</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang='ts'>
import type { PopupSearchConfig } from '@/components/search/interface'

const props = defineProps<{
  config: PopupSearchConfig
}>()

const emits = defineEmits<{
  (e: 'close'): void
  (e: 'confirm', selection: any[]): void
  (e: 'search', keyword: string): void
}>()

const state = reactive<{
  dialogVisible: boolean
  selection: any[]
  keyword: string
}>({
  dialogVisible: false,
  selection: [],
  keyword: ''
})

function onConfirm() {
  emits('confirm', state.selection || [])
  hide()
}

function show() {
  emits('search', state.keyword)
  state.dialogVisible = true
}

function hide() {
  emits('close')
  state.dialogVisible = false
}

defineExpose({
  show,
  hide
})
</script>

<style scoped lang='scss'>

</style>