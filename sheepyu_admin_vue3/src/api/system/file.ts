import { request } from '@/util/request'
import type { CancelTokenSource } from 'axios'

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

export function upload(data: FormData) {
  return request.post<string>('/system/file/upload', data)
}

export function preparePart(data: FormData) {
  checkCancel()
  return request.post<string>('/system/file/preparePart', data, { cancelToken: source.token })
}

export function uploadPart(data: FormData, uploadId: string) {
  checkCancel()
  return request.post<string>(`/system/file/uploadPart/${uploadId}`, data, { cancelToken: source.token })
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

interface SystemFileRespVo {
  id: number;
  uploadId: string
  //("文件名")
  filename: string;
  //("md5")
  md5: string;
  //("url")
  url: string;
  //("mimeType")
  mimeType: string;
  //("size")
  size: number;
  //("地域")
  domain: string;
  //("相对路径")
  path: string;
  //("是否完成")
  complete: boolean;
  //("备注")
  remark: string;
  //("如果没有完成, 已完成的最后一块分片的坐标")
  partIndex: number;
}
