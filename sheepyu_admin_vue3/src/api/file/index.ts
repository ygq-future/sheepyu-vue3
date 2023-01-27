import { request } from '@/util/request'
import type { CancelTokenSource } from 'axios'
import type { SystemFileRespVo } from '@/api/file/pojo'

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
  return request.post<number>('/system/file/preparePart', data, { cancelToken: source.token })
}

export function uploadPart(data: FormData, fileId: number) {
  checkCancel()
  return request.post<string>(`/system/file/uploadPart/${fileId}`, data, { cancelToken: source.token })
}

export function abortPart() {
  if (isCancel) {
    return
  }
  source.cancel('请求取消')
  isCancel = true
}

export function completePart(fileId: number) {
  checkCancel()
  return request.post<string>(`/system/file/completePart/${fileId}`, { cancelToken: source.token })
}
