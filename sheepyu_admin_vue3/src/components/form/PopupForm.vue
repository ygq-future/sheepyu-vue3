<template>
  <el-dialog
    append-to-body
    v-model='state.dialogVisible'
    :style='{maxWidth: `${config.maxWidth ?? 700}px`, minWidth: "370px"}'
    :title='config.title'
    :width="config.width || '50%'"
    :close-on-click-modal='config.closeOnClickModal ?? true'
    @close='hide'
  >

    <el-scrollbar :max-height='config.maxHeight ?? 400' style='padding: 0 15px'>
      <el-form
        v-if='state.dialogVisible'
        v-loading='state.formLoading'
        ref='formRef'
        :model='form'
        :rules='state.rules'
        :label-width='config.labelWidth ?? 80'
      >
        <template v-for='itemConfig in config.formItemConfigs'>
          <FormItemRender
            v-if='!config.hideProps || !config.hideProps.includes(itemConfig.prop)'
            :form='form'
            :config='itemConfig'
            :disabled='(config.disabledProps && config.disabledProps.includes(itemConfig.prop)) || itemConfig.disabled || config.looked'
          />
        </template>
        <slot name='form-items'></slot>
      </el-form>
    </el-scrollbar>

    <template #footer>
      <span class='dialog-footer'>
        <el-button :disabled='state.formLoading' @click='hide'>取消</el-button>
        <el-button v-if='!config.looked' :disabled='state.formLoading' type='primary' @click='onSubmitAndNext'>
            {{ !config.isEdit || state.isLastEdit ? '确定' : '保存并编辑下一个' }}
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang='ts'>
import type { PopupFormConfig } from '@/components/form/interface'
import FormItemRender from './render/FormItemRender.vue'
import type { FormInstance, FormRules } from 'element-plus'

const props = defineProps<{
  form: any
  config: PopupFormConfig
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
    state.formLoading = false
  }, props.config.timeout ?? 3000)

  emits('submit', () => {
    clearTimeout(timer)
    if (props.config.isEdit && !state.isLastEdit) {
      state.formLoading = false
      return onNext()
    }
    hide()
  })
}

function onNext() {
  if (!props.config.ids) return
  const size = props.config.ids.length
  if (size > 0 && !state.isLastEdit) {
    emits('next', props.config.ids[state.editIndex++])
    if (size === state.editIndex) {
      state.isLastEdit = true
    }
  }
}

function show() {
  formRef.value?.clearValidate(props.config.formItemConfigs.map(item => item.prop))
  if (props.config.isEdit) {
    onNext()
  }
  state.dialogVisible = true
}

function hide() {
  state.dialogVisible = false
  state.formLoading = false
  if (props.config.isEdit) {
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
  props.config.formItemConfigs.forEach(config => {
    if (config.required === undefined || config.required) {
      state.rules[config.prop] = [{ required: true, message: `${config.label}不能为空`, trigger: 'blur' }]
    }
  })
})
</script>
