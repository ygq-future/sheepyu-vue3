package top.sheepyu.module.system.service.dict;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.dict.type.SystemDictTypeCreateVo;
import top.sheepyu.module.system.controller.admin.dict.type.SystemDictTypeQueryVo;
import top.sheepyu.module.system.controller.admin.dict.type.SystemDictTypeUpdateVo;
import top.sheepyu.module.system.convert.dict.SystemDictConvert;
import top.sheepyu.module.system.dao.dict.SystemDictType;
import top.sheepyu.module.system.dao.dict.SystemDictTypeMapper;

import java.util.List;
import java.util.stream.Collectors;

import static top.sheepyu.module.common.enums.status.VisibleStatusEnum.VISIBLE;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.DICT_TYPE_NOT_EXISTS;

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
        SystemDictType dictType = SystemDictConvert.CONVERT.convert(createVo);
        save(dictType);
    }

    @Override
    public void updateDictType(SystemDictTypeUpdateVo updateVo) {
        SystemDictType dictType = SystemDictConvert.CONVERT.convert(updateVo);
        updateById(dictType);
    }

    @Override
    public boolean deleteDictType(Long id) {
        return removeById(id);
    }

    @Override
    public PageResult<SystemDictType> pageDictType(SystemDictTypeQueryVo queryVo) {
        return page(queryVo, buildQuery()
                .likeIfPresent(SystemDictType::getName, queryVo.getKeyword())
                .eqIfPresent(SystemDictType::getVisible, queryVo.getVisible()));
    }

    @Override
    public SystemDictType findDictType(Long id) {
        return findByIdValidateExists(id);
    }

    @Override
    public boolean existsById(Long typeId) {
        return lambdaQuery().eq(SystemDictType::getId, typeId).exists();
    }

    @Override
    public List<Long> idList() {
        return lambdaQuery()
                .eq(SystemDictType::getVisible, VISIBLE.getCode())
                .select(SystemDictType::getId)
                .list().stream().map(SystemDictType::getId)
                .collect(Collectors.toList());
    }

    private SystemDictType findByIdValidateExists(Long id) {
        return findByIdValidateExists(id, DICT_TYPE_NOT_EXISTS);
    }

}
