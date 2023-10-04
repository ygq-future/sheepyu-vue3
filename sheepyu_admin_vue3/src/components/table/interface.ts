import type { DictTypeEnum } from '@/enums/DictTypeEnum'
import type { DictRender } from '@/components/dict/interface'

export interface TableConfig {
  //是否斑马风格
  stripe?: boolean
  //是否需要懒加载
  lazy?: boolean
  //是否需要多选功能
  selection?: boolean
  //是否需要分页
  pagination?: boolean
  expandAll?: boolean
  loading?: boolean
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
  render?: 'text' | 'icon' | 'img' | 'link' | 'rich'
  label: string
  prop: string
  align?: string
  width?: number | string
  radius?: number | string
  sortable?: boolean
  showTip?: boolean
  dictRender?: DictRender
  dictType?: DictTypeEnum
  change?: (row: any, val: any) => any
  /**
   * render为link时跳转的路由, 只能带路径参数, 参数只能是row数据中的一个属性
   * 例如我要跳转到 /system/dict/data/:type
   * 那么path这样写: /system/dict/data/{}, pathParams这样写: [type]
   * 这样就会取表格行数据中的type属性作为参数, 参数可以传多个, 会按照顺序替换
   * 如果path不需要参数, 可不传pathParams
   */
  path?: string
  pathProps?: string[]
}
