package top.sheepyu.module.system.service.dict;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.module.common.util.MyStrUtil;
import top.sheepyu.module.system.controller.admin.dict.data.SystemDictDataCreateVo;
import top.sheepyu.module.system.controller.admin.dict.data.SystemDictDataUpdateVo;
import top.sheepyu.module.system.dao.dict.SystemDictData;
import top.sheepyu.module.system.dao.dict.SystemDictDataMapper;

import java.util.Arrays;
import java.util.List;

import static top.sheepyu.module.common.enums.CommonStatusEnum.ENABLE;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.DICT_DATA_EXISTS;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.DICT_DATA_NOT_EXISTS;
import static top.sheepyu.module.system.convert.dict.SystemDictConvert.CONVERT;

/**
 * @author ygq
 * @date 2023-01-24 13:48
 **/
@Service
@Slf4j
@Validated
public class SystemDictDataServiceImpl extends ServiceImplX<SystemDictDataMapper, SystemDictData> implements SystemDictDataService {

    @Override
    public boolean existsByType(String type) {
        return lambdaQuery().eq(SystemDictData::getDictType, type).exists();
    }

    @Override
    public SystemDictData createDictData(SystemDictDataCreateVo createVo) {
        SystemDictData dictData = CONVERT.convert(createVo);
        checkRepeatByFieldsThrow(dictData, Arrays.asList(
                SystemDictData::getDictType,
                SystemDictData::getValue
        ), DICT_DATA_EXISTS);
        save(dictData);
        return dictData;
    }

    @Override
    public SystemDictData updateDictData(SystemDictDataUpdateVo updateVo) {
        SystemDictData dictData = CONVERT.convert(updateVo);
        updateById(dictData);
        return findByIdThrowIfNotExists(dictData.getId());
    }

    @Override
    public String batchDeleteDictData(String ids) {
        batchDelete(ids, SystemDictData::getId);

        List<String> idList = MyStrUtil.split(ids, ',');
        if (CollUtil.isNotEmpty(idList)) {
            for (String id : idList) {
                SystemDictData one = lambdaQuery().eq(SystemDictData::getId, id).one();
                if (one != null) {
                    return one.getDictType();
                }
            }
        }

        return "";
    }

    @Override
    public List<SystemDictData> listDictData(String type, boolean enable) {
        return lambdaQuery()
                .eq(SystemDictData::getDictType, type)
                .eq(enable, SystemDictData::getStatus, ENABLE.getCode())
                .orderByAsc(SystemDictData::getSort)
                .list();
    }

    @Override
    public SystemDictData findDictData(Long id) {
        return findByIdThrowIfNotExists(id);
    }

    private SystemDictData findByIdThrowIfNotExists(Long id) {
        return findByIdThrowIfNotExists(id, DICT_DATA_NOT_EXISTS);
    }

}
