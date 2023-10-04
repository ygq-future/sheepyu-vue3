<template>
  <el-upload
    :show-file-list='false'
    :http-request='httpRequest'
    :before-upload='beforeUpload'
  >
    <el-button type='primary'>点击上传</el-button>
    <template #tip>
      <div class='el-upload__tip'>
        请上传类型为
        <span style='color: #ff4d4d'>{{ fileTypes.join('/') }}</span>
        且大小不超过
        <span style='color: #ff4d4d'>{{ size }}MB</span>
        的文件
      </div>
      <div class='operate'>
        <el-form-item prop='remark'>
          <el-input class='remark' placeholder='文件备注' v-model='remark' />
        </el-form-item>
      </div>
      <el-input disabled v-model='url' style='margin-top: 5px' placeholder='文件url' />
    </template>
  </el-upload>
</template>
<script setup lang='ts'>
import type { UploadProps } from 'element-plus'
import { ElLoading, ElNotification } from 'element-plus'
import { useMd5Worker } from '@/stores/worker/md5Worker'
import { checkMd5Api, uploadApi } from '@/api/system/file'
import type { UploadData } from '@/api/system/file'
import type { WritableComputedRef } from '@vue/reactivity'

const md5Store = useMd5Worker()

const props = withDefaults(defineProps<{
  extendTypes?: string[]
  size?: number
  modelValue: string
}>(), {
  extendTypes: () => [],
  size: 50,
  modelValue: ''
})

const emits = defineEmits<{
  (e: 'update:modelValue', value: string): void
}>()

const url: WritableComputedRef<string> = computed({
  get() {
    return props.modelValue
  },
  set(value) {
    emits('update:modelValue', value)
  }
})

const remark = ref<string>('')
const fileTypes = ['png', 'jpg', 'jpeg', 'xls', 'xlsx', 'ppt', 'pptx', 'doc', 'docx', 'pdf', ...props.extendTypes]

const beforeUpload: UploadProps['beforeUpload'] = (file) => {
  let suffix = file.name.substring(file.name.lastIndexOf('.') + 1)
  if (!fileTypes.includes(suffix)) {
    ElNotification.error('文件格式错误!')
    return false
  }

  if (file.size > props.size * 1024 * 1024) {
    ElNotification.error(`文件大小不能超过 ${props.size} MB!`)
    return false
  }

  return true
}

const httpRequest: UploadProps['httpRequest'] = (options) => {
  return new Promise(async (resolve) => {
    const file = options.file
    //计算md5
    const md5 = await md5Store.computeMd5(file)
    //查看是否有这个文件
    const res = await checkMd5Api(md5)
    if (res.data && res.data.complete) {
      //如果文件存在会返回url, 直接回调更新值
      emits('update:modelValue', res.data.url)
      return resolve(true)
    }

    const instance = ElLoading.service({ text: '正在上传文件...', fullscreen: true })
    try {
      const data: UploadData = { file, md5, remark: remark.value }
      emits('update:modelValue', (await uploadApi(data)).data)
    } finally {
      instance.close()
    }
    return resolve(true)
  })
}

</script>

<style scoped lang='scss'>
.operate {
  margin-top: 5px;

  .remark {
    width: 250px;
    margin-right: 10px;
  }
}

:deep(.el-upload__tip) {
  font-weight: 700;
  color: gray;
  line-height: 16px;
}
</style>
