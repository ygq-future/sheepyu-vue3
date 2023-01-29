import { request } from '@/util/request'

export function demo() {
  return request.get<Boolean>('/system/demo')
}
