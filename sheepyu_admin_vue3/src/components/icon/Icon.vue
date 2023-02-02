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
      default: '#000000'
    }
  },
  setup(props) {
    const iconStyle = computed((): CSSProperties => {
      const { size, color } = props
      return {
        fontSize: `${size}px`,
        color
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
