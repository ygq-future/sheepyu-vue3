import { defineStore } from 'pinia'
import { IdEnum } from '@/stores/storeId'
import { StorePersistKey } from '@/stores/storePersistKey'
import { useColorMode, useDark } from '@vueuse/core'

interface Layout {
  /*
  * 布局相关
  * */
  layoutMode: 'default' | 'classic'
  //是否为紧凑模式, 当屏幕尺寸小于1000时
  shrink: boolean
  //是否显示设置面板
  showConfig: boolean

  /*
  * 全局样式
  * */
  //主题模式
  colorMode: ['light', 'dark', 'auto'] //auto就是跟随系统主题
  //当前主题模式的下标
  colorModeIndex: number
  //当前是否为暗黑模式
  isDark: boolean
  //页面切换动画
  pageAnimation: string

  /*
  * 侧边栏
  * */
  //侧边栏背景色
  asideBackColor: string[]
  //侧边栏文字色
  asideTextColor: string[]
  //侧边栏激活项背景色
  asideActiveBackColor: string[]
  //侧边栏激活项文字色
  asideActiveTextColor: string[]
  //侧边栏展开式宽度
  asideWidth: number
  //侧边栏子项默认的图标
  asideDefaultIcon: string
  //侧边栏是否折叠
  asideCollapse: boolean
  //是否开启侧边栏手风琴模式
  asideAccordion: boolean
  //是否显示侧边栏上面的logo栏
  logoShow: boolean
  //logo栏的背景色
  logoBackColor: string[]
  //logo栏的文字颜色
  logoTextColor: string[]

  /*
  * 顶栏
  * */
  //顶栏背景色
  topBackColor: string[]
  //顶栏文字颜色
  topTextColor: string[]
  //顶栏鼠标悬停时背景色
  topHoverBackColor: string[]
  //顶栏菜单激活时背景色
  topMenuActiveBackColor: string[]
  //顶栏菜单激活时文字色
  topMenuActiveTextColor: string[]
  //顶栏菜单经典模式下的激活背景色
  classicTopMenuActiveBackColor: string[]
}

interface SystemConfig {
  enableCaptcha: boolean
}

const lightBackColor = '#ffffff'
const lightTextColor = '#303133'
const lightActiveTextColor = '#409eff'
const lightHoverBackColor = '#f5f5f5'

const darkBackColor = '#1d1e1f'
const darkTextColor = '#CFD3DC'
const darkActiveTextColor = '#3375b9'
const darkHoverBackColor = '#18222c'

const backColor = [lightBackColor, darkBackColor, '']
const textColor = [lightTextColor, darkTextColor, '']
const activeTextColor = [lightActiveTextColor, darkActiveTextColor, '']
const hoverBackColor = [lightHoverBackColor, darkHoverBackColor, '']
const classicTopMenuActiveBackColor = ['#f5f5f5', '#121212', '']

export const useConfig = defineStore(IdEnum.CONFIG, () => {
  const layout = reactive<Layout>({
    layoutMode: 'default',
    shrink: false,
    showConfig: false,
    colorMode: ['light', 'dark', 'auto'],
    colorModeIndex: 2,
    isDark: false,
    pageAnimation: 'el-zoom-in-center',
    asideBackColor: [...backColor],
    asideTextColor: [...textColor],
    asideActiveBackColor: [...backColor],
    asideActiveTextColor: [...activeTextColor],
    asideWidth: 200,
    asideDefaultIcon: 'el-icon-Minus',
    asideCollapse: false,
    asideAccordion: false,
    logoShow: true,
    logoBackColor: [...backColor],
    logoTextColor: [...activeTextColor],
    topBackColor: [...backColor],
    topTextColor: [...textColor],
    topHoverBackColor: [...hoverBackColor],
    topMenuActiveBackColor: [...backColor],
    topMenuActiveTextColor: [...activeTextColor],
    classicTopMenuActiveBackColor: [...classicTopMenuActiveBackColor]
  })

  //暂时没用, 登录页实时获取验证码开启状态
  const system = reactive<SystemConfig>({
    enableCaptcha: import.meta.env.VITE_APP_ENABLE_CAPTCHA
  })

  watch(() => layout.layoutMode, (value) => {
    if (value === 'default') {
      layout.topMenuActiveBackColor = [...backColor]
    } else if (value === 'classic') {
      layout.topMenuActiveBackColor = [...classicTopMenuActiveBackColor]
    }
    setAutoColor(layout.isDark)
  }, {
    immediate: true
  })

  useDark({
    onChanged(isDark) {
      setAutoColor(layout.isDark = isDark)
    }
  })
  const colorMode = useColorMode()

  function changeColorMode(colorModeIndex: number) {
    colorMode.value = layout.colorMode[layout.colorModeIndex = colorModeIndex]
  }

  function menuWidth() {
    if (layout.shrink) {
      return layout.asideCollapse ? '0px' : layout.asideWidth + 'px'
    }
    // 菜单是否折叠
    return layout.asideCollapse ? '60px' : layout.asideWidth + 'px'
  }

  function collapseMenu() {
    layout.asideCollapse = !layout.asideCollapse
  }

  function getColor(key: keyof Layout) {
    const colors = layout[key] as string[]
    return colors[layout.colorModeIndex]
  }

  function resetCurrentTheme() {
    if (layout.colorModeIndex === 2) {
      const isDark = useDark()
      setAutoColor(isDark.value)
      return
    }

    changeColorOfIndex(layout.colorModeIndex, layout.colorModeIndex)
  }

  function resetConfig() {
    localStorage.removeItem(StorePersistKey.CONFIG_STORE_KEY)
    location.reload()
  }

  /**
   * 如果当前为暗黑模式, 就把auto的所有颜色设置为暗黑模式, 反之设置成light模式
   * @param isDark 当前是否为暗黑模式
   */
  function setAutoColor(isDark: boolean) {
    isDark ? changeColorOfIndex(1, 2) : changeColorOfIndex(0, 2)
  }

  function changeColorOfIndex(source: number, target: number) {
    const originBackColor = backColor[source]
    const originTextColor = textColor[source]
    const originActiveTextColor = activeTextColor[source]
    const originHoverBackColor = hoverBackColor[source]
    const originClassicActiveBackColor = classicTopMenuActiveBackColor[source]

    layout.asideBackColor[target] = originBackColor
    layout.asideTextColor[target] = originTextColor
    layout.asideActiveBackColor[target] = originBackColor
    layout.asideActiveTextColor[target] = originActiveTextColor
    layout.logoBackColor[target] = originBackColor
    layout.logoTextColor[target] = originActiveTextColor
    layout.topBackColor[target] = originBackColor
    layout.topTextColor[target] = originTextColor
    layout.topHoverBackColor[target] = originHoverBackColor
    layout.topMenuActiveBackColor[target] = layout.layoutMode === 'classic' ? originClassicActiveBackColor : originBackColor
    layout.topMenuActiveTextColor[target] = originActiveTextColor
  }

  return { layout, system, menuWidth, getColor, resetCurrentTheme, resetConfig, changeColorMode, collapseMenu }
}, {
  persist: {
    key: StorePersistKey.CONFIG_STORE_KEY
  }
})
