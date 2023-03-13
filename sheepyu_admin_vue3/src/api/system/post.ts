import { request } from '@/util/request'
import type { PageResult } from '@/util/request'

export function createPostApi(data: SystemPostCreateVo) {
  return request.post<boolean>('/system/post', data)
}

export function updatePostApi(data: SystemPostUpdateVo) {
  return request.put<boolean>('/system/post', data)
}

export function deletePostApi(ids: string) {
  return request.delete<boolean>('/system/post/' + ids)
}

export function findPostApi(id: number) {
  return request.get<SystemPostRespVo>('/system/post/' + id)
}

export function pagePostApi(params: SystemPostQueryVo) {
  return request.get<PageResult<SystemPostRespVo>>('/system/post/page', { params })
}

export interface SystemPostBaseVo {
  //岗位名称
  name: string
  //显示顺序
  sort: number
}

export interface SystemPostCreateVo extends SystemPostBaseVo {
}

export interface SystemPostUpdateVo extends SystemPostBaseVo {
  //岗位ID
  id: number
}

export interface SystemPostRespVo extends SystemPostBaseVo {
  //岗位ID
  id: number
  //创建者
  creator?: string
  //创建时间
  createTime: string
}

export interface SystemPostQueryVo {
  //当前页
  current?: number
  //页大小
  size?: number
  //总记录数
  total?: number
  //快速搜索关键字
  keyword?: string
}
