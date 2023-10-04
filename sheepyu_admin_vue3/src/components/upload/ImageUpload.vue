<template>
  <el-upload
    class='avatar-uploader'
    :http-request='httpRequest'
    :show-file-list='false'
    :before-upload='beforeUpload'
  >
    <img v-if='modelValue' :src='modelValue' class='avatar' alt='' />
    <MyIcon v-else name='el-icon-Plus' />
  </el-upload>
</template>

<script lang='ts' setup>
import type { UploadProps } from 'element-plus'
import { ElLoading, ElNotification } from 'element-plus'
import { checkMd5Api, uploadApi } from '@/api/system/file'
import { useMd5Worker } from '@/stores/worker/md5Worker'

const props = withDefaults(defineProps<{
  size?: number
  modelValue: string
  width?: string
}>(), {
  size: 10,
  modelValue: '',
  width: '178px'
})

const emits = defineEmits<{
  (e: 'update:modelValue', modelValue: string): void
}>()

const md5Store = useMd5Worker()

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
      const data = { file, md5, remark: '图片上传' }
      emits('update:modelValue', (await uploadApi(data)).data)
    } finally {
      instance.close()
    }
    return resolve(true)
  })
}

const beforeUpload: UploadProps['beforeUpload'] = (file) => {
  let suffix = file.name.substring(file.name.lastIndexOf('.') + 1)
  if (!['jpg', 'png', 'jpeg', 'gif'].includes(suffix)) {
    ElNotification.error('只能上传图片!')
    return false
  }

  if (file.size > props.size * 1024 * 1024) {
    ElNotification.error(`文件大小不能超过 ${props.size} MB!`)
    return false
  }

  return true
}
</script>

<style scoped lang='scss'>
$width: v-bind(width);

.avatar {
  width: $width;
  height: $width;
  display: block;
}

:deep(.el-upload) {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

:deep(.el-upload):hover {
  border-color: var(--el-color-primary);
}

.el-icon {
  font-size: 28px;
  color: #8c939d;
  width: $width;
  height: $width;
  text-align: center;
}
</style>
