<template>
  <el-upload
    :show-file-list='false'
    :http-request='(options) => httpRequest(options)'
    :before-upload='(file) => beforeUpload(file)'
  >
    <el-button type='primary'>点击上传</el-button>
    <template #tip>
      <div class='el-upload__tip'>
        <el-progress :percentage='pageData.percentage' />
        请上传类型为
        <span style='color: #ff4d4d'>{{ fileTypes.join('/') }}</span>
        且大小不超过
        <span style='color: #ff4d4d'>{{ size * chunkNum }}MB</span>
        的文件
      </div>
      <div class='operate'>
        <el-input class='remark' placeholder='文件备注' v-model='pageData.remark' />
        <el-button type='danger' @click='cancelUpload'>取消上传</el-button>
      </div>
    </template>
  </el-upload>
</template>

<script setup lang='ts'>
import type { UploadProps } from 'element-plus'
import { ElLoading, ElNotification } from 'element-plus'
import Md5Worker from '@/util/worker/md5Worker.ts?worker'
import { useMd5Store } from '@/stores/worker/md5Store'
import { abortPart, checkMd5, completePart, preparePart, uploadPart } from '@/api/system/file'

const md5Store = useMd5Store()
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

const pageData = reactive<{
  uploadId: string
  remark: string
  percentage: number
}>({
  uploadId: '',
  remark: '',
  percentage: 0
})
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
  if (!pageData.uploadId) {
    return ElNotification.warning('没有正在上传的文件')
  }
  abortPart()
}

/**
 * 切割完成的文件为分片数据
 * @param file 文件
 * @param index 从哪个分片开始切割
 */
function createPartFile(file: File, index: number = 0): FormData[] {
  const partList: FormData[] = []
  let cur = index * partSize
  while (cur < file.size) {
    const blob = file.slice(cur, cur = cur + partSize)
    const part = new FormData()
    part.append('file', blob)
    part.append('index', `${++index}`)
    partList.push(part)
  }
  if (index > props.chunkNum) {
    ElNotification.error('文件超出设置大小')
  }
  return partList
}

function computeMd5(file: File): Promise<string> {
  return new Promise(resolve => {
    let md5 = md5Store.findCache(file.name, file.lastModified)
    if (md5) {
      return resolve(md5)
    }
    //使用worker线程计算md5值
    const worker = new Md5Worker()
    const instance = ElLoading.service({ text: '正在计算文件...', fullscreen: true })
    worker.postMessage(file)
    worker.onmessage = (e) => {
      instance.close()
      md5Store.addCache(file.name, file.lastModified, md5 = e.data)
      resolve(md5)
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
    //如果没有这个文件
    if (!data) {
      //调用分片准备接口, 创建全局唯一id
      const prepareData = new FormData()
      prepareData.append('md5', md5)
      prepareData.append('filename', file.name)
      prepareData.append('remark', pageData.remark)
      pageData.uploadId = <string>(await preparePart(prepareData)).data
    } else {
      //如果已经有此md5的文件, 直接取uploadId
      pageData.uploadId = data.uploadId
      //如果完成了, 直接返回
      if (data.complete) {
        //如果文件存在会返回url, 直接回调更新值
        emit('update:modelValue', data.url)
        pageData.percentage = 100
        return resolve(true)
      }
    }

    //从上次分片的后一个开始
    let i = data ? data.partIndex : 0
    const partList = createPartFile(file, i)
    //逐个上传分片
    const total = i + partList.length
    for (const part of partList) {
      pageData.percentage = Math.ceil((++i / total) * 100) - 1
      await uploadPart(part, pageData.uploadId)
    }

    //合并分片
    emit('update:modelValue', (await completePart(pageData.uploadId)).data)
    pageData.percentage = 100
    pageData.uploadId = ''
    ElNotification.success('上传成功')
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
