import { request } from '@/util/request'
import type { CancelTokenSource } from 'axios'
import type { PageResult } from '@/util/request'

let source: CancelTokenSource = request.getSource()
let isCancel: boolean = false

function checkCancel() {
  if (isCancel) {
    isCancel = false
    //重置CancelTokenSource
    source = request.getSource()
    throw Error('方法中止')
  }
}

export function checkMd5(md5: string) {
  return request.get<SystemFileRespVo>(`/system/file/checkMd5/${md5}`)
}

export function upload(data: UploadData) {
  const formData = new FormData()
  formData.append('file', data.file)
  formData.append('md5', data.md5)
  formData.append('remark', data.remark || '')
  return request.post<string>('/system/file/upload', formData)
}

export function preparePart(data: PreparePartData) {
  checkCancel()
  const formData = new FormData()
  formData.append('md5', data.md5)
  formData.append('filename', data.filename)
  formData.append('remark', data.remark || '')
  return request.post<string>('/system/file/preparePart', formData, { cancelToken: source.token })
}

export function uploadPart(uploadId: string, data: UploadPartData) {
  checkCancel()
  const formData = new FormData()
  formData.append('part', data.part)
  formData.append('index', data.index.toString())
  return request.post<string>(`/system/file/uploadPart/${uploadId}`, formData, { cancelToken: source.token })
}

export function abortPart() {
  if (isCancel) {
    return
  }
  source.cancel('请求取消')
  isCancel = true
}

export function completePart(uploadId: string) {
  checkCancel()
  return request.post<string>(`/system/file/completePart/${uploadId}`, { cancelToken: source.token })
}

export function deleteFileApi(ids: string) {
  return request.delete<boolean>('/system/file/' + ids)
}

export function pageFileApi(params: SystemFileQueryVo) {
  return request.get<PageResult<SystemFileRespVo>>('/system/file/page', { params })
}

export interface UploadData {
  file: File
  md5: string
  remark?: string
}

export interface PreparePartData {
  md5: string
  filename: string
  remark?: string
}

export interface UploadPartData {
  part: Blob
  index: number
}

export interface SystemFileBaseVo {
  //兼容s3协议唯一上传id
  uploadId?: string
  //如果是分片上传, 当前上传了几块, 1代表上传了一块
  partIndex: number
  //文件名
  filename: string
  //文件md5
  md5?: string
  //文件 URL
  url: string
  //MIME类型
  mimeType?: string
  //文件大小, 字节
  size: number
  //文件上传地域, 例如: http://localhost
  domain?: string
  //文件上传相对路径, 例如: /2022/08/xxx.jpg
  path?: string
  //是否完成
  complete: number
  //备注
  remark?: string
}

export interface SystemFileRespVo extends SystemFileBaseVo {
  id: number
  //创建者
  creator: string
  //创建时间
  createTime: string
}

export interface SystemFileQueryVo {
  //当前页
  current?: number
  //页大小
  size?: number
  //总记录数
  total?: number
  //快速搜索关键字
  keyword?: string
  //文件大小, 字节
  sizes?: number[]
  //是否完成
  complete?: number
  //创建时间
  createTimes?: string[]
}