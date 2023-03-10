import type { RouteRecordRaw } from 'vue-router'

export const staticRoutes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'layout',
    redirect: '/dashboard',
    component: () => import('@/layouts/layout.vue'),
    children: [
      {
        name: 'system-codegen-edit',
        path: '/system/codegen/edit/:id',
        component: () => import('@/views/system/codegen/edit.vue'),
        meta: {
          title: '代码生成(修改页)',
          keepalive: true,
          fullpath: '/system/codegen/edit/:id'
        }
      }
    ]
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
