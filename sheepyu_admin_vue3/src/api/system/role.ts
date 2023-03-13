import { request } from '@/util/request'
import type { PageResult } from '@/util/request'
import type { SystemMenuRespVo } from '@/api/system/menu'

export function createRoleApi(data: SystemRoleCreateVo) {
  return request.post<boolean>('/system/permission/role', data)
}

export function updateRoleApi(data: SystemRoleUpdateVo) {
  return request.put<boolean>('/system/permission/role', data)
}

export function deleteRoleApi(ids: string) {
  return request.delete<boolean>('/system/permission/role/' + ids)
}

export function findRoleApi(id: number) {
  return request.get<SystemRoleRespVo>('/system/permission/role/' + id)
}

export function pageRoleApi(params: SystemRoleQueryVo) {
  return request.get<PageResult<SystemRoleRespVo>>('/system/permission/role/page', { params })
}

export function assignMenuApi(roleId: number, menuIds: Array<number>) {
  return request.put<PageResult<SystemRoleRespVo>>('/system/permission/menu/assign/' + roleId, menuIds)
}

export function menuByRoleApi(roleId: number) {
  return request.get<Array<number>>('/system/permission/menu-by-role/' + roleId)
}

export interface SystemRoleBaseVo {
  //角色名称
  name: string
  //角色权限字符串
  code: string
  //显示顺序
  sort: number
  //角色状态
  status: number
  //备注
  remark?: string
}

export interface SystemRoleCreateVo extends SystemRoleBaseVo {
}

export interface SystemRoleUpdateVo extends SystemRoleBaseVo {
  //角色ID
  id: number
}

export interface SystemRoleRespVo extends SystemRoleBaseVo {
  //角色ID
  id: number
  menus?: Array<SystemMenuRespVo>
}

export interface SystemRoleQueryVo {
  //当前页
  current?: number
  //页大小
  size?: number
  //总记录数
  total?: number
  //快速搜索关键字
  keyword?: string
  //角色状态
  status?: number
}
