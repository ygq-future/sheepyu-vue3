import { request } from '@/util/request'
import type { PageResult } from '@/util/request'

export function createDictTypeApi(data: SystemDictTypeCreateVo) {
  return request.post<boolean>('/system/dict/type', data)
}

export function updateDictTypeApi(data: SystemDictTypeUpdateVo) {
  return request.put<boolean>('/system/dict/type', data)
}

export function deleteDictTypeApi(id: number) {
  return request.delete<boolean>('/system/dict/type/' + id)
}

export function findDictTypeApi(id: number) {
  return request.get<SystemDictTypeRespVo>('/system/dict/type/' + id)
}

export function pageDictTypeApi(params: SystemDictTypeQueryVo) {
  return request.get<PageResult<SystemDictTypeRespVo>>('/system/dict/type/page', { params })
}

export function dictTypeListApi() {
  return request.get<SystemDictTypeRespVo[]>('/system/dict/type')
}

export function createDictDataApi(data: SystemDictDataCreateVo) {
  return request.post<boolean>('/system/dict/data', data)
}

export function updateDictDataApi(data: SystemDictDataUpdateVo) {
  return request.put<boolean>('/system/dict/data', data)
}

export function deleteDictDataApi(ids: string) {
  return request.delete<boolean>('/system/dict/data/' + ids)
}

export function findDictDataApi(id: number) {
  return request.get<SystemDictDataRespVo>('/system/dict/data/id/' + id)
}

export function listDictDataApi(params: SystemDictDataQueryVo) {
  return request.get<Array<SystemDictDataRespVo>>('/system/dict/data/' + params.dictType)
}

export interface SystemDictTypeBaseVo {
  //字典名称
  name: string
  //开启状态
  status: number
  //备注
  remark?: string
}

export interface SystemDictTypeCreateVo extends SystemDictTypeBaseVo {
  //类型
  type: string
}

export interface SystemDictTypeUpdateVo extends SystemDictTypeBaseVo {
  //主键
  id: number
}

export interface SystemDictTypeRespVo extends SystemDictTypeBaseVo {
  //主键
  id: number
  //类型
  type: string
  //创建者
  creator?: string
  //创建时间
  createTime: string
  dictDataList: SystemDictDataRespVo[]
}

export interface SystemDictTypeQueryVo {
  //当前页
  current?: number
  //页大小
  size?: number
  //总记录数
  total?: number
  //快速搜索关键字
  keyword?: string
  //开启状态
  status?: number
}

export interface SystemDictDataBaseVo {
  //字典类型
  dictType: string
  //字典排序
  sort: number
  //字典标签
  label: string
  //字典值
  value: string
  //通用状态
  status: number
  //颜色类型: primary, info, warning, success, error
  colorType?: string
  //备注
  remark?: string
}

export interface SystemDictDataCreateVo extends SystemDictDataBaseVo {
}

export interface SystemDictDataUpdateVo extends SystemDictDataBaseVo {
  //主键
  id: number
}

export interface SystemDictDataRespVo extends SystemDictDataBaseVo {
  //主键
  id: number
}

export interface SystemDictDataQueryVo {
  //字典类型
  dictType?: string
}