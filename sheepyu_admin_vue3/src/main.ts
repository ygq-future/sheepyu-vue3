import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import App from './App.vue'
import router from './router'
import { registerIcons } from '@/util/common'

const app = createApp(App)
//注册自定义组件Icon和ElementPlus的所有图标
registerIcons(app)
app.use(createPinia().use(piniaPluginPersistedstate)).use(router).mount('#app')
