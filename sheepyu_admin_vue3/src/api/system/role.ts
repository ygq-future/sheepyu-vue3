import type { PageResult } from '@/util/request'
import { request } from '@/util/request'

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

export function listRoleApi() {
  return request.get<Array<SystemRoleRespVo>>('/system/permission/role')
}

export function pageRoleApi(params: SystemRoleQueryVo) {
  return request.get<PageResult<SystemRoleRespVo>>('/system/permission/role/page', { params })
}

export function assignMenuToRoleApi(roleId: number, menuIds: Array<number>) {
  return request.put<boolean>('/system/permission/menu/assign/role/' + roleId, menuIds)
}

export function menuByRoleApi(roleId: number) {
  return request.get<Array<number>>('/system/permission/menu-by-role/' + roleId)
}

export interface SystemRoleBaseVo {
  //所属部门
  deptId?: number
  //角色名称
  name: string
  //角色权限字符串
  code: string
  //显示顺序
  sort: number
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
  creator?: string
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
  //部门
  deptId?: number
}
