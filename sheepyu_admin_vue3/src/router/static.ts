import type { RouteRecordRaw } from 'vue-router'

export const staticRoutes: Array<RouteRecordRaw> = [
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
  }, {
    path: '/:path(.*)*',
    redirect: '/404'
  }, {
    path: '/404',
    name: '404',
    component: () => import('@/views/common/404.vue')
  }
]
