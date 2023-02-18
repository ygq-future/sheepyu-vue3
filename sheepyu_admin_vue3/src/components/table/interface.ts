import type { DictTypeEnum } from '@/stores/dict/dictTypeEnum'
import type { ElTable } from 'element-plus'

export type ComSearchConfig = Array<ComSearchConfigItem>

export class SyTable {
  table: TableConfig

  constructor(table: TableConfig) {
    this.table = reactive<TableConfig>(table)
  }
}

export interface TableConfig {
  stripe?: boolean
  index?: boolean
  selection?: boolean
  ref?: InstanceType<typeof ElTable>
  rows?: any[]
  columns: ColumnConfig[]
  buttons: string[]
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
