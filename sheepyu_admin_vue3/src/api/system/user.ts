import { request } from '@/util/request'
import type { PageResult } from '@/util/request'

export function login(data: SystemUserLoginVo) {
  return request.post<LoginUser>('/system/user/login', data)
}

export function refreshToken(refreshToken: string) {
  return request.post<LoginUser>(`/system/user/refreshToken/${refreshToken}`)
}

export function info() {
  return request.get<SystemUserRespVo>('/system/user/info')
}

export function captcha() {
  return request.get<CaptchaRespVo>('/captcha')
}

export function createUserApi(data: SystemUserCreateVo) {
  return request.post<boolean>('/system/user', data)
}

export function updateUserApi(data: SystemUserUpdateVo) {
  return request.put<boolean>('/system/user', data)
}

export function resetPasswordApi(id: number) {
  return request.patch<boolean>('/system/user/reset-password/' + id)
}

export function updatePassApi(data: SystemUpdatePassVo) {
  return request.patch<boolean>('/system/user/password', data)
}

export function deleteUserApi(id: number) {
  return request.delete<boolean>('/system/user/' + id)
}

export function findUserApi(id: number) {
  return request.get<SystemUserRespVo>('/system/user/' + id)
}

export function pageUserApi(params: SystemUserQueryVo) {
  return request.get<PageResult<SystemUserRespVo>>('/system/user/page', { params })
}

export function listUserApi(params: SystemUserQueryVo) {
  return request.get<Array<SystemUserRespVo>>('/system/user', { params })
}

export function exportUserApi(params: SystemUserQueryVo) {
  return request.download('/system/user/export', '用户导出数据.xlsx', { params })
}

export function assignRoleToUserApi(userId: number, roleIds: Array<number>) {
  return request.put<boolean>('/system/permission/role/assign/user/' + userId, roleIds)
}

export function roleByUserApi(userId: number) {
  return request.get<Array<number>>('/system/permission/role-by-user/' + userId)
}

export function statisticsUserApi() {
  return request.get<SystemUserStatisticsVo>('/system/user/statistics')
}

export interface SystemUserStatisticsVo {
  todayIncrement: number
  todayPercent: number
  total: number
  weekIncrement: Array<number>
  weekAccess: Array<number>
  nearUserList: Array<SystemUserRespVo>
}

export interface SystemUpdatePassVo {
  oldPass: string
  newPass: string
}

export interface SystemUserBaseVo {
  //用户昵称
  nickname: string
  //用户邮箱
  email?: string
  //手机号码
  mobile?: string
  //头像地址
  avatar?: string
  //部门-职位ids
  deptIds?: Array<number>
  //账号状态
  status: number
  //备注
  remark?: string
}

export interface SystemUserCreateVo extends SystemUserBaseVo {
  //用户账号
  username: string
  //密码
  password: string
}

export interface SystemUserUpdateVo extends SystemUserBaseVo {
  //用户ID
  id: number
}

export interface SystemUserRespVo extends SystemUserBaseVo {
  //用户ID
  id: number
  //用户类型
  type: number
  //用户名
  username: string
  //最后登录IP
  loginIp?: string
  //部门-职位ids
  deptIds?: Array<number>
  //部门
  deptNames?: string
  //最后登录时间
  loginTime?: string
  //创建时间
  createTime: string
}

export interface SystemUserQueryVo {
  //当前页
  current?: number
  //页大小
  size?: number
  //总记录数
  total?: number
  //快速搜索关键字
  keyword?: string
  //部门id
  deptId?: number
  //账号状态
  status?: number
  //身份类型
  type?: number
  //最后登录时间
  loginTimes?: string[]
  //创建时间
  createTimes?: string[]
}

export interface LoginUser {
  id: number
  username: string
  userType: number
  accessToken: string
  refreshToken: string
  expireTime: string
  refreshExpireTime: string
}

export interface SystemUserLoginVo {
  login: string
  password: string
  key?: string
  code?: string
}

export interface CaptchaRespVo {
  base64: string
  arithmetic: string
  key: string
}
