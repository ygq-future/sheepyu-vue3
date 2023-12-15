<template>
  <el-upload
    :show-file-list='false'
    :http-request='httpRequest'
    :before-upload='beforeUpload'
  >
    <el-button type='primary'>点击上传</el-button>
    <template #tip>
      <div class='el-upload__tip'>
        <el-progress :percentage='state.percentage' />
        请上传类型为
        <span style='color: #ff4d4d'>{{ fileTypes.join('/') }}</span>
        且大小不超过
        <span style='color: #ff4d4d'>{{ size * chunkNum }}MB</span>
        的文件
      </div>
      <div class='operate'>
        <el-form-item prop='remark'>
          <el-input class='remark' placeholder='文件备注' v-model='state.remark' />
        </el-form-item>
        <el-button type='danger' @click='cancelUpload'>取消上传</el-button>
      </div>
      <el-input disabled v-model='url' style='margin-top: 5px' placeholder='文件url' />
    </template>
  </el-upload>
</template>

<script setup lang='ts'>
import type { UploadProps } from 'element-plus'
import { ElNotification } from 'element-plus'
import { useMd5Worker } from '@/stores/worker/md5Worker'
import type { PreparePartData, UploadPartData } from '@/api/system/file'
import { abortPart, checkMd5Api, completePartApi, preparePartApi, uploadPartApi } from '@/api/system/file'
import type { WritableComputedRef } from '@vue/reactivity'

const md5Store = useMd5Worker()

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

const emits = defineEmits<{
  (e: 'update:modelValue', url: string): void
  (e: 'complete', url: string): void
}>()

const url: WritableComputedRef<string> = computed({
  get() {
    return props.modelValue
  },
  set(value) {
    emits('update:modelValue', value)
  }
})

const state = reactive<{
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
const fileTypes = ['mp4', 'zip', '7z', 'rar', ...props.extendTypes]

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
  if (!state.uploadId) {
    return ElNotification.warning('没有正在上传的文件')
  }
  abortPart()
}

/**
 * 切割完成的文件为分片数据
 * @param file 文件
 * @param index 从哪个分片开始切割
 */
function createPartFile(file: File, index: number = 0): Array<UploadPartData> {
  const partList: Array<UploadPartData> = []
  let cur = index * partSize
  while (cur < file.size) {
    const blob = file.slice(cur, cur = cur + partSize)
    partList.push({ part: blob, index: ++index })
  }
  return partList
}

const httpRequest: UploadProps['httpRequest'] = (options) => {
  return new Promise(async (resolve) => {
    const file = options.file
    //计算md5
    const md5 = await md5Store.computeMd5(file)
    //查看是否有这个文件
    const { data } = await checkMd5Api(md5)
    //如果没有这个文件
    if (!data) {
      //调用分片准备接口, 创建全局唯一id
      const prepareData: PreparePartData = { md5, filename: file.name, remark: state.remark }
      state.uploadId = <string>(await preparePartApi(prepareData)).data
    } else {
      //如果已经有此md5的文件, 直接取uploadId
      state.uploadId = data.uploadId!
      //如果完成了, 直接返回
      if (data.complete) {
        //如果文件存在会返回url, 直接回调更新值
        emits('update:modelValue', data.url)
        emits('complete', data.url)
        state.percentage = 100
        return resolve(true)
      }
    }

    //从上次分片的后一个开始
    let i = data ? data.partIndex : 0
    const partList = createPartFile(file, i)
    //逐个上传分片
    const total = i + partList.length
    for (const part of partList) {
      state.percentage = Math.ceil((++i / total) * 100) - 1
      await uploadPartApi(state.uploadId, part)
    }

    //合并分片
    const url = (await completePartApi(state.uploadId)).data
    emits('update:modelValue', url)
    emits('complete', url)
    state.percentage = 100
    state.uploadId = ''
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
  line-height: 16px;
}
</style>
