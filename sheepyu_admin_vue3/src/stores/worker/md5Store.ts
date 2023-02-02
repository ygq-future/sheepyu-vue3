import { defineStore } from 'pinia'
import { IdEnum } from '@/stores/storeId'
import { StorePersistKey } from '@/stores/storePersistKey'

interface FileCache {
  name: string
  lastModified: number
  md5: string
}

export const useMd5Store = defineStore(IdEnum.MD5_WORKER, () => {
  const md5Caches = reactive(new Array<FileCache>())

  function addCache(name: string, lastModified: number, md5: string) {
    for (let i = 0; i < md5Caches.length; i++) {
      const cache = md5Caches[i]
      if (cache.name == name && cache.lastModified == lastModified) {
        return cache.md5 = md5
      }
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
    key: StorePersistKey.MD5_STORE_KEY
  }
})
