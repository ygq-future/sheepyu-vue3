import { request } from '@/util/request'
import type { PageResult } from '@/util/request'

export function createJobApi(data: SystemJobCreateVo) {
  return request.post<boolean>('/system/job', data)
}

export function updateJobApi(data: SystemJobUpdateVo) {
  return request.put<boolean>('/system/job', data)
}

export function updateJobStatusApi(id: number) {
  return request.patch<boolean>('/system/job/job-status/' + id)
}

export function executeJobApi(id: number) {
  return request.patch<boolean>('/system/job/execute/' + id)
}

export function deleteJobApi(ids: string) {
  return request.delete<boolean>('/system/job/' + ids)
}

export function findJobApi(id: number) {
  return request.get<SystemJobRespVo>('/system/job/' + id)
}

export function pageJobApi(params: SystemJobQueryVo) {
  return request.get<PageResult<SystemJobRespVo>>('/system/job/page', { params })
}

export function pageJobLogApi(params: SystemJobLogQueryVo) {
  return request.get<PageResult<SystemJobLogRespVo>>('/system/job/log/page', { params })
}

export interface SystemJobBaseVo {
  //任务名称
  name: string
  //参数
  handlerParam?: string
  //CRON 表达式
  cron: string
  //重试次数
  retryCount: number
  //重试间隔
  retryInterval: number
}

export interface SystemJobCreateVo extends SystemJobBaseVo {
  //处理器名字
  handlerName: string
}

export interface SystemJobUpdateVo extends SystemJobBaseVo {
  //任务编号
  id: number
}

export interface SystemJobRespVo extends SystemJobBaseVo {
  //任务编号
  id: number
  //处理器名字
  handlerName: string
  //任务状态
  status: number
  //创建时间
  createTime: string
}

export interface SystemJobQueryVo {
  //当前页
  current?: number
  //页大小
  size?: number
  //总记录数
  total?: number
  //快速搜索关键字
  keyword?: string
  //任务状态
  status?: number
}

export interface SystemJobLogBaseVo {
  //任务编号
  jobId: number
  //处理器名字
  handlerName: string
  //参数
  handlerParam?: string
  //重试次数
  retryCount: number
  //开始时间
  beginTime: string
  //结束时间
  endTime?: string
  //执行时长
  duration?: number
  //任务状态
  status: number
  //结果数据
  result?: string
}

export interface SystemJobLogRespVo extends SystemJobLogBaseVo {
  //日志编号
  id: number
}

export interface SystemJobLogQueryVo {
  //当前页
  current?: number
  //页大小
  size?: number
  //总记录数
  total?: number
  //任务编号
  jobId?: number
  //执行时长
  durations?: number[]
  //任务状态
  status?: number
}
