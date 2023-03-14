import type { AxiosInstance, AxiosRequestConfig, AxiosResponse, CancelTokenSource } from 'axios'
import axios, { AxiosError } from 'axios'
import { ElNotification } from 'element-plus'
import { useAdmin } from '@/stores/user/user'
import { refreshToken } from '@/api/system/user'
import router from '@/router/router'
import { download } from '@/util/common'
//@ts-ignore
import qs from 'qs'
import { useTabs } from '@/stores/tabs/tabs'

export interface Result<T = any> {
  code: number
  msg: string
  data: T
}

export interface PageResult<T = any> {
  list: Array<T>
  total: number
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
              const tabs = useTabs()
              admin.clear()
              this.requestList.forEach(cb => cb())
              router.push('/login?redirectUrl=' + tabs.state.activeRoute?.fullPath).then(() => {
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

        if (res.config.method !== 'get' && !this.isRefreshing) {
          ElNotification.success({
            message: res.config.url === '/system/user/login' ? '登录成功' : '操作成功',
            duration: 2000
          })
        }
        const resType = res.config.responseType
        return resType === 'blob' || resType === 'arraybuffer' ? res : data
      }, (error: AxiosError) => {
        ElNotification.error(error.message)
      }
    )
  }

  getSource(): CancelTokenSource {
    return axios.CancelToken.source()
  }

  contentType(config?: AxiosRequestConfig): AxiosRequestConfig {
    return {
      ...config,
      headers: { 'Content-Type': 'application/json;charset=utf8' }
    }
  }

  get<T>(url: string, config?: AxiosRequestConfig): Promise<Result<T>> {
    return this.service.get(url, {
      ...config,
      paramsSerializer: {
        serialize: (params) => {
          return qs.stringify(params, { arrayFormat: 'indices' })
        }
      }
    })
  }

  post<T>(url: string, data?: object, config?: AxiosRequestConfig): Promise<Result<T>> {
    return this.service.post(url, data, this.contentType(config))
  }

  put<T>(url: string, data?: object, config?: AxiosRequestConfig): Promise<Result<T>> {
    return this.service.put(url, data, this.contentType(config))
  }

  patch<T>(url: string, data?: object, config?: AxiosRequestConfig): Promise<Result<T>> {
    return this.service.patch(url, data, this.contentType(config))
  }

  delete<T>(url: string, config?: AxiosRequestConfig): Promise<Result<T>> {
    return this.service.delete(url, config)
  }

  /**
   * 下载文件
   * @param url url
   * @param fileName 下载的文件名
   * @param config 配置
   */
  download(url: string, fileName: string, config?: AxiosRequestConfig): Promise<boolean> {
    return new Promise<boolean>((resolve, reject) => {
      this.service.get<Blob>(url, {
        ...config,
        responseType: 'blob'
      }).then(res => {
        download(fileName, res.data)
        resolve(true)
      }).catch(() => {
        reject(false)
      })
    })
  }
}

export const request = new Request(config)
