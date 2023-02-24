import type { RouteRecordRaw } from 'vue-router'

export const homeRoute: RouteRecordRaw = {
  path: 'dashboard',
  name: 'dashboard',
  component: () => import('@/views/system/dashboard.vue'),
  meta: {
    title: '控制台',
    icon: 'fa fa-dashboard',
    keepalive: true,
    fullpath: '/dashboard'
  }
}

export const staticRoutes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'layout',
    redirect: '/dashboard',
    component: () => import('@/layouts/layout.vue'),
    children: [homeRoute]
  }, {
    path: '/login',
    name: 'login',
    component: () => import('@/views/system/login.vue')
  }, {
    path: '/404',
    name: '404',
    component: () => import('@/views/common/404.vue')
  }
]
