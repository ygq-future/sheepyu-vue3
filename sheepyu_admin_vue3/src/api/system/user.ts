import { request } from '@/util/request'

export default {
  login(data: SystemUserLoginVo) {
    return request.post<LoginUser>('/system/user/login', data)
  }
}

interface LoginUser {
  id: number
  username: string
  userType: number
  accessToken: string
  refreshToken: string
  expireTime: string
}

interface SystemUserLoginVo {
  login: string
  password: string
  key: string
  code: string
}
