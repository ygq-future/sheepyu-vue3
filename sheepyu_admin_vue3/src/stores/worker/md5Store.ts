import { defineStore } from 'pinia'
import { IdEnum } from '@/stores/storeId'

interface FileCache {
  name: string
  lastModified: number
  md5: string
}

export const useMd5Store = defineStore(IdEnum.MD5_WORKER, {
  persist: true,
  state: () => ({
    md5Caches: new Array<FileCache>()
  }),
  actions: {
    addCache(name: string, lastModified: number, md5: string) {
      for (let i = 0; i < this.md5Caches.length; i++) {
        const cache = this.md5Caches[i]
        if (cache.name == name && cache.lastModified == lastModified) {
          return cache.md5 = md5
        }
      }
      this.md5Caches.push({ name, lastModified, md5 })
    },
    findCache(name: string, lastModified: number): string {
      for (let cache of this.md5Caches) {
        if (cache.name == name && cache.lastModified == lastModified) {
          return cache.md5
        }
      }
      return ''
    }
  }
})
