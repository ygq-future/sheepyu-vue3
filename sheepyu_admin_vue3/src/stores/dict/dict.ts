import { defineStore } from 'pinia'
import { IdEnum } from '@/stores/storeId'
import type { SystemDictDataRespVo, SystemDictTypeRespVo } from '@/api/system/dict'
import type { DictTypeEnum } from '@/stores/dict/dictTypeEnum'
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
        return JSON.parse(value) || []
      },
      serialize: (stateTree) => {
        const dictMap = stateTree.state.dictMap
        return JSON.stringify([...dictMap])
      }
    }
  }
})
