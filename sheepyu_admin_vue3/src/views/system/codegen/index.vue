<template>
  <div class='default-main'>
    <TableHeader
      ref='tableHeaderRef'
      v-model='state.query.keyword'
      auth='system:codegen'
      :buttons="['delete']"
      :rows='state.selection'
      @refresh='pageCodegen'
      @batch-delete='onBatchDelete'
      @input-enter='pageCodegen'
      @input-clear='$nextTick(() => pageCodegen())'
    >
      <template #comSearch>
        <ComSearch
          :com-search-config='state.comSearchConfig'
          :form='state.query'
          @search='pageCodegen'
          @reset='pageCodegen'
        />
      </template>

      <template #buttons>
        <el-tooltip :show-after='500' content='导入代码生成表' placement='top'>
          <div class='button-item' v-auth="'system:codegen:create'">
            <el-button v-blur type='primary' @click='popupSearchRef.show()'>
              <Icon name='fa fa-sign-in' />
              <span class='button-text'>导入生成表</span>
            </el-button>
          </div>
        </el-tooltip>
      </template>
    </TableHeader>

    <Table
      ref='tableRef'
      v-model:selection='state.selection'
      auth='system:codegen'
      :data='state.tableData'
      :table-config='state.tableConfig'
      :pagination='state.query'
      @edit='(row) => onEdit(row.id)'
      @delete='(row) => onBatchDelete([row.id])'
      @current-change='pageCodegen'
      @size-change='pageCodegen'
    >
      <template #buttons='scope'>
        <el-tooltip content='预览' placement='top' :show-after='500'>
          <el-button v-auth="'system:codegen:generate'" v-blur type='warning' @click='previewCodegen(scope.data.id)'>
            <template #icon>
              <Icon name='el-icon-View' />
            </template>
          </el-button>
        </el-tooltip>

        <el-tooltip content='生成代码' placement='top' :show-after='500'>
          <el-button v-auth="'system:codegen:generate'" v-blur type='success' @click='codegenGenerate(scope.data.id)'>
            <template #icon>
              <Icon name='fa fa-file-code-o' />
            </template>
          </el-button>
        </el-tooltip>

        <el-tooltip content='同步' placement='top' :show-after='500'>
          <el-button v-auth="'system:codegen:create'" v-blur type='info' @click='syncCodegen(scope.data.id)'>
            <template #icon>
              <Icon name='el-icon-Refresh' />
            </template>
          </el-button>
        </el-tooltip>
      </template>
    </Table>

    <PopupSearch
      ref='popupSearchRef'
      :config='state.popupSearchConfig'
      @search='tableList'
      @confirm='onCreate'
    />

    <el-dialog v-model='state.previewShow' title='代码预览' :close-on-click-modal='false' width='1000px' center>
      <div class='code-panel'>
        <el-scrollbar class='left'>
          <div
            :class='["item", state.previewCodeTab === item[0] ? "active" : ""]'
            v-for='item in state.previewCodes'
            @click='changeCodeTab(item[0])'
          >
            {{ item[0] }}
          </div>
        </el-scrollbar>
        <el-scrollbar class='right'>
          <el-tabs v-model='state.previewCodeTab'>
            <el-tab-pane v-for='item in state.previewCodes' :label='item[0]' :name='item[0]'>
              <el-scrollbar>
                <pre><code v-html='highlightCode(item[0], item[1])'></code></pre>
              </el-scrollbar>
            </el-tab-pane>
          </el-tabs>
        </el-scrollbar>
      </div>
    </el-dialog>
  </div>
</template>
<script setup lang='ts'>
import TableHeader from '@/components/table/header/TableHeader.vue'
import Table from '@/components/table/Table.vue'
import PopupSearch from '@/components/search/PopupSearch.vue'
import type { ComSearchConfig, TableConfig } from '@/components/table/interface'
import type {
  SystemCodegenQueryVo,
  SystemCodegenRespVo
} from '@/api/system/codegen'
import {
  createCodegenApi,
  deleteCodegenApi,
  pageCodegenApi,
  tableListApi,
  codegenGenerateApi, syncCodegenApi, previewCodegenApi
} from '@/api/system/codegen'
import { DictTypeEnum } from '@/enums/DictTypeEnum'
import type { PopupSearchConfig } from '@/components/search/interface'
import { useTabs } from '@/stores/tabs/tabs'
import { ElLoading, ElNotification } from 'element-plus'
import 'highlight.js/styles/atom-one-dark.css'
import highlight from 'highlight.js'
import java from 'highlight.js/lib/languages/java'
import xml from 'highlight.js/lib/languages/java'
import vue from 'highlight.js/lib/languages/java'
import ts from 'highlight.js/lib/languages/java'

highlight.registerLanguage('java', java)
highlight.registerLanguage('xml', xml)
highlight.registerLanguage('vue', vue)
highlight.registerLanguage('ts', ts)

const tabs = useTabs()
const router = useRouter()
const tableRef = ref()
const tableHeaderRef = ref()
const popupSearchRef = ref()

const state = reactive<{
  previewShow: boolean
  previewCodeTab?: string
  previewCodes?: Map<string, string>
  selection: any[]
  query: SystemCodegenQueryVo
  tableData: SystemCodegenRespVo[]
  comSearchConfig: ComSearchConfig
  tableConfig: TableConfig
  popupSearchConfig: PopupSearchConfig
}>({
  previewShow: false,
  selection: [],
  query: {
    current: 1,
    size: 10,
    total: 0
  },
  tableData: [],
  comSearchConfig: [
    {
      label: '场景',
      prop: 'scene',
      placeholder: '选择场景',
      render: 'dict',
      dictType: DictTypeEnum.SYSTEM_CODEGEN_SCENE
    },
    { label: '创建时间', prop: 'createTimes', placeholder: '创建时间', render: 'datetime' }
  ],
  tableConfig: {
    selection: true,
    pagination: true,
    columns: [
      { label: '场景', prop: 'scene', dictRender: 'tag', dictType: DictTypeEnum.SYSTEM_CODEGEN_SCENE },
      { label: '表名称', prop: 'tableName', render: 'text', width: 150 },
      { label: '表描述', prop: 'tableComment', render: 'text' },
      { label: '模块名', prop: 'moduleName', render: 'text' },
      { label: '业务名', prop: 'businessName', render: 'text' },
      { label: '类名称', prop: 'className', render: 'text' },
      { label: '类描述', prop: 'classComment', render: 'text' },
      { label: '作者', prop: 'author', render: 'text' },
      {
        label: '列表操作',
        prop: 'requireList',
        dictRender: 'tag',
        dictType: DictTypeEnum.COMMON_BOOLEAN_STATUS,
        width: 100
      },
      {
        label: '分页操作',
        prop: 'requirePage',
        dictRender: 'tag',
        dictType: DictTypeEnum.COMMON_BOOLEAN_STATUS,
        width: 100
      },
      {
        label: '导出操作',
        prop: 'requireExport',
        dictRender: 'tag',
        dictType: DictTypeEnum.COMMON_BOOLEAN_STATUS,
        width: 100
      },
      {
        label: '导入操作',
        prop: 'requireImport',
        dictRender: 'tag',
        dictType: DictTypeEnum.COMMON_BOOLEAN_STATUS,
        width: 100
      },
      { label: '创建时间', prop: 'createTime', render: 'text', width: 100 },
      { label: '备注', prop: 'remark', render: 'text' }
    ],
    operate: {
      buttons: ['edit', 'delete'],
      width: 200
    }
  },
  popupSearchConfig: {
    title: '导入表',
    tableConfig: {
      selection: true,
      columns: [
        { label: '名称', prop: 'name' },
        { label: '注释', prop: 'comment' }
      ]
    }
  }
})

function changeCodeTab(filename: string) {
  state.previewCodeTab = filename
}

function highlightCode(filename: string, code: string): string {
  const language = filename.substring(filename.lastIndexOf('.') + 1)
  const result = highlight.highlight(code, {
    language,
    ignoreIllegals: true
  })
  return result.value || ''
}

function previewCodegen(id: number) {
  const service = ElLoading.service({ fullscreen: true })
  previewCodegenApi(id).then((res) => {
    state.previewCodes = new Map()
    let flag = true
    for (let key in res.data) {
      const filename = key.substring(key.lastIndexOf('/') + 1)
      //取第一个
      if (flag) {
        state.previewCodeTab = filename
        flag = false
      }
      state.previewCodes.set(filename, res.data[key])
    }
    state.previewShow = true
    service.close()
  }).catch(() => {
    service.close()
  })
}

function codegenGenerate(id: number) {
  const service = ElLoading.service({ fullscreen: true })
  codegenGenerateApi(id).then(() => {
    service.close()
  }).catch(() => {
    service.close()
  })
}

function syncCodegen(id: number) {
  const service = ElLoading.service({ fullscreen: true })
  syncCodegenApi(id).then(() => {
    service.close()
  }).catch(() => {
    service.close()
  })
}

async function onBatchDelete(ids: number[]) {
  await deleteCodegenApi(ids.join(','))
  await pageCodegen()
}

function onEdit(id: number) {
  const currentTabs = toRaw(tabs.state.tabsView)
  for (let currentTab of currentTabs) {
    //因为编辑页有一个路径参数, 只要参数不同就会导致每次都会重新创建一个组件
    //产生的问题就是如果当前存在两个标签, 例如: edit/111, edit/112
    //只要关闭其中的一个, 那么另外一个keepalive就会失效(因为他们名字相同, 一个关闭就会导致其他都失效)
    //如果有正在编辑的内容, 那么就会丢失
    if (currentTab.name === 'system-codegen-edit') {
      ElNotification.warning('请先完成正在编辑的代码生成(修改页)')
      router.push(currentTab)
      return
    }
  }
  router.push('/system/codegen/edit/' + id)
}

async function onCreate(selection: any[]) {
  const tableNames = selection.map(item => item.name)
  if (tableNames.length === 0) {
    return
  }
  await createCodegenApi(tableNames)
  await pageCodegen()
}

async function pageCodegen() {
  state.tableConfig.loading = true
  const { data } = await pageCodegenApi(toRaw(state.query))
  state.tableConfig.loading = false
  state.tableData = data.list
  state.query.total = data.total
}

async function tableList(keyword: string) {
  state.popupSearchConfig.tableConfig.loading = true
  const { data } = await tableListApi(keyword)
  state.popupSearchConfig.data = data
  state.popupSearchConfig.tableConfig.loading = false
}

onMounted(() => {
  pageCodegen()
})
</script>

<script lang='ts'>
export default defineComponent({
  name: 'system-codegen'
})
</script>

<style scoped lang='scss'>
.code-panel {
  display: flex;
  height: 500px;

  .left {
    width: 200px;
    margin-right: 10px;

    .item {
      font-size: 12px;
      padding: 10px 0;
      border-radius: var(--el-border-radius-base);
      cursor: pointer;
    }

    .item:hover {
      color: var(--el-color-primary);
    }

    .active {
      color: var(--el-color-primary);
    }
  }

  .right {
    flex: 1;
  }
}
</style>