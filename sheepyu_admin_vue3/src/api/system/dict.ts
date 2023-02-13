import { request } from '@/util/request'

export function dictTypeList() {
  return request.get<SystemDictTypeRespVo[]>('/system/dict/type')
}

export interface SystemDictTypeRespVo {
  id: number
  type: string
  name: string
  status: number
  remark?: string
  createTime: string
  creator?: string
  dictDataList: SystemDictDataRespVo[]
}

export interface SystemDictDataRespVo {
  id: number
  dictType: string
  sort: number
  label: string
  value: string
  status: number
  colorType?: string
  remark?: string
}
