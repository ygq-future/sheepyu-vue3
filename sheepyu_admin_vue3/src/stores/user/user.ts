import { defineStore } from 'pinia'
import { IdEnum } from '@/stores/storeId'
import { StorePersistKey } from '@/stores/storePersistKey'
import type { LoginUser, SystemUserRespVo } from '@/api/system/user'

interface User {
  id?: number
  username?: string
  email?: string
  mobile?: string
  avatar?: string
  nickname?: string
  deptId?: number
  deptName?: string
  postIds?: number[]
  postNames?: string
  permissions?: string[]
  userType?: number
  accessToken?: string
  refreshToken?: string
  loginTime?: string
  expireTime?: string
  refreshExpireTime?: string
}

export const useUser = defineStore(IdEnum.USER, () => {
  const state = reactive<{
    user: User
  }>({
    user: {}
  })

  function clear() {
    state.user = {}
  }

  function setAuthInfo(loginUser: LoginUser) {
    state.user = { ...state.user, ...loginUser }
  }

  function setUserInfo(user: SystemUserRespVo) {
    state.user = { ...state.user, ...user }
  }

  function setPermissions(permissions: string[]) {
    state.user.permissions = permissions
  }

  function hasToken(): boolean {
    const date = new Date(state.user.refreshExpireTime || 0)
    return date.getTime() > Date.now() && state.user.accessToken !== undefined
  }

  function get(): User {
    return state.user
  }

  return { clear, setAuthInfo, setUserInfo, setPermissions, hasToken, get, state }
}, {
  persist: {
    key: StorePersistKey.USER_STORE_KEY,
    paths: ['state.user']
  }
})
