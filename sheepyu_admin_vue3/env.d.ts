/// <reference types="vite/client" />
declare module 'element-plus/dist/locale/zh-cn.mjs'

interface ImportMetaEnv {
  readonly VITE_APP_BASE_URL: string
  readonly VITE_APP_BASE_ROUTER: string
  readonly VITE_APP_ENABLE_CAPTCHA: boolean
}
