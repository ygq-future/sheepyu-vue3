import type { App } from 'vue'
import { useEventListener } from '@vueuse/core'

export function registerDirectives(app: App) {
  blurDirective(app)
}

function blurDirective(app: App) {
  app.directive('blur', {
    mounted(el) {
      useEventListener(el, 'focus', () => el.blur())
    }
  })
}
