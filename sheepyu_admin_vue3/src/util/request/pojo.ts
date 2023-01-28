import type { AxiosInstance, AxiosRequestConfig, AxiosResponse, CancelTokenSource } from 'axios'
import axios, { AxiosError } from 'axios'
import { ElNotification } from 'element-plus'

export interface Result<T = any> {
  code: number
  msg: string
  data?: T
}

export enum RequestEnums {
  TIMEOUT = 300000,
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
    this.service.interceptors.request.use((config: any) => {
        const token = localStorage.getItem('Authorization') || ''
        return {
          ...config,
          headers: {
            'Authorization': 'test01'
          }
        }
      }
    )

    /**
     * 响应拦截器
     * 服务器换返回信息 -> [拦截统一处理] -> 客户端JS获取到信息
     */
    this.service.interceptors.response.use((res: AxiosResponse) => {
        const { data } = res
        //未登录, 或者登录失效
        if (data.code === RequestEnums.NOT_AUTHORIZE) {
          localStorage.removeItem('Authorization')
          // router.replace({
          //   path: '/login'
          // })
          return Promise.reject(data)
        }

        // 全局错误信息拦截（防止下载文件得时候返回数据流，没有code，直接报错）
        if (data.code && data.code !== RequestEnums.SUCCESS) {
          ElNotification.error(data.message)
          // ElMessage.error(data)
          return Promise.reject(data)
        }

        return data
      }, (error: AxiosError) => {
        ElNotification.error(error.message)
      }
    )
  }

  getSource(): CancelTokenSource {
    return axios.CancelToken.source()
  }

  get<T>(url: string, config?: AxiosRequestConfig): Promise<Result<T>> {
    return this.service.get(url, config)
  }

  post<T>(url: string, data?: object, config?: AxiosRequestConfig): Promise<Result<T>> {
    return this.service.post(url, data, config)
  }

  put<T>(url: string, data?: object, config?: AxiosRequestConfig): Promise<Result<T>> {
    return this.service.put(url, data, config)
  }

  patch<T>(url: string, data?: object, config?: AxiosRequestConfig): Promise<Result<T>> {
    return this.service.patch(url, data, config)
  }

  delete<T>(url: string, config?: AxiosRequestConfig): Promise<Result<T>> {
    return this.service.delete(url, config)
  }
}
