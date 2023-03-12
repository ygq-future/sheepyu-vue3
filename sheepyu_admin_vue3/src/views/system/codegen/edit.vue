<template>
  <div class='default-main' v-loading='state.loading'>
    <el-tabs type='border-card' v-model='state.activeTab'>
      <el-tab-pane label='基本配置' name='basicTab'>
        <el-form ref='formRef' v-if='state.codegenTable' :model='state.codegenTable' label-width='100px' :rules='rules'>
          <el-row>
            <el-col :xs='24' :sm='12' :md='8' :lg='8'>
              <el-form-item label='场景' prop='scene'>
                <Dict v-model='state.codegenTable.scene' render='select' :type='DictTypeEnum.SYSTEM_CODEGEN_SCENE' />
              </el-form-item>
            </el-col>

            <el-col :xs='24' :sm='12' :md='8' :lg='8'>
              <el-form-item label='表名' prop='tableName'>
                <el-input disabled v-model='state.codegenTable.tableName' />
              </el-form-item>
            </el-col>

            <el-col :xs='24' :sm='12' :md='8' :lg='8'>
              <el-form-item label='表描述' prop='tableComment'>
                <el-input v-model='state.codegenTable.tableComment' />
              </el-form-item>
            </el-col>

            <el-col :xs='24' :sm='12' :md='8' :lg='8'>
              <el-form-item label='模块名' prop='moduleName'>
                <el-input v-model='state.codegenTable.moduleName' />
              </el-form-item>
            </el-col>

            <el-col :xs='24' :sm='12' :md='8' :lg='8'>
              <el-form-item label='业务名' prop='businessName'>
                <el-input v-model='state.codegenTable.businessName' />
              </el-form-item>
            </el-col>

            <el-col :xs='24' :sm='12' :md='8' :lg='8'>
              <el-form-item label='类名' prop='className'>
                <el-input v-model='state.codegenTable.className' />
              </el-form-item>
            </el-col>

            <el-col :xs='24' :sm='12' :md='8' :lg='8'>
              <el-form-item label='类描述' prop='classComment'>
                <el-input v-model='state.codegenTable.classComment' />
              </el-form-item>
            </el-col>

            <el-col :xs='24' :sm='12' :md='8' :lg='8'>
              <el-form-item label='作者' prop='author'>
                <el-input v-model='state.codegenTable.author' />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :xs='12' :sm='6'>
              <el-form-item label='是否需要列表' prop='requireList'>
                <el-checkbox v-model='state.codegenTable.requireList' />
              </el-form-item>
            </el-col>

            <el-col :xs='12' :sm='6'>
              <el-form-item label='是否需要分页' prop='requirePage'>
                <el-checkbox v-model='state.codegenTable.requirePage' />
              </el-form-item>
            </el-col>

            <el-col :xs='12' :sm='6'>
              <el-form-item label='是否需要导出' prop='requireExport'>
                <el-checkbox v-model='state.codegenTable.requireExport' />
              </el-form-item>
            </el-col>

            <el-col :xs='12' :sm='6'>
              <el-form-item label='是否需要导入' prop='requireImport'>
                <el-checkbox v-model='state.codegenTable.requireImport' />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :xs='24' style='display: flex; justify-content: center'>
              <el-button type='info' @click='cancel' style='width: 100px'>取消</el-button>
              <el-button type='primary' @click='submit' style='width: 100px'>提交</el-button>
            </el-col>
          </el-row>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label='字段配置' name='fieldTab'>
        <el-table :data='state.tableData' border stripe>
          <el-table-column fixed='left' label='字段' prop='name' width='120' :show-overflow-tooltip='true' />
          <el-table-column label='字段类型' prop='type' width='120' :show-overflow-tooltip='true' />
          <el-table-column label='描述' prop='comment' :width='state.inputWidth' align='center'>
            <template #default='scope'>
              <CellForm v-model='scope.row.comment' />
            </template>
          </el-table-column>
          <el-table-column label='Java类型' prop='javaType' :width='state.inputWidth' align='center'>
            <template #default='scope'>
              <CellForm
                v-model='scope.row.javaType'
                render='select'
                :data='javaTypes'
                :props='{label: "value", value: "value"}'
              />
            </template>
          </el-table-column>
          <el-table-column label='Java字段' prop='javaField' :width='state.inputWidth' align='center'>
            <template #default='scope'>
              <CellForm v-model='scope.row.javaField' />
            </template>
          </el-table-column>
          <el-table-column label='示例' prop='example' :width='state.inputWidth' align='center'>
            <template #default='scope'>
              <CellForm v-model='scope.row.example' />
            </template>
          </el-table-column>
          <el-table-column label='允许为空' prop='nullable' :width='100' align='center'>
            <template #default='scope'>
              <el-checkbox v-model='scope.row.nullable' />
            </template>
          </el-table-column>
          <el-table-column label='创建' prop='createOperation' align='center'>
            <template #default='scope'>
              <el-checkbox v-model='scope.row.createOperation' />
            </template>
          </el-table-column>
          <el-table-column label='修改' prop='updateOperation' align='center'>
            <template #default='scope'>
              <el-checkbox v-model='scope.row.updateOperation' />
            </template>
          </el-table-column>
          <el-table-column label='快速搜索' prop='quickSearch' :width='100' align='center'>
            <template #default='scope'>
              <el-checkbox v-model='scope.row.quickSearch' />
            </template>
          </el-table-column>
          <el-table-column label='查询' prop='queryOperation' align='center'>
            <template #default='scope'>
              <el-checkbox v-model='scope.row.queryOperation' />
            </template>
          </el-table-column>
          <el-table-column label='查询条件' prop='queryCondition' :width='state.inputWidth' align='center'>
            <template #default='scope'>
              <CellForm
                v-model='scope.row.queryCondition'
                render='select'
                :data='queryTypes'
                :props='{label: "value", value: "value"}'
              />
            </template>
          </el-table-column>
          <el-table-column label='结果' prop='listOperationResult' align='center'>
            <template #default='scope'>
              <el-checkbox v-model='scope.row.listOperationResult' />
            </template>
          </el-table-column>
          <el-table-column label='表单显示类型' prop='formShowType' :width='state.inputWidth' align='center'>
            <template #default='scope'>
              <CellForm
                v-model='scope.row.formShowType'
                render='select'
                :data='formTypes'
                :props='{label: "value", value: "value"}'
              />
            </template>
          </el-table-column>
          <el-table-column label='字典' prop='dictType' :width='state.inputWidth' align='center'>
            <template #default='scope'>
              <CellForm
                v-model='scope.row.dictType'
                render='select'
                :data='state.dictTypes'
                :props='{label: "name", value: "type"}'
              />
            </template>
          </el-table-column>
          <el-table-column label='排序' prop='sort' :width='state.inputWidth' align='center'>
            <template #default='scope'>
              <CellForm
                v-model='scope.row.sort'
                render='select'
                :data='sortTypes'
                :props='{label: "value", value: "value"}'
              />
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang='ts'>
import { findCodegenApi, updateCodegenApi } from '@/api/system/codegen'
import { dictTypeList } from '@/api/system/dict'
import type { SystemCodegenColumn, SystemCodegenRespVo, SystemCodegenUpdateVo } from '@/api/system/codegen'
import type { SystemDictTypeRespVo } from '@/api/system/dict'
import type { FormRender } from '@/components/form/interface'
import { DictTypeEnum } from '@/enums/DictTypeEnum'
import type { FormRules } from 'element-plus'
import { ElForm, ElMessageBox } from 'element-plus'

const instance = getCurrentInstance()
const route = useRoute()

const formRef = ref<InstanceType<typeof ElForm>>()

const state = reactive<{
  codegenTable?: SystemCodegenRespVo
  tableData: SystemCodegenColumn[]
  dictTypes: SystemDictTypeRespVo[]
  inputWidth: number
  loading?: boolean
  activeTab: string
}>({
  tableData: [],
  dictTypes: [],
  inputWidth: 130,
  activeTab: 'fieldTab'
})
const javaTypes = [{ value: 'Long' }, { value: 'Integer' }, { value: 'String' }, { value: 'Double' }, { value: 'Float' },
  { value: 'Date' }, { value: 'Boolean' }, { value: 'BigDecimal' }]
const sortTypes = [{ value: 'asc' }, { value: 'desc' }]
const queryTypes = [{ value: '=' }, { value: '!=' }, { value: '>' }, { value: '<' }, { value: '>=' }, { value: '<=' }, { value: 'like' }, { value: 'between' }]
const formTypes: { name: string, value: FormRender }[] = [
  { name: '数字', value: 'number' },
  { name: '文本', value: 'text' },
  { name: '文本域', value: 'textarea' },
  { name: '开关', value: 'switch' },
  { name: '单选', value: 'radio' },
  { name: '多选', value: 'checkbox' },
  { name: '下拉选择', value: 'select' },
  { name: '树形选择', value: 'tree-select' },
  { name: '日期', value: 'datetime' },
  { name: '文件上传', value: 'upload' },
  { name: '文件分片上传', value: 'part-upload' }
]
const rules: FormRules = {
  scene: [{ required: true, message: '场景不能为空', trigger: 'blur' }],
  tableName: [{ required: true, message: '表名不能为空', trigger: 'blur' }],
  tableComment: [{ required: true, message: '表注释不能为空', trigger: 'blur' }],
  moduleName: [{ required: true, message: '模块名不能为空', trigger: 'blur' }],
  businessName: [{ required: true, message: '业务名不能为空', trigger: 'blur' }],
  className: [{ required: true, message: '类名不能为空', trigger: 'blur' }],
  classComment: [{ required: true, message: '类注释不能为空', trigger: 'blur' }],
  author: [{ required: true, message: '作者不能为空', trigger: 'blur' }]
}

function cancel() {
  ElMessageBox.confirm('确认取消当前编辑的内容吗?', '提示', {
    closeOnClickModal: false
  }).then(() => {
    instance?.proxy?.$bus.emit('closeCurrentToRoute', '/system/codegen')
  }).catch(() => {
  })
}

async function submit() {
  const value = await formRef.value?.validate(value => value)
  if (!value) {
    return
  }
  await updateCodegenApi(state.codegenTable as SystemCodegenUpdateVo)
  instance?.proxy?.$bus.emit('closeCurrentToRoute', '/system/codegen')
}

async function findCodegen() {
  state.loading = true
  const id: any = route.params.id
  const { data } = await findCodegenApi(id)
  state.codegenTable = data
  state.tableData = state.codegenTable.columns
  state.loading = false
}

async function findDictTypes() {
  const { data } = await dictTypeList()
  state.dictTypes = data
}

onMounted(() => {
  findCodegen()
  findDictTypes()
})
</script>

<script lang='ts'>
export default defineComponent({
  name: 'system-codegen-edit'
})
</script>