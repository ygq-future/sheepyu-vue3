import type { DictTypeEnum } from '@/stores/dict/dictTypeEnum'

export type ComSearchConfig = Array<ComSearchConfigItem>

export interface TableConfig {
  //是否斑马风格
  stripe?: boolean
  //是否需要多选功能
  selection?: boolean
  //是否需要分页
  pagination?: boolean
  //数据为树形数据时必须
  rowKey?: string
  //数据列配置
  columns: ColumnConfig[]
  // 树形数据配置项, 例如: { hasChildren: 'hasChildren', children: 'children' }
  treeProps?: object
  //如果没有, 那么操作列不会在展示
  operate?: {
    width?: string | number
    fixed?: 'right' | 'left'
    align?: string
    buttons: string[]
  }
}

export interface ColumnConfig {
  render?: 'text' | 'icon' | 'img'
  label: string
  prop: string
  align?: string
  width?: number | string
  sortable?: boolean
  dictRender?: 'tag' | 'switch' | 'select'
  dictType?: DictTypeEnum
}

export interface ComSearchConfigItem {
  label: string
  prop: string
  //默认值 text
  render?: undefined | 'text' | 'number' | 'datetime' | 'select' | 'dict'
  placeholder?: string
  //render为select时必传, 否则组件不会显示
  selectOptions?: Array<any>
  selectIdKey?: string
  selectLabelKey?: string
  //如果render为dict, 那么字典类型必传
  dictType?: DictTypeEnum
}

export interface SelectOptionItem {
  id: number | string
  label: number | string
}
