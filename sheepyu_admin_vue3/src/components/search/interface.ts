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
  render?: undefined | 'text' | 'number' | 'datetime' | 'select' | 'dict' | 'tree-select' | 'number-range'
  placeholder?: string
  //render为select或者tree-select时必传
  data?: Array<any>
  props?: { label?: string, value?: string }
  //如果render为dict, 那么字典类型必传
  dictType?: DictTypeEnum
  clearable?: boolean
}

export interface SelectOptionItem {
  value: string
  label:  string
}
