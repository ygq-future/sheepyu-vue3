export interface TableConfig {
  border?: boolean
  columnConfigs: TableColumnConfig[]
}

export interface TableColumnConfig {
  prop?: string
  label?: string
  type?: string
  render?: string
  align?: string
  width?: string | number
}
