import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import App from './App.vue'
import router from './router'

createApp(App)
  .use(createPinia().use(piniaPluginPersistedstate))
  .use(router).mount('#app')
