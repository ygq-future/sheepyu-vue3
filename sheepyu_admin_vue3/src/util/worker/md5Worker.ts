import SparkMD5 from 'spark-md5'

addEventListener('message', (e) => {
  const file = e.data as File
  const reader = new FileReader()
  const spark = new SparkMD5.ArrayBuffer()
  reader.readAsArrayBuffer(file)
  reader.onload = (e) => {
    spark.append(e.target!.result as ArrayBuffer)
    const md5 = spark.end()
    self.postMessage(md5)
  }
})

export default {}
