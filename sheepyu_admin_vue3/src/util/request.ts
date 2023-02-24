import type { AxiosInstance, AxiosRequestConfig, AxiosResponse, CancelTokenSource } from 'axios'
import axios, { AxiosError } from 'axios'
import { ElNotification } from 'element-plus'
import { useAdmin } from '@/stores/user/user'
import { refreshToken } from '@/api/system/user'
import router from '@/router/router'

export interface Result<T = any> {
  code: number
  msg: string
  data: T
}

export enum RequestEnums {
  TIMEOUT = 300000,
  NOT_AUTHORIZE = 401, // 登录失效
  SUCCESS = 200, // 请求成功
}

const config = {
  // 默认地址
  baseURL: import.meta.env.VITE_APP_BASE_URL,
  // 设置超时时间
  timeout: RequestEnums.TIMEOUT,
  // 跨域时候允许携带凭证
  withCredentials: true
}

export class Request {
  // 定义成员变量并指定类型
  service: AxiosInstance
  isRefreshing = false
  requestList: Array<Function> = []

  public constructor(config: AxiosRequestConfig) {
    // 实例化axios
    this.service = axios.create(config)

    /**
     * 请求拦截器
     * 客户端发送请求 -> [请求拦截器] -> 服务器
     * token校验(JWT) : 接受服务器返回的token,存储到vuex/pinia/本地储存当中
     */
    this.service.interceptors.request.use((config): any => {
      const admin = useAdmin()
      const token = admin.state.accessToken
      return {
        ...config,
        headers: {
          'Authorization': token
        }
      }
    })

    /**
     * 响应拦截器
     * 服务器换返回信息 -> [拦截统一处理] -> 客户端JS获取到信息
     */
    this.service.interceptors.response.use(async (res: AxiosResponse) => {
        const admin = useAdmin()
        const { data } = res
        //未登录, 或者登录失效
        if (data.code === RequestEnums.NOT_AUTHORIZE) {
          if (!this.isRefreshing) {
            this.isRefreshing = true

            try {
              const result = await refreshToken(admin.state.refreshToken as string)
              admin.setAuthInfo(result.data)
              this.requestList.forEach(cb => cb())
              return this.service(res.config)
            } catch (e) {
              this.requestList.forEach(cb => cb())
              router.push('/login').then(() => {
                admin.clear()
                ElNotification.error('登录已过期')
              })
              return Promise.reject('登录已过期')
            } finally {
              this.requestList = []
              this.isRefreshing = false
            }
          } else {
            return new Promise((resolve) => {
              this.requestList.push(() => {
                resolve(this.service(res.config))
              })
            })
          }
        }

        // 全局错误信息拦截（防止下载文件得时候返回数据流，没有code，直接报错）
        if (data.code && data.code !== RequestEnums.SUCCESS) {
          if (!this.isRefreshing) {
            ElNotification.error(data.message)
          }
          return Promise.reject(data)
        }

        if (res.config.method !== 'get') {
          ElNotification.success({
            message: '操作成功',
            duration: 2000
          })
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

export const request = new Request(config)
