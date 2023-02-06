<script lang='ts'>
import type { CSSProperties } from 'vue'
import { createVNode, resolveComponent } from 'vue'

export default defineComponent({
  name: 'Icon',
  props: {
    name: {
      type: String,
      required: true
    },
    size: {
      type: Number,
      default: 18
    },
    color: {
      type: String,
      required: false
    }
  },
  setup(props) {
    const iconStyle = computed((): CSSProperties => {
      return {
        color: props.color,
        fontSize: `${props.size}px`
      }
    })

    if (props.name.startsWith('el-icon-')) {
      return () => createVNode('el-icon', {
        class: 'icon el-icon',
        style: iconStyle.value
      }, [createVNode(resolveComponent(props.name))])
    } else {
      return () => createVNode('i', {
        class: [props.name, 'icon'],
        style: iconStyle.value
      })
    }
  }
})
</script>

<style lang='scss' scoped>

</style>
