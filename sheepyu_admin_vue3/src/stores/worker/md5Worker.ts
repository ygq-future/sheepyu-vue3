import { defineStore } from 'pinia'
import { IdEnum } from '@/stores/storeId'
import { StorePersistKey } from '@/stores/storePersistKey'

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

  return { md5Caches, addCache, findCache }
}, {
  persist: {
    key: StorePersistKey.MD5_WORKER_STORE_KEY
  }
})
