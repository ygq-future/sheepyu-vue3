import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.VITE_APP_BASE_ROUTER),
  routes: [
    {
      path: '/',
      name: 'layout',
      component: () => import('@/layouts/layout.vue'),
      children: [{
        path: 'dashboard',
        name: 'dashboard',
        component: () => import('@/views/dashboard/dashboard.vue'),
        meta: {
          title: '控制台',
          keepalive: true
        }
      }, {
        path: 'home',
        name: 'home',
        component: () => import('@/views/dashboard/home.vue'),
        meta: {
          title: '主页'
        }
      }, {
        path: 'about',
        name: 'about',
        component: () => import('@/views/dashboard/about.vue'),
        meta: {
          title: '关于',
          keepalive: true
        }
      }]
    }
  ]
})

export default router
