import type { App } from 'vue'
import { useEventListener } from '@vueuse/core'
import { useUser } from '@/stores/user/user'

export function registerDirectives(app: App) {
  blurDirective(app)

  authDirective(app)
}

function blurDirective(app: App) {
  app.directive('blur', {
    mounted(el) {
      useEventListener(el, 'focus', () => el.blur())
    }
  })
}

function authDirective(app: App) {
  app.directive('auth', {
    mounted(el, binding) {
      const auth = binding.value
      if (auth.startsWith('none')) {
        return
      }
      const user = useUser()
      if (!auth) return
      const result = user.get().permissions?.some(permission => permission === auth)
      if (!result) {
        el.parentNode.removeChild(el)
      }
    }
  })
}
