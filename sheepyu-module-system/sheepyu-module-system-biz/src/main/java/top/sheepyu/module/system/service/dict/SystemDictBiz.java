package top.sheepyu.module.system.service.dict;

import cn.hutool.core.collection.CollUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.dict.data.SystemDictDataCreateVo;
import top.sheepyu.module.system.controller.admin.dict.data.SystemDictDataUpdateVo;
import top.sheepyu.module.system.controller.admin.dict.type.SystemDictTypeCreateVo;
import top.sheepyu.module.system.controller.admin.dict.type.SystemDictTypeQueryVo;
import top.sheepyu.module.system.controller.admin.dict.type.SystemDictTypeUpdateVo;
import top.sheepyu.module.system.dao.dict.SystemDictData;
import top.sheepyu.module.system.dao.dict.SystemDictType;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static top.sheepyu.module.common.enums.CommonStatusEnum.ENABLE;
import static top.sheepyu.module.common.exception.CommonException.exception;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.DICT_TYPE_HAS_DATA;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.DICT_TYPE_NOT_EXISTS;

/**
 * @author ygq
 * @date 2023-01-24 13:55
 **/
@Service
@Slf4j
@Validated
@AllArgsConstructor
public class SystemDictBiz {
    private SystemDictDataService systemDictDataService;
    private SystemDictTypeService systemDictTypeService;
    private SystemDictRedisService systemDictRedisService;

    public void createDictType(SystemDictTypeCreateVo createVo) {
        systemDictTypeService.createDictType(createVo);
    }

    public void updateDictType(SystemDictTypeUpdateVo updateVo) {
        String type = systemDictTypeService.updateDictType(updateVo);
        //修改之后重新加载字典数据, 因为可能修改了状态
        loadDictType(type);
    }

    public void deleteDictType(Long typeId) {
        //查询类型
        SystemDictType dictType = systemDictTypeService.findDictType(typeId);
        //检查类型下是否还有字典数据
        boolean result = systemDictDataService.existsByType(dictType.getType());
        if (result) {
            throw exception(DICT_TYPE_HAS_DATA);
        }

        //同时删除redis中的数据
        if (systemDictTypeService.removeById(dictType)) {
            systemDictRedisService.deleteDictType(dictType.getType());
        }
    }

    public PageResult<SystemDictType> pageDictType(SystemDictTypeQueryVo queryVo) {
        return systemDictTypeService.pageDictType(queryVo);
    }

    public SystemDictType findDictType(Long id) {
        return systemDictTypeService.findDictType(id);
    }

    public void createDictData(@Valid SystemDictDataCreateVo createVo) {
        //检查dictType是否存在
        if (!systemDictTypeService.existsByType(createVo.getDictType())) {
            throw exception(DICT_TYPE_NOT_EXISTS);
        }
        SystemDictData dictData = systemDictDataService.createDictData(createVo);

        //将数据添加到redis
        if (Objects.equals(dictData.getStatus(), ENABLE.getCode())) {
            systemDictRedisService.addDictData(dictData.getDictType(), dictData);
        }
    }

    public void updateDictData(SystemDictDataUpdateVo updateVo) {
        SystemDictData dictData = systemDictDataService.updateDictData(updateVo);
        //将数据添加到redis
        if (Objects.equals(dictData.getStatus(), ENABLE.getCode())) {
            systemDictRedisService.addDictData(dictData.getDictType(), dictData);
        } else {
            systemDictRedisService.delDictData(dictData.getDictType(), dictData);
        }
    }

    public void batchDeleteDictData(String ids) {
        String type = systemDictDataService.batchDeleteDictData(ids);
        loadDictType(type);
    }

    public List<SystemDictData> listDictData(String type) {
        return systemDictDataService.listDictData(type, false);
    }

    public SystemDictData findDictData(String type, String value) {
        HashSet<SystemDictData> set = systemDictRedisService.listByType(type);
        if (CollUtil.isNotEmpty(set)) {
            for (SystemDictData dictData : set) {
                if (Objects.equals(value, dictData.getValue())) {
                    return dictData;
                }
            }
        }
        return null;
    }

    public SystemDictData findDictData(Long id) {
        return systemDictDataService.findDictData(id);
    }

    public List<SystemDictType> listDictType() {
        List<SystemDictType> dictTypeList = systemDictTypeService.listDictType();
        dictTypeList.forEach(type -> {
            HashSet<SystemDictData> dictDataSet = systemDictRedisService.listByType(type.getType());
            type.setDictDataList(new ArrayList<>(dictDataSet));
        });
        return dictTypeList;
    }

    /**
     * 类初始化时加载全部字典数据到redis中
     */
    @PostConstruct
    public void initDict() {
        log.info("初始化加载字典数据...");
        //清空旧数据
        systemDictRedisService.clear();
        List<String> typeList = systemDictTypeService.typeList();
        for (String type : typeList) {
            loadDictType(type);
        }
        log.info("加载字典数据完成");
    }

    private void loadDictType(String type) {
        List<SystemDictData> list = systemDictDataService.listDictData(type, true);
        systemDictRedisService.loadDictType(type, new HashSet<>(list));
    }
}
