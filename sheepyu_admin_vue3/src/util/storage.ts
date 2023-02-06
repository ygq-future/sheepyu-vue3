export const Local = {
  set(key: string, val: any) {
    localStorage.setItem(key, JSON.stringify(val))
  },
  get(key: string) {
    const json: any = localStorage.getItem(key)
    return JSON.parse(json)
  },
  remove(key: string) {
    localStorage.removeItem(key)
  },
  clear() {
    localStorage.clear()
  }
}

export const Session = {
  set(key: string, val: any) {
    sessionStorage.setItem(key, JSON.stringify(val))
  },
  get(key: string) {
    const json: any = sessionStorage.getItem(key)
    return JSON.parse(json)
  },
  remove(key: string) {
    sessionStorage.removeItem(key)
  },
  clear() {
    sessionStorage.clear()
  }
}
