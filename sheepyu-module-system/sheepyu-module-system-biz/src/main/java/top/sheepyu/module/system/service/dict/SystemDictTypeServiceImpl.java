package top.sheepyu.module.system.service.dict;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.dict.type.SystemDictTypeCreateVo;
import top.sheepyu.module.system.controller.admin.dict.type.SystemDictTypeQueryVo;
import top.sheepyu.module.system.controller.admin.dict.type.SystemDictTypeUpdateVo;
import top.sheepyu.module.system.dao.dict.SystemDictType;
import top.sheepyu.module.system.dao.dict.SystemDictTypeMapper;

import java.util.List;
import java.util.stream.Collectors;

import static top.sheepyu.module.common.enums.CommonStatusEnum.ENABLE;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.DICT_TYPE_NOT_EXISTS;
import static top.sheepyu.module.system.convert.dict.SystemDictConvert.CONVERT;

/**
 * @author ygq
 * @date 2023-01-24 13:49
 **/
@Service
@Slf4j
@Validated
public class SystemDictTypeServiceImpl extends ServiceImplX<SystemDictTypeMapper, SystemDictType> implements SystemDictTypeService {

    @Override
    public void createDictType(SystemDictTypeCreateVo createVo) {
        SystemDictType dictType = CONVERT.convert(createVo);
        save(dictType);
    }

    @Override
    public String updateDictType(SystemDictTypeUpdateVo updateVo) {
        SystemDictType dictType = findByIdValidateExists(updateVo.getId());
        String type = dictType.getType();
        dictType = CONVERT.convert(updateVo);
        updateById(dictType);
        return type;
    }

    @Override
    public PageResult<SystemDictType> pageDictType(SystemDictTypeQueryVo queryVo) {
        String keyword = queryVo.getKeyword();
        return page(queryVo, buildQuery()
                .and(StrUtil.isNotBlank(keyword), e -> e
                        .eq(SystemDictType::getId, keyword)
                        .like(SystemDictType::getType, keyword)
                        .like(SystemDictType::getName, keyword))
                .eqIfPresent(SystemDictType::getStatus, queryVo.getStatus()));
    }

    @Override
    public SystemDictType findDictType(Long id) {
        return findByIdValidateExists(id);
    }

    @Override
    public boolean existsByType(String type) {
        return lambdaQuery().eq(SystemDictType::getType, type).exists();
    }

    @Override
    public List<String> typeList() {
        return lambdaQuery()
                .eq(SystemDictType::getStatus, ENABLE.getCode())
                .select(SystemDictType::getType)
                .list().stream().map(SystemDictType::getType)
                .collect(Collectors.toList());
    }

    @Override
    public List<SystemDictType> listDictType() {
        return lambdaQuery().eq(SystemDictType::getStatus, ENABLE.getCode()).list();
    }

    private SystemDictType findByIdValidateExists(Long id) {
        return findByIdValidateExists(id, DICT_TYPE_NOT_EXISTS);
    }
}
