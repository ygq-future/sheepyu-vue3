<template>
  <el-upload
    :show-file-list='false'
    :http-request='(options) => httpRequest(options)'
    :before-upload='(file) => beforeUpload(file)'
  >
    <el-button type='primary'>点击上传</el-button>
    <template #tip>
      <div class='el-upload__tip'>
        <el-progress :percentage='percentage' />
        请上传类型为
        <span style='color: #ff4d4d'>{{ fileTypes.join('/') }}</span>
        且大小不超过
        <span style='color: #ff4d4d'>{{ size * chunkNum }}MB</span>
        的文件
      </div>
      <div class='operate'>
        <el-input class='remark' placeholder='文件备注' v-model='remark' />
        <el-button type='danger' @click='cancelUpload'>取消上传</el-button>
      </div>
    </template>
  </el-upload>
</template>

<script setup lang='ts'>
import type { UploadProps } from 'element-plus'
import { ElLoading, ElNotification } from 'element-plus'
import Md5Worker from '@/util/worker/md5Worker.ts?worker'
import { abortPart, checkMd5, completePart, preparePart, uploadPart } from '@/api/file'

const emit = defineEmits(['update:modelValue'])
const props = withDefaults(defineProps<{
  extendTypes?: string[]
  size?: number
  chunkNum?: number
  modelValue: string
}>(), {
  extendTypes: () => [],
  size: 50,
  chunkNum: 40,
  modelValue: ''
})

let fileId: number = 0
const remark = ref<string>('')
const percentage = ref<number>(0)
//将props.size转为字节单位, 作为每一份part的大小
const partSize = props.size * 1024 * 1024
//文件类型
const fileTypes = ['png', 'jpg', 'mp4', ...props.extendTypes]

const beforeUpload: UploadProps['beforeUpload'] = (file) => {
  let suffix = file.name.substring(file.name.lastIndexOf('.') + 1)
  if (!fileTypes.includes(suffix)) {
    ElNotification.error('文件格式错误!')
    return false
  }

  if (file.size > partSize * props.chunkNum) {
    ElNotification.error(`文件大小不能超过 ${props.size * props.chunkNum} MB!`)
    return false
  }

  return true
}

function cancelUpload() {
  if (!fileId) {
    return ElNotification.warning('没有正在上传的文件')
  }
  abortPart()
}

function createPartFile(file: File): FormData[] {
  const partList: FormData[] = []
  let index = 0
  let cur = 0
  while (cur < file.size) {
    const blob = file.slice(cur, cur = cur + partSize)
    const part = new FormData()
    part.append('file', blob)
    part.append('index', `${index++}`)
    partList.push(part)
  }
  if (index > props.chunkNum) {
    ElNotification.error('文件超出设置大小')
  }
  return partList
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
    //查看是否有这个文件
    const { data } = await checkMd5(md5)
    if (data && data.complete) {
      //如果文件存在会返回url, 直接回调更新值
      emit('update:modelValue', data.url)
      percentage.value = 100
      return resolve(true)
    }

    //调用分片准备接口, 创建全局唯一id
    const prepareData = new FormData()
    prepareData.append('md5', md5)
    prepareData.append('filename', file.name)
    prepareData.append('remark', remark.value)
    const res = await preparePart(prepareData)
    fileId = <number>res.data

    //逐个上传分片
    const partList = createPartFile(file)
    let i = 0
    //从上次分片的后一个开始
    if (data) {
      i = data.index + 1
    }
    for (; i < partList.length; i++) {
      const data = partList[i]
      await uploadPart(data, fileId)
      percentage.value = Math.ceil(((i + 1) / partList.length) * 100) - 1
    }

    //合并分片
    const result = await completePart(fileId)
    percentage.value = 100
    ElNotification.success('上传成功')
    emit('update:modelValue', result.data)
    fileId = 0
    resolve(true)
  })
}

</script>

<style scoped lang='scss'>
:deep(.el-progress span) {
  font-size: 12px !important;
}

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
