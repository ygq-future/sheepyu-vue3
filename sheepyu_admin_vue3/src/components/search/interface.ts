import type { ComSearchConfig, TableConfig } from '@/components/table/interface'

export interface PopupSearchConfig {
  title: string
  width?: number
  maxWidth?: number
  tableConfig: TableConfig
  data?: any[]
}