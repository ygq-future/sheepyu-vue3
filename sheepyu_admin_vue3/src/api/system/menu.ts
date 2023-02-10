import { request } from '@/util/request'

export function userPermission() {
  return request.get<string[]>('/system/menu/user-permission')
}

export function userMenu() {
  return request.get<SystemMenuRespVo[]>('/system/menu/user-menu')
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
