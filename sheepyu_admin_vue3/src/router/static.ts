import type { RouteRecordRaw } from 'vue-router'

export const staticRoutes: Array<RouteRecordRaw> = [
  {
    name: 'layout',
    path: '/',
    redirect: '/dashboard',
    component: () => import('@/layouts/layout.vue'),
    children: [
      {
        name: 'dashboard',
        path: '/dashboard',
        component: () => import('@/views/dashboard.vue'),
        meta: {
          title: '控制台',
          icon: 'fa fa-dashboard',
          keepalive: true,
          fullpath: '/dashboard'
        }
      },
      {
        name: 'system-codegen-edit',
        path: '/system/codegen/edit/:id',
        component: () => import('@/views/system/codegen/edit.vue'),
        meta: {
          title: '代码生成(修改页)',
          keepalive: true,
          fullpath: '/system/codegen/edit/:id'
        }
      },
      {
        name: 'system-dict-data',
        path: '/system/dict/:type',
        component: () => import('@/views/system/dict/data.vue'),
        meta: {
          title: '字典数据({type})',
          keepalive: true,
          fullpath: '/system/dict/:type'
        }
      },
      {
        name: 'system-user-info',
        path: '/system/user/info',
        component: () => import('@/views/system/user/info.vue'),
        meta: {
          title: '个人信息',
          keepalive: true,
          fullpath: '/system/user/info'
        }
      },
      {
        name: 'system-job-log',
        path: '/system/job/log/:jobId',
        component: () => import('@/views/system/job/log.vue'),
        meta: {
          title: '定时任务日志',
          keepalive: true,
          fullpath: '/system/job/log/:jobId'
        }
      }
    ]
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('@/views/login.vue')
  },
  {
    path: '/404',
    name: '404',
    component: () => import('@/views/common/404.vue')
  }
]
