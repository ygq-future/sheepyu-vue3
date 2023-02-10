import { createApp } from 'vue'
import App from './App.vue'
import router from './router/router'
import { registerIcons } from '@/util/common'
import { registerDirectives } from '@/util/directives'
import { createPinia } from 'pinia'
import iconFontInit from '@/util/iconfont'
import type { Emitter } from 'mitt'
import mitt from 'mitt'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

import 'element-plus/theme-chalk/dark/css-vars.css'

//注册mit
const Mit: Emitter<{
  [propName: string]: any
}> = mitt()
declare module 'vue' {
  export interface ComponentCustomProperties {
    $bus: typeof Mit
  }
}

const app = createApp(App)
app.config.globalProperties.$bus = Mit
//注册自定义组件Icon和ElementPlus的所有图标
registerIcons(app)
//注册内置指令
registerDirectives(app)
//初始化自定义图标
iconFontInit()
app.use(createPinia().use(piniaPluginPersistedstate)).use(router).mount('#app')
