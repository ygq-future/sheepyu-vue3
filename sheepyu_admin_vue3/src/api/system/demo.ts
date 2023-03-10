import { request } from '@/util/request'
import type { PageResult } from '@/util/request'

export function createDemoApi(data: SystemDemoCreateVo) {
  return request.post<boolean>('/system/demo', data)
}

export function updateDemoApi(data: SystemDemoUpdateVo) {
  return request.put<boolean>('/system/demo', data)
}

export function deleteDemoApi(ids: string) {
  return request.delete<boolean>('/system/demo/' + ids)
}

export function pageDemoApi(params: SystemDemoQueryVo) {
  return request.get<PageResult<SystemDemoRespVo>>('/system/demo/page', { params })
}

export function findDemoApi(id: number) {
  return request.get<SystemDemoRespVo>('/system/demo/' + id)
}

export function exportDemoApi(params: SystemDemoQueryVo) {
  return request.download('/system/demo/export', '测试导出数据.xlsx', { params })
}

export function importDemoApi(file: File) {
  const formData = new FormData()
  formData.append("file", file)
  return request.post<boolean>('/system/demo/batchImport', formData)
}

export interface SystemDemoBaseVo {
  //测试名称
  name: string
  //测试内容
  content?: string
  //启用状态
  status: number
  //开始时间
  beginTime: string
  //年龄
  age: number
  //性别
  sex: number
}

export interface SystemDemoCreateVo extends SystemDemoBaseVo {
}

export interface SystemDemoUpdateVo extends SystemDemoBaseVo {
  //id
  id: number
}

export interface SystemDemoRespVo extends SystemDemoBaseVo {
  //id
  id: number
  creator?: string
  createTime: string
}

export interface SystemDemoQueryVo {
  //当前页
  current?: number
  //页大小
  size?: number
  //总记录数
  total?: number
  //快速搜索关键字
  keyword?: string
  //启用状态
  status?: number
  //开始时间
  beginTimes?: string[]
  createTimes?: string[]
  //年龄
  age?: number
  //性别
  sex?: number
}
