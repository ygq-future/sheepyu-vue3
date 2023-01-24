package top.sheepyu.module.system.service.dict;

import cn.hutool.core.collection.CollUtil;
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
import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static top.sheepyu.module.common.enums.status.VisibleStatusEnum.VISIBLE;
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
public class SystemDictBiz {
    @Resource
    private SystemDictDataService systemDictDataService;
    @Resource
    private SystemDictTypeService systemDictTypeService;
    @Resource
    private SystemDictRedisService systemDictRedisService;

    public void createDictType(SystemDictTypeCreateVo createVo) {
        systemDictTypeService.createDictType(createVo);
    }

    public void updateDictType(SystemDictTypeUpdateVo updateVo) {
        systemDictTypeService.updateDictType(updateVo);
        //修改之后重新加载字典数据, 因为可能修改了状态
        loadDictType(updateVo.getId());
    }

    public void deleteDictType(Long id) {
        //检查类型下是否还有字典数据
        boolean result = systemDictDataService.existsByTypeId(id);
        if (result) {
            throw exception(DICT_TYPE_HAS_DATA);
        }

        //同时删除redis中的数据
        if (systemDictTypeService.deleteDictType(id)) {
            systemDictRedisService.deleteDictType(id);
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
        if (!systemDictTypeService.existsById(createVo.getDictTypeId())) {
            throw exception(DICT_TYPE_NOT_EXISTS);
        }
        SystemDictData dictData = systemDictDataService.createDictData(createVo);

        //将数据添加到redis
        if (Objects.equals(dictData.getVisible(), VISIBLE.getCode())) {
            systemDictRedisService.addDictData(dictData.getDictTypeId(), dictData);
        }
    }

    public void updateDictData(SystemDictDataUpdateVo updateVo) {
        SystemDictData dictData = systemDictDataService.updateDictData(updateVo);
        //将数据添加到redis
        if (Objects.equals(dictData.getVisible(), VISIBLE.getCode())) {
            systemDictRedisService.addDictData(dictData.getDictTypeId(), dictData);
        } else {
            systemDictRedisService.delDictData(dictData.getDictTypeId(), dictData);
        }
    }

    public void batchDeleteDictData(String ids) {
        Long typeId = systemDictDataService.batchDeleteDictData(ids);
        loadDictType(typeId);
    }

    public List<SystemDictData> listDictData(Long typeId) {
        return systemDictDataService.listDictData(typeId, false);
    }

    public SystemDictData findDictData(Long typeId, String value) {
        HashSet<SystemDictData> set = systemDictRedisService.listByType(typeId);
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

    /**
     * 类初始化时加载全部字典数据到redis中
     */
    @PostConstruct
    public void initDict() {
        log.info("初始化加载字典数据...");
        //清空旧数据
        systemDictRedisService.clear();
        List<Long> idList = systemDictTypeService.idList();
        for (Long typeId : idList) {
            loadDictType(typeId);
        }
        log.info("加载字典数据完成");
    }

    private void loadDictType(Long typeId) {
        List<SystemDictData> list = systemDictDataService.listDictData(typeId, true);
        systemDictRedisService.loadDictType(typeId, new HashSet<>(list));
    }
}
