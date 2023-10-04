<template>
  <div class='default-main'>
    <el-row :gutter='15'>
      <el-col :xs='24' :lg='18'>
        <div class='header'>
          <img :src='headerSvg' alt=''>
          <div class='header-info'>
            <div class='header-title'>{{ user.get().nickname }}, 上午好! 欢迎回来!</div>
            <div class='header-note'>
              开源等于互助；开源需要大家一起来支持，支持的方式有很多种，比如使用、推荐、写教程、保护生态、贡献代码、回答问题、分享经验、打赏赞助等；欢迎您加入我们！
            </div>
          </div>
        </div>
      </el-col>
      <el-col class='hidden-md-and-down' :lg='6'>
        <div class='working'>
          <img :src='coffeeSvg' alt=''>
          <span>您今天已经工作了{{ workingText }}</span>
          <div class='rest' @click='onChangeWorkStatus'>{{ state.workingText }}</div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter='15'>
      <el-col :xs='24' :sm='12' :md='6'>
        <div class='card'>
          <span class='card-title'>今日会员注册量</span>
          <div class='card-content'>
            <div class='card-content-main'>
              <MyIcon color='#8595F4' name='fa fa-line-chart' />
              <span id='today-user-register'>{{ state.statisticsUser?.todayIncrement }}</span>
            </div>
            <span class='card-content-tip'>
              <span v-if='state.statisticsUser?.todayPercent'>+</span>
              {{ state.statisticsUser?.todayPercent }}%
            </span>
          </div>
        </div>
      </el-col>
      <el-col :xs='24' :sm='12' :md='6'>
        <div class='card'>
          <span class='card-title'>今日附件上传量</span>
          <div class='card-content'>
            <div class='card-content-main'>
              <MyIcon color='#8595F4' name='fa fa-line-chart' />
              <span id='today-file-upload'>{{ state.statisticsFile?.todayIncrement }}</span>
            </div>
            <span class='card-content-tip'>
              <span v-if='state.statisticsFile?.todayPercent'>+</span>
              {{ state.statisticsFile?.todayPercent }}%
            </span>
          </div>
        </div>
      </el-col>
      <el-col :xs='24' :sm='12' :md='6'>
        <div class='card'>
          <span class='card-title'>会员总数量</span>
          <div class='card-content'>
            <div class='card-content-main'>
              <MyIcon color='#F48595' name='fa fa-users' />
              <span id='user-register'>{{ state.statisticsUser?.total }}</span>
            </div>
            <span class='card-content-tip'>+{{ state.statisticsUser?.todayIncrement }}</span>
          </div>
        </div>
      </el-col>
      <el-col :xs='24' :sm='12' :md='6'>
        <div class='card'>
          <span class='card-title'>附件上传总数量</span>
          <div class='card-content'>
            <div class='card-content-main'>
              <MyIcon color='#AD85F4' name='fa fa-file-text' />
              <span id='file-upload'>{{ state.statisticsFile?.total }}</span>
            </div>
            <span class='card-content-tip'>+{{ state.statisticsFile?.todayIncrement }}</span>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter='15'>
      <el-col :xs='24' :md='12' :lg='9'>
        <el-card>
          <template #header>
            会员上周统计图
          </template>
          <div class='user-register-chart' id='user-register-chart'></div>
        </el-card>
      </el-col>
      <el-col :xs='24' :md='12' :lg='9'>
        <el-card>
          <template #header>
            文件上传周统计
          </template>
          <div class='file-upload-chart' id='file-upload-chart'></div>
        </el-card>
      </el-col>
      <el-col :xs='24' :lg='6'>
        <el-card>
          <template #header>
            最近加入的会员
          </template>
          <div class='near-register-user'>
            <el-scrollbar style='width: 100%' :height='260'>
              <div class='item' :title='item.nickname' v-for='item in state.statisticsUser?.nearUserList'>
                <el-avatar :src='item.avatar' :size='50' />
                <div class='item-info'>
                  <span class='nickname'>{{ item.nickname }}</span>
                  <span>注册时间: {{ item.createTime }}</span>
                </div>
                <MyIcon name='el-icon-ArrowRightBold' :size='14' color='#8598f6' />
              </div>
            </el-scrollbar>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang='ts'>
import headerSvg from '@/assets/dashboard/header.svg'
import coffeeSvg from '@/assets/dashboard/coffee.svg'
import { useUser } from '@/stores/user/user'
import { CountUp } from 'countup.js'
import * as echarts from 'echarts'
import type { SystemUserStatisticsVo } from '@/api/system/user'
import { statisticsUserApi } from '@/api/system/user'
import type { SystemFileStatisticsVo } from '@/api/system/file'
import { statisticsFileApi } from '@/api/system/file'
import { useEventListener } from '@vueuse/core'
import { useTabs } from '@/stores/tabs/tabs'
import { Local } from '@/util/storage'
import { StorePersistKey } from '@/stores/storePersistKey'
import { useConfig } from '@/stores/config/config'

const user = useUser()
const tabs = useTabs()
const config = useConfig()

const state = reactive<{
  echarts: Map<string, echarts.ECharts>
  statisticsUser?: SystemUserStatisticsVo
  statisticsFile?: SystemFileStatisticsVo
  timer?: number
  seconds: number
  isWorking?: boolean
  workingText: string
}>({
  echarts: new Map(),
  seconds: 0,
  workingText: '休息一下'
})

const cardColor = computed(() => config.layout.isDark ? '#1d1e1f' : '#e9edf2')
const headerColor = computed(() => config.layout.isDark ? '#1d1e1f' : '#e1eaf9')
const workingText = computed(() => {
  let seconds = state.seconds % 60
  let minute = (parseInt((state.seconds / 60).toString()) % 60)
  let hour = parseInt(((state.seconds / 60) / 60).toString())
  return `${hour}时${minute}分${seconds}秒`
})

function countUp(id: string) {
  const element = document.getElementById(id)
  if (!element) return
  new CountUp(element, parseInt(element.innerText.replace(',', '')), {
    startVal: 0,
    duration: 3
  }).start()
}

function startCount() {
  countUp('today-user-register')
  countUp('user-register')
  countUp('today-file-upload')
  countUp('file-upload')
}

function drawChart(id: string, options: echarts.EChartsOption) {
  if (state.echarts.has(id)) return
  const userRegisterChart = document.getElementById(id)
  if (!userRegisterChart) return
  const chart = echarts.init(userRegisterChart)
  chart.setOption(options)
  state.echarts.set(id, chart)
}

function initChart() {
  const weeks = ['周一', '周二', '周三', '周四', '周五', '周六', '周日']

  drawChart('user-register-chart', {
    legend: {
      data: ['注册量', '访问量']
    },
    toolbox: {
      feature: {
        saveAsImage: {
          title: '保存为照片'
        }
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: [
      {
        data: weeks
      }
    ],
    yAxis: {
      type: 'value',
      minInterval: 1
    },
    series: [
      {
        name: '注册量',
        type: 'line',
        smooth: true,
        areaStyle: {
          color: '#ad85f4'
        },
        emphasis: {
          focus: 'series'
        },
        data: state.statisticsUser?.weekIncrement || []
      },
      {
        name: '访问量',
        type: 'line',
        smooth: true,
        areaStyle: {
          color: '#f48595'
        },
        emphasis: {
          focus: 'series'
        },
        data: state.statisticsUser?.weekAccess || []
      }
    ]
  })

  drawChart('file-upload-chart', {
    grid: {
      top: 30,
      right: 0,
      bottom: 20,
      left: 0,
      containLabel: true
    },
    toolbox: {
      feature: {
        saveAsImage: {
          title: '保存为照片'
        }
      }
    },
    tooltip: {
      trigger: 'item'
    },
    legend: {
      type: 'scroll',
      bottom: 15,
      data: weeks,
      textStyle: {
        color: '#73767a'
      }
    },
    visualMap: {
      type: 'continuous',
      top: 'middle',
      right: 10,
      calculable: true,
      inRange: {
        color: ['yellow', 'red']
      }
    },
    radar: {
      indicator: [
        { name: '图片' },
        { name: '文档' },
        { name: '影音' },
        { name: '压缩包' }
      ]
    },
    series: (function() {
      const result: Array<echarts.SeriesOption> = []
      const data = (state.statisticsFile?.weekIncrement || [])
      for (let i = 0; i < data.length; i++) {
        result.push({
          type: 'radar',
          symbol: 'none',
          lineStyle: {
            width: 1
          },
          emphasis: {
            areaStyle: {
              color: 'rgba(0, 250, 0, 0.3)'
            }
          },
          data: [
            {
              value: data[i],
              name: weeks[i]
            }
          ]
        })
      }
      return result
    })()
  })
}

function echartsResize() {
  for (let entry of state.echarts) {
    entry[1].resize()
  }
}

async function statisticsUser() {
  const { data } = await statisticsUserApi()
  state.statisticsUser = data
}

async function statisticsFile() {
  const { data } = await statisticsFileApi()
  state.statisticsFile = data
}

type WORK = { date: string, startTime: number, pauseTime?: number }

function onChangeWorkStatus() {
  const work: WORK = Local.get(StorePersistKey.WORK_TIME)
  //如果目前正在工作中
  if (state.isWorking) {
    clearInterval(state.timer)
    work.pauseTime = Date.now()
    state.workingText = '继续工作'
    state.isWorking = false
  } else {
    state.workingText = '休息一下'
    state.isWorking = true
    work.startTime += Date.now() - (work.pauseTime || 0)
    work.pauseTime = 0
    state.timer = setInterval(() => {
      state.seconds++
    }, 1000)
  }
  Local.set(StorePersistKey.WORK_TIME, work)
}

function startWorking() {
  const now = new Date()
  const date = `${now.getFullYear()}-${now.getMonth() + 1}-${now.getDate()}`
  const work: WORK = Local.get(StorePersistKey.WORK_TIME) || { startTime: now.getTime(), date }
  if (date !== work.date) {
    state.seconds = 0
    work.date = date
    work.startTime = now.getTime()
    work.pauseTime = 0
  }
  Local.set(StorePersistKey.WORK_TIME, work)
  if (work.pauseTime) {
    state.workingText = '继续工作'
    state.isWorking = false
    state.seconds = parseInt(((work.pauseTime - work.startTime) / 1000).toString())
    return
  }
  state.workingText = '休息一下'
  state.isWorking = true
  state.seconds = parseInt(((now.getTime() - work.startTime) / 1000).toString())
  state.timer = setInterval(() => {
    state.seconds++
  }, 1000)
}

watch(() => tabs.state.tabFullScreen, () => {
  nextTick(() => {
    echartsResize()
  })
})

onMounted(async () => {
  await statisticsUser()
  await statisticsFile()
  startCount()
  initChart()
  startWorking()
  useEventListener('resize', echartsResize)
})

onActivated(() => {
  startCount()
  echartsResize()
})

onUnmounted(() => {
  clearInterval(state.timer)
})
</script>

<script lang='ts'>
export default defineComponent({
  name: 'dashboard'
})
</script>

<style scoped lang='scss'>
.header {
  display: flex;
  align-items: center;
  padding: 15px 20px;
  background-color: v-bind(headerColor);
  transition: all 0.3s ease;
  border-radius: 10px;
  margin-bottom: 15px;

  &:hover {
    transform: scale(1.02);
  }

  img {
    height: 100px;
    margin-right: 10px;
    user-select: none;
  }

  .header-info {
    .header-title {
      font-size: 1.4rem;
      line-height: 35px;
      color: #357fff;
    }

    .header-note {
      padding-top: 6px;
      font-size: 15px;
      color: var(--el-text-color-primary);
    }
  }
}

.working {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  font-size: 14px;
  padding: 15px 0;

  .rest {
    position: absolute;
    top: -40px;
    right: 0;
    opacity: 0;
    cursor: pointer;
    color: var(--el-text-color-primary);
    padding: 10px 25px;
    background-color: rgba(0, 0, 0, .2);
    border-radius: 20px;
    transition: all 0.5s ease;
  }

  img {
    transition: all 0.3s ease;
    width: 80px;
  }

  &:hover {
    img {
      transform: translateY(-10px);
    }

    .rest {
      top: 0;
      opacity: 1;
    }
  }
}

.card {
  display: flex;
  flex-direction: column;
  padding: 20px;
  background-color: v-bind(cardColor);
  border-radius: 10px;
  transition: all 0.3s ease;
  margin-bottom: 15px;

  &:hover {
    transform: translateY(-5px) scale(1.05);
    box-shadow: 0 14px 24px rgba(0, 0, 0, 0.2);
  }

  .card-title {
    color: gray;
    margin-bottom: 15px;
  }

  .card-content {
    display: flex;
    align-items: flex-end;

    .card-content-tip {
      font-size: 18px;
    }

    .card-content-main {
      font-size: 28px;
      margin-right: 15px;

      .icon {
        margin-right: 10px;
      }
    }
  }
}

.near-register-user {
  display: flex;
  flex-direction: column;
  align-items: center;
  box-sizing: border-box;
  padding: 10px 0;

  .item {
    flex: 1;
    display: flex;
    align-items: center;
    padding: 10px;
    margin-bottom: 20px;
    box-shadow: 0 0 5px rgba(0, 0, 0, .1);
    border-radius: 10px;
    cursor: pointer;

    .item-info {
      flex: 1;
      display: flex;
      flex-direction: column;
      margin: 0 5px;

      span {
        font-size: 12px;
      }

      .nickname {
        width: 100px;
        font-size: 16px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
    }
  }
}

.user-register-chart, .file-upload-chart, .near-register-user {
  height: 260px;
}

.el-card {
  box-shadow: none;
  border-radius: 10px;
  border: none;
  margin-bottom: 15px;

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 0 12px rgba(0, 0, 0, .2);
  }
}
</style>
