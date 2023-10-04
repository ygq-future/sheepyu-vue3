import { request } from '@/util/request'
import type { ConfigKeyEnum } from '@/enums/ConfigKeyEnum'
import type { PageResult } from '@/util/request'

export function getConfigApi(key: ConfigKeyEnum) {
  return request.get<any>(`/system/config/by-key/${key}`)
}

export function createConfigApi(data: SystemConfigCreateVo) {
  return request.post<boolean>('/system/config', data)
}

export function updateConfigApi(data: SystemConfigUpdateVo) {
  return request.put<boolean>('/system/config', data)
}

export function deleteConfigApi(id: number) {
  return request.delete<boolean>('/system/config/' + id)
}

export function findConfigApi(id: number) {
  return request.get<SystemConfigRespVo>('/system/config/' + id)
}

export function pageConfigApi(params: SystemConfigQueryVo) {
  return request.get<PageResult<SystemConfigRespVo>>('/system/config/page', { params })
}

export interface SystemConfigBaseVo {
  //参数名称
  name: string
  //参数键值
  configValue: string
  //备注
  remark?: string
}

export interface SystemConfigCreateVo extends SystemConfigBaseVo {
  //参数键名
  configKey: string
}

export interface SystemConfigUpdateVo extends SystemConfigBaseVo {
  //参数主键
  id: number
}

export interface SystemConfigRespVo extends SystemConfigBaseVo {
  //参数主键
  id: number
  //参数键名
  configKey: string
  //创建者
  creator: string
  //创建时间
  createTime: string
}

export interface SystemConfigQueryVo {
  //当前页
  current?: number
  //页大小
  size?: number
  //总记录数
  total?: number
  //快速搜索关键字
  keyword?: string
}
