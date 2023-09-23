import { defineStore } from 'pinia'
import { IdEnum } from '@/stores/storeId'
import { StorePersistKey } from '@/stores/storePersistKey'
import Md5Worker from '@/util/md5Worker?worker'
import { ElLoading } from 'element-plus'

interface FileCache {
  name: string
  lastModified: number
  md5: string
}

export const useMd5Worker = defineStore(IdEnum.MD5_WORKER, () => {
  const md5Caches = reactive<Array<FileCache>>([])
  const LIMIT_COUNT = 20

  function addCache(name: string, lastModified: number, md5: string) {
    for (let i = 0; i < md5Caches.length; i++) {
      const cache = md5Caches[i]
      if (cache.name == name && cache.lastModified == lastModified) {
        return cache.md5 = md5
      }
    }
    //如果超过了限制的次数就删除最早的文件
    if (md5Caches.length === LIMIT_COUNT) {
      md5Caches.splice(0, 1)
    }
    md5Caches.push({ name, lastModified, md5 })
  }

  function findCache(name: string, lastModified: number): string {
    for (let cache of md5Caches) {
      if (cache.name == name && cache.lastModified == lastModified) {
        return cache.md5
      }
    }
    return ''
  }

  function computeMd5(file: File): Promise<string> {
    return new Promise(resolve => {
      let md5 = findCache(file.name, file.lastModified)
      if (md5) {
        return resolve(md5)
      }
      //使用worker线程计算md5值
      const worker = new Md5Worker()
      const instance = ElLoading.service({ text: '正在计算文件...', fullscreen: true })
      worker.postMessage(file)
      worker.onmessage = (e) => {
        instance.close()
        addCache(file.name, file.lastModified, md5 = e.data)
        resolve(md5)
      }
    })
  }

  return { md5Caches, addCache, findCache, computeMd5 }
}, {
  persist: {
    key: StorePersistKey.MD5_WORKER_STORE_KEY
  }
})
