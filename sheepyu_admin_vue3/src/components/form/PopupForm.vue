<template>
  <el-dialog
    append-to-body
    v-model='state.dialogVisible'
    :style='{maxWidth: `${popupFormConfig.maxWidth ?? 700}px`, minWidth: "370px"}'
    :title='popupFormConfig.title'
    :width='popupFormConfig.width'
    :close-on-click-modal='false'
    @close='hide'
  >

    <el-scrollbar max-height='400px' style='padding: 0 15px'>
      <el-form
        v-loading='state.formLoading'
        ref='formRef'
        :model='form'
        :rules='state.rules'
        :label-width='popupFormConfig.labelWidth ?? 80'
      >
        <template v-for='config in popupFormConfig.formItemConfigs'>
          <FormItemRender
            v-if='!popupFormConfig.hideProps.includes(config.prop)'
            :form='form'
            :config='config'
            :disabled='popupFormConfig.disabledProps.includes(config.prop)'
          />
        </template>
      </el-form>
    </el-scrollbar>

    <template #footer>
      <span class='dialog-footer'>
        <el-button :disabled='state.formLoading' @click='hide'>取消</el-button>
        <el-button :disabled='state.formLoading' type='primary' @click='onSubmitAndNext'>
            {{ !popupFormConfig.isEdit || state.isLastEdit ? '确定' : '保存并编辑下一个' }}
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang='ts'>
import type { PopupFormConfig } from '@/components/form/interface'
import FormItemRender from './render/FormItemRender.vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElNotification } from 'element-plus'

const props = defineProps<{
  form: object
  popupFormConfig: PopupFormConfig
}>()

const emits = defineEmits<{
  (e: 'close'): void
  (e: 'submit', cb: Function): void
  (e: 'next', id: any): void
}>()

const formRef = ref<FormInstance>()

const state = reactive<{
  dialogVisible: boolean
  formLoading: boolean
  rules: FormRules
  editIndex: number
  isLastEdit: boolean
}>({
  dialogVisible: false,
  formLoading: false,
  rules: {},
  editIndex: 0,
  isLastEdit: false
})

async function onSubmitAndNext() {
  if (!formRef.value) return
  const value = await formRef.value.validate(valid => valid)
  if (!value) return

  state.formLoading = true
  //回调超时处理
  const timer = setTimeout(() => {
    ElNotification.warning('回调超时, 操作失败')
    state.formLoading = false
    hide()
  }, props.popupFormConfig.timeout ?? 5000)

  emits('submit', () => {
    clearTimeout(timer)
    if (props.popupFormConfig.isEdit && !state.isLastEdit) {
      state.formLoading = false
      return onNext()
    }
    hide()
  })
}

function onNext() {
  if (!props.popupFormConfig.ids) return
  const size = props.popupFormConfig.ids.length
  if (size > 0 && !state.isLastEdit) {
    emits('next', props.popupFormConfig.ids[state.editIndex++])
    if (size === state.editIndex) {
      state.isLastEdit = true
    }
  }
}

function show() {
  formRef.value?.clearValidate(props.popupFormConfig.formItemConfigs.map(item => item.prop))
  if (props.popupFormConfig.isEdit) {
    onNext()
  }
  state.dialogVisible = true
}

function hide() {
  state.dialogVisible = false
  state.formLoading = false
  if (props.popupFormConfig.isEdit) {
    state.editIndex = 0
    state.isLastEdit = false
  }
  emits('close')
}

defineExpose({
  show,
  hide
})

onBeforeMount(() => {
  props.popupFormConfig.formItemConfigs.forEach(config => {
    if (config.required === undefined || config.required) {
      state.rules[config.prop] = [{ required: true, message: `${config.label}不能为空`, trigger: 'blur' }]
    }
  })
})
</script>

<style scoped lang='scss'>

</style>
