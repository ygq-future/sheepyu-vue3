import { request } from '@/util/request'

export function userPermission() {
  return request.get<string[]>('/system/permission/user-permission')
}

export function userMenu() {
  return request.get<SystemMenuRespVo[]>('/system/permission/user-menu')
}

export function menuList(params: SystemMenuQueryVo) {
  return request.get<SystemMenuRespVo[]>('/system/permission/menu', { params })
}

export interface SystemMenuQueryVo {
  keyword?: string
  status?: number
}

export interface SystemMenuRespVo {
  id: number
  name: string
  permission?: string
  type: number
  sort: number
  parentId: number
  path?: string
  icon?: string
  component?: string
  status: number
  keepAlive: number
  children: SystemMenuRespVo[]
}
