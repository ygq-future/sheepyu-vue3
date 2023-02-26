import type { RouteRecordRaw } from 'vue-router'

export const staticRoutes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'layout',
    redirect: '/dashboard',
    component: () => import('@/layouts/layout.vue'),
    children: []
  }, {
    path: '/login',
    name: 'login',
    component: () => import('@/views/login.vue')
  }, {
    path: '/404',
    name: '404',
    component: () => import('@/views/common/404.vue')
  }
]
