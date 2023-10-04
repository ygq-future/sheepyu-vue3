import type { PageResult } from '@/util/request'
import { request } from '@/util/request'

export function pageAccessLogApi(params: SystemAccessLogQueryVo) {
  return request.get<PageResult<SystemAccessLogRespVo>>('/system/log/access/page', { params })
}

export function pageApiLogApi(params: SystemApiLogQueryVo) {
  return request.get<PageResult<SystemApiLogRespVo>>('/system/log/api/page', { params })
}

export function processApiLogApi(id: number) {
  return request.patch<boolean>('/system/log/api/process/' + id)
}

export interface SystemAccessLogBaseVo {
  //用户编号
  userId: number
  //用户类型
  userType: number
  //登录类型
  loginType: number
  //登录结果
  loginResult: number
  //登录账号
  username: string
  //昵称
  nickname?: string
  //登录IP
  userIp: string
  //浏览器 UA
  userAgent: string
}

export interface SystemAccessLogRespVo extends SystemAccessLogBaseVo {
  //主键
  id: number
  //登录时间
  createTime: string
}

export interface SystemAccessLogQueryVo {
  //当前页
  current?: number
  //页大小
  size?: number
  //总记录数
  total?: number
  //快速搜索关键字
  keyword?: string
  //用户类型
  userType?: number
  //登录类型
  loginType?: number
  //登录结果
  loginResult?: number
  //登录时间
  createTimes?: string[]
}

export interface SystemApiLogBaseVo {
  //用户编号
  userId: number
  //用户类型
  userType: number
  //操作名
  name: string
  //操作类型
  type: number
  //请求方法名
  requestMethod: string
  //请求地址
  requestUrl: string
  //请求参数
  requestParams: string
  //用户IP
  userIp: string
  //执行时长
  duration: number
  //结果码
  resultCode: number
  //结果数据
  resultData?: string
  //结果状态
  status: number
  //异常发生时间
  exceptionTime?: string
  //异常类
  exceptionName?: string
  //异常根信息
  exceptionRootCauseMessage?: string
  //异常栈信息
  exceptionStackTraceFull?: string
  //异常关键信息
  exceptionStackTraceCrucial?: string
  //处理状态
  processStatus: number
  //处理时间
  processTime?: string
  //处理用户
  processUserId?: number
}

export interface SystemApiLogRespVo extends SystemApiLogBaseVo {
  //编号
  id: number
  //请求时间
  createTime: string
}

export interface SystemApiLogQueryVo {
  //当前页
  current?: number
  //页大小
  size?: number
  //总记录数
  total?: number
  //快速搜索关键字
  keyword?: string
  //操作类型
  type?: number
  //执行时长
  durations?: number[]
  //结果状态
  status?: number
  //处理状态
  processStatus?: number
  //请求时间
  createTimes?: string[]
}

