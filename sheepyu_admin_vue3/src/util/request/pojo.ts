import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import axios, { AxiosError } from 'axios'
import { ElMessage } from 'element-plus'

export interface Result<T = any> {
  code: number
  msg: string
  data?: T
}

export enum RequestEnums {
  TIMEOUT = 60000,
  NOT_AUTHORIZE = 401, // 登录失效
  SUCCESS = 200, // 请求成功
}

export class Request {
  // 定义成员变量并指定类型
  service: AxiosInstance

  public constructor(config: AxiosRequestConfig) {
    // 实例化axios
    this.service = axios.create(config)

    /**
     * 请求拦截器
     * 客户端发送请求 -> [请求拦截器] -> 服务器
     * token校验(JWT) : 接受服务器返回的token,存储到vuex/pinia/本地储存当中
     */
    this.service.interceptors.request.use(
      (config: any) => {
        const token = localStorage.getItem('Authorization') || ''
        return {
          ...config,
          headers: {
            'Authorization': token
          }
        }
      }
    )

    /**
     * 响应拦截器
     * 服务器换返回信息 -> [拦截统一处理] -> 客户端JS获取到信息
     */
    this.service.interceptors.response.use(
      (response: AxiosResponse) => {
        const { data } = response
        //未登录, 或者登录失效
        if (data.code === RequestEnums.NOT_AUTHORIZE) {
          localStorage.removeItem('token')
          // router.replace({
          //   path: '/login'
          // })
          return Promise.reject(data)
        }

        // 全局错误信息拦截（防止下载文件得时候返回数据流，没有code，直接报错）
        if (data.code && data.code !== RequestEnums.SUCCESS) {
          ElMessage.error(data)
          return Promise.reject(data)
        }
        return data
      }, (error: AxiosError) => {
        ElMessage.error(error.message)
      }
    )
  }

  get<T>(url: string, params?: object): Promise<Result<T>> {
    return this.service.get(url, { params })
  }

  post<T>(url: string, data?: object, params?: object): Promise<Result<T>> {
    return this.service.post(url, data, { params })
  }

  put<T>(url: string, data?: object, params?: object): Promise<Result<T>> {
    return this.service.put(url, data, { params })
  }

  patch<T>(url: string, data?: object, params?: object): Promise<Result<T>> {
    return this.service.patch(url, data, { params })
  }

  delete<T>(url: string, params?: object): Promise<Result<T>> {
    return this.service.delete(url, { params })
  }
}
