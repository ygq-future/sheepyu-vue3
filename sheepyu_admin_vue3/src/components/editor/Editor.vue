<template>
  <div class='editor-box'>
    <Toolbar
      style='border-bottom: 1px solid #ccc'
      :editor='editorRef as IDomEditor'
      :defaultConfig='state.toolbarConfig'
    />
    <Editor
      style='height: 300px; overflow-y: hidden;'
      v-model='content'
      :defaultConfig='state.editorConfig'
      @onCreated='onCreate'
    />
  </div>
</template>

<script setup lang='ts'>
import '@wangeditor/editor/dist/css/style.css' // 引入 css
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { checkMd5Api, uploadApi } from '@/api/system/file'
import type { IDomEditor, IEditorConfig, IToolbarConfig } from '@wangeditor/editor'
import { ElLoading } from 'element-plus'
import { useMd5Worker } from '@/stores/worker/md5Worker'
import type { WritableComputedRef } from '@vue/reactivity'

const md5Store = useMd5Worker()

const props = defineProps<{
  modelValue: string
}>()

const emits = defineEmits<{
  (e: 'update:modelValue', modelValue: string): void
}>()

const editorRef = shallowRef<IDomEditor>()
const state = reactive<{
  toolbarConfig: Partial<IToolbarConfig>
  editorConfig: Partial<IEditorConfig>
}>({
  toolbarConfig: {},
  editorConfig: {
    MENU_CONF: {
      uploadImage: {
        allowedFileTypes: ['image/*'],
        // 自定义上传
        customUpload
      }
    }
  }
})
const content: WritableComputedRef<string> = computed({
  get() {
    return props.modelValue
  },
  set(value: string) {
    emits('update:modelValue', value)
  }
})

function onCreate(editor: IDomEditor) {
  editorRef.value = editor
}

async function customUpload(file: File, insertFn: Function) {
  //计算md5
  const md5 = await md5Store.computeMd5(file)
  //查看是否有这个文件
  const res = await checkMd5Api(md5)
  if (res.data && res.data.complete) {
    //如果文件存在会返回url, 直接回调更新值
    return insertFn(res.data.url)
  }

  const instance = ElLoading.service({ text: '正在上传文件...', fullscreen: true })
  try {
    const { data } = await uploadApi({ file, md5, remark: '图片上传' })
    // 自己实现上传，并得到图片 url alt href
    insertFn(data)
  } finally {
    instance.close()
  }
}

onUnmounted(() => editorRef.value && editorRef.value.destroy())
</script>

<style scoped lang='scss'>
.editor-box {
  border: 1px solid #ccc;
  z-index: 2;
}
</style>
