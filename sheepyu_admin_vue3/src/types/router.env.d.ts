export {}

import 'vue-router'

declare module 'vue-router' {
  interface RouteMeta {
    title?: string
    keepalive?: boolean
    icon?: string
    //用于渲染菜单的时候menu-item跳转的路径
    fullpath: string
  }
}
