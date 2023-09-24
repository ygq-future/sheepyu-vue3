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
import Dict from '@/components/dict/Dict.vue'
import { ElTreeSelect } from 'element-plus'

import 'element-plus/theme-chalk/dark/css-vars.css'
import 'element-plus/theme-chalk/display.css'
import Icon from '@/components/icon/Icon.vue'

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
//强迫症患者, 在这里加上webstorm就可以识别指令, 而后在registerDirectives方法内覆盖了这里这两个空指令
app.directive('blur', {})
app.directive('auth', {})
registerDirectives(app)
//初始化自定义图标
iconFontInit()
//注册自定义组件, 因为Icon组件名和其他的有点冲突没有提示, 所以换成MyIcon
app.component('MyIcon', Icon).component('Dict', Dict).component('ElTreeSelect', ElTreeSelect)
app.use(createPinia().use(piniaPluginPersistedstate)).use(router).mount('#app')
