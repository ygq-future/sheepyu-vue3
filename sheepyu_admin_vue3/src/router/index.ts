import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.VITE_APP_BASE_ROUTER),
  routes: [
    {
      path: '/',
      component: () => import('@/App.vue')
    }
  ]
})

export default router
