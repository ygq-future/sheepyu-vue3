import type { FormRules } from 'element-plus'
import type { DictTypeEnum } from '@/enums/DictTypeEnum'
import type { DictRender } from '@/components/dict/interface'

export interface PopupFormConfig {
  title: string
  width?: number | string
  maxWidth?: number
  labelWidth?: number | string
  formItemConfigs: FormItemConfig[]
  rules?: FormRules
  //需要隐藏的属性名字列表
  hideProps?: string[]
  //需要禁用的属性名字列表
  disabledProps?: string[]
  //是否是编辑模式
  isEdit?: boolean
  //唯一标识的数据
  ids?: any[]
  //回调超时时间, 默认5000ms
  timeout?: number
}

export interface FormItemConfig {
  prop: string
  label: string
  placeholder?: string
  //form-item下面的小提示
  tip?: string
  render?: FormRender
  //dict和dictRender不为空时, render就为dict
  dictType?: DictTypeEnum
  dictRender?: DictRender
  //如果render是switch, 或者select, 需要传递数据进行渲染
  data?: any
  //有些树形渲染的数据需要用到, 配置可自定义
  props?: { label: string, value: string }
  //switch或者select, dict=select时变化调用的函数, 可以解决select联动的问题
  change?: Function
  //用于表单校验, 默认为true
  required?: boolean
  //是否开启多选, 用于tree-select和select
  multiple?: boolean
  disabled?: boolean
}

/**
 * 1. 推荐switch, radio, select, checkbox能用字典就用字典
 * 2. tree-select-checkbox无法选中父节点的值
 * 3. tree-checkbox可以选中父节点的值, 但是取消了关联选择
 *
 * remark:  其实这些都可以又开发者根据需求去扩展,
 *          不能修改, 不然会影响现有的功能, 比如你需要关联选择
 *          你可以自己扩展一个类型tree-checkbox-strictly
 */
export type FormRender =
  'number' | 'text' | 'switch' | 'password' |
  'radio' | 'select' | 'icon' |
  'slider' | 'upload' | 'tree-select' |
  'checkbox' | 'datetime' | 'part-upload' |
  'textarea' | 'tree-select-checkbox' | 'tree-checkbox'
