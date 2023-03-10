import { request } from '@/util/request'
import type { PageResult } from '@/util/request'

export function createCodegenApi(data: string[]) {
  return request.post<boolean>('/system/codegen', data)
}

export function updateCodegenApi(data: SystemCodegenUpdateVo) {
  return request.put<boolean>('/system/codegen', data)
}

export function deleteCodegenApi(ids: string) {
  return request.delete<boolean>('/system/codegen/' + ids)
}

export function findCodegenApi(id: number) {
  return request.get<SystemCodegenRespVo>('/system/codegen/' + id)
}

export function pageCodegenApi(params: SystemCodegenQueryVo) {
  return request.get<PageResult<SystemCodegenRespVo>>('/system/codegen/page', { params })
}

export function tableListApi(keyword?: string) {
  return request.get<Array<TableInfoRespVo>>('/system/codegen/table-list', { params: { keyword } })
}

export function codegenGenerateApi(id: number) {
  return request.download('/system/codegen/generate/' + id, 'codegen.zip')
}

export function syncCodegenApi(id: number) {
  return request.patch<boolean>('/system/codegen/sync/' + id)
}

export function previewCodegenApi(id: number) {
  return request.get<any>('/system/codegen/preview/' + id)
}

export interface SystemCodegenBaseVo {
  //生成场景, 0管理端, 1用户端
  scene: number
  //表名称
  tableName: string
  //表描述
  tableComment: string
  //备注
  remark?: string
  //模块名
  moduleName: string
  //业务名
  businessName: string
  //类名称
  className: string
  //类描述
  classComment: string
  //作者
  author: string
  //是否需要list接口
  requireList: boolean
  //是否需要page接口
  requirePage: boolean
  //是否需要excel导出接口
  requireExport: boolean
  //是否需要excel导入接口
  requireImport: boolean
  //列
  columns: SystemCodegenColumn[]
}

export interface SystemCodegenUpdateVo extends SystemCodegenBaseVo {
  //编号
  id: number
}

export interface SystemCodegenRespVo extends SystemCodegenBaseVo {
  //编号
  id: number
  //创建时间
  createTime: string
}

export interface SystemCodegenQueryVo {
  //当前页
  current?: number
  //页大小
  size?: number
  //总记录数
  total?: number
  //快速搜索关键字
  keyword?: string
  //生成场景, 0管理端, 1用户端
  createTimes?: string[]
}

export interface TableInfoRespVo {
  name: string
  comment?: string
}

export interface SystemCodegenColumn {
  id: number
  //tableId
  tableId: number
  //字段名
  name: string
  //字段类型
  type: string
  //字段描述
  comment: string
  //是否允许为空
  nullable: boolean
  //是否主键
  primaryKey: boolean
  //是否自增
  autoIncrement: boolean
  //Java 属性类型
  javaType: string
  //Java 属性名
  javaField: string
  //字典类型
  dictType?: string
  //数据示例
  example?: string
  //是否为 Create 创建操作的字段
  createOperation: boolean
  //是否为 Update 更新操作的字段
  updateOperation: boolean
  //是否为 List 查询操作的字段
  queryOperation: boolean
  //List 查询操作的条件类型
  queryCondition: string
  //是否为 List 查询操作的返回字段
  listOperationResult: boolean
  //表单显示类型
  formShowType: string
  //是否为快速搜索字段
  quickSearch: boolean
  //是否按照此字段排序, 如果是, 那么是asc还是desc
  sort?: string
}
