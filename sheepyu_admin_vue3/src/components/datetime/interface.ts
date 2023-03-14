const MINUTE = 1000 * 60
const HOUR = MINUTE * 60
const DAY = HOUR * 24
const MONTH = DAY * 30

function getDateTimeRange(offset: number): Date[] {
  const endTime = new Date()
  const startTime = new Date(endTime.getTime() - offset)
  return [startTime, endTime]
}

function getDateTime(offset: number): Date {
  return new Date(new Date().getTime() - offset)
}

export const rangeShortcuts = [
  {
    text: '最近十分钟',
    value: () => getDateTimeRange(MINUTE * 10)
  }, {
    text: '最近半小时',
    value: () => getDateTimeRange(MINUTE * 30)
  }, {
    text: '最近一小时',
    value: () => getDateTimeRange(HOUR)
  }, {
    text: '最近十小时',
    value: () => getDateTimeRange(HOUR * 10)
  }, {
    text: '最近半天',
    value: () => getDateTimeRange(HOUR / 12)
  }, {
    text: '最近一天',
    value: () => getDateTimeRange(DAY)
  }, {
    text: '最近三天',
    value: () => getDateTimeRange(DAY * 3)
  }, {
    text: '最近一周',
    value: () => getDateTimeRange(DAY * 7)
  }, {
    text: '最近两周',
    value: () => getDateTimeRange(DAY * 14)
  }, {
    text: '最近一个月',
    value: () => getDateTimeRange(MONTH)
  }, {
    text: '最近三个月',
    value: () => getDateTimeRange(MONTH * 3)
  }, {
    text: '最近半年',
    value: () => getDateTimeRange(MONTH * 6)
  }, {
    text: '最近一年',
    value: () => getDateTimeRange(MONTH * 12)
  }, {
    text: '未来十分钟',
    value: () => getDateTimeRange(-MINUTE * 10)
  }, {
    text: '未来半小时',
    value: () => getDateTimeRange(-MINUTE * 30)
  }, {
    text: '未来一小时',
    value: () => getDateTimeRange(-HOUR)
  }, {
    text: '未来十小时',
    value: () => getDateTimeRange(-HOUR * 10)
  }, {
    text: '未来半天',
    value: () => getDateTimeRange(-HOUR / 12)
  }, {
    text: '未来一天',
    value: () => getDateTimeRange(-DAY)
  }, {
    text: '未来三天',
    value: () => getDateTimeRange(-DAY * 3)
  }, {
    text: '未来一周',
    value: () => getDateTimeRange(-DAY * 7)
  }, {
    text: '未来两周',
    value: () => getDateTimeRange(-DAY * 14)
  }, {
    text: '未来一个月',
    value: () => getDateTimeRange(-MONTH)
  }, {
    text: '未来三个月',
    value: () => getDateTimeRange(-MONTH * 3)
  }, {
    text: '未来半年',
    value: () => getDateTimeRange(-MONTH * 6)
  }, {
    text: '未来一年',
    value: () => getDateTimeRange(-MONTH * 12)
  }
]

export const shortcuts = [
  {
    text: '最近十分钟',
    value: () => getDateTime(MINUTE * 10)
  }, {
    text: '最近半小时',
    value: () => getDateTime(MINUTE * 30)
  }, {
    text: '最近一小时',
    value: () => getDateTime(HOUR)
  }, {
    text: '最近十小时',
    value: () => getDateTime(HOUR * 10)
  }, {
    text: '最近半天',
    value: () => getDateTime(HOUR / 12)
  }, {
    text: '最近一天',
    value: () => getDateTime(DAY)
  }, {
    text: '最近三天',
    value: () => getDateTime(DAY * 3)
  }, {
    text: '最近一周',
    value: () => getDateTime(DAY * 7)
  }, {
    text: '最近两周',
    value: () => getDateTime(DAY * 14)
  }, {
    text: '最近一个月',
    value: () => getDateTime(MONTH)
  }, {
    text: '最近三个月',
    value: () => getDateTime(MONTH * 3)
  }, {
    text: '最近半年',
    value: () => getDateTime(MONTH * 6)
  }, {
    text: '最近一年',
    value: () => getDateTime(MONTH * 12)
  }, {
    text: '未来十分钟',
    value: () => getDateTime(-MINUTE * 10)
  }, {
    text: '未来半小时',
    value: () => getDateTime(-MINUTE * 30)
  }, {
    text: '未来一小时',
    value: () => getDateTime(-HOUR)
  }, {
    text: '未来十小时',
    value: () => getDateTime(-HOUR * 10)
  }, {
    text: '未来半天',
    value: () => getDateTime(-HOUR / 12)
  }, {
    text: '未来一天',
    value: () => getDateTime(-DAY)
  }, {
    text: '未来三天',
    value: () => getDateTime(-DAY * 3)
  }, {
    text: '未来一周',
    value: () => getDateTime(-DAY * 7)
  }, {
    text: '未来两周',
    value: () => getDateTime(-DAY * 14)
  }, {
    text: '未来一个月',
    value: () => getDateTime(-MONTH)
  }, {
    text: '未来三个月',
    value: () => getDateTime(-MONTH * 3)
  }, {
    text: '未来半年',
    value: () => getDateTime(-MONTH * 6)
  }, {
    text: '未来一年',
    value: () => getDateTime(-MONTH * 12)
  }
]