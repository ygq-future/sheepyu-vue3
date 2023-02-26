import type { FormRules } from 'element-plus'
import type { DictTypeEnum } from '@/stores/dict/dictTypeEnum'
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
  //如果render是switch, 或者select, dict, 需要传递数据进行渲染
  data?: any
  //有些树形渲染的数据需要用到, 配置可自定义
  props?: object
  //switch或者select, dict变化调用的函数, 可以解决select联动的问题
  callback?: Function
  //用于表单校验, 默认为true
  required?: boolean
}

type FormRender =
  'number' | 'text' | 'switch' |
  'radio' | 'select' | 'icon' |
  'dict' | 'slider' | 'upload' |
  'tree-select' | 'checkbox' | 'datetime' |
  'part-upload' | 'textarea'
