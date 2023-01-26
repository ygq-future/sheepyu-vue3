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
  key: string
  code: string
}
