<template>
  <transition name='el-zoom-in-top' v-show='state.comSearch'>
    <slot name='comSearch'></slot>
  </transition>

  <div class='table-header'>
    <el-scrollbar>
      <div class='table-header-content'>
        <div class='left'>
          <el-tooltip content='刷新' placement='top'>
            <el-button v-blur type='info' color='#40485b' @click='emits("refresh")'>
              <Icon name='fa fa-refresh' />
            </el-button>
          </el-tooltip>

          <el-tooltip v-if="props.buttons.includes('add')" content='新增' placement='top'>
            <el-button v-blur type='primary' @click='emits("add")'>
              <Icon name='fa fa-plus' />
              <span v-if='!shrink' class='button-text'>新增</span>
            </el-button>
          </el-tooltip>

          <el-tooltip v-if="props.buttons.includes('edit')" content='编辑' placement='top'>
            <el-button v-blur type='primary' :disabled='rows.length === 0' @click='emits("edit", rows)'>
              <Icon name='fa fa-pencil' />
              <span v-if='!shrink' class='button-text'>编辑</span>
            </el-button>
          </el-tooltip>

          <el-popconfirm
            v-if="props.buttons.includes('delete')"
            confirm-button-type='danger'
            title='确认删除这些记录吗?'
            :disabled='rows.length === 0'
            @confirm='emits("delete", rows)'
          >
            <template #reference>
              <div class='button-item'>
                <el-tooltip content='删除' placement='top'>
                  <el-button v-blur type='danger' :disabled='rows.length === 0'>
                    <Icon name='fa fa-trash' />
                    <span v-if='!shrink' class='button-text'>删除</span>
                  </el-button>
                </el-tooltip>
              </div>
            </template>
          </el-popconfirm>

          <el-tooltip
            v-if="props.buttons.includes('unfold')"
            :content="state.unfold ? '全部展开' : '全部折叠'"
            placement='top'
          >
            <div class='button-item'>
              <el-button v-blur :type="state.unfold ? 'warning' : 'danger'" @click='onUnfold'>
                <Icon :name='state.unfold ? "fa fa-folder-open" : "fa fa-folder"' />
                <span v-if='!shrink' class='button-text'>{{ state.unfold ? '全部展开' : '全部折叠' }}</span>
              </el-button>
            </div>
          </el-tooltip>

          <div style='margin-left: 12px'>
            <slot name='btn'></slot>
          </div>
        </div>

        <div class='right'>
          <el-input
            v-if='search'
            placeholder='关键字模糊搜索'
            clearable
            v-model='state.searchValue'
            @input='onInput'
            @keydown.enter='emits("inputEnter", state.searchValue)'
            @clear='emits("inputClear")'
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

const props = withDefaults(defineProps<{
  //是否开启通用搜索
  comSearch?: boolean
  //是否开启关键字搜索
  search?: boolean
  //默认显示的按钮
  buttons?: string[]
  //是否显示快捷模糊搜索栏
  like?: boolean
  //是否开启紧凑模式, 开启之后不会显示按钮的文字, 只会显示icon并带有title提示
  shrink?: boolean
  rows?: object[],
  //开启关键字搜索后可以绑定input框的值
  modelValue?: string
}>(), {
  comSearch: true,
  search: true,
  buttons: () => [],
  like: true,
  shrink: false,
  rows: () => []
})

const emits = defineEmits<{
  (e: 'refresh'): void
  (e: 'add'): void
  (e: 'edit', rows: object[]): void
  (e: 'delete', rows: object[]): void
  (e: 'unfold', unfold: boolean): void
  (e: 'input', searchValue: string): void
  (e: 'inputEnter', searchValue: string): void
  (e: 'inputClear'): void
  (e: 'update:modelValue', modelValue: string): void
}>()

const state = reactive({
  searchValue: '',
  unfold: true,
  comSearch: true
})

function onUnfold() {
  emits('unfold', state.unfold)
  state.unfold = !state.unfold
}

function onInput(value: string) {
  emits('update:modelValue', value)
  emits('input', value)
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
  border: 0.5px solid var(--el-border-color);
  border-bottom: none;

  .button-text {
    margin-left: 5px;
  }
}

.button-item {
  display: inline-block;
  margin-left: 12px;
}
</style>
