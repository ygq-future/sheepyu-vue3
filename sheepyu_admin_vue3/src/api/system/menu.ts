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

export function findMenu(id: number) {
  return request.get<SystemMenuRespVo>(`/system/permission/menu/${id}`)
}

export function createMenu(data: SystemMenuCreateVo) {
  return request.post<boolean>('/system/permission/menu', data)
}

export function updateMenu(data: SystemMenuUpdateVo) {
  return request.put<boolean>('/system/permission/menu', data)
}

export function deleteMenu(id: number) {
  return request.delete<boolean>(`/system/permission/menu/${id}`)
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

export interface SystemMenuCreateVo extends SystemMenuBaseVo {
}

export interface SystemMenuUpdateVo extends SystemMenuBaseVo {
  id: number
}

interface SystemMenuBaseVo {
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
}
