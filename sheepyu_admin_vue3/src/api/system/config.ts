import { request } from '@/util/request'

export function getConfig(key: ConfigKeyEnum) {
  return request.get<any>(`/system/config/by-key/${key}`)
}

export enum ConfigKeyEnum {
  CAPTCHA_ENABLE = 'captcha.enable'
}

export interface SystemConfigRespVo {
  id: number
  name: string
  configKey: string
  configValue: string
  remark?: string
  creator?: string
  createTime?: string
}
