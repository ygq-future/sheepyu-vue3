import { defineStore } from 'pinia'
import { IdEnum } from '@/stores/storeId'
import { StorePersistKey } from '@/stores/storePersistKey'
import type { LoginUser, SystemUserRespVo } from '@/api/system/user'

interface Admin {
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
}

export const useAdmin = defineStore(IdEnum.ADMIN, () => {
  const state = reactive<Admin>({})

  function clear() {
    delete state.id
    delete state.username
    delete state.email
    delete state.mobile
    delete state.avatar
    delete state.nickname
    delete state.deptId
    delete state.deptName
    delete state.postIds
    delete state.postNames
    delete state.permissions
    delete state.userType
    delete state.accessToken
    delete state.refreshToken
    delete state.loginTime
  }

  function setAuthInfo(loginUser: LoginUser) {
    state.id = loginUser.id
    state.username = loginUser.username
    state.userType = loginUser.userType
    state.accessToken = loginUser.accessToken
    state.refreshToken = loginUser.refreshToken
  }

  function setAdminInfo(admin: SystemUserRespVo) {
    state.nickname = admin.nickname
    state.email = admin.email
    state.mobile = admin.mobile
    state.avatar = admin.avatar
    state.deptId = admin.deptId
    state.deptName = admin.deptName
    state.postIds = admin.postIds
    state.postNames = admin.postNames
    state.loginTime = admin.loginTime
  }

  return { state, clear, setAuthInfo, setAdminInfo }
}, {
  persist: {
    key: StorePersistKey.ADMIN_STORE_KEY
  }
})
