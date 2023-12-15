import { loadCss } from '@/util/common'
import * as elIcons from '@element-plus/icons-vue'

const cssUrls = [
  'https://at.alicdn.com/t/c/font_3876395_n8wkznhweqo.css',
  'https://cdn.jsdelivr.net/npm/font-awesome@4.7.0/css/font-awesome.min.css'
]

export default function init() {
  cssUrls.forEach(async url => {
    loadCss(url)
  })
}

function getStylesFromDomain(domain: string) {
  const sheets = []
  const styles: StyleSheetList = document.styleSheets

  for (const style of styles) {
    if (style.href && style.href.indexOf(domain) > -1) {
      sheets.push(style)
    }
  }

  return sheets
}

export function getAwesomeIconfontNames() {
  return new Promise<string[]>((resolve, reject) => {
    const iconNames = []
    const sheets = getStylesFromDomain('font-awesome.min.css')
    for (const sheet of sheets) {
      for (const rule of sheet.cssRules) {
        if (rule instanceof CSSStyleRule && /^\.fa-(.*)::before$/.test(rule.selectorText)) {
          if (rule.selectorText.indexOf(', ') > -1) {
            const iconArr = rule.selectorText.split(', ')
            iconNames.push(`${iconArr[0].substring(1, iconArr[0].length).replace(/::before/gi, '')}`)
          } else {
            iconNames.push(`${rule.selectorText.substring(1, rule.selectorText.length).replace(/::before/gi, '')}`)
          }
        }
      }
    }

    if (iconNames.length > 0) {
      return resolve(iconNames)
    }
    reject('No Iconfont style sheet')
  })
}

export function getIconfontNames() {
  return new Promise<string[]>((resolve, reject) => {
    const iconNames = []
    const sheets = getStylesFromDomain('at.alicdn.com')
    for (const sheet of sheets) {
      for (const rule of sheet.cssRules) {
        if (rule instanceof CSSStyleRule && /^\.icon-(.*)::before$/.test(rule.selectorText)) {
          iconNames.push(rule.selectorText.replace('.', '').replace('::before', ''))
        }
      }
    }

    if (iconNames.length > 0) {
      return resolve(iconNames)
    }
    reject('No Iconfont style sheet')
  })
}

export function getElementPlusIconfontNames() {
  return new Promise<string[]>((resolve, reject) => {
    const iconNames = []
    const icons = elIcons as any
    for (const key in icons) {
      iconNames.push(`el-icon-${icons[key].name}`)
    }

    if (iconNames.length > 0) {
      return resolve(iconNames)
    }
    reject('No ElementPlus Icons')
  })
}
