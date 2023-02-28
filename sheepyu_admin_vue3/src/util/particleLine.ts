export class ParticleLine {
  canvas: HTMLCanvasElement
  ctx: CanvasRenderingContext2D
  width: number = window.innerWidth
  height: number = window.innerHeight
  isDark: boolean = false
  radius: number
  lineWidth: number
  dots: Dot[] = []
  mouseX: number = 0
  mouseY: number = 0
  followLength: number = 80
  fillStyle: string
  connection: number = 120
  animationFrame: number = 0
  dotsNum: number = 80

  constructor(isDark: boolean = false) {
    this.canvas = document.getElementById('canvas') as HTMLCanvasElement
    const ctx = this.canvas.getContext('2d')
    if (!ctx) {
      throw new Error('创建画布失败!')
    }
    this.ctx = ctx
    this.isDark = isDark
    this.radius = this.lineWidth = 0.5
    this.fillStyle = this.getColor(this.isDark, .3)
  }

  init() {
    this.changeCanvasSize()
    this.initDots(this.dotsNum)
    this.moveDots()
    document.onmousemove = (e) => this.mouseMove(e)
    document.onmouseout = () => this.mouseOut()
    document.onclick = () => this.mouseClick()
    window.onresize = () => this.changeCanvasSize()
  }

  changeCanvasSize() { // 改变画布尺寸
    this.width = window.innerWidth
    this.height = window.innerHeight
    this.canvas.width = this.width
    this.canvas.height = this.height
    this.ctx.clearRect(0, 0, this.width, this.height)
    this.dots = []
    if (this.animationFrame) window.cancelAnimationFrame(this.animationFrame)
    this.initDots(this.dotsNum)
    this.moveDots()
  }

  initDots(num: number) { // 初始化粒子
    this.ctx.fillStyle = this.fillStyle
    this.ctx.lineWidth = this.lineWidth
    for (let i = 0; i < num; i++) {
      const x = Math.floor(Math.random() * this.width)
      const y = Math.floor(Math.random() * this.height)
      const dot = new Dot(x, y)
      this.dotDraw(dot)
      this.dots.push(dot)
    }
  }

  moveDots() { // 移动并建立点与点之间的连接线
    this.ctx.clearRect(0, 0, this.width, this.height)
    for (const dot of this.dots) {
      this.dotMove(dot)
    }
    for (let i = 0; i < this.dots.length; i++) {
      for (let j = i; j < this.dots.length; j++) {
        const distance = Math.sqrt((this.dots[i].x - this.dots[j].x) ** 2 + (this.dots[i].y - this.dots[j].y) ** 2)
        if (distance <= this.connection) {
          const opacity = (1 - distance / this.connection) * 0.5
          this.ctx.strokeStyle = this.getColor(this.isDark, opacity)
          this.ctx.beginPath()
          this.ctx.moveTo(this.dots[i].x, this.dots[i].y)
          this.ctx.lineTo(this.dots[j].x, this.dots[j].y)
          this.ctx.stroke()
          this.ctx.closePath()
        }
      }
    }
    this.animationFrame = window.requestAnimationFrame(() => {
      this.moveDots()
    })
  }

  dotMove(dot: Dot) {
    if (dot.x >= this.width || dot.x <= 0) dot.speedX = -dot.speedX
    if (dot.y >= this.height || dot.y <= 0) dot.speedY = -dot.speedY
    dot.x += dot.speedX
    dot.y += dot.speedY
    if (dot.speedX >= 1) dot.speedX--
    if (dot.speedX <= -1) dot.speedX++
    if (dot.speedY >= 1) dot.speedY--
    if (dot.speedY <= -1) dot.speedY++
    this.dotCorrect(dot)
    this.dotConnectMouse(dot)
    this.dotDraw(dot)
  }

  dotCorrect(dot: Dot) { // 根据鼠标的位置修正
    if (!this.mouseX || !this.mouseY) return
    let lengthX = this.mouseX - dot.x
    let lengthY = this.mouseY - dot.y
    const distance = Math.sqrt(lengthX ** 2 + lengthY ** 2)
    if (distance <= this.followLength) {
      dot.follow = true
    } else if (dot.follow && distance > this.followLength && distance <= this.followLength + 8) {
      let proportion = this.followLength / distance
      lengthX *= proportion
      lengthY *= proportion
      dot.x = this.mouseX - lengthX
      dot.y = this.mouseY - lengthY
    } else {
      dot.follow = false
    }
  }

  dotConnectMouse(dot: Dot) { // 点与鼠标连线
    if (!this.mouseX && !this.mouseY) return
    let lengthX = this.mouseX - dot.x
    let lengthY = this.mouseY - dot.y
    const distance = Math.sqrt(lengthX ** 2 + lengthY ** 2)
    if (distance <= this.connection) {
      const opacity = (1 - distance / this.connection) * 0.5
      this.ctx.strokeStyle = this.getColor(this.isDark, opacity)
      this.ctx.beginPath()
      this.ctx.moveTo(dot.x, dot.y)
      this.ctx.lineTo(this.mouseX, this.mouseY)
      this.ctx.stroke()
      this.ctx.closePath()
    }
  }

  dotDraw(dot: Dot) {
    this.ctx.beginPath()
    this.ctx.arc(dot.x, dot.y, this.radius, 0, 2 * Math.PI)
    this.ctx.fill()
    this.ctx.closePath()
  }

  dotElastic(dot: Dot) { // 鼠标点击后的弹射
    let lengthX = this.mouseX - dot.x
    let lengthY = this.mouseY - dot.y
    const distance = Math.sqrt(lengthX ** 2 + lengthY ** 2)
    if (distance >= this.connection) return
    const rate = 1 - distance / this.connection // 距离越小此值约接近1
    dot.speedX = 40 * rate * -lengthX / distance
    dot.speedY = 40 * rate * -lengthY / distance
  }

  mouseMove(e: MouseEvent) {
    this.mouseX = e.clientX
    this.mouseY = e.clientY
  }

  mouseOut() {
    this.mouseX = 0
    this.mouseY = 0
  }

  mouseClick() {
    for (const dot of this.dots) this.dotElastic(dot)
  }

  getColor(isDark: boolean, opacity: number): string {
    if (isDark) {
      return `rgba(255, 0, 0, ${opacity})`
    }
    return `rgba(0, 0, 0, ${opacity})`
  }
}

class Dot {
  x: number
  y: number
  speedX: number
  speedY: number
  follow: boolean

  constructor(x: number, y: number) {
    this.x = x
    this.y = y
    this.speedX = Math.random() * 2 - 1
    this.speedY = Math.random() * 2 - 1
    this.follow = false
  }
}
