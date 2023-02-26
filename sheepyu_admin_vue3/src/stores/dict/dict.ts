import { defineStore } from 'pinia'
import { IdEnum } from '@/stores/storeId'
import type { SystemDictDataRespVo, SystemDictTypeRespVo } from '@/api/system/dict'
import type { DictTypeEnum } from '@/enums/DictTypeEnum'
import { StorePersistKey } from '@/stores/storePersistKey'

export const useDict = defineStore(IdEnum.DICT, () => {
  const state = reactive<{
    dictMap: Map<string, SystemDictDataRespVo[]>
  }>({
    dictMap: new Map<string, SystemDictDataRespVo[]>()
  })

  function setDictMap(dictTypeList: SystemDictTypeRespVo[]) {
    state.dictMap.clear()
    dictTypeList.forEach(dictType => {
      state.dictMap.set(dictType.type, dictType.dictDataList)
    })
  }

  function findDictDataByType(type: DictTypeEnum): SystemDictDataRespVo[] {
    return state.dictMap.get(type) || []
  }

  function findDictData(type: DictTypeEnum, value: string): SystemDictDataRespVo | undefined {
    const dictDataList = findDictDataByType(type)
    for (let dictData of dictDataList) {
      if (dictData.value === value) {
        return dictData
      }
    }
  }

  return { state, setDictMap, findDictDataByType, findDictData }
}, {
  persist: {
    key: StorePersistKey.DICT_STORE_KEY,
    serializer: {
      deserialize: (value) => {
        //因为存储的时候把map变成数组了, 这里要转回来
        const dictArr = JSON.parse(value) || []
        const dictMap = new Map<string, SystemDictDataRespVo>()
        dictArr.forEach((item: any) => {
          dictMap.set(item[0], item[1])
        })
        return { state: { dictMap } }
      },
      serialize: (stateTree) => {
        const dictMap = toRaw(stateTree.state.dictMap)
        return JSON.stringify([...dictMap])
      }
    }
  }
})
