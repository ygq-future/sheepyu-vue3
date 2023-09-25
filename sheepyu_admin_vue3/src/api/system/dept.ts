import { request } from '@/util/request'

export function createDeptApi(data: SystemDeptCreateVo) {
  return request.post<boolean>('/system/dept', data)
}

export function updateDeptApi(data: SystemDeptUpdateVo) {
  return request.put<boolean>('/system/dept', data)
}

export function deleteDeptApi(id: number) {
  return request.delete<boolean>('/system/dept/' + id)
}

export function findDeptApi(id: number) {
  return request.get<SystemDeptRespVo>('/system/dept/' + id)
}

export function listDeptApi(params: SystemDeptQueryVo) {
  return request.get<Array<SystemDeptRespVo>>('/system/dept', { params })
}

export function treeDeptApi() {
  return request.get<Array<SystemDeptRespVo>>('/system/dept/tree')
}

export function assignRoleToDeptApi(deptId: number, roleIds: Array<number>) {
  return request.put<boolean>('/system/permission/role/assign/dept/' + deptId, roleIds)
}

export function roleByDeptApi(deptId: number) {
  return request.get<Array<number>>('/system/permission/role-by-dept/' + deptId)
}

export interface SystemDeptBaseVo {
  //部门名称
  name: string
  //父部门id
  parentId?: number
  //类型
  type: number
  //显示顺序
  sort: number
  //负责人
  leaderUserId?: number
  //联系电话
  phone?: string
  //邮箱
  email?: string
}

export interface SystemDeptCreateVo extends SystemDeptBaseVo {
}

export interface SystemDeptUpdateVo extends SystemDeptBaseVo {
  //部门id
  id: number
}

export interface SystemDeptRespVo extends SystemDeptBaseVo {
  //部门id
  id: number
  children?: Array<SystemDeptRespVo>
}

export interface SystemDeptQueryVo {
  //快速搜索关键字
  keyword?: string
}
