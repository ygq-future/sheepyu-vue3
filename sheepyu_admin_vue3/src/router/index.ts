import { createRouter, createWebHistory } from 'vue-router'
import { staticRoutes } from '@/router/static'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

const router = createRouter({
  history: createWebHistory(import.meta.env.VITE_APP_BASE_ROUTER),
  routes: staticRoutes
})

router.beforeEach((to, from, next) => {
  NProgress.configure({ showSpinner: false })
  NProgress.start()
  next()
})

router.afterEach((to, from, failure) => {
  NProgress.done()
})
export default router
