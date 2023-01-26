import { Request, RequestEnums } from '@/util/request/pojo'

const config = {
  // 默认地址
  baseURL: import.meta.env.VITE_APP_BASE_URL,
  // 设置超时时间
  timeout: RequestEnums.TIMEOUT,
  // 跨域时候允许携带凭证
  withCredentials: true
}

export const request = new Request(config)
