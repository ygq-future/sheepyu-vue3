import type { App } from 'vue'
import * as elIcons from '@element-plus/icons-vue'
import { useDict } from '@/stores/dict/dict'
import { dictTypeListApi } from '@/api/system/dict'

export function registerIcons(app: App) {
  /*
   * 全局注册 MyIcon, 在main.ts中已经注册, 在这里注册编辑器没有提示
   * 使用方式: <MyIcon name="name" size="size" color="color" />
   */
  // app.component('MyIcon', Icon)

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

export function loadDict() {
  const dict = useDict()
  dictTypeListApi().then(res => {
    dict.setDictMap(res.data)
  })
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

export function download(fileName: string, data?: Blob) {
  if (!data) return

  const suffix = fileName.substring(fileName.lastIndexOf('.') + 1)
  const type: string = contentTypeMap.get(suffix) || 'application/octet-stream'
  const blob: Blob = new Blob([data], { type })
  window.URL = window.URL || window.webkitURL
  const href = URL.createObjectURL(blob)
  const downA = document.createElement('a')
  downA.href = href
  downA.download = fileName
  downA.click()
  // 销毁超连接
  window.URL.revokeObjectURL(href)
}


const contentTypeMap: Map<string, string> = new Map([
  ['xls', 'application/vnd.ms-excel'],
  ['doc', 'application/msword'],
  ['xlsx', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'],
  ['docx', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'],
  ['zip', 'application/zip'],
  ['7zip', 'application/zip'],
  ['rar', 'application/rar'],
  ['tar', 'application/x-tar'],
  ['tgz', 'application/x-tar'],
  ['pdf', 'application/pdf']
])
