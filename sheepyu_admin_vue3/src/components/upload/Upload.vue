<template>
  <el-upload
    :show-file-list='false'
    :http-request='(options) => httpRequest(options)'
    :before-upload='(file) => beforeUpload(file)'
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
        <el-input class='remark' placeholder='文件备注' v-model='remark' />
      </div>
    </template>
  </el-upload>
</template>
<script setup lang='ts'>
import type { UploadProps } from 'element-plus'
import { ElLoading, ElNotification } from 'element-plus'
import Md5Worker from '@/util/worker/md5Worker.ts?worker'
import { checkMd5, upload } from '@/api/file'

const emit = defineEmits(['update:modelValue'])
const props = withDefaults(defineProps<{
  extendTypes?: string[]
  size?: number
  modelValue: string
}>(), {
  extendTypes: () => [],
  size: 100,
  modelValue: ''
})
const remark = ref<string>('')
const fileTypes = ['png', 'jpg', 'mp4', ...props.extendTypes]

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

function computeMd5(file: File): Promise<string> {
  return new Promise(resolve => {
    //使用worker线程计算md5值
    const worker = new Md5Worker()
    const instance = ElLoading.service({ text: '正在计算文件...', fullscreen: true })
    worker.postMessage(file)
    worker.onmessage = (e) => {
      instance.close()
      resolve(e.data)
    }
  })
}

const httpRequest: UploadProps['httpRequest'] = (options) => {
  return new Promise(async (resolve) => {
    const file = options.file
    //计算md5
    const md5 = await computeMd5(file)
    console.log(md5)
    //查看是否有这个文件
    const res = await checkMd5(md5)
    if (res.data && res.data.complete) {
      //如果文件存在会返回url, 直接回调更新值
      emit('update:modelValue', res.data.url)
      return resolve(true)
    }

    const instance = ElLoading.service({ text: '正在上传文件...', fullscreen: true })
    const data = new FormData()
    data.append('file', file)
    data.append('md5', md5)
    data.append('remark', remark.value)
    emit('update:modelValue', (await upload(data)).data)
    instance.close()
    return resolve(true)
  })
}

</script>

<style scoped lang='scss'>
.operate {
  display: flex;
  align-items: center;
  margin-top: 5px;

  .remark {
    width: 250px;
    margin-right: 10px;
  }
}

:deep(.el-upload__tip) {
  font-weight: 700;
  color: gray;
}
</style>
