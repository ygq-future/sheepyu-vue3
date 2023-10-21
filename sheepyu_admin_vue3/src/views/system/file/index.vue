<template>
  <div class='default-main'>
    <TableHeader
      v-model='state.query.keyword'
      auth='system:file'
      :buttons='["delete"]'
      :rows='state.selection'
      @batch-delete='onBatchDelete'
      @refresh='pageFile'
      @input-enter='pageFile'
      @input-clear='$nextTick(() => pageFile())'
    >
      <template #comSearch>
        <ComSearch
          :com-search-config='state.comSearchConfig'
          :form='state.query'
          @search='pageFile'
          @reset='pageFile'
        />
      </template>
      <template #buttons>
        <el-tooltip :show-after='500' content='上传文件' placement='top'>
          <el-button v-auth="'system:file:create'" v-blur type='primary' @click='onCreate'>
            <MyIcon name='el-icon-UploadFilled' />
            <span class='button-text'>上传文件</span>
          </el-button>
        </el-tooltip>
      </template>
    </TableHeader>

    <Table
      auth='system:file'
      v-model:selection='state.selection'
      :data='state.tableData'
      :table-config='state.tableConfig'
      :pagination='state.query'
      @delete='(row) => onBatchDelete([row.id])'
      @current-change='pageFile'
      @size-change='pageFile'
    >
    </Table>

    <PopupForm
      ref='popupFormRef'
      :form='state.form'
      :config='state.popupFormConfig'
      @submit='onSubmit'
    />
  </div>
</template>
<script setup lang='ts'>
import TableHeader from '@/components/table/header/TableHeader.vue'
import Table from '@/components/table/Table.vue'
import type { TableConfig } from '@/components/table/interface'
import type { ComSearchConfig } from '@/components/search/interface'
import type { SystemFileQueryVo, SystemFileRespVo } from '@/api/system/file'
import { deleteFileApi, pageFileApi } from '@/api/system/file'
import { DictTypeEnum } from '@/enums/DictTypeEnum'
import { onMounted, reactive, toRaw } from 'vue'
import type { PopupFormConfig } from '@/components/form/interface'
import ComSearch from '@/components/search/ComSearch.vue'
import PopupForm from '@/components/form/PopupForm.vue'

const popupFormRef = shallowRef()

const state = reactive<{
  selection: any[]
  form: { url: string }
  query: SystemFileQueryVo
  tableData: SystemFileRespVo[]
  comSearchConfig: ComSearchConfig
  tableConfig: TableConfig
  popupFormConfig: PopupFormConfig
}>({
  selection: [],
  form: { url: '' },
  query: {
    current: 1,
    size: 10,
    total: 0
  },
  tableData: [],
  comSearchConfig: [
    { label: '文件大小(MB)', prop: 'sizes', placeholder: '文件大小(MB)', render: 'number-range' },
    {
      label: '是否完成',
      prop: 'complete',
      placeholder: '是否完成',
      render: 'dict',
      dictType: DictTypeEnum.SYSTEM_FILE_COMPLETE
    },
    { label: '创建时间', prop: 'createTimes', placeholder: '创建时间', render: 'datetime' }
  ],
  tableConfig: {
    rowKey: 'id',
    pagination: true,
    selection: true,
    columns: [
      { label: '上传ID', prop: 'uploadId', render: 'text', width: 100, showTip: true },
      { label: '文件名', prop: 'filename', render: 'text', width: 200, showTip: true },
      { label: 'URL', prop: 'url', render: 'text', width: 150, showTip: true },
      { label: 'MD5', prop: 'md5', render: 'text', showTip: true },
      { label: '文件类型', prop: 'mimeType', dictRender: 'tag', dictType: DictTypeEnum.FILE_TYPE, width: 100 },
      { label: '文件大小', prop: 'size', render: 'text', width: 100 },
      { label: '上传索引', prop: 'partIndex', render: 'text', width: 100 },
      { label: '上传地域', prop: 'domain', render: 'text', width: 100, showTip: true },
      { label: '相对路径', prop: 'path', render: 'text', width: 100, showTip: true },
      {
        label: '是否完成',
        prop: 'complete',
        dictRender: 'tag',
        dictType: DictTypeEnum.SYSTEM_FILE_COMPLETE,
        width: 100
      },
      { label: '备注', prop: 'remark', render: 'text' },
      { label: '创建者', prop: 'creator', render: 'text' },
      { label: '创建时间', prop: 'createTime', render: 'text', width: 130 }
    ],
    operate: {
      buttons: ['delete'],
      width: 80
    }
  },
  popupFormConfig: {
    title: '文件上传',
    labelWidth: 100,
    formItemConfigs: [
      { label: '分片上传', prop: 'url', render: 'part-upload' },
      { label: '单文件上传', prop: 'url', render: 'upload' }
    ]
  }
})

function onCreate() {
  popupFormRef.value.show()
}

async function onBatchDelete(ids: number[]) {
  await deleteFileApi(ids.join(','))
  await pageFile()
}

async function onSubmit(cb: Function) {
  await pageFile()
  cb && cb()
}

async function pageFile() {
  state.tableConfig.loading = true
  const params = toRaw(state.query)
  const sizes = params.sizes
  if (sizes && sizes[0]) {
    sizes[0] = sizes[0] * 1024 * 1024
  }
  if (sizes && sizes[1]) {
    sizes[1] = sizes[1] * 1024 * 1024
  }
  const { data } = await pageFileApi(params)
  state.tableConfig.loading = false
  state.tableData = data.list
  state.query.total = data.total
}

onMounted(() => {
  pageFile()
})

onActivated(() => {
  pageFile()
})
</script>

<script lang='ts'>
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'system-file'
})
</script>
