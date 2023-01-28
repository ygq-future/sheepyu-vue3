import SparkMD5 from 'spark-md5'

//做了一个小小的本地缓存, 后面改成菠萝
interface FileCache {
  name: string
  lastModified: number
  md5: string
}

const caches: FileCache[] = []

//TODO 有问题, forEach没有进入, 后面再看
function checkCache(file: File): string | null {
  let md5: string = ''
  caches.forEach(item => {
    if (item.name === file.name && item.lastModified === file.lastModified) {
      console.log('has cache md5 = ' + item.md5)
      return md5 = item.md5
    }
  })
  return md5
}

addEventListener('message', (e) => {
  const file = e.data as File
  const cacheMd5 = checkCache(file)
  if (cacheMd5) {
    self.postMessage(cacheMd5)
    return
  }
  const reader = new FileReader()
  const spark = new SparkMD5.ArrayBuffer()
  reader.readAsArrayBuffer(file)
  reader.onload = (e) => {
    spark.append(e.target!.result as ArrayBuffer)
    const md5 = spark.end()
    caches.push({
      name: file.name,
      lastModified: file.lastModified,
      md5
    })
    self.postMessage(md5)
  }
})

export default {}
