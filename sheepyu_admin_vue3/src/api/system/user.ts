import { request } from '@/util/request'

export function login(data: SystemUserLoginVo) {
  return request.post<LoginUser>('/system/user/login', data)
}

export function info() {
  return request.get<SystemUserRespVo>('/system/user/info')
}

export function captcha() {
  return request.get<CaptchaRespVo>('/captcha')
}

export interface LoginUser {
  id: number
  username: string
  userType: number
  accessToken: string
  refreshToken: string
  expireTime: string
}

export interface SystemUserLoginVo {
  login: string
  password: string
  key?: string
  code?: string
}

export interface SystemUserRespVo {
  id: number;
  username: string;
  nickname?: string;
  email?: string;
  mobile?: string;
  avatar?: string;
  status: number;
  deptId?: number;
  postIds?: number[];
  remark?: string;
  loginIp: string;
  deptName?: string;
  postNames?: string;
  loginTime: string;
}

export interface CaptchaRespVo {
  base64: string
  arithmetic: string
  key: string
}
