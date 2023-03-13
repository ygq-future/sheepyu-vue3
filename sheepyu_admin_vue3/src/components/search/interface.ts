import type { TableConfig } from '@/components/table/interface'
import type { DictTypeEnum } from '@/enums/DictTypeEnum'

export interface PopupSearchConfig {
  title: string
  width?: number
  maxWidth?: number
  tableConfig: TableConfig
  data?: any[]
}

export type ComSearchConfig = Array<ComSearchConfigItem>

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
  clearable?: boolean
}

export interface SelectOptionItem {
  id: number | string
  label: number | string
}