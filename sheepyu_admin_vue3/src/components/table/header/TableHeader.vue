<template>
  <transition name='el-zoom-in-top' v-show='state.comSearch'>
    <slot name='comSearch'></slot>
  </transition>

  <div class='table-header'>
    <el-scrollbar>
      <div class='table-header-content'>
        <div class='left' :class='enableShrink || shrink ? "btn-shrink" : ""'>
          <el-tooltip :show-after='500' content='刷新' placement='top'>
            <el-button v-blur type='info' color='#40485b' @click='emits("refresh")'>
              <Icon name='fa fa-refresh' />
            </el-button>
          </el-tooltip>

          <el-tooltip :show-after='500' v-if="props.buttons.includes('add')" content='新增' placement='top'>
            <el-button v-blur type='primary' @click='emits("add")'>
              <Icon name='fa fa-plus' />
              <span class='button-text'>新增</span>
            </el-button>
          </el-tooltip>

          <el-tooltip :show-after='500' v-if="props.buttons.includes('edit')" content='编辑' placement='top'>
            <el-button v-blur type='primary' :disabled='rows.length === 0' @click='onBatchEdit'>
              <Icon name='fa fa-pencil' />
              <span class='button-text'>编辑</span>
            </el-button>
          </el-tooltip>

          <el-popconfirm
            v-if="props.buttons.includes('delete')"
            confirm-button-type='danger'
            title='确认删除这些记录吗?'
            :disabled='rows.length === 0'
            @confirm='onBatchDelete'
          >
            <template #reference>
              <div class='button-item'>
                <el-tooltip :show-after='500' content='删除' placement='top'>
                  <el-button v-blur type='danger' :disabled='rows.length === 0'>
                    <Icon name='fa fa-trash' />
                    <span class='button-text'>删除</span>
                  </el-button>
                </el-tooltip>
              </div>
            </template>
          </el-popconfirm>

          <el-tooltip
            v-if="props.buttons.includes('unfold')"
            :content="state.unfold ? '全部展开' : '全部折叠'"
            :show-after='500'
            placement='top'
          >
            <div class='button-item'>
              <el-button v-blur :type="state.unfold ? 'warning' : 'danger'" @click='onUnfold'>
                <Icon :name='state.unfold ? "fa fa-folder-open" : "fa fa-folder"' />
                <span class='button-text'>{{ state.unfold ? '全部展开' : '全部折叠' }}</span>
              </el-button>
            </div>
          </el-tooltip>

          <div style='margin-left: 12px'>
            <!--
            需要自定义的按钮, 使用这个插槽, 为了适配shrink模式, 按钮如果有文字,
            文字请带上class button-text, 按钮较多时, 可能会超出left区域, 这个时候
            请开启enableShrink模式, 如果还超出, 就没有办法了😁
             -->
            <slot name='buttons'></slot>
          </div>
        </div>

        <div class='right'>
          <el-input
            v-if='search'
            placeholder='关键字模糊搜索'
            clearable
            v-model='state.searchValue'
            @input='onInput'
            @keydown.enter='emits("input-enter", state.searchValue)'
            @clear='emits("input-clear")'
          />
          <el-tooltip v-if='comSearch' content='展开通用搜索' placement='top'>
            <Icon v-blur name='el-icon-Search' :size='16' @click='state.comSearch = !state.comSearch' />
          </el-tooltip>
        </div>
      </div>
    </el-scrollbar>
  </div>
</template>

<script setup lang='ts'>
import { useConfig } from '@/stores/config/config'

const config = useConfig()

//是否开启紧凑模式, 开启之后不会显示按钮的文字, 只会显示icon并带有title提示
const shrink = computed(() => config.layout.shrink)
const props = withDefaults(defineProps<{
  //是否开启通用搜索
  comSearch?: boolean
  //是否开启关键字搜索
  search?: boolean
  //默认显示的按钮
  buttons?: string[]
  rows?: any[]
  //适配某些可能不是根据id来进行批量操作的业务
  rowKey?: string
  //开启关键字搜索后可以绑定input框的值
  modelValue?: string
  //手动开启shrink模式, 不显示按钮文字
  enableShrink?: boolean
}>(), {
  comSearch: true,
  search: true,
  buttons: () => [],
  rows: () => [],
  rowKey: 'id'
})

const emits = defineEmits<{
  (e: 'refresh'): void
  (e: 'add'): void
  (e: 'batch-edit', ids: any[]): void
  (e: 'batch-delete', ids: any[]): void
  (e: 'unfold', unfold: boolean): void
  (e: 'input', searchValue: string): void
  (e: 'input-enter', searchValue: string): void
  (e: 'input-clear'): void
  (e: 'update:modelValue', modelValue: string): void
}>()

const state = reactive({
  searchValue: '',
  unfold: true,
  comSearch: false
})

function onUnfold() {
  emits('unfold', state.unfold)
  state.unfold = !state.unfold
}

function onInput(value: string) {
  emits('update:modelValue', value)
  emits('input', value)
}

function onBatchEdit() {
  emits('batch-edit', props.rows.map(item => item[props.rowKey]))
}

function onBatchDelete() {
  emits('batch-delete', props.rows.map(item => item[props.rowKey]))
}

</script>

<style scoped lang='scss'>
.left {
  display: flex;
  flex: 1;
  align-items: center;
  margin-right: 15px;
}

.right {
  display: flex;
  align-items: center;

  .el-input {
    width: 180px;
    margin-right: 15px;
  }

  .icon {
    padding: 8px 15px;
    border: var(--el-border);
    border-radius: var(--el-border-radius-base);
    cursor: pointer;
    transition: all 0.3s ease-in-out;

    &:hover {
      background-color: v-bind("config.getColor('topHoverBackColor')");
    }
  }
}

.table-header-content {
  display: flex;
}

.table-header {
  background-color: var(--el-bg-color);
  padding: 13px 15px;
  max-width: 100%;
  border: 1px solid var(--el-border-color);
  border-bottom: none;
}

:deep(.button-text) {
  margin-left: 5px;
}

.button-item {
  display: inline-block;
  margin-left: 12px;
}

.btn-shrink {
  :deep(.button-text) {
    display: none;
  }
}
</style>
