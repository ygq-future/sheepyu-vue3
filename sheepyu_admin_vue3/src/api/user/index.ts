import { request } from '@/util/request'
import type { LoginUser, SystemUserLoginVo } from '@/api/user/pojo'

export default {
  login(data: SystemUserLoginVo) {
    return request.post<LoginUser>('/system/user/login', data)
  }
}
