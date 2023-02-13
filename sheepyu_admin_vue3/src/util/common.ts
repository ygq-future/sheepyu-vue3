import type { App } from 'vue'
import Icon from '@/components/icon/Icon.vue'
import * as elIcons from '@element-plus/icons-vue'

export function registerIcons(app: App) {
  /*
   * 全局注册 Icon
   * 使用方式: <Icon name="name" size="size" color="color" />
   */
  app.component('Icon', Icon)

  /*
   * 全局注册element Plus的icon
   * 并自定义组件名, 为了方便Icon组件中的resolveComponent解析
   */
  const icons = elIcons as any
  for (const key in icons) {
    const name = `el-icon-${icons[key].name}`
    app.component(name, icons[key])
  }
}

/**
 * 加载网络css文件
 * @param url css资源url
 */
export function loadCss(url: string): void {
  const link = document.createElement('link')
  link.rel = 'stylesheet'
  link.href = url
  link.crossOrigin = 'anonymous'
  document.getElementsByTagName('head')[0].appendChild(link)
}

/**
 * 加载网络js文件
 * @param url js资源url
 */
export function loadJs(url: string): void {
  const link = document.createElement('script')
  link.src = url
  document.body.appendChild(link)
}